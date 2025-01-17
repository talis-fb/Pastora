package br.ufrn.imd.pastora.mappers;

import br.ufrn.imd.pastora.domain.MonitorData;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MonitorMapper {
    MonitorModel toMonitorModel(MonitorData monitor);
    MonitorData fromMonitorModel(MonitorModel monitorModel);
}
