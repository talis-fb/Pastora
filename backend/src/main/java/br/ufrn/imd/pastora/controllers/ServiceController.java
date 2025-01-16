package br.ufrn.imd.pastora.controllers;

import br.ufrn.imd.pastora.components.PhotoStorageComponent;
import br.ufrn.imd.pastora.domain.Service;
import br.ufrn.imd.pastora.exceptions.BusinessException;
import br.ufrn.imd.pastora.exceptions.EntityNotFoundException;
import br.ufrn.imd.pastora.persistence.ServiceModel;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import br.ufrn.imd.pastora.usecases.CreateServiceUseCase;
import br.ufrn.imd.pastora.usecases.DeleteServiceUseCase;
import br.ufrn.imd.pastora.usecases.GetServiceByNameText;
import br.ufrn.imd.pastora.usecases.GetServiceIconUseCase;
import br.ufrn.imd.pastora.usecases.GetServiceUseCase;
import br.ufrn.imd.pastora.usecases.GetServicesUseCase;
import br.ufrn.imd.pastora.usecases.UpdateServiceUseCase;
import br.ufrn.imd.pastora.utils.AuthenticatedUserUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
    private AuthenticatedUserUtils authenticatedUserUtils;

    private ServiceRepository serviceRepository;

    private PhotoStorageComponent photoStorageComponent;

    @SneakyThrows
    @PostMapping
    public ResponseEntity<String> createService(
        @RequestParam(required = true) String name,
        @RequestParam (required = false) String description,
        @RequestParam (required = false) MultipartFile photo
    ) {
        final String userId = this.authenticatedUserUtils.getAuthenticatedUserId();

        this.validatePhotoType(photo);
        final Service service = Service.builder()
            .name(name)
            .description(description)
            .iconUrl(null)
            .userId(userId)
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
        final String userId = this.authenticatedUserUtils.getAuthenticatedUserId();

        this.validatePhotoType(photo);
        final Service service = Service.builder()
                .name(name)
                .description(description)
                .iconUrl(null)
                .build();

        final Service updated = new UpdateServiceUseCase(
            serviceRepository,
            photoStorageComponent
        ).execute(id, service, photo, userId);

        return ResponseEntity.ok(updated);
    }

    @SneakyThrows
    @DeleteMapping("/{id}")
    public ResponseEntity<Service> deleteService(
        @PathVariable(required = true) String id
    ) {
        final String userId = this.authenticatedUserUtils.getAuthenticatedUserId();

        final Service deletedService = new DeleteServiceUseCase(
            serviceRepository,
            photoStorageComponent
        ).execute(id, userId);

        return ResponseEntity.ok(deletedService);
    }

    @SneakyThrows
    @GetMapping("/{id}")
    public ResponseEntity<ServiceModel> getServiceById(
        @PathVariable(required = true) String id
    ) {
        final String userId = this.authenticatedUserUtils.getAuthenticatedUserId();

        final Optional<ServiceModel> finded = new GetServiceUseCase(
            serviceRepository
        ).execute(id, userId);

        return ResponseEntity.of(finded);
    }

    @SneakyThrows
    @GetMapping
    public ResponseEntity<Iterable<ServiceModel>> getAllServices(
        @RequestParam(required = false) String name
    ) {
        final String userId = this.authenticatedUserUtils.getAuthenticatedUserId();
        Iterable<ServiceModel> services = null;
        if (name != null && !name.isEmpty()) {
            services = new GetServiceByNameText(serviceRepository).execute(name, userId);
        } else {
            services = new GetServicesUseCase(serviceRepository).execute(userId);
        }

        return ResponseEntity.ok(services);
    }

    @SneakyThrows
    @GetMapping("/download-icon/{fileName}")
    public ResponseEntity<Resource> getPhoto(@PathVariable String fileName) {
        // Caminho completo do arquivo
        Path filePath = Paths.get(photoStorageComponent.getFilePath(fileName)).normalize();

        // Verificar se o arquivo existe e é legível
        if (!Files.exists(filePath) || !Files.isReadable(filePath)) {
            throw new EntityNotFoundException("Arquivo não encontrado ou não pode ser lido: " + fileName);
        }

        // Criar um recurso a partir do arquivo
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            throw new EntityNotFoundException("Recurso não encontrado: " + fileName);
        }

        // Detectar o tipo MIME do arquivo
        String mimeType = Files.probeContentType(filePath);
        if (mimeType == null) {
            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }

        // Construir a resposta
        return ResponseEntity.ok()
                //.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .header("attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType(mimeType))
                .body(resource);
    }
   

    private void validatePhotoType(MultipartFile photo) throws BusinessException{
        if(photo != null && !photo.isEmpty()) {
            String contentType = photo.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                throw new BusinessException("O arquivo enviado não é uma imagem válida.");
            }
        }
    }
}
