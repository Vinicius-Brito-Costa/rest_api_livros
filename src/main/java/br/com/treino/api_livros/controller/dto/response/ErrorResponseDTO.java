package br.com.treino.api_livros.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponseDTO implements IResponse{
    private List<String> messages;
}
