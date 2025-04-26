package iuh.fit.connectee;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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
