package br.ufrn.imd.pastora.persistence.repository;

import br.ufrn.imd.pastora.persistence.MonitorModel;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;
import java.util.List;

public interface MonitorRepository extends CrudRepository<MonitorModel, String> {
    List<MonitorModel> findMonitorModelByEnabledEqualsAndIdNotIn(Boolean enabled, Collection<String> ids);
}
