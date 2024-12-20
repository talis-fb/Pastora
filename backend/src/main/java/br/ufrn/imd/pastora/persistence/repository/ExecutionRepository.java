package br.ufrn.imd.pastora.persistence.repository;

import br.ufrn.imd.pastora.persistence.ExecutionModel;
import org.springframework.data.repository.CrudRepository;

public interface ExecutionRepository extends CrudRepository<ExecutionModel, String> {

}
