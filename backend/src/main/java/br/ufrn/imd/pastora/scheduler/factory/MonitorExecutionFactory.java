package br.ufrn.imd.pastora.scheduler.factory;

import br.ufrn.imd.pastora.components.HttpExecutor;
import br.ufrn.imd.pastora.domain.ExecutionData;
import br.ufrn.imd.pastora.domain.MonitorData;
import br.ufrn.imd.pastora.domain.MonitorHttpDefinition;
import br.ufrn.imd.pastora.domain.http.HttpResponse;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorValidationRepository;
import br.ufrn.imd.pastora.scheduler.SchedulerExecutions;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.concurrent.*;

@Component
@RequiredArgsConstructor
public class MonitorExecutionFactory {
    final static Logger logger = LoggerFactory.getLogger(MonitorExecutionFactory.class);
    final ExecutionRepository executionRepository;
    final MonitorRepository monitorRepository;
    final MonitorValidationRepository monitorValidationRepository;
    final HttpExecutor httpExecutor;
    final ExecutorService executorService;

    public Callable<ExecutionData> createMonitorRunner(String monitorId) {
        return () -> {
            logger.info("Init task scheduler monitor: {}", monitorId);
            MonitorModel model = monitorRepository.findById(monitorId).orElseThrow();
            MonitorData data = model.toMonitorData();
            var validationModels = monitorValidationRepository.findAllById(model.getValidations());

            var definition = model.getDefinition();
            var httpDefition = (MonitorHttpDefinition) definition;

            logger.info("Make HTTP Request of monitor: {}", monitorId);

            Date startTime = new Date();
            HttpResponse response = httpExecutor.submitRequest(httpDefition.toHttpRequest());
            Date finishTime = new Date();
            logger.info("Finish HTTP Request of monitor: {}", monitorId);

            var execution = ExecutionData.builder()
                    .monitorId(monitorId)
                    .startedTime(startTime)
                    .finishedTime(finishTime)
                    .data(response.getBody())
                    .build();

            // TODO: Here would be the validation
            //  if is True the 'anotherMonotiorToExec' is gonna be the result of 'getOnSucess'
            //  if is False the 'anotherMonotiorToExec' is gonna be the result of 'getOnFail'
            var anotherMonotiorToExec = model.getOnSuccess();

            logger.info("Exec children of: {}", monitorId);
            List<ExecutionData> children = anotherMonotiorToExec
                    .stream()
                    .map(id -> executorService.submit(createMonitorRunner(id)))
                    .map(future -> {
                        try {
                            return future.get();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();

            execution = execution.withChildren(children);
            return execution;
        };
    }

}