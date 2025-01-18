package br.ufrn.imd.pastora.scheduler;

import br.ufrn.imd.pastora.components.HttpExecutor;
import br.ufrn.imd.pastora.domain.MonitorData;
import br.ufrn.imd.pastora.mappers.ExecutionMapper;
import br.ufrn.imd.pastora.mappers.MonitorMapper;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import br.ufrn.imd.pastora.usecases.execution.FinishRunningExecutionUseCase;
import br.ufrn.imd.pastora.usecases.execution.RunExecutionsUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class SchedulerExecutions {
    private final static Logger logger = LoggerFactory.getLogger(SchedulerExecutions.class);

    private final MonitorRepository monitorRepository;
    private final ExecutionRepository executionRepository;

    private final HttpExecutor httpExecutor;

    @Scheduled(cron = "0 * * * * *") // Runs every minute
    public void runMonitors() {
        logger.info("Executing scheduler tasks at {}", new Date());

        final int currentMinutes = switch ((Integer) LocalTime.now().getMinute()) {
            case 0 -> 60;
            case Integer n -> n;
        };
        logger.info("\t Current minute {}", currentMinutes);

        List<String> monitorsToExec = monitorRepository
                .findMonitorModelByEnabledEquals(true)
                .parallelStream()
                .filter(monitor -> monitor.getIntervalRate() > 0)
                .filter(monitor -> (currentMinutes % monitor.getIntervalRate()) == 0)
                .map(MonitorModel::getId)
                .toList();

        logger.info("\t Monitors to execure: {} -> {}", monitorsToExec.size(), monitorsToExec);

        var tasks = new RunExecutionsUseCase(executionRepository, monitorRepository, httpExecutor).execute(monitorsToExec);

        tasks.forEach(future -> {
            future.thenAccept(executionModel -> {
                new FinishRunningExecutionUseCase(executionRepository).execute(executionModel);
                logger.info("\t \t -> monitor {} finished", executionModel.getMonitorId());
            }).exceptionally(ex -> {
                System.out.println("Error at Scheduler Execution: " + ex.getMessage());
                return null;
            });
        });

        logger.info("Monitors are running...");
    }

}
