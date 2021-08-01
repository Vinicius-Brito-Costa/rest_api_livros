package br.com.treino.api_livros.exception;

import java.util.List;

public class ApiLivrosBaseException extends RuntimeException{

    private final List<String> messages;

    public ApiLivrosBaseException(List<String> messages) {
        super("Invalid Book Json");
        this.messages = messages;
    }

    public List<String> getMessages() {
        return messages;
    }
}
