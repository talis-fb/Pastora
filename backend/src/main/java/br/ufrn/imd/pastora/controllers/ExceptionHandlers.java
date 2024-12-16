package br.ufrn.imd.pastora.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.ufrn.imd.pastora.exceptions.EntityNotFoundException;

@ControllerAdvice
public class ExceptionHandlers extends ResponseEntityExceptionHandler {
  @ExceptionHandler(EntityNotFoundException.class)
  private ResponseEntity<String> entityNotFoundHandler(EntityNotFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
  }
}
