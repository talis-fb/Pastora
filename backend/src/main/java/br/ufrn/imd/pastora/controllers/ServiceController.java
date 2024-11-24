package br.ufrn.imd.pastora.controllers;

import br.ufrn.imd.pastora.domain.Service;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import br.ufrn.imd.pastora.usecases.CreateServiceUseCase;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("services")
@AllArgsConstructor
public class ServiceController {
    private ServiceRepository serviceRepository;

    @PostMapping
    public String createService(@Valid @RequestBody Service service) {
        return new CreateServiceUseCase(serviceRepository).execute(service);
    }
}
