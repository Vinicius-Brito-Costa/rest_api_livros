package br.com.treino.api_livros.domain.book;

import br.com.treino.api_livros.exception.InvalidISBNException;

public class ISBNValidator {

    public static Boolean checkISBN(String isbn){
        if (!isbn.matches("^(?:ISBN(?:-13)?:?\\ )?(?=[0-9]{13}$|(?=(?:[0-9]+[-\\ ]){4})[-\\ 0-9]{17}$)97[89][-\\ ]?[0-9]{1,5}[-\\ ]?[0-9]+[-\\ ]?[0-9]+[-\\ ]?[0-9]$"))
            throw new InvalidISBNException();
        return true;
    }
}
