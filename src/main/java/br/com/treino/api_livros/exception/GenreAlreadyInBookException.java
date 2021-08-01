package br.com.treino.api_livros.exception;

public class GenreAlreadyInBookException extends GenreException{
    public GenreAlreadyInBookException() {
        this("Book already have this genre.");
    }

    public GenreAlreadyInBookException(String message) {
        super(message);
    }
}
