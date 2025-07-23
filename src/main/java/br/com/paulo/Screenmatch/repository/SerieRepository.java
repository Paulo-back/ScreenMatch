package br.com.paulo.Screenmatch.repository;

import br.com.paulo.Screenmatch.Model.Categoria;
import br.com.paulo.Screenmatch.Model.Episodio;
import br.com.paulo.Screenmatch.Model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface SerieRepository extends JpaRepository<Serie, Long>{
        Optional<Serie> findByTituloContainingIgnoreCase(String nomeSerie);

    List<Serie> findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(String nomeAtor, Double avaliacao);

    List<Serie> findTop5ByOrderByAvaliacaoDesc();

    List<Serie> findByGenero(Categoria genero);

    List<Serie>findBytotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int totalTemporadas, double avaliacao);

    @Query("SELECT s FROM Serie s WHERE s.totalTemporadas <= :totalTemporadas AND s.avaliacao >= :avaliacao")
    List<Serie>seriesPorTemporadaEAvaliacao(int totalTemporadas, double avaliacao);



    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE e.Titulo ILIKE %:episodioTrecho%")
    List<Episodio> episodiosPorTrecho(String episodioTrecho);

    @Query("SELECT ep FROM Serie s JOIN s.episodios ep WHERE s = :serie ORDER BY ep.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);


    @Query("SELECT ep FROM Serie s JOIN s.episodios ep WHERE s = :serie AND YEAR(ep.dtLancamento) >= :ano")
    List<Episodio> buscarApartirDeData(Serie serie, int ano);

@Query("SELECT s FROM Serie s " +
        "JOIN s.episodios e " +
        "GROUP BY s " +
        "ORDER BY MAX(e.dtLancamento) DESC LIMIT 5")
    List<Serie> lancamentosMaisRecentes();


@Query("SELECT ep FROM Serie s JOIN s.episodios ep WHERE s.id = :id AND ep.temporada = :temporada")
    List<Episodio> findByIdAndTemporada(Long id, Long temporada);

    @Query("SELECT ep FROM Serie s JOIN s.episodios ep WHERE s.id = :id ORDER BY ep.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerieId(Long id);
}
