package br.com.treino.api_livros.config;

import br.com.treino.api_livros.controller.dto.response.ErrorResponseDTO;
import br.com.treino.api_livros.exception.ApiLivrosBaseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ApiLivrosBaseException.class)
    public ResponseEntity<?> handleHub(Throwable ex){
        System.out.println(ex.getClass());
        if(ex instanceof ApiLivrosBaseException){
            ApiLivrosBaseException exception = (ApiLivrosBaseException) ex;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO(exception.getMessages()));
        }
        if (ex instanceof HttpMessageNotReadableException){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponseDTO(List.of("Json structure is invalid")));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDTO(List.of(ex.getMessage())));
    }
}
