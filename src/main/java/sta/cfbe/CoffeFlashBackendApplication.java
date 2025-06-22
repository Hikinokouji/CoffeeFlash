package sta.cfbe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
@SpringBootApplication
public class CoffeFlashBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoffeFlashBackendApplication.class, args);
    }

    @GetMapping("/api")
    public void hello(){
        System.out.printf("Hello World! im workking");
    }

}
