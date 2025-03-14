package nl.fontys.s3.ticketmaster.controller;

import nl.fontys.s3.ticketmaster.business.exception.InvalidCompetitionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CompetitionControllerAdvice {
    @ExceptionHandler(InvalidCompetitionException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCompetitionException(InvalidCompetitionException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("Invalid Request", ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Internal Server Error", "An unexpected error occurred."));
    }
}