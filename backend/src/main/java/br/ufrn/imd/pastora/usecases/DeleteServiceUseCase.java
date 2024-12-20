package br.ufrn.imd.pastora.usecases;

import org.springframework.transaction.annotation.Transactional;

import br.ufrn.imd.pastora.components.PhotoStorageComponent;
import br.ufrn.imd.pastora.domain.Service;
import br.ufrn.imd.pastora.exceptions.EntityNotFoundException;
import br.ufrn.imd.pastora.persistence.ServiceModel;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteServiceUseCase {
  private final ServiceRepository serviceRepository;

  private final PhotoStorageComponent photoStorageComponent;

  @Transactional(rollbackFor = Exception.class)
  public Service execute(String serviceId, String userId) throws EntityNotFoundException{
    final ServiceModel serviceToDelete = this.serviceRepository.findByIdAndUserId(serviceId, userId)
      .orElseThrow(() -> new EntityNotFoundException("no service found with this ID!"));

    final String iconToDeleteUrl = serviceToDelete.getIconUrl();

    if(iconToDeleteUrl != null && !iconToDeleteUrl.isBlank())
      this.photoStorageComponent.deletePhoto(iconToDeleteUrl);
    
    this.serviceRepository.delete(serviceToDelete);
    return serviceToDelete.toEntity();
  }
}
