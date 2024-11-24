package br.ufrn.imd.pastora.usecases;

import br.ufrn.imd.pastora.domain.Service;
import br.ufrn.imd.pastora.persistence.ServiceModel;
import br.ufrn.imd.pastora.persistence.repository.ServiceRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CreateServiceUseCase {
    private final ServiceRepository serviceRepository;

    public String execute(Service service) {
        ServiceModel createdService = serviceRepository.save(ServiceModel.fromEntity(service));
        return createdService.getId();
    }
}
