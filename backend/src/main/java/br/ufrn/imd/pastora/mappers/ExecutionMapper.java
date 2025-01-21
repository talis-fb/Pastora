package br.ufrn.imd.pastora.mappers;


import br.ufrn.imd.pastora.domain.ExecutionData;
import br.ufrn.imd.pastora.persistence.ExecutionModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ExecutionMapper {
    ExecutionModel toExecutionModel(ExecutionData execution);
    ExecutionData fromExecutionModel(ExecutionModel executionModel);
}
