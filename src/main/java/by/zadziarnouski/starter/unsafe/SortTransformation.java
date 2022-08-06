package by.zadziarnouski.starter.unsafe;

import java.util.List;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Component;

@Component
public class SortTransformation implements SparkTransformation {

  @Override
  public Dataset<Row> transform(Dataset<Row> dataset, List<String> fieldNames, OrderedBag<Object> args) {
    return dataset.orderBy(fieldNames.remove(0),fieldNames.stream().toArray(String[]::new));
  }
}
