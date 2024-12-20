package br.ufrn.imd.pastora.persistence.repository;

import br.ufrn.imd.pastora.persistence.ServiceModel;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<ServiceModel, String> {
  public Optional<ServiceModel> findByIdAndUserId(String id, String userId);
  public Iterable<ServiceModel> findByUserId(String userId);
}
