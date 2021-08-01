package br.com.treino.api_livros.repository;

public interface AggregateRepository<T> {
    T findByName(String name);
}
