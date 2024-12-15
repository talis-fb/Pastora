package br.ufrn.imd.pastora.controllers;

import br.ufrn.imd.pastora.components.PhotoStorageComponent;
import br.ufrn.imd.pastora.domain.Service;
import br.ufrn.imd.pastora.persistence.ServiceModel;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import br.ufrn.imd.pastora.usecases.CreateServiceUseCase;
import br.ufrn.imd.pastora.usecases.DeleteServiceUseCase;
import br.ufrn.imd.pastora.usecases.GetServiceIconUseCase;
import br.ufrn.imd.pastora.usecases.GetServiceUseCase;
import br.ufrn.imd.pastora.usecases.GetServicesUseCase;
import br.ufrn.imd.pastora.usecases.UpdateServiceUseCase;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.util.Optional;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("services")
@AllArgsConstructor
public class ServiceController {
    private ServiceRepository serviceRepository;

    private PhotoStorageComponent photoStorageComponent;

    @PostMapping
    public ResponseEntity<String> createService(
        @RequestParam(required = true) String name,
        @RequestParam (required = false) String description,
        @RequestParam (required = false) MultipartFile photo
    ) {
        final Service service = Service.builder()
            .name(name)
            .description(description)
            .iconUrl(null)
            .build();

        final String createdServiceId = new CreateServiceUseCase(
            serviceRepository,
            photoStorageComponent
        ).execute(service, photo);

        return ResponseEntity.status(HttpStatus.CREATED).body(createdServiceId);
    }

    @SneakyThrows
    @PutMapping
    public ResponseEntity<Service> updateService(
        @RequestParam(required = true) String id,
        @RequestParam(required = true) String name,
        @RequestParam (required = false) String description,
        @RequestParam (required = false) MultipartFile photo        
    ) {
        final Service service = Service.builder()
                .name(name)
                .description(description)
                .iconUrl(null)
                .build();

        final Service updated = new UpdateServiceUseCase(
            serviceRepository,
            photoStorageComponent
        ).execute(id, service, photo);

        return ResponseEntity.ok(updated);
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity<Service> deleteService(
        @PathVariable(required = true) String id
    ) {
        final Service deletedService = new DeleteServiceUseCase(
            serviceRepository,
            photoStorageComponent
        ).execute(id);

        return ResponseEntity.ok(deletedService);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceModel> getServiceById(
        @PathVariable(required = true) String id
    ) {
        final Optional<ServiceModel> finded = new GetServiceUseCase(
            serviceRepository
        ).execute(id);

        return ResponseEntity.of(finded);
    }

    @GetMapping
    public ResponseEntity<Iterable<ServiceModel>> getAllServices() {
        final var services = new GetServicesUseCase(serviceRepository).execute();
        return ResponseEntity.ok(services);
    }

    @SneakyThrows
    @GetMapping("/download-icon/{fileName}")
    public ResponseEntity<byte[]> getPhoto(
        @PathVariable String fileName
    ) {
        byte[] photoBytes = new GetServiceIconUseCase(photoStorageComponent).execute(fileName);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.IMAGE_PNG)
                .body(photoBytes);
    }    
}
