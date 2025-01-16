package br.ufrn.imd.pastora.usecases.monitor;

import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMonitorsUseCase {
  private final MonitorRepository monitorRepository;

  public Iterable<MonitorModel> execute(String userId) {
      return this.monitorRepository.findByUserId(userId);
  }
}
