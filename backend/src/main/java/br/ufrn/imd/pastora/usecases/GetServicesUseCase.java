package br.ufrn.imd.pastora.usecases;

import br.ufrn.imd.pastora.persistence.ServiceModel;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetServicesUseCase {
  private final ServiceRepository serviceRepository;

  public Iterable<ServiceModel> execute() {
    return serviceRepository.findAll();    
  }
}
