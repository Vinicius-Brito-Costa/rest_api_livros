package br.com.treino.api_livros.controller.dto.response;

import java.util.List;

public interface IResponse {
    List<String> getMessages();
    void setMessages(List<String> messages);
}
