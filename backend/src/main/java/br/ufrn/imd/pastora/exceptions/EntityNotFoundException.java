package br.ufrn.imd.pastora.exceptions;

public class EntityNotFoundException extends Exception{
  public EntityNotFoundException(String message) {
    super(message);
  }

  public EntityNotFoundException(String message, Throwable err) {
    super(message, err);
  }
}
