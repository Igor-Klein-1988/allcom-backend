package de.allcom.exceptions;


import de.allcom.dto.StandardResponseDto;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import jakarta.mail.MessagingException;
//import org.springframework.mail.MailAuthenticationException;
//import org.springframework.mail.MailParseException;
//import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

public class GlobalExceptionHandler {

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handlerNullPointerException(NullPointerException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<String> handlerSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleControllerViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<StandardResponseDto> handlerNotFoundException(NotFoundException e) {
        StandardResponseDto responseDto = new StandardResponseDto(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDto);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<StandardResponseDto> handleUnauthorizedException(UnauthorizedException e) {
        StandardResponseDto responseDto = new StandardResponseDto(e.getMessage(), LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(responseDto);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleIOException(){
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("IO Error");
    }
//    @ExceptionHandler(IsAlreadyExistException.class)
//    public ResponseEntity<String> handlerIsAlreadyExistException(IsAlreadyExistException e) {
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//    @ExceptionHandler(MailAuthenticationException.class)
//    public ResponseEntity<String> handleMailAuthenticationException(MailAuthenticationException e){
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                .body("Mail Authentication Error");
//    }
//
//    @ExceptionHandler(MailSendException.class)
//    public ResponseEntity<String> handleMailSendException(MailSendException e){
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Mail Send Error");
//    }

//    @ExceptionHandler(MailParseException.class)
//    public ResponseEntity<String> handleMailParseException(MailParseException e){
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                .body("Mail Parse Error");
//    }
//    @ExceptionHandler(MessagingException.class)
//    public ResponseEntity<String> handleMessagingException(MessagingException e){
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                .body("Messaging Error");
//    }

//    @ExceptionHandler(InvalidJwtException.class)
//    public ResponseEntity<String> handlerInvalidJwtException(InvalidJwtException e){
//        return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
//    }
}
