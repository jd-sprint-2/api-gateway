package com.example.api_gateway.controller;

import com.example.api_gateway.dto.ErrorResponseDto;
import com.example.api_gateway.exceptions.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponseDto> handleEntityNotFoundException(UnauthorizedException exception) {
        ErrorResponseDto errorResponseDto = new ErrorResponseDto(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(errorResponseDto);
    }
}