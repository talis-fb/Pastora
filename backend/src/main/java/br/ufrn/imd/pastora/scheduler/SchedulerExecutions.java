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
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class SchedulerExecutions {
    final static Logger logger = LoggerFactory.getLogger(SchedulerExecutions.class);

    final MonitorRepository monitorRepository;
    final ExecutionRepository executionRepository;
    final HttpExecutor httpExecutor;

    final ExecutorService executorService;
    MonitorExecutionFactory monitorExecutionFactory;

    final Map<String, Future<ExecutionResult>> runningMonitors = new ConcurrentHashMap<>();

    @Scheduled(fixedRate = 100)
    public void runMonitors() {
        logger.info("Executing scheduler tasks");

        var runningMonitors = this.runningMonitors.keySet();
        List<MonitorModel> monitorsToExec = monitorRepository.findMonitorModelByEnabledEqualsAndIdNotIn(true, runningMonitors);

        List<String> monitorIds = monitorsToExec.stream().map(MonitorModel::getId).toList();
        for (var id : monitorIds) {
            var executionTask = executorService.submit(monitorExecutionFactory.createMonitorRunner(id));
            executorService.submit(() -> {
                try {
                    var executionData = executionTask.get();
                    saveExecution(executionData);
                    logger.info("Removing for running monitors: {}", id);
                    runningMonitors.remove(id);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public Integer saveExecution(ExecutionData executionData) {
        var triggered = new ArrayList<Integer>();
        if (executionData.getChildren() != null) {
            triggered.addAll(executionData.getChildren().stream().map(this::saveExecution).toList());
        }

        var model = ExecutionModel.fromExecutionData(executionData).withTriggered(triggered);
        var modelSaved = executionRepository.save(model);
        return modelSaved.getId();
    }

}
