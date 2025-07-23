package br.com.paulo.Screenmatch;

import br.com.paulo.Screenmatch.Principal.Principal;
import br.com.paulo.Screenmatch.Service.Translator;
import br.com.paulo.Screenmatch.repository.SerieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplicationWeb {


	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplicationWeb.class, args);
	}


}
