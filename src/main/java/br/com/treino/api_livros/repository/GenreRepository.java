package br.com.treino.api_livros.repository;

import br.com.treino.api_livros.domain.book.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Long>, AggregateRepository{
    @Override
    Genre findByName(String name);
}
