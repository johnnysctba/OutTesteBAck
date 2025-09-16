package br.com.johnny.repository;

import br.com.johnny.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * Repositório para operações de persistência da entidade Movie.
 * Esta interface segue o padrão Repository, abstraindo a camada de persistência
 * e aplicando o princípio de inversão de dependência (DIP) do SOLID.
 */
@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    /**
     * Busca todos os filmes vencedores ordenados por ano.
     * 
     * @return lista de filmes vencedores ordenados por ano
     */
    @Query("SELECT m FROM Movie m WHERE m.winner = true ORDER BY m.yearMovie")
    List<Movie> findWinnerMoviesOrderedByYear();

    /**
     * Verifica se existem dados na tabela.
     * 
     * @return true se existem filmes cadastrados, false caso contrário
     */
    @Query("SELECT COUNT(m) > 0 FROM Movie m")
    boolean existsAny();
}

