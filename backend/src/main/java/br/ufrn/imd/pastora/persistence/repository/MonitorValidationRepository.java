package br.ufrn.imd.pastora.persistence.repository;

import br.ufrn.imd.pastora.persistence.MonitorValidationModel;
import org.springframework.data.repository.CrudRepository;

public interface MonitorValidationRepository extends CrudRepository<MonitorValidationModel, String> {
}
