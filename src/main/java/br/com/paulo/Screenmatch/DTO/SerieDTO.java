package br.com.paulo.Screenmatch.DTO;

import br.com.paulo.Screenmatch.Model.Categoria;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record SerieDTO(
        Long id,
        String  titulo,
        Integer totalTemporadas,
        Double avaliacao,
        Categoria genero,
        String atores,
        String diretor,
        String poster,
        String sinopse) {}

