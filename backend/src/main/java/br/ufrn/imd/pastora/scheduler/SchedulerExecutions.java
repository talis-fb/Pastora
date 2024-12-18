package br.ufrn.imd.pastora.scheduler;

import br.ufrn.imd.pastora.components.HttpExecutor;
import br.ufrn.imd.pastora.domain.ExecutionData;
import br.ufrn.imd.pastora.persistence.ExecutionModel;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import br.ufrn.imd.pastora.scheduler.factory.MonitorExecutionFactory;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
public class SchedulerExecutions {
    final static Logger logger = LoggerFactory.getLogger(SchedulerExecutions.class);

    final MonitorRepository monitorRepository;
    final ExecutionRepository executionRepository;

    final ExecutorService executorService;
    final MonitorExecutionFactory monitorExecutionFactory;

    final Map<String, Future<ExecutionData>> lockRunningMonitors = new ConcurrentHashMap<>();

    @Scheduled(initialDelay = 5,fixedRate = 60, timeUnit = TimeUnit.SECONDS)
    public void runMonitors() {
        logger.info("Executing scheduler tasks");

        var monitorsInProgress = this.lockRunningMonitors.keySet();
        logger.info("Running Monitors: {}", monitorsInProgress);

        List<MonitorModel> monitorsToExec = monitorRepository.findMonitorModelByEnabledEqualsAndIdIsNotIn(true, monitorsInProgress);

        List<String> monitorIds = monitorsToExec.stream().map(MonitorModel::getId).toList();
        for (var id : monitorIds) {
            // Create the runner for Monitor
            logger.info("Init monitors execution: {}", id);
            var monitorTask = executorService.submit(monitorExecutionFactory.createMonitorRunner(id));
            lockRunningMonitors.put(id, monitorTask);

            // Handle when Runner finishes
            executorService.submit(() -> {
                try {
                    var executionData = monitorTask.get();
                    logger.info("Saving monitors execution: {}", id);
                    saveExecution(executionData);
                    logger.info("Removing for running monitors: {}", id);
                    lockRunningMonitors.remove(id);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public Integer saveExecution(ExecutionData executionData) {
        var triggered = new ArrayList<Integer>();
        if (executionData.getChildren() != null && !executionData.getChildren().isEmpty()) {
            triggered.addAll(executionData.getChildren().stream().map(this::saveExecution).toList());
        }

        var model = ExecutionModel.fromExecutionData(executionData).withTriggered(triggered);


        logger.info(" -> Saving monitors execution with {} triggered", triggered);
        var modelSaved = executionRepository.save(model);
        logger.info(" -> Saved execution {}", modelSaved.getId());


        return modelSaved.getId();
    }

}
