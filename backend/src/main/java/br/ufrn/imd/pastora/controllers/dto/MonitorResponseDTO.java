package br.ufrn.imd.pastora.controllers.dto;

import java.util.List;

import br.ufrn.imd.pastora.domain.monitor.MonitorData;
import br.ufrn.imd.pastora.domain.monitor.definition.AbstractMonitorDefinition;
import br.ufrn.imd.pastora.persistence.MonitorValidationModel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MonitorResponseDTO {
    private String id;
    private String name;
    private String description;
    private Boolean enabled;
    private String userId;
    private String serviceId;
    private List<String> onSuccess;
    private List<String> onFail;
    private MonitorData.SaveSuccessWhen saveSuccessWhen; 
    
    private AbstractMonitorDefinition definition;

    private List<MonitorValidationModel> validations;
}
