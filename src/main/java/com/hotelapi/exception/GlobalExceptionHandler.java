package com.hotelapi.exception;

import com.hotelapi.dto.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse<Object>> handleResourceNotFound(ResourceNotFoundException ex) {
        GenericResponse<Object> response = GenericResponse.fail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<GenericResponse<Object>> handleNoDataFound(NoDataFoundException ex) {
        GenericResponse<Object> response = GenericResponse.fail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<GenericResponse<Object>> handleInvalidInput(InvalidInputException ex) {
        GenericResponse<Object> response = GenericResponse.fail(ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(BillNotFoundException.class)
    public ResponseEntity<GenericResponse<Object>> handleBillNotFound(BillNotFoundException ex) {
        GenericResponse<Object> response = GenericResponse.fail(ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InvalidUpdateException.class)
    public ResponseEntity<GenericResponse<Object>> handleInvalidUpdate(InvalidUpdateException ex) {
        GenericResponse<Object> response = GenericResponse.fail(ex.getMessage());
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<GenericResponse<Object>> handleDatabaseException(DatabaseException ex) {
        GenericResponse<Object> response = GenericResponse.fail("A database error occurred: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Object>> handleGlobalException(Exception ex) {
        GenericResponse<Object> response = GenericResponse.fail("Internal Server Error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
