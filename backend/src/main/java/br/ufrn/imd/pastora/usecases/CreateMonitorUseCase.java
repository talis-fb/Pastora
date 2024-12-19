package br.ufrn.imd.pastora.usecases;

import br.ufrn.imd.pastora.domain.monitor.MonitorData;
import br.ufrn.imd.pastora.domain.monitor.MonitorValidation;
import br.ufrn.imd.pastora.domain.monitor.definition.AbstractMonitorDefinition;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
public class CreateMonitorUseCase {
    private final MonitorRepository monitorRepository;

    @Transactional
    public String execute(MonitorData monitor, AbstractMonitorDefinition definition, List<MonitorValidation> validations) {
        MonitorModel monitorModel = MonitorModel.fromMonitorData(monitor).withDefinition(definition).withValidations(validations);
        MonitorModel createdMonitor = monitorRepository.save(monitorModel);

        return createdMonitor.getId();
    }
}
