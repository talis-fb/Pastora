package br.ufrn.imd.pastora.usecases;

import br.ufrn.imd.pastora.exceptions.EntityNotFoundException;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetMonitorsByServiceUseCase {
  private final MonitorRepository monitorRepository;
  private final ServiceRepository serviceRepository;

  public Iterable<MonitorModel> execute(String serviceId, String userId) throws EntityNotFoundException {
    final boolean serviceExists = serviceRepository.existsByIdAndUserId(serviceId, userId);

    if(!serviceExists)
      throw new EntityNotFoundException("no service found with this ID!");

    return monitorRepository.findByServiceIdAndUserId(serviceId, userId);
  }
}
