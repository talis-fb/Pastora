package br.ufrn.imd.pastora.usecases;

import br.ufrn.imd.pastora.persistence.ServiceModel;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetServiceByNameText {
  private final ServiceRepository repository;

  public Iterable<ServiceModel> execute(String search, String userId) {
    return this.repository.findByUserIdAndNameContainingIgnoreCase(userId, search);
  }
}
