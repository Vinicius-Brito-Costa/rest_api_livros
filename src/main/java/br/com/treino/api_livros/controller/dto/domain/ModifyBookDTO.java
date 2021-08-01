package br.com.treino.api_livros.controller.dto.domain;

import br.com.treino.api_livros.controller.dto.response.SucessResponseDTO;
import br.com.treino.api_livros.domain.book.Author;
import br.com.treino.api_livros.domain.book.Book;
import br.com.treino.api_livros.domain.book.Genre;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ManyToMany;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyBookDTO {

    private String title;
    private String subtitle;
    private String description;
    private Integer pages;
    @ManyToMany
    private List<Genre> genres;
    @ManyToMany
    private List<Author> authors;
    private String publisher;

    public SucessResponseDTO convertToSuccessResponse(Book book){
        List<String> messages = new ArrayList<>();
        if(this.title != null && !this.title.equals(book.getTitle())){
            book.setTitle(this.title);
            messages.add("EDITED: TITLE");
        }
        if(this.subtitle != null && !this.subtitle.equals(book.getSubtitle())){
            book.setSubtitle(this.subtitle);
            messages.add("EDITED: SUBTITLE");
        }
        if(this.description != null && !this.description.equals(book.getDescription())){
            book.setDescription(this.description);
            messages.add("EDITED: DESCRIPTION");
        }
        if(this.pages != null && !this.pages.equals(book.getPages())){
            book.setPages(this.pages);
            messages.add("EDITED: PAGE COUNT");
        }
        if(this.genres != null && this.genres != book.getGenres()){
            book.setGenres(this.genres);
            messages.add("EDITED: GENRES");
        }
        if(this.authors != null && this.authors != book.getAuthors()){
            book.setAuthors(this.authors);
            messages.add("EDITED: AUTHORS");
        }
        if(this.publisher != null && !this.publisher.equals(book.getPublisher())){
            book.setPublisher(this.publisher);
            messages.add("EDITED: PUBLISHER");
        }
        return new SucessResponseDTO(messages, book);
    }
}
