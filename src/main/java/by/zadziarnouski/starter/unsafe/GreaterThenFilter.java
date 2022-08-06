package by.zadziarnouski.starter.unsafe;

import java.util.List;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.stereotype.Component;

@Component("greaterThan")
public class GreaterThenFilter implements FilterTransformation {

  @Override
  public Dataset<Row> transform(Dataset<Row> dataset, List<String> fieldName, OrderedBag<Object> args) {
    return dataset.filter(functions.col(fieldName.get(0)).geq(args.takeAndRemove()));
  }
}
