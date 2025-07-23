//package br.com.paulo.Screenmatch;
//
//import br.com.paulo.Screenmatch.Principal.Principal;
//import br.com.paulo.Screenmatch.Service.Translator;
//import br.com.paulo.Screenmatch.repository.SerieRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//
//@SpringBootApplication
//public class ScreenmatchApplication implements CommandLineRunner {
//
//	@Autowired
//	private SerieRepository repositorio;
//
//	public static void main(String[] args) {
//		SpringApplication.run(ScreenmatchApplication.class, args);
//	}
//
//	@Override
//	public void run(String... args) throws Exception {
//		Principal principal = new Principal(repositorio);
//		Translator consulta = new Translator();
////		String res = consulta.traduzir("Hellow, how are you? I'm so excited to find my key to another world","PT");
////		System.out.println("Tradução: " + res);
//		principal.exibiMenu();
//
//
//	}
//}
