package br.ufrn.imd.pastora.usecases.execution;

import br.ufrn.imd.pastora.components.HttpExecutor;
import br.ufrn.imd.pastora.domain.ExecutionData;
import br.ufrn.imd.pastora.persistence.ExecutionModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class GetOnFinishMonitorsOfExecutionUseCase {
    private final MonitorRepository monitorRepository;

    public List<String> execute(ExecutionModel executionModel) {
        boolean finishedSucceed = executionModel.getErrors().isEmpty();
        return monitorRepository
                .findById(executionModel.getMonitorId())
                .map(monitor -> finishedSucceed ? monitor.getOnSuccess() : monitor.getOnFail())
                .orElse(new ArrayList<>());
    }
}
