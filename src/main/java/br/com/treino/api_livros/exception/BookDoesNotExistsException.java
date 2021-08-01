package br.com.treino.api_livros.exception;

import java.util.List;

public class BookDoesNotExistsException extends ApiLivrosBaseException{
    public BookDoesNotExistsException() {
        super(List.of("Book does not exists"));
    }
}
