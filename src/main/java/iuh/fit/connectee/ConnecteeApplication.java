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
        SpringApplication.run(ConnecteeApplication.class, args);
    }

}
