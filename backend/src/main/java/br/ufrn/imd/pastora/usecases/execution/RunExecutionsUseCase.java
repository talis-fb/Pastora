package br.ufrn.imd.pastora.usecases.execution;

import br.ufrn.imd.pastora.components.HttpExecutor;
import br.ufrn.imd.pastora.components.ValidatorHttpResponse;
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
    final static Logger logger = LoggerFactory.getLogger(RunExecutionsUseCase.class);

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
        return monitorIds
                .parallelStream()
                .map(this::createTaskContextByMonitorId)
                .map(taskContext -> CompletableFuture.completedFuture(taskContext)
                    .thenApply(this::submitHttpRequest)
                    .thenApply(this::validateHttpResponse)
                    .thenApply(this::setFinishTime)
                    .thenApply(this::setExecutionAsFinished)
                    .thenApply(this::setExecutionAsFinished)
                    .thenApply(ctx -> {
                        var execution = ctx.execution();
                        logger.info("\t \t -> monitor {} finished with {} errors", execution.getMonitorId(), execution.getErrors().size());

                        List<String> monitorsToExecOnFinish = this.getOnFinishMonitors(ctx);

                        if (!monitorsToExecOnFinish.isEmpty()) {
                            logger.info("\t \t \t -> executing on Finish: {}", monitorsToExecOnFinish);
                            
                            // Call itself to run on finished monitors
                            new RunExecutionsUseCase(executionRepository, monitorRepository, httpExecutor).execute(monitorsToExecOnFinish);
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

    private TaskContext createTaskContextByMonitorId(String monitorId) {
        Date startTime = new Date();
        var monitorModel = monitorRepository.findById(monitorId).orElseThrow();

        var execution = ExecutionModel.builder()
                .startedTime(startTime)
                .monitorId(monitorModel.getId())
                .status(ExecutionData.Status.RUNNING)
                .errors(new ArrayList<>())
                .build();

        ExecutionModel createdExecutionModel = executionRepository.save(execution);

        return new TaskContext(createdExecutionModel, monitorModel, null);
    }

    private TaskContext submitHttpRequest(TaskContext taskContext) {
        var monitor = taskContext.monitor();
        var request = monitor.getHttp().toHttpRequest();
        var response = httpExecutor.submitRequest(request);
        return taskContext.withResponse(response);
    }

    private TaskContext validateHttpResponse(TaskContext taskContext) {
        var validator = new ValidatorHttpResponse(taskContext.response());
        var errors = taskContext
                .monitor()
                .getValidations()
                .parallelStream()
                .map(validator::validateForError)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        var newExecution = taskContext.execution().withErrors(errors);
        return taskContext.withExecution(newExecution);
    }

    private TaskContext setFinishTime(TaskContext taskContext) {
        Date finishTime = new Date();
        var newExecution = taskContext.execution().withFinishedTime(finishTime);
        return taskContext.withExecution(newExecution);
    }

    private TaskContext setExecutionAsFinished(TaskContext taskContext) {
        new FinishRunningExecutionUseCase(executionRepository).execute(taskContext.execution());
        return taskContext;
    }

    private List<String> getOnFinishMonitors(TaskContext taskContext) {
        return new GetOnFinishMonitorsOfExecutionUseCase(monitorRepository).execute(taskContext.execution());
    }
}
