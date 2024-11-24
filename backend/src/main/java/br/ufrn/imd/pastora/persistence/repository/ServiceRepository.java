package br.ufrn.imd.pastora.persistence.repository;

import br.ufrn.imd.pastora.persistence.ServiceModel;
import org.springframework.data.repository.CrudRepository;

public interface ServiceRepository extends CrudRepository<ServiceModel, String> {
}
