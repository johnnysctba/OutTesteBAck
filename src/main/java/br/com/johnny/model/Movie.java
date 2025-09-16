package br.com.johnny.model;

import jakarta.persistence.*;
import java.util.Objects;

/**
 * Entidade que representa um filme indicado ou vencedor do Golden Raspberry Awards.
 * Esta classe segue os princípios de Clean Code com nomes descritivos,
 * métodos pequenos e responsabilidade única (SRP).
 */
@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "yearMovie", nullable = false)
    private Integer yearMovie;

    @Column(name = "title", nullable = false, length = 500)
    private String title;

    @Column(name = "studios", length = 500)
    private String studios;

    @Column(name = "producers", nullable = false, length = 500)
    private String producers;

    @Column(name = "winner", nullable = false)
    private Boolean winner;

    /**
     * Construtor padrão necessário para JPA.
     */
    public Movie() {
    }

    /**
     * Construtor com todos os parâmetros.
     * 
     * @param yearMovie ano do filme
     * @param title título do filme
     * @param studios estúdios responsáveis
     * @param producers produtores do filme
     * @param winner indica se foi vencedor
     */
    public Movie(Integer yearMovie, String title, String studios, String producers, Boolean winner) {
        this.yearMovie = yearMovie;
        this.title = title;
        this.studios = studios;
        this.producers = producers;
        this.winner = winner;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getYear() {
        return yearMovie;
    }
    public String getProducers() {
        return producers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) &&
               Objects.equals(yearMovie, movie.yearMovie) &&
               Objects.equals(title, movie.title) &&
               Objects.equals(producers, movie.producers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, yearMovie, title, producers);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", yearMovie=" + yearMovie +
                ", title='" + title + '\'' +
                ", studios='" + studios + '\'' +
                ", producers='" + producers + '\'' +
                ", winner=" + winner +
                '}';
    }
}

