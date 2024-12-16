package br.ufrn.imd.pastora.scheduler;

import br.ufrn.imd.pastora.domain.Execution;
import br.ufrn.imd.pastora.domain.monitor.MonitorData;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Component
@RequiredArgsConstructor
public class SchedulerExecutions {
    final static Logger logger = LoggerFactory.getLogger(SchedulerExecutions.class);
    final Map<String, Future<ExecutionResult>> runningMonitors = new ConcurrentHashMap<>();

    ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

    final MonitorRepository monitorRepository;

    @Scheduled(fixedRate = 100)
    public void run() {
        logger.info("Executing scheduler tasks");
        List<MonitorModel> enabledMonitors = monitorRepository.findMonitorModelByEnabledEquals(true);
        List<MonitorModel> notInProgressMonitors = enabledMonitors.parallelStream().filter(m -> !runningMonitors.containsKey(m.getId())).toList();
        if (notInProgressMonitors.isEmpty()) {
            return;
        }

        for (MonitorModel monitorModel : notInProgressMonitors) {
            var future = this.executorService.submit(() -> {

            });
            runningMonitors.put(monitorModel.getId(), future);
        }


    }
}
