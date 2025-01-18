package br.ufrn.imd.pastora.persistence.repository;

import br.ufrn.imd.pastora.persistence.ExecutionModel;
import org.springframework.data.domain.Limit;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ExecutionRepository extends CrudRepository<ExecutionModel, String> {
  Iterable<ExecutionModel> findByMonitorId(String monitorId);
  List<ExecutionModel> findByMonitorIdOrderByStartedTimeAsc(String monitorId, Limit limit);
}
