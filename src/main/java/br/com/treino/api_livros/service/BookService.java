package br.com.treino.api_livros.service;

import br.com.treino.api_livros.controller.BookController;
import br.com.treino.api_livros.controller.dto.domain.ModifyBookDTO;
import br.com.treino.api_livros.controller.dto.response.SucessResponseDTO;
import br.com.treino.api_livros.domain.book.Author;
import br.com.treino.api_livros.domain.book.Book;
import br.com.treino.api_livros.domain.book.Genre;
import br.com.treino.api_livros.domain.book.IBookAggregate;
import br.com.treino.api_livros.exception.BookDoesNotExistsException;
import br.com.treino.api_livros.exception.ApiLivrosBaseException;
import br.com.treino.api_livros.repository.AggregateRepository;
import br.com.treino.api_livros.repository.AuthorRepository;
import br.com.treino.api_livros.repository.GenreRepository;
import org.hibernate.PropertyValueException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {
    private final List<String> successMessages = new ArrayList<>();
    private List<String> errorMessages = new ArrayList<>();

    private Boolean noErrors = true;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    public BookService(AuthorRepository authorRepository, GenreRepository genreRepository) {
        this.authorRepository = authorRepository;
        this.genreRepository = genreRepository;
    }

    public ResponseEntity<Book> findBook(Optional<Book> book){

        if(book.isEmpty()){
            throw new BookDoesNotExistsException();
        }
        book.get().add(linkTo(methodOn(BookController.class).listBooks()).withRel("books"));

        return ResponseEntity.ok().body(book.get());
    }

    public Book verifyAuthorAndGenre(Book book) throws ApiLivrosBaseException {
        String bookTitleSubTitle = book.getTitle() + ":" + book.getSubtitle();
        this.successMessages.clear();
        this.successMessages.add("CREATED: Book(" + bookTitleSubTitle.toUpperCase() + ")");
        Book verifiedBook = verifyGenre(verifyAuthor(book));
        if(!this.noErrors){
            this.noErrors = true;
            List<String> tempErrorList = List.copyOf(this.errorMessages);
            this.errorMessages.clear();
            throw new ApiLivrosBaseException(tempErrorList);
        }
        return  verifiedBook;
    }

    public SucessResponseDTO fullyModifyBook(Book book, Boolean bookExists){
        if(!bookExists){
            throw new BookDoesNotExistsException();
        }
        return new SucessResponseDTO(
                List.of("Book Fully Modified"),
                verifyAuthorAndGenre(book));
    }

    public SucessResponseDTO partiallyModifyBook(Optional<Book> originalBook, ModifyBookDTO newBook){
        if(originalBook.isEmpty()){
            throw new BookDoesNotExistsException();
        }
        return newBook.convertToSuccessResponse(verifyAuthorAndGenre(originalBook.get()));
    }
    private Book verifyAuthor(Book book){
        List<Author> authors = removeDuplicate(verify(book.getAuthors()),authorRepository)
                .stream().map(aggregate -> (Author) aggregate).collect(Collectors.toList());
        authorRepository.saveAll(authors);
        book.setAuthors(authors);
        return book;
    }

    private Book verifyGenre(Book book){
        List<Genre> genres = removeDuplicate(verify(book.getGenres()), genreRepository)
                .stream().map(aggregate -> (Genre) aggregate).collect(Collectors.toList());
        genreRepository.saveAll(genres);
        book.setGenres(genres);
        return book;
    }

    public List<String> getMessages(){
        return this.successMessages;
    }

    private List<IBookAggregate> removeDuplicate(List<? extends  IBookAggregate> aggregates, AggregateRepository repository){
        List<IBookAggregate> verified = new ArrayList<>();
        for(IBookAggregate aggregate : aggregates){
            IBookAggregate found = (IBookAggregate) repository.findByName(aggregate.getName());
            if(found != null){
                verified.add(found);
                continue;
            }
            verified.add(aggregate);
            this.successMessages.add("CREATED: " + aggregate.aggregateName() +
                    "(" + aggregate.getName().toUpperCase(Locale.ROOT) + ")");
        }
        return verified;
    }
    private List<IBookAggregate> verify(List<? extends IBookAggregate> aggregates) {
        List<IBookAggregate> verified = new ArrayList<>();
        for(IBookAggregate aggregate : aggregates){
            if(aggregate.getDescription() != null && aggregate.getName() != null){
                if(aggregate.getId() == null){
                    try{
                        aggregate.getName();
                        aggregate.getDescription();
                    }
                    catch (PropertyValueException ex){
                        this.errorMessages.add("ERROR: " + aggregate.aggregateName() +
                                "(" + aggregate.getName().toUpperCase(Locale.ROOT) + ") body is invalid");
                        this.noErrors = false;
                    }
                }
                verified.add(aggregate);
            }
            else{
                this.errorMessages.add("ERROR: " + aggregate.aggregateName() + " does not have name/description");
                this.noErrors = false;
            }
        }
        return verified;
    }
}
