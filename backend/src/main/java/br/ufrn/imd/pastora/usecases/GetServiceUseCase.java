package br.ufrn.imd.pastora.usecases;

import java.util.Optional;

import br.ufrn.imd.pastora.persistence.ServiceModel;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetServiceUseCase {
  private final ServiceRepository serviceRepository;

  public Optional<ServiceModel> execute(String id) {
    return this.serviceRepository.findById(id);
  }
}
