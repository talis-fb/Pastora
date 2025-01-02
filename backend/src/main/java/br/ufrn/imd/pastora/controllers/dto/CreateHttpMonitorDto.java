package br.ufrn.imd.pastora.controllers.dto;

import br.ufrn.imd.pastora.domain.MonitorData;
import br.ufrn.imd.pastora.domain.MonitorHttpDefinition;
import br.ufrn.imd.pastora.domain.MonitorValidation;
import jakarta.validation.constraints.NotNull;
import lombok.Value;

import java.util.List;

@Value
public class CreateHttpMonitorDto {
    @NotNull
    MonitorData data;

    @NotNull
    MonitorHttpDefinition definition;

    @NotNull
    List<MonitorValidation> validations;
}
