package br.com.paulo.Screenmatch.Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.persistence.*;
import org.hibernate.annotations.ManyToAny;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;


@Entity
@Table(name = "episodios")
public class Episodio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Auto incremental
    private long id;

    private Integer temporada;
    private String Titulo;
    private Integer numeroEpisodio;
    private Double avaliacao;
    private LocalDate dtLancamento;


    @ManyToOne
    private Serie serie;

    public Episodio(Integer temporada, String titulo, Integer numeroEpisodio, Double avaliacao, LocalDate dtLancamento) {
        this.temporada = temporada;
        Titulo = titulo;
        this.numeroEpisodio = numeroEpisodio;
        this.avaliacao = avaliacao;
        this.dtLancamento = dtLancamento;
    }

    public Episodio() {
    }

    public Episodio(Integer numeroTemporada, DadosEpisodio dadosEpisodio) {
        this.temporada = numeroTemporada;
        this.Titulo = dadosEpisodio.Titulo();
        this.numeroEpisodio = dadosEpisodio.numeroEpisodio();
        try {
            this.avaliacao = Double.valueOf(dadosEpisodio.avaliacao());
        }catch (NumberFormatException ex){
            this.avaliacao = 0.0;

        }
    try {
        this.dtLancamento = LocalDate.parse(dadosEpisodio.dtLancamento());
    }catch (DateTimeParseException e ){
        this.dtLancamento = null;
    }


    }

    public Integer getTemporada() {
        return temporada;
    }

    public void setTemporada(Integer temporada) {
        this.temporada = temporada;
    }

    public String getTitulo() {
        return Titulo;
    }

    public void setTitulo(String titulo) {
        Titulo = titulo;
    }

    public Integer getNumeroEpisodio() {
        return numeroEpisodio;
    }

    public void setNumeroEpisodio(Integer numeroEpisodio) {
        this.numeroEpisodio = numeroEpisodio;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public LocalDate getDtLancamento() {
        return dtLancamento;
    }

    public void setDtLancamento(LocalDate dtLancamento) {
        this.dtLancamento = dtLancamento;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Serie getSerie() {
        return serie;
    }

    public void setSerie(Serie serie) {
        this.serie = serie;
    }

    @Override
    public String toString() {
        return "temporada = " + temporada +
                ", Titulo = " + Titulo + '\'' +
                ", numeroEpisodio = " + numeroEpisodio +
                ", avaliacao = " + avaliacao +
                ", dtLancamento = "  + dtLancamento;
    }
}