package br.ufrn.imd.pastora.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.ufrn.imd.pastora.exceptions.BusinessException;
import br.ufrn.imd.pastora.exceptions.EntityNotFoundException;
import br.ufrn.imd.pastora.exceptions.InvalidCredentialsException;
import br.ufrn.imd.pastora.exceptions.UserNotAuthenticatedException;

@ControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {
  @ExceptionHandler(EntityNotFoundException.class)
  private ResponseEntity<String> entityNotFoundHandler(EntityNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }

  @ExceptionHandler(BusinessException.class)
  private ResponseEntity<String> businessExceptionHandler(BusinessException exception) {
    return ResponseEntity.badRequest().body(exception.getMessage());
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  private ResponseEntity<String> invalidCredentialsExceptionHandler(InvalidCredentialsException exception) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
  }

  @ExceptionHandler(UserNotAuthenticatedException.class)
  private ResponseEntity<String> userNotAuthenticatedExceptionHandler(UserNotAuthenticatedException exception) {
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
  }  
}
