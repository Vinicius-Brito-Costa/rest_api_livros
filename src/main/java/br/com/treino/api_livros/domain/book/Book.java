package br.com.treino.api_livros.domain.book;

import br.com.treino.api_livros.controller.dto.domain.ModifyBookDTO;
import br.com.treino.api_livros.exception.AuthorAlreadyInBookException;
import br.com.treino.api_livros.exception.GenreAlreadyInBookException;
import br.com.treino.api_livros.exception.GenreLimitExcecedException;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.*;
import java.util.List;

@Table(name = "book")
@Entity
@Data
@NoArgsConstructor
public class Book extends RepresentationModel<Book> {

    @Id
    private String isbn;
    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String title;
    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String subtitle;
    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String description;
    @Column(nullable = false)
    private Integer pages;

    @ManyToMany
    private List<Genre> genres;
    @ManyToMany
    private List<Author> authors;
    @Column(nullable = false)
    private String publisher;

    public Book(String isbn,
                String title,
                String sub_title,
                String description,
                Integer pages,
                List<Genre> genres,
                List<Author> authors,
                String publisher) {
        this.publisher = publisher;
        setIsbn(isbn);
        this.title = title;
        this.subtitle = sub_title;
        this.description = description;
        this.pages = pages;
        this.genres = genres;
        this.authors = authors;
    }

    private void setIsbn(String isbn){
        if(ISBNValidator.checkISBN(isbn))
            this.isbn = isbn;
    }
    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }


    public void addGenre(Genre genre){
        if(this.genres.contains(genre)){
            throw new GenreAlreadyInBookException();
        }
        if(this.genres.size() > 3){
            throw new GenreLimitExcecedException();
        }
        this.genres.add(genre);
    }

    public void addAuthor(Author author){
        if(this.authors.contains(author))
            throw new AuthorAlreadyInBookException();
        this.authors.add(author);
    }

    public ModifyBookDTO convertToDTO(){
        return new ModifyBookDTO(
                this.title,
                this.subtitle,
                this.description,
                this.pages,
                this.genres,
                this.authors,
                this.publisher);
    }
}
