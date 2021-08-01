package br.com.treino.api_livros.controller.dto.domain;

import br.com.treino.api_livros.domain.book.Book;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
public class BookDTO extends RepresentationModel<Book> {
    private String isbn;
    private String title;

    public BookDTO(Book book){
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
    }
}
