package br.ufrn.imd.pastora.exceptions;

public class UserNotAuthenticatedException extends Exception {
  public UserNotAuthenticatedException(String message) {
    super(message);
  }

  public UserNotAuthenticatedException(String message, Throwable error) {
    super(message, error);
  }
}
