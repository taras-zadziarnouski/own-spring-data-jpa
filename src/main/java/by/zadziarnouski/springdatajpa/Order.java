package by.zadziarnouski.springdatajpa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Source("data/orders.csv")
public class Order {
  private String name;
  private String desc;
  private int price;
  private long criminalId;
}
