package br.ufrn.imd.pastora.scheduler;

import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
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
    final HttpExecutor httpExecutor;

    ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    final Map<String, Future<ExecutionResult>> runningMonitors = new ConcurrentHashMap<>();


    @Scheduled(fixedRate = 100)
    public void runMonitors() {
        logger.info("Executing scheduler tasks");
        List<MonitorModel> enabledMonitors = monitorRepository.findMonitorModelByEnabledEquals(true);
        List<MonitorModel> notInProgressMonitors = enabledMonitors.parallelStream().filter(m -> !runningMonitors.containsKey(m.getId())).toList();
        if (notInProgressMonitors.isEmpty()) {
            return;
        }

        for (MonitorModel monitorModel : notInProgressMonitors) {
            var task = new AsyncExecutor(httpExecutor, monitorModel.toMonitorData());
            var future = this.executorService.submit(task);
            runningMonitors.put(monitorModel.getId(), future);
        }
    }

    public void handleExecutionResult(ExecutionResult executionResult) {
        executionRepository.save(executionResult);
        if (executionResult.childExecutions != null) {
            executionResult.childExecutions.parallelStream().forEach(childExecution -> {

            });
        }
    }



    private void addRunningMonitor(String monitorId, Future<ExecutionResult> future) {
        executorService.submit(() -> {
            runningMonitors.put(monitorId, future);
            ExecutionResult result = null;
            try {
                result = future.get();
                handleExecutionResult(result);
                runningMonitors.remove(monitorId);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
