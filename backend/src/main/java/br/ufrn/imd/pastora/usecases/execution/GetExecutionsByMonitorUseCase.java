package br.ufrn.imd.pastora.usecases.execution;

import br.ufrn.imd.pastora.exceptions.EntityNotFoundException;
import br.ufrn.imd.pastora.persistence.ExecutionModel;
import br.ufrn.imd.pastora.persistence.repository.ExecutionRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;

@RequiredArgsConstructor
public class GetExecutionsByMonitorUseCase {
  private final MonitorRepository monitorRepository;
  private final ExecutionRepository executionRepository;

  public Iterable<ExecutionModel> execute(String monitorId, String userId, int limit) throws EntityNotFoundException{
    final boolean monitorExists = this.monitorRepository.existsByIdAndUserId(monitorId, userId);

    if(!monitorExists)
      throw new EntityNotFoundException("No monitor found with this id");
    
    return this.executionRepository.findByMonitorIdOrderByStartedTimeAsc(monitorId, Limit.of(limit));
  }
}
