package br.com.paulo.Screenmatch;

import br.com.paulo.Screenmatch.Model.DadosSerie;
import br.com.paulo.Screenmatch.Service.ConsumoApi;
import br.com.paulo.Screenmatch.Service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {


		Scanner sc = new Scanner(System.in);
		System.out.print("Qual serie voce quer procurar: ");
		String filme = sc.next();
		String apiKey = "&apikey=4ff372f3";
		String urlApi = "https://www.omdbapi.com/?t=";
		String url = urlApi+filme+apiKey; // URL da API
//

		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados(url);
		System.out.println(json);
//		json = consumoApi.obterDados("https://coffee.alexflipnote.dev/random.json");
//		System.out.println(json);
		ConverteDados conversor = new ConverteDados();
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);
	}
}
