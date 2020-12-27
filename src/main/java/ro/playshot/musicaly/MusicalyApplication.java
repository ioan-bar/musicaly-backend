package ro.playshot.musicaly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ro.playshot.musicaly.repository.XPathParser;

@SpringBootApplication
public class MusicalyApplication {

    public static void main(String[] args) {
        XPathParser.startConfiguration();
        SpringApplication.run(MusicalyApplication.class, args);
    }

}
