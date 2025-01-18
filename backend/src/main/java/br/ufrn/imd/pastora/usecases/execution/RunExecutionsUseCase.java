package br.ufrn.imd.pastora.usecases.execution;

import br.ufrn.imd.pastora.components.HttpExecutor;
import br.ufrn.imd.pastora.domain.ExecutionData;
import br.ufrn.imd.pastora.domain.http.HttpResponse;
import br.ufrn.imd.pastora.persistence.ExecutionModel;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;
import lombok.With;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class RunExecutionsUseCase {
    final static Logger logger = LoggerFactory.getLogger(HttpExecutor.class);

    private final ExecutionRepository executionRepository;
    private final MonitorRepository monitorRepository;
    private final HttpExecutor httpExecutor;

    @With
    private record TaskContext(
            ExecutionModel execution,
            MonitorModel monitor,
            HttpResponse response
    ) {}

    public Iterable<CompletableFuture<Boolean>> execute(List<String> monitorIds) {
        Date startTime = new Date();

        return monitorIds
                .parallelStream()
                .map(monitorId -> {
                    var monitorModel = monitorRepository.findById(monitorId).orElseThrow();

                    var execution = ExecutionModel.builder()
                            .startedTime(startTime)
                            .monitorId(monitorModel.getId())
                            .status(ExecutionData.Status.RUNNING)
                            .errors(new ArrayList<>())
                            .build();

                    ExecutionModel createdExecutionModel = executionRepository.save(execution);

                    return new TaskContext(createdExecutionModel, monitorModel, null);
                })
                .map(CompletableFuture::completedFuture)
                .map(f -> f
                    .thenApply(taskContext -> {
                        /// --------------------
                        /// Http request
                        /// --------------------
                        var monitor = taskContext.monitor();
                        var request = monitor.getHttp().toHttpRequest();
                        var response = httpExecutor.submitRequest(request);
                        return taskContext.withResponse(response);
                    })
                    .thenApply(taskContext -> {
                        /// --------------------
                        /// Validation
                        /// --------------------
                        var validator = new ValidatorHttpResponse(taskContext.response());
                        var errors = taskContext
                                .monitor()
                                .getValidations()
                                .parallelStream()
                                .map(validator::validateForError)
                                .filter(Optional::isPresent)
                                .map(Optional::get)
                                .toList();

                        return taskContext.execution().withErrors(errors);
                    })
                    .thenApply(executionModel -> {
                        /// --------------------
                        /// Set finish time
                        /// --------------------
                        Date finishTime = new Date();
                        return executionModel.withFinishedTime(finishTime);
                    })
                    .thenApply(executionModel -> {
                        logger.info("\t \t -> monitor {} finished with {} errors", executionModel.getMonitorId(), executionModel.getErrors().size());
                        var monitorsToExecOnFinish = new FinishRunningExecutionUseCase(executionRepository, monitorRepository, httpExecutor).execute(executionModel);

                        if (!monitorsToExecOnFinish.isEmpty()) {
                            logger.info("\t \t \t -> executing on Finish: {}", monitorsToExecOnFinish);
                        }

                        return true;
                    })
                    .exceptionally(ex -> {
                        logger.error("Error at Scheduler Execution: {}", ex.getMessage());
                        return false;
                    })
                )
                .toList();
    }
}
