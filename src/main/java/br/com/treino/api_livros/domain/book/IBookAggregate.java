package br.com.treino.api_livros.domain.book;

public interface IBookAggregate {
    Integer getId();

    String aggregateName();

    String getName();

    String getDescription();
}
