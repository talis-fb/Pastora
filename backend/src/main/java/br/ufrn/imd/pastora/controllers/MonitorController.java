package br.ufrn.imd.pastora.controllers;

import br.ufrn.imd.pastora.controllers.dto.CreateHttpMonitorDto;
import br.ufrn.imd.pastora.persistence.MonitorModel;
import br.ufrn.imd.pastora.persistence.repository.MonitorRepository;
import br.ufrn.imd.pastora.usecases.CreateMonitorUseCase;
import br.ufrn.imd.pastora.usecases.GetMonitorUseCase;
import br.ufrn.imd.pastora.usecases.GetMonitorsByServiceUseCase;
import br.ufrn.imd.pastora.usecases.GetMonitorsUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

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
    private MonitorRepository monitorRepository;

    @PostMapping("http")
    public ResponseEntity<String> createMonitor(@Valid @RequestBody CreateHttpMonitorDto monitorDto) {
        final String createdId = new CreateMonitorUseCase(
            monitorRepository
        ).execute(
            monitorDto.getData(),
            monitorDto.getDefinition(),
            monitorDto.getValidations()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(createdId);
    }

    @GetMapping("http")
    public ResponseEntity<Iterable<MonitorModel>> getMonitors() {
        final Iterable<MonitorModel> finded = new GetMonitorsUseCase(
            monitorRepository
        ).execute();

        return ResponseEntity.ok(finded);
    }

    @GetMapping("http/{id}")
    public ResponseEntity<MonitorModel> getMonitor(
        @PathVariable(required = true) String id    
    ) {
        final Optional<MonitorModel> finded = new GetMonitorUseCase(
            monitorRepository
        ).execute(id);
        
        return ResponseEntity.of(finded);
    }

    @GetMapping("/service/{id}")
    public ResponseEntity<Iterable<MonitorModel>> getMonitorsByServiceId(
            @PathVariable(required = true) String id
    ) {
        final Iterable<MonitorModel> finded = new GetMonitorsByServiceUseCase(
            monitorRepository
        ).execute(id);

        return ResponseEntity.ok(finded);
    }    
}