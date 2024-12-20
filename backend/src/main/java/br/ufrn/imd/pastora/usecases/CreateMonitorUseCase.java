package br.ufrn.imd.pastora.usecases;

import br.ufrn.imd.pastora.domain.MonitorAbstractDefinition;
import br.ufrn.imd.pastora.domain.MonitorData;
import br.ufrn.imd.pastora.domain.MonitorHttpDefinition;
import br.ufrn.imd.pastora.domain.MonitorValidation;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.MonitorValidationModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorValidationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CreateMonitorUseCase {
    private final MonitorRepository monitorRepository;
    private final MonitorValidationRepository monitorValidationRepository;

    @Transactional
    public String execute(MonitorData monitor, MonitorHttpDefinition definition, List<MonitorValidation> validations) {
        List<String> validationIds = new ArrayList<>();

        List<MonitorValidationModel> validationModels = validations.stream().map(MonitorValidationModel::fromEntity).toList();
        monitorValidationRepository.saveAll(validationModels).forEach(model -> validationIds.add(model.getId()));

        MonitorModel monitorModel = MonitorModel.fromMonitorData(monitor).withDefinition(definition).withValidations(validationIds);
        MonitorModel createdMonitor = monitorRepository.save(monitorModel);

        return createdMonitor.getId();
    }
}
