package br.ufrn.imd.pastora.usecases;

import java.util.Optional;

import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMonitorUseCase {
  private final MonitorRepository monitorRepository;

  public Optional<MonitorModel> execute(String id) {
    final Optional<MonitorModel> findedMonitor = this.monitorRepository.findById(id);
    
    return findedMonitor;
  }
}
