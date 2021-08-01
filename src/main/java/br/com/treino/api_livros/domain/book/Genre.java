package br.com.treino.api_livros.domain.book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Genre implements IBookAggregate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            nullable = false,
            columnDefinition = "TEXT"
    )
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;

    @Override
    public String aggregateName() {
        return "Genre";
    }
}
