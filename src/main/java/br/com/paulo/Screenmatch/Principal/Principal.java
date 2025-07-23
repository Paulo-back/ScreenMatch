package br.com.paulo.Screenmatch.Principal;

import br.com.paulo.Screenmatch.Model.*;
import br.com.paulo.Screenmatch.Service.ConsumoApi;
import br.com.paulo.Screenmatch.Service.ConverteDados;
import br.com.paulo.Screenmatch.Service.Translator;
import br.com.paulo.Screenmatch.repository.SerieRepository;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private Scanner sc = new Scanner(System.in);

    private final String ENDERECO = "https://omdbapi.com/?t=";
    private final String API_KEY = "&apikey=4ff372f3";
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private List<Serie> series = new ArrayList<>();
    private Optional<Serie> serieBuscada;


    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private Translator tradutor = new Translator();

    private SerieRepository repositorio;


    public Principal(SerieRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void exibiMenu() {

        int i = 1;
        while (i != 0) {


            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Séries buscadas
                    4 - Buscar série por nome
                    5 - Buscar série por ator
                    6 - Top 5 séries
                    7 - Busca Por categoria
                    8 - Busca Personalizada 
                    9 - Busca Episódio Por Trecho
                    11 - Top 5 Episódios
                    12 - Buscar Episódios por lançamento
                    0 - Sair
                    
                    """;
            System.out.print(menu);
            System.out.print("Digite aqui: ");
            var opcao = 10;
            try {
                opcao = sc.nextInt();
                sc.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
                sc.nextLine();
            }


            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    break;
                case 5:
                    buscarSeriePorAtor();
                    break;

                case 6:
                    buscarTop5Series();
                    break;

                case 7:
                    buscarPorGenero();
                    break;
                case 8:
                    buscaPersonalizada();
                    break;
                case 9:
                    buscaPorTrecho();
                    break;
                case 11:
                    Top5EpisodiosPorSerie();
                    break;
                case 12:
                    buscarEpisodioAposData();
                    break;


                case 0:
                    System.out.println("Saindo...");
                    i = 0;
                    break;
                case 10:
                    continue;

                default:
                    System.out.println("Opção inválida");
            }
        }


    }


    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
//        dadosSeries.add(dados);
        repositorio.save(serie); //Salva no banco de dados, mais esta sendo passado da classe screenMatch.

        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = sc.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
//        DadosSerie dadosSerie = getDadosSerie();
        listarSeriesBuscadas();

        System.out.print("Escolha uma série pelo seu nome: ");
        var nomeSerie = sc.nextLine();
        Optional<Serie> serie = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serie.isPresent()) {


            var serieEncontrada = serie.get();
            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(d -> d.episodios().stream()
                            .map(e -> new Episodio(d.numeroEpisodio(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);

            repositorio.save(serieEncontrada);


        } else {
            System.out.println("Série não encontrada! ");
        }
    }

    private void listarSeriesBuscadas() {
        series = repositorio.findAll();

        series.stream().sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);

//        series.stream().sorted(Comparator.comparing(Serie::getGenero))
//                    .forEach(s -> System.out.println(s.getTotalTemporadas()));


//        if (dadosSeries != null && !dadosSeries.isEmpty()){
//            dadosSeries.forEach(System.out::println);
//        }else {
//            System.out.println("\nVocê ainda não procurou nenhuma série\n");
//        }

    }

    private void buscarSeriePorTitulo() {

        System.out.print("Escolha uma série pelo seu nome: ");
        var nomeSerie = sc.nextLine();
        serieBuscada = repositorio.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("Dados da série " + serieBuscada.get());


        } else {
            System.out.println("Série não encontrada.");
        }
    }

    private void buscarSeriePorAtor() {
        System.out.println("Qual o nome do ator procurado?");
        var nomeAtor = sc.nextLine();
        System.out.println("Avaliação a partir de qual valor?");
        var avaliacao = sc.nextDouble();
        List<Serie> seriesEcontradas = repositorio.findByAtoresContainingIgnoreCaseAndAvaliacaoGreaterThanEqual(nomeAtor, avaliacao);
        System.out.println("Séries em que " + nomeAtor + " trabalhou: ");
        seriesEcontradas.forEach(s -> System.out.println(s.getTitulo() + " Avaliações: " + s.getAvaliacao()));
    }

    private void buscarTop5Series() {
        List<Serie> seriesTop5 = repositorio.findTop5ByOrderByAvaliacaoDesc();
        seriesTop5.forEach(s -> System.out.println(s.getTitulo() + "|  Avaliação: " + s.getAvaliacao()));
    }

    private void buscarPorGenero() {
        System.out.println("Escolha uma das categorias abaixo: ");
        for (Categoria categoria : Categoria.values()) {
            System.out.println(categoria);
        }
        System.out.print("Digite aqui: ");
        String genero = sc.nextLine();
        try {
            Categoria categoria = Categoria.fromPortugues(genero.toUpperCase());
            List<Serie> serieCategoria = repositorio.findByGenero(categoria);
            if (Objects.isNull(serieCategoria) || serieCategoria.isEmpty()) {
                System.out.println("Não a séries com este genero: " + categoria + "\n");
            }
            serieCategoria.forEach(c -> System.out.println(c.getTitulo() + "| Categoria: " + c.getGenero()));
        } catch (IllegalArgumentException e) {
            System.out.println("Argumento inválido: " + e);
        }
    }

    private void buscaPersonalizada() {
        System.out.println("Para a pesquisa personalizada, por favor digite o total de temporadas que deseja\nE a nota de avaliação");
        var totalTemporadas = sc.nextInt();
        var avaliacao = sc.nextDouble();
//        List<Serie> buscaPerso = repositorio.findBytotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(totalTemporadas,avaliacao);
        List<Serie> buscaPerso = repositorio.seriesPorTemporadaEAvaliacao(totalTemporadas, avaliacao);
        buscaPerso.forEach(b -> System.out.println("Nome: " + b.getTitulo()
                + "\nTemporadas: " + b.getTotalTemporadas() + "\nAvaliação: " + b.getAvaliacao()));

    }

    private void buscaPorTrecho() {
        var episodioTrecho = sc.nextLine();
        List<Episodio> episodiosEncotrados = repositorio.episodiosPorTrecho(episodioTrecho);
        episodiosEncotrados.forEach(e -> System.out.println(
                "Série: " + e.getSerie().getTitulo() + "\nTemporada: " + e.getTemporada()
                        + "\nEpisódio: " + e.getNumeroEpisodio() + " - " + e.getTitulo()
        ));

    }

    private void Top5EpisodiosPorSerie() {

        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            List<Episodio> topEpisodios = repositorio.topEpisodiosPorSerie(serie);

            topEpisodios.forEach(e -> System.out.println(
                    "Série: " + e.getSerie().getTitulo() + "\nTemporada: " + e.getTemporada()
                            + "\nEpisódio: " + e.getNumeroEpisodio() + " - " + e.getTitulo()
                            + "\nAvaliação: " + e.getAvaliacao() + "\n"
            ));
        }
    }

    private void buscarEpisodioAposData() {
        buscarSeriePorTitulo();
        if (serieBuscada.isPresent()) {
            Serie serie = serieBuscada.get();
            System.out.println("Digite o ano limite de lançamento");
            var anoBusca = sc.nextInt();
            sc.nextLine();

            List<Episodio> episodiosAno = repositorio.buscarApartirDeData(serie, anoBusca);

            episodiosAno.forEach(System.out::println);

        }


    }
}
