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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class RunExecutionsUseCase {
    private final ExecutionRepository executionRepository;
    private final MonitorRepository monitorRepository;
    private final HttpExecutor httpExecutor;

    @With
    private record TaskContext(ExecutionModel execution, MonitorModel monitor, HttpResponse response) {
    }

    public Iterable<CompletableFuture<ExecutionModel>> execute(List<String> monitorIds) {
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
                .map(f -> f.thenApply(pairExecutionMonitor -> {
                    var monitor = pairExecutionMonitor.monitor();
                    var request = monitor.getDefinition().toHttpRequest();
                    var response = httpExecutor.submitRequest(request);
                    return pairExecutionMonitor.withResponse(response);
                }))
                .map(f -> f.thenApply(pairExecutionMonitor -> {
                    // TODO: VALIDATIONS
                    return pairExecutionMonitor;
                }))
                .map(f -> f.thenApply(pairExecutionMonitor -> {
                    Date finishTime = new Date();
                    return pairExecutionMonitor.execution().withFinishedTime(finishTime);
                }))
                .toList();
    }
}
