package br.ufrn.imd.pastora.scheduler;

import br.ufrn.imd.pastora.components.HttpExecutor;
import br.ufrn.imd.pastora.domain.ExecutionData;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import br.ufrn.imd.pastora.usecases.execution.CreateExecutionsByMonitorUseCase;
import br.ufrn.imd.pastora.usecases.execution.CreateExecutionsByOnFinishExecutionUseCase;
import br.ufrn.imd.pastora.usecases.execution.FinishRunningExecutionUseCase;
import br.ufrn.imd.pastora.usecases.execution.RunExecutionsUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
public class SchedulerExecutions {
    private final static Logger logger = LoggerFactory.getLogger(SchedulerExecutions.class);

    private final MonitorRepository monitorRepository;
    private final ExecutionRepository executionRepository;

    // private final Map<String, String> lockRunningMonitors = new ConcurrentHashMap<>();
    private final HttpExecutor httpExecutor;

    @Scheduled(initialDelay = 2,fixedRate = 60, timeUnit = TimeUnit.SECONDS)
    public void runMonitors() {
        logger.info("Executing scheduler tasks");

        List<MonitorModel> monitorsToExec = monitorRepository.findMonitorModelByEnabledEquals(true);

        List<String> executionsIds = createExecutionForMonitors(monitorsToExec.stream().map(MonitorModel::getId).toList());
        for (String executionId : executionsIds) {
            CompletableFuture<ExecutionData> task = new RunExecutionsUseCase(executionRepository, monitorRepository, httpExecutor).execute(executionId);
            task
                .thenApply(executionData -> {
                    logger.info("Finished execution {} ", executionId);
                    new FinishRunningExecutionUseCase(executionRepository).execute(executionId, executionData);
                    logger.info("Finished execution {} -> executing children", executionId);
                    new CreateExecutionsByOnFinishExecutionUseCase(executionRepository, monitorRepository).execute(executionId);
                    logger.info("Finished execution {} and children", executionId);
                    return null;
                });
        }
        logger.info("Monitors are running...");
    }

    public List<String> createExecutionForMonitors(List<String> monitorIds) {
        return new CreateExecutionsByMonitorUseCase(executionRepository).execute(monitorIds);
    }

}
