package br.com.treino.api_livros.exception;

public class InvalidISBNException extends RuntimeException {
    public InvalidISBNException() {
        this("Invalid ISBN.");
    }

    public InvalidISBNException(String message) {
        super(message);
    }
}
