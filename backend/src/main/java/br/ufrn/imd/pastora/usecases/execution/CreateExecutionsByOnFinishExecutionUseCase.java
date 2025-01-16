package br.ufrn.imd.pastora.usecases.execution;

import br.ufrn.imd.pastora.domain.ExecutionData;
import br.ufrn.imd.pastora.persistence.ExecutionModel;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CreateExecutionsByOnFinishExecutionUseCase {
    private final static Logger logger = LoggerFactory.getLogger(CreateExecutionsByOnFinishExecutionUseCase.class);
    private final ExecutionRepository executionRepository;
    private final MonitorRepository monitorRepository;

    public List<String> execute(String executionId) {
        ExecutionModel executionModel = executionRepository.findById(executionId).orElseThrow();

        boolean hasFinished = executionModel.getStatus() == ExecutionData.Status.FINISHED;
        if (!hasFinished) {
            return new ArrayList<>();
        }

        boolean hasSuccessfully = executionModel.getErrors().isEmpty();
        MonitorModel monitor = monitorRepository.findById(executionModel.getMonitorId()).orElseThrow();
        List<String> monitorsToExec =  hasSuccessfully ? monitor.getOnSuccess() : monitor.getOnFail();

        if (monitorsToExec == null || monitorsToExec.isEmpty()) {
            return new ArrayList<>();
        }
        logger.info("Event runned: {}", hasSuccessfully ? "onSuccess" : "onFail");
        logger.info("Found children of monitor to exec: {}", monitorsToExec);

        List<String> triggeredExecutionCreated = new CreateExecutionsByMonitorUseCase(executionRepository).execute(monitorsToExec);
        executionRepository.save(executionModel.withTriggered(triggeredExecutionCreated));
        return triggeredExecutionCreated;
    }
}
