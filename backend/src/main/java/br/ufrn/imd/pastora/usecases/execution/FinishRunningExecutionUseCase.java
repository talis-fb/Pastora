package br.ufrn.imd.pastora.usecases.execution;

import br.ufrn.imd.pastora.domain.ExecutionData;
import br.ufrn.imd.pastora.persistence.ExecutionModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
public class FinishRunningExecutionUseCase {
    private final ExecutionRepository executionRepository;

    public void execute(ExecutionModel executionModel) {
        executionRepository.save(executionModel.withStatus(ExecutionData.Status.FINISHED));
    }
}
