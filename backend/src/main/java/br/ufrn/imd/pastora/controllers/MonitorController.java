package br.ufrn.imd.pastora.controllers;

import br.ufrn.imd.pastora.controllers.dto.CreateHttpMonitorDto;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import br.ufrn.imd.pastora.usecases.monitor.CreateMonitorUseCase;
import br.ufrn.imd.pastora.usecases.monitor.GetMonitorUseCase;
import br.ufrn.imd.pastora.usecases.monitor.GetMonitorsByServiceUseCase;
import br.ufrn.imd.pastora.usecases.monitor.GetMonitorsUseCase;
import br.ufrn.imd.pastora.utils.AuthenticatedUserUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("monitors")
@AllArgsConstructor
public class MonitorController {
    private AuthenticatedUserUtils authenticatedUserUtils;
    private MonitorRepository monitorRepository;
    private ServiceRepository serviceRepository;

    @SneakyThrows
    @PostMapping("http")
    public ResponseEntity<String> createMonitor(@Valid @RequestBody CreateHttpMonitorDto monitorDto) {
        final String userId = authenticatedUserUtils.getAuthenticatedUserId();
        
        final String createdId = new CreateMonitorUseCase(
            monitorRepository
        ).execute(
            monitorDto.getData().withUserId(userId),
            monitorDto.getDefinition(),
            monitorDto.getValidations()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdId);
    }

    @SneakyThrows
    @GetMapping("http")
    public ResponseEntity<Iterable<MonitorModel>> getMonitors() {
        final String userId = authenticatedUserUtils.getAuthenticatedUserId();

        final Iterable<MonitorModel> finded = new GetMonitorsUseCase(
            monitorRepository
        ).execute(userId);

        return ResponseEntity.ok(finded);
    }

    @SneakyThrows
    @GetMapping("http/{id}")
    public ResponseEntity<MonitorModel> getMonitor(
        @PathVariable(required = true) String id    
    ) {
        final String userId = authenticatedUserUtils.getAuthenticatedUserId();

        final Optional<MonitorModel> finded = new GetMonitorUseCase(
            monitorRepository
        ).execute(id, userId);
        
        return ResponseEntity.of(finded);
    }

    @SneakyThrows
    @GetMapping("/service/{id}")
    public ResponseEntity<Iterable<MonitorModel>> getMonitorsByServiceId(
            @PathVariable(required = true) String id
    ) {
        final String userId = authenticatedUserUtils.getAuthenticatedUserId();

        final Iterable<MonitorModel> finded = new GetMonitorsByServiceUseCase(
            monitorRepository,
            serviceRepository
        ).execute(id, userId);

        return ResponseEntity.ok(finded);
    }    
}