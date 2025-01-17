package br.ufrn.imd.pastora.mappers;

import br.ufrn.imd.pastora.domain.Service;
import br.ufrn.imd.pastora.persistence.ServiceModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceModel toServiceModel(Service service);
    Service fromServiceModel(ServiceModel serviceModel);
}
