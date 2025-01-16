package br.ufrn.imd.pastora.usecases.execution;


import br.ufrn.imd.pastora.domain.ExecutionData;
import br.ufrn.imd.pastora.persistence.ExecutionModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CreateExecutionsByMonitorUseCase {
    private final ExecutionRepository executionRepository;

    public List<String> execute(List<String> monitorIds) {
        List<ExecutionModel> models = monitorIds.stream()
                .map(monitorId -> ExecutionModel.builder()
                        .monitorId(monitorId)
                        .status(ExecutionData.Status.RUNNING)
                        .build()
                )
                .toList();

        List<String> executionIds = new ArrayList<>();
        Iterable<ExecutionModel> modelsSaved = executionRepository.saveAll(models);
        modelsSaved.forEach(model -> executionIds.add(model.getId()));

        return executionIds;
    }
}
