package br.ufrn.imd.pastora.usecases;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.ufrn.imd.pastora.components.PhotoStorageComponent;
import br.ufrn.imd.pastora.domain.Service;
import br.ufrn.imd.pastora.exceptions.EntityNotFoundException;
import br.ufrn.imd.pastora.persistence.ServiceModel;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateServiceUseCase {
  private final ServiceRepository serviceRepository;

  private final PhotoStorageComponent photoStorageComponent;


  @Transactional(rollbackFor = Exception.class)
  public Service execute(String id, Service service, MultipartFile photo) throws EntityNotFoundException{
    final ServiceModel finded = this.serviceRepository.findById(id)
      .orElseThrow(() -> new EntityNotFoundException("no service found with this ID!"));
      
    // Remove o ícone antigo
    final String oldIconUrl = finded.getIconUrl();

    if (oldIconUrl != null && !oldIconUrl.isBlank())
      this.photoStorageComponent.deletePhoto(oldIconUrl);

    // Salva o novo ícone
    String newIconUrl = null;

    if(photo != null && !photo.isEmpty()) {
      newIconUrl = this.photoStorageComponent.storePhoto(photo);
    }

    final ServiceModel serviceToUpdate = finded.withName(service.getName())
      .withDescription(service.getDescription())
      .withIconUrl(newIconUrl);

    final ServiceModel updated = serviceRepository.save(serviceToUpdate);
    return updated.toEntity();
  } 
}
