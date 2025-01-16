package br.ufrn.imd.pastora.usecases.execution;

import br.ufrn.imd.pastora.components.HttpExecutor;
import br.ufrn.imd.pastora.domain.ExecutionData;
import br.ufrn.imd.pastora.domain.MonitorHttpDefinition;
import br.ufrn.imd.pastora.domain.http.HttpRequest;
import br.ufrn.imd.pastora.domain.http.HttpResponse;
import br.ufrn.imd.pastora.persistence.ExecutionModel;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;

import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class RunExecutionsUseCase {
    private final ExecutionRepository executionRepository;
    private final MonitorRepository monitorRepository;
    private final HttpExecutor httpExecutor;

    public CompletableFuture<ExecutionData> execute(String executionId) {
        ExecutionModel execution = executionRepository.findById(executionId).orElseThrow();
        MonitorModel monitor = monitorRepository.findById(execution.getMonitorId()).orElseThrow();

        var future = CompletableFuture.supplyAsync(() -> {
            MonitorHttpDefinition definition = monitor.getDefinition();
            Date startTime = new Date();
            HttpRequest request = definition.toHttpRequest();
            Date finishTime = new Date();
            HttpResponse response = httpExecutor.submitRequest(request);

            // TODO: Validations

            ExecutionModel model = executionRepository.findById(executionId)
                    .orElseThrow().withStatus(ExecutionData.Status.RUNNING)
                    .withStartedTime(startTime)
                    .withFinishedTime(finishTime)
                    .withData(response.getBody());

            executionRepository.save(model);

            return ExecutionData.builder()
                    .data(response.getBody())
                    .finishedTime(finishTime)
                    .startedTime(startTime)
                    .monitorId(monitor.getId())
                    .status(ExecutionData.Status.RUNNING)
                    .build();
        });
        executionRepository.save(execution.withStatus(ExecutionData.Status.RUNNING));

        return future;
    }
}
