package br.ufrn.imd.pastora.usecases.service;

import br.ufrn.imd.pastora.mappers.ServiceMapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.ufrn.imd.pastora.components.PhotoStorageComponent;
import br.ufrn.imd.pastora.domain.Service;
import br.ufrn.imd.pastora.persistence.ServiceModel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateServiceUseCase {
    private final PhotoStorageComponent photoStorageComponent;
    private final ServiceMapper serviceMapper;

    @Transactional(rollbackFor = Exception.class)
    public String execute(Service service, MultipartFile photo) {
        if(photo != null && !photo.isEmpty()) {
            final String iconUrl = photoStorageComponent.storePhoto(photo);
            service.setIconUrl(iconUrl);
        }
        
        ServiceModel createdService = serviceMapper.toServiceModel(service); // serviceRepository.save(ServiceModel.fromEntity(service));
        return createdService.getId();
    }
}
