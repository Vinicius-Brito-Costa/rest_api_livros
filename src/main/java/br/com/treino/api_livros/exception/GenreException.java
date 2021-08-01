package br.com.treino.api_livros.exception;

public class GenreException extends RuntimeException{
    public GenreException() {
    }

    public GenreException(String message) {
        super(message);
    }
}
