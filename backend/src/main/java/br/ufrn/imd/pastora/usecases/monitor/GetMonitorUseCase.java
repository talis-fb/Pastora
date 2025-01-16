package br.ufrn.imd.pastora.usecases.monitor;

import java.util.Optional;

import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMonitorUseCase {
  private final MonitorRepository monitorRepository;

  public Optional<MonitorModel> execute(String id, String userId) {
    final Optional<MonitorModel> findedMonitor = this.monitorRepository.findByIdAndUserId(id, userId);
    
    return findedMonitor;
  }
}
