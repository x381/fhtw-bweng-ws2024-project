package at.fhtw.bweng_ws24;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*") // instead of * use your frontend url e.g. http://localhost:4200
@SpringBootApplication
public class BwengWs24Application {

    public static void main(String[] args) {
        SpringApplication.run(BwengWs24Application.class, args);
    }

}
