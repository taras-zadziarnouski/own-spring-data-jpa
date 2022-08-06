package by.zadziarnouski.springdatajpa;

import java.util.List;
import org.springframework.stereotype.Component;

@Component
public interface CriminalRepository extends SparkRepository<Criminal> {

  List<Criminal> findByNumberBetween(int min, int max);
  List<Criminal> findByNumberGreaterThan(int min);
  long findByNumberGreaterThanCount(int min);
  List<Criminal> findByNumberGreaterThanOrderByNumber(int min);
}
