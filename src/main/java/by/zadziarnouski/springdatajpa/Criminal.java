package by.zadziarnouski.springdatajpa;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Source("data/criminals.csv")
public class Criminal {

  private long id;
  private String name;
  private int number;

  @ForeignKeyName("criminalId")
  private List<Order> orders;
}
