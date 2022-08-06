package by.zadziarnouski.starter.unsafe;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.stereotype.Component;

@Component("count")
public class CountFinalizer implements Finalizer {

  @Override
  public Object doAction(Dataset<Row> dataset, Class<?> modelClass, OrderedBag<?> orderedBag) {
    return dataset.count();
  }
}
