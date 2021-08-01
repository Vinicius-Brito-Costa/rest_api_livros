package br.com.treino.api_livros.exception;

import java.util.List;

public class BookAlreadyExistsException extends ApiLivrosBaseException {
    public BookAlreadyExistsException() {
        this(List.of("ERROR: Book already exists"));
    }

    public BookAlreadyExistsException(List<String> errorList) {
        super(errorList);
    }
}
