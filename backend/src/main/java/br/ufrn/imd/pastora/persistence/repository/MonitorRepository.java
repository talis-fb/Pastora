package br.ufrn.imd.pastora.persistence.repository;

import br.ufrn.imd.pastora.persistence.MonitorModel;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

public interface MonitorRepository extends CrudRepository<MonitorModel, String> {
  Iterable<MonitorModel> findByServiceIdAndUserId(String serviceId, String userId);
  Iterable<MonitorModel> findByUserId(String userId);
  Optional<MonitorModel> findByIdAndUserId(String id, String userId);
}
