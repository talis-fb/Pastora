package br.ufrn.imd.pastora.exceptions;

public class InvalidCredentialsException extends Exception{
  public InvalidCredentialsException(String message) {
    super(message);
  }

  public InvalidCredentialsException(String message, Throwable err) {
    super(message, err);
  }  
}
