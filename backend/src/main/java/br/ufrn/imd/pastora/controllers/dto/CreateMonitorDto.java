package br.ufrn.imd.pastora.controllers.dto;

import br.ufrn.imd.pastora.domain.monitor.MonitorData;
import br.ufrn.imd.pastora.domain.monitor.MonitorValidation;
import br.ufrn.imd.pastora.domain.monitor.definition.AbstractMonitorDefinition;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Value;
import lombok.With;

import java.util.ArrayList;
import java.util.List;

@Value
public class CreateMonitorDto {
    @NotNull
    MonitorData data;

    @NotNull
    AbstractMonitorDefinition definition;

    @NotNull
    @NotEmpty
    List<MonitorValidation> validations;
}
