package br.ufrn.imd.pastora.usecases;

import br.ufrn.imd.pastora.components.PhotoStorageComponent;
import br.ufrn.imd.pastora.exceptions.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GetServiceIconUseCase {
  private final PhotoStorageComponent photoStorageComponent;

  public byte[] execute(String fileName) throws EntityNotFoundException{
    return photoStorageComponent.downloadPhoto(fileName);
  }
}
