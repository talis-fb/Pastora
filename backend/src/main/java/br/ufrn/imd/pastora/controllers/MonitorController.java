package br.ufrn.imd.pastora.controllers;

import br.ufrn.imd.pastora.controllers.dto.CreateHttpMonitorDto;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import br.ufrn.imd.pastora.persistence.repository.MonitorValidationRepository;
import br.ufrn.imd.pastora.usecases.CreateMonitorUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("monitors")
@AllArgsConstructor
public class MonitorController {
    private MonitorRepository monitorRepository;
    private MonitorValidationRepository monitorValidationRepository;

    @PostMapping("http")
    public String createMonitor(@Valid @RequestBody CreateHttpMonitorDto monitorDto) {
        return new CreateMonitorUseCase(
            monitorRepository,
            monitorValidationRepository
        ).execute(
            monitorDto.getData(),
            monitorDto.getDefinition(),
            monitorDto.getValidations()
        );
    }
}
