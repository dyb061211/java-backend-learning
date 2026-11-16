package learning.web;

import learning.web.config.CatalogProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(CatalogProperties.class)
public class Week25SpringWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(Week25SpringWebApplication.class, args);
	}

}
