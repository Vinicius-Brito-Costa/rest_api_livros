package br.com.treino.api_livros.controller.dto.response;

import br.com.treino.api_livros.domain.book.Book;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SucessResponseDTO implements IResponse {
    private List<String> messages;
    private Book book;
}
