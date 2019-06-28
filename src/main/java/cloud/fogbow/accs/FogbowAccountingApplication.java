package cloud.fogbow.accs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;

@SpringBootApplication(exclude = RepositoryRestMvcAutoConfiguration.class)
public class FogbowAccountingApplication {

	public static void main(String[] args) {
		SpringApplication.run(FogbowAccountingApplication.class, args);
	}

}
