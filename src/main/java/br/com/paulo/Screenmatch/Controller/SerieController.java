package br.com.paulo.Screenmatch.Controller;


import br.com.paulo.Screenmatch.DTO.EpisodioDTO;
import br.com.paulo.Screenmatch.DTO.SerieDTO;
import br.com.paulo.Screenmatch.Model.Episodio;
import br.com.paulo.Screenmatch.Service.SerieService;
import jakarta.persistence.Id;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/series") //todos os caminhos começaram com /series //Sendo assim sem necessidade de ter no primeiro metodo

public class SerieController {


    @Autowired
    private SerieService servico;

    @GetMapping
    public List<SerieDTO> obterSeries(){

        return servico.obterTodasSeries();

    }
    @GetMapping("/top5")
    public  List<SerieDTO> obterTop5Series(){
        return servico.obterTop5Series();
    }

    @GetMapping("/lancamentos")
        public List<SerieDTO> obterLancamentos(){
        return servico.obterLancamentos();
    }

    @GetMapping("/{id}")
    public SerieDTO obterId(@PathVariable Long id){
        return servico.obterId(id);
    }

    @GetMapping("/{id}/temporadas/todas")
    public List<EpisodioDTO> obterTodasTemporadas(@PathVariable Long id){
        return servico.obterTodasTemporadas(id);

    }

    @GetMapping("/{id}/temporadas/{temporada}")
    public List<EpisodioDTO> obterTemporada(@PathVariable Long id,@PathVariable Long temporada){
        return servico.obterTemporada(id, temporada);

    }
    @GetMapping("/categoria/{nomeGenero}")
    public List<SerieDTO> obterSeriesPorGenero(@PathVariable String nomeGenero){
        return servico.obterSeriesPorGenero(nomeGenero);
    }

    @GetMapping("/{id}/temporadas/top")
    public List<EpisodioDTO> obterTop5(@PathVariable Long id){
        return servico.obterTop5(id);
    }

}
