package br.com.paulo.Screenmatch.Model;

import br.com.paulo.Screenmatch.Service.Translator;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;

@Entity
@Table(name = "series")
public class Serie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//Auto incremental
    private long id;


    @Column(unique = true)
    private String  titulo;

    private Integer totalTemporadas;

    private Double avaliacao;

    @Enumerated(EnumType.STRING)
    private Categoria genero;

    private String atores;

    private String diretor;
    private String poster;

    private String sinopse;

    @JsonIgnore
    @Transient
    private Translator tradutor = new Translator();

    @OneToMany(mappedBy = "serie", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Episodio> episodios = new ArrayList<>();


    public Serie() {}

    public Serie(DadosSerie dadosSerie){
        this.titulo = dadosSerie.titulo();
        this.totalTemporadas = dadosSerie.totalTemporadas();
        this.avaliacao = OptionalDouble.of(Double.valueOf(dadosSerie.avaliacao())).orElse(0);
        this.genero = Categoria.fromString(dadosSerie.genero().split(",")[0].trim());
        this.atores = dadosSerie.atores();
        this.diretor = dadosSerie.diretor();
        this.poster = dadosSerie.poster();
        this.sinopse = tradutor.traduzir(dadosSerie.Sinopse().trim(),"PT-br");


    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Integer getTotalTemporadas() {
        return totalTemporadas;
    }

    public void setTotalTemporadas(Integer totalTemporadas) {
        this.totalTemporadas = totalTemporadas;
    }

    public Double getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(Double avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Categoria getGenero() {
        return genero;
    }

    public void setGenero(Categoria genero) {
        this.genero = genero;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {this.sinopse = sinopse;}

    public long getId() {return id;}

    public void setId(long id){this.id = id;}


    public Translator getTradutor() {
        return tradutor;
    }

    public void setTradutor(Translator tradutor) {
        this.tradutor = tradutor;
    }

    public List<Episodio> getEpisodios() {
        return episodios;
    }

    public void setEpisodios(List<Episodio> episodios) {
        episodios.forEach(e -> e.setSerie(this));//Seta a chave estrangeira
        this.episodios = episodios;
    }

    @Override
    public String toString() {
        return

                "\nTitulo = " + titulo +
                "\nTotal de Temporadas = " + totalTemporadas +
                "\nAvaliação = " + avaliacao +
                "\nGenero = " + genero +
                "\nAtores = " + atores +
                "\nDiretor = " + diretor +
                "\nPoster = " + poster +
                "\nSinopse = " + sinopse +
                "\nEpisodios = " + episodios;
    }
}
