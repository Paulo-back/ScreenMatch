package br.com.paulo.Screenmatch.Principal;

import br.com.paulo.Screenmatch.Model.DadosEpisodio;
import br.com.paulo.Screenmatch.Model.DadosSerie;
import br.com.paulo.Screenmatch.Model.DadosTemporada;
import br.com.paulo.Screenmatch.Model.Episodio;
import br.com.paulo.Screenmatch.Service.ConsumoApi;
import br.com.paulo.Screenmatch.Service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class sla {
    private Scanner sc = new Scanner(System.in);
    private final String ENDERECO = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=4ff372f3";

    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();


        public void sla2(){


        System.out.println("Digite o nome da série para busca");
        var nomeSerie = sc.nextLine();
        var urlSerie = ENDERECO + nomeSerie.replace(" ","+") + API_KEY;
        var json = consumo.obterDados(urlSerie);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        ArrayList<DadosTemporada> temporadas = new ArrayList<>();

		System.out.println("Serie temporadas: ");
            for (int i = 1; i<=dados.totalTemporadas();i++){
                var urlSeason = ENDERECO + nomeSerie.replace(" ","+")+ "&season=" + i + API_KEY;
                json = consumo.obterDados(urlSeason);
                DadosTemporada temporada = conversor.obterDados(json,DadosTemporada.class);
                System.out.print("Dados da temporada:\n"+temporada);
                temporadas.add(temporada);
		    }
		System.out.println("Dados temporada:");
		temporadas.forEach(System.out::println);

        for (int i = 0; i<dados.totalTemporadas();i++){
            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
            for (int j = 0; j<episodiosTemporada.size(); j++){
                System.out.println(episodiosTemporada.get(j).Titulo());
            }
        }
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.Titulo())));

        List<DadosEpisodio> dadosEpisodios = temporadas.stream().
                flatMap(t -> t.episodios().stream()).
                collect(Collectors.toUnmodifiableList());
        System.out.println("\nTop 10 episodios");
        dadosEpisodios.stream().
                filter(e -> !e.avaliacao().equalsIgnoreCase("N/A")).
                peek(e-> System.out.println("Primeiro filtro(N/A " + e))
                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                .peek(e-> System.out.println("É ordenação " + e))
                .limit(10)
                .peek(e-> System.out.println("Limitador " + e))
                .map(e -> e.Titulo().toUpperCase())
                .peek(e-> System.out.println("Mapeamento " + e))
                .forEach(System.out::println);
        System.out.println("Fim");

            List<Episodio> episodios = temporadas.stream().
                    flatMap(t -> t.episodios().stream()
                    .map(d -> new Episodio(t.numeroEpisodio(), d)
                    )).collect(Collectors.toList());

            episodios.forEach(System.out::println);

        System.out.println("Digite trecho do título do episódio desejado");
        var trechTitulo = sc.nextLine();
        Optional<Episodio> episodioBuscado = episodios.stream()
                .filter(e -> e.getTitulo().toUpperCase().contains(trechTitulo.toUpperCase()))
                .findFirst();
        if(episodioBuscado.isPresent()){
            System.out.println("Episodio encontrado: ");
            System.out.println("Temporada: " + episodioBuscado.get());
        }else {
            System.out.println("Episódio não encontrado!");
        }

        System.out.println(episodioBuscado);

        System.out.println("A partir de que ano voce desesja ver o episodios?");
        var ano = sc.nextInt();
        sc.nextLine();
        System.out.println(ano);


        LocalDate dataBusca = LocalDate.of(ano,1,1);
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //Formata a data para estilo pt-br

        episodios.stream()
                .filter(e -> e.getDtLancamento() != null && e.getDtLancamento().isAfter(dataBusca))
                .forEach(e -> System.out.println(
                            "\nTemporada "+ e.getTemporada() +
                            "\nEpisodio: " + e.getTitulo() +
                            "\nData Lançamento "+ e.getDtLancamento().format(formatador))
                );
        System.out.println("ok");

        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                .filter(episodio -> episodio.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        DoubleSummaryStatistics est = episodios.stream().
                filter(e -> e.getAvaliacao() > 0.0)
                        .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));


        System.out.println(avaliacoesPorTemporada);
        System.out.println("est Média: " + est.getAverage());
        System.out.println("Melhor episódio: "+ est.getMax());
        System.out.println("Pior episódio: "+ est.getMin());
        System.out.println("Quantidade de episódio avaliados: "+ est.getCount());
        }
}
