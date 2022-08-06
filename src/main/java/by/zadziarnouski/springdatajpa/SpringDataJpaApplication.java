package by.zadziarnouski.springdatajpa;

import java.util.List;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringDataJpaApplication {

  public static void main(String[] args) {
    ConfigurableApplicationContext context = SpringApplication.run(SpringDataJpaApplication.class, args);
    CriminalRepository criminalRepository = context.getBean(CriminalRepository.class);
    List<Criminal> criminals = criminalRepository.findByNumberBetween(10,80);

    criminals.get(0).getOrders().forEach(System.out::println);
  }
}

