package br.ufrn.imd.pastora.controllers;

import br.ufrn.imd.pastora.components.PhotoStorageComponent;
import br.ufrn.imd.pastora.domain.Service;
import br.ufrn.imd.pastora.exceptions.BusinessException;
import br.ufrn.imd.pastora.exceptions.EntityNotFoundException;
import br.ufrn.imd.pastora.exceptions.UserNotAuthenticatedException;
import br.ufrn.imd.pastora.mappers.ServiceMapper;
import br.ufrn.imd.pastora.persistence.ServiceModel;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import br.ufrn.imd.pastora.usecases.service.CreateServiceUseCase;
import br.ufrn.imd.pastora.usecases.service.DeleteServiceUseCase;
import br.ufrn.imd.pastora.usecases.service.GetServiceByNameText;
import br.ufrn.imd.pastora.usecases.service.GetServiceIconUseCase;
import br.ufrn.imd.pastora.usecases.service.GetServiceUseCase;
import br.ufrn.imd.pastora.usecases.service.GetServicesUseCase;
import br.ufrn.imd.pastora.usecases.service.UpdateServiceUseCase;
import br.ufrn.imd.pastora.utils.AuthenticatedUserUtils;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final ServiceMapper serviceMapper;

    private final Logger logger = LoggerFactory.getLogger(ServiceController.class);


    @PostMapping
    public ResponseEntity<String> createService(
        @RequestParam(required = true) String name,
        @RequestParam (required = false) String description,
        @RequestParam (required = false) MultipartFile icon
    ) throws UserNotAuthenticatedException, BusinessException {
        logger.info("Creating new service");
        final String userId = this.authenticatedUserUtils.getAuthenticatedUserId();
        logger.info("User ID: {}", userId);

        this.validatePhotoType(icon);
        final Service service = Service.builder()
            .name(name)
            .description(description)
            .iconUrl(null)
            .userId(userId)
            .build();

        logger.info("Service to create {}, {}", service.getName(), service.getDescription());


        final String createdServiceId = new CreateServiceUseCase(
            serviceRepository,
            photoStorageComponent,
            serviceMapper
        ).execute(service, icon);

        logger.info("Created Service ID: {}", createdServiceId);


        return ResponseEntity.status(HttpStatus.CREATED).body(createdServiceId);
    }

    @SneakyThrows
    @PutMapping
    public ResponseEntity<Service> updateService(
        @RequestParam(required = true) String id,
        @RequestParam(required = true) String name,
        @RequestParam (required = false) String description,
        @RequestParam (required = false) MultipartFile icon        
    ) {
        logger.debug("Updating service: {}", id);
        final String userId = this.authenticatedUserUtils.getAuthenticatedUserId();

        this.validatePhotoType(icon);
        final Service service = Service.builder()
                .name(name)
                .description(description)
                .iconUrl(null)
                .build();

        final Service updated = new UpdateServiceUseCase(
            serviceRepository,
            photoStorageComponent,
            serviceMapper
        ).execute(id, service, icon, userId);

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
            photoStorageComponent,
            serviceMapper
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
