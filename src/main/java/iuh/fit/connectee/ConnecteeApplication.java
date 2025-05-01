package iuh.fit.connectee;

import io.github.cdimascio.dotenv.Dotenv;
import iuh.fit.connectee.model.AppUser;
import iuh.fit.connectee.repo.AppUserRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class ConnecteeApplication {


    public static void main(String[] args) {

        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            dotenv.entries().forEach(entry -> System.setProperty(entry.getKey(), entry.getValue()));
        } catch (Exception e) {
            System.err.println("Failed to load .env: " + e.getMessage());
        }

        SpringApplication.run(ConnecteeApplication.class, args);
    }

}
