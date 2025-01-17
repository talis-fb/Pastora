package br.ufrn.imd.pastora.usecases.monitor;

import br.ufrn.imd.pastora.domain.MonitorData;
import br.ufrn.imd.pastora.mappers.MonitorMapper;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class CreateMonitorUseCase {
    private final MonitorRepository monitorRepository;
    private final MonitorMapper monitorMapper;

    @Transactional
    public String execute(MonitorData monitorData) {
        MonitorModel monitorModel = monitorMapper.toMonitorModel(monitorData);
        MonitorModel createdMonitor = monitorRepository.save(monitorModel);
        return createdMonitor.getId();
    }
}
