package br.com.paulo.Screenmatch.Model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true) //ignora os valores que nao estao no record
public record DadosSerie(@JsonAlias("Title") String  titulo,@JsonAlias("totalSeasons")
                         Integer totalTemporadas,@JsonAlias("imdbRating")
                         String avaliacao) {
//    @JsonAlias le somente o jsob com o nome que foi passado no parametro
    // @JsonProperty le o json com o nome que foi passado o parametro e
    // quando retorna  voltara o valor do parametro tambem.
}
