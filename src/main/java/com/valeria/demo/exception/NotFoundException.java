package com.valeria.demo.exception;
import com.valeria.demo.exception.ApiException;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {
    public NotFoundException() {
        this("Не найдено");
    }

    public NotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}