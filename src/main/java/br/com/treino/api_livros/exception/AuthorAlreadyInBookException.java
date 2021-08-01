package br.com.treino.api_livros.exception;

import java.util.List;

public class AuthorAlreadyInBookException extends ApiLivrosBaseException{
    public AuthorAlreadyInBookException() {
        this(List.of("Book already have this author."));
    }

    public AuthorAlreadyInBookException(List<String> message) {
        super(message);
    }
}
