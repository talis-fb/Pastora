package br.ufrn.imd.pastora.controllers.dto;

import br.ufrn.imd.pastora.domain.monitor.MonitorData;
import br.ufrn.imd.pastora.domain.monitor.MonitorValidation;
import br.ufrn.imd.pastora.domain.monitor.definition.HttpMonitorDefinition;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class CreateHttpMonitorDto {
    @NotNull
    MonitorData data;

    @NotNull
    HttpMonitorDefinition definition;

    @NotNull
    @NotEmpty
    List<MonitorValidation> validations;
}
