package br.ufrn.imd.pastora.exceptions;

public class BusinessException extends Exception{
  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(String message, Throwable err) {
    super(message, err);
  }  
}
