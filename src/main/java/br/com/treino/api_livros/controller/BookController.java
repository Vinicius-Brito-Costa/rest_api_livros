package br.com.treino.api_livros.controller;

import br.com.treino.api_livros.controller.dto.domain.BookDTO;
import br.com.treino.api_livros.controller.dto.domain.ModifyBookDTO;
import br.com.treino.api_livros.controller.dto.response.CustomPageDTO;
import br.com.treino.api_livros.controller.dto.response.IResponse;
import br.com.treino.api_livros.controller.dto.response.SucessResponseDTO;
import br.com.treino.api_livros.domain.book.Book;
import br.com.treino.api_livros.exception.ApiLivrosBaseException;
import br.com.treino.api_livros.exception.BookAlreadyExistsException;
import br.com.treino.api_livros.exception.BookDoesNotExistsException;
import br.com.treino.api_livros.repository.AuthorRepository;
import br.com.treino.api_livros.repository.BookRepository;
import br.com.treino.api_livros.repository.GenreRepository;
import br.com.treino.api_livros.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/v1")
public class BookController {

    @Autowired
    private BookRepository repository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private GenreRepository genreRepository;

    @Autowired
    private BookService service;

    @GetMapping("/livros")
    @Cacheable("list_books_cache")
    public ResponseEntity<CustomPageDTO<BookDTO>> listBooks(
            @RequestParam(value = "page_size", required = false) Integer pageSize,
            @RequestParam(value = "page", required = false) Integer page){

        Pageable pageable = PageRequest.of(
                page != null ? page : 0,
                pageSize != null ? pageSize : 10,
                Sort.by("isbn"));

        Page<BookDTO> bookList = new PageImpl<>(
                this.repository.findAll(pageable)
                        .stream()
                        .map(BookDTO::new)
                        .collect(Collectors.toList()));

        for(BookDTO book : bookList){
            book.add(linkTo(methodOn(BookController.class).getBookByIsbn(book.getIsbn())).withSelfRel());
        }
        CustomPageDTO<BookDTO> customPage = new CustomPageDTO<>(bookList);

        return ResponseEntity.ok().body(customPage);
    }

    public ResponseEntity<CustomPageDTO<BookDTO>> listBooks(){
        return listBooks(10, 0);
    }

    @GetMapping("/livros/{isbn}")
    @Cacheable(value = "book_by_isbn", key = "'BookIsbn'+#isbn")
    public ResponseEntity<Book> getBookByIsbn(@PathVariable("isbn") String isbn){
        return service.findBook(this.repository.findById(isbn));
    }

    @PostMapping("/livros")
    @Transactional(rollbackFor = {ApiLivrosBaseException.class})
    @CacheEvict(value = "list_books_cache", allEntries = true)
    public ResponseEntity<IResponse> addBook(@RequestBody Book book) throws ApiLivrosBaseException {

        if(this.repository.findById(book.getIsbn()).isPresent()){
            throw new BookAlreadyExistsException();
        }

        Book addedBook = this.repository.save(service.verifyAuthorAndGenre(book));
        List<String> messages = service.getMessages();

        addedBook.add(linkTo(methodOn(BookController.class).getBookByIsbn(book.getIsbn())).withRel("location"));
        addedBook.add(linkTo(methodOn(BookController.class).listBooks()).withRel("books"));

        SucessResponseDTO response = new SucessResponseDTO(messages, addedBook);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/livros/{isbn}")
    @Caching(evict = {
            @CacheEvict(value = "book_by_isbn", key = "'BookIsbn'+#isbn"),
            @CacheEvict(value = "list_books_cache", allEntries = true)
    })
    public ResponseEntity<SucessResponseDTO> fullyModifyBook(
            @PathVariable("isbn") String isbn,
            @RequestBody Book book) throws BookDoesNotExistsException, ApiLivrosBaseException {

        SucessResponseDTO responseDTO = service.fullyModifyBook(book, this.repository.existsById(isbn));
        this.repository.save(responseDTO.getBook());

        responseDTO.getBook().add(linkTo(methodOn(BookController.class).getBookByIsbn(book.getIsbn())).withRel("location"));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDTO);
    }

    @PatchMapping("/livros/{isbn}")
    @Caching(evict = {
            @CacheEvict(value = "book_by_isbn", key = "'BookIsbn'+#isbn"),
            @CacheEvict(value = "list_books_cache", allEntries = true)
    })
    public ResponseEntity<SucessResponseDTO> modifyBook(
            @PathVariable("isbn") String isbn,
            @RequestBody ModifyBookDTO book) throws ApiLivrosBaseException, BookDoesNotExistsException {

        SucessResponseDTO responseDTO = service.partiallyModifyBook(this.repository.findById(isbn), book);
        this.repository.save(responseDTO.getBook());

        responseDTO.getBook().add(linkTo(methodOn(BookController.class).getBookByIsbn(isbn)).withRel("location"));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(responseDTO);
    }

    @DeleteMapping("/livros/{isbn}")
    @Caching(evict = {
            @CacheEvict(value = "book_by_isbn", key = "'BookIsbn'+#isbn"),
            @CacheEvict(value = "list_books_cache", allEntries = true)
    })
    public ResponseEntity deleteBookByIsbn(@PathVariable(required = true) String isbn){
        if(this.repository.findById(isbn).isEmpty()){
            throw new BookDoesNotExistsException();
        }
        this.repository.deleteById(isbn);
        return ResponseEntity.ok().build();
    }
}
