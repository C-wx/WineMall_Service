package winemall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WinemallApplication {

    public static void main(String[] args) {
        SpringApplication.run(WinemallApplication.class, args);
        System.out.println("-------------------启动完成-----------------");
    }

}
