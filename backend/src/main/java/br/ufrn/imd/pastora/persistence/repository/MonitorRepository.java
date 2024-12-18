package br.ufrn.imd.pastora.persistence.repository;

import br.ufrn.imd.pastora.persistence.MonitorModel;
import org.springframework.data.repository.CrudRepository;

public interface MonitorRepository extends CrudRepository<MonitorModel, String> {
  Iterable<MonitorModel> findByServiceId(String serviceId);
}
