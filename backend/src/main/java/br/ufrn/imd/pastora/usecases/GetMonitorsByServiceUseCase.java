package br.ufrn.imd.pastora.usecases;

import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMonitorsByServiceUseCase {
  private final MonitorRepository monitorRepository;

  public Iterable<MonitorModel> execute(String serviceId) {
    return monitorRepository.findByServiceId(serviceId);
  }
}
