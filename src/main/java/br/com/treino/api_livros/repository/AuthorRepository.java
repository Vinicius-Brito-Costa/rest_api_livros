package br.com.treino.api_livros.repository;

import br.com.treino.api_livros.domain.book.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long>, AggregateRepository{
    @Override
    Author findByName(String name);
}
