package br.com.treino.api_livros.exception;

public class GenreLimitExcecedException extends GenreException{
    public GenreLimitExcecedException() {
        this("Genre limit exceded.");
    }

    public GenreLimitExcecedException(String message) {
        super(message);
    }
}
