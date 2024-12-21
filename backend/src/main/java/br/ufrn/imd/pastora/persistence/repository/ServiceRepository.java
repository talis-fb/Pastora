package br.ufrn.imd.pastora.persistence.repository;

import br.ufrn.imd.pastora.persistence.ServiceModel;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<ServiceModel, String> {
  public Optional<ServiceModel> findByIdAndUserId(String id, String userId);
  public Iterable<ServiceModel> findByUserId(String userId);
  public boolean existsByIdAndUserId(String id, String userId);
  
  // Busca textual e filtragem pelo userId
  public Iterable<ServiceModel> findByUserIdAndNameContainingIgnoreCase(String userId, String name);

  // Busca textual e filtragem por userId
  // @Query("{ $and: [ { $text: { $search: ?0 } }, { userId: ?1 } ] }")
  // public Iterable<ServiceModel> searchByTextAndUserId(String text, String userId);
}
