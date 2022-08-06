package by.zadziarnouski.starter.unsafe;

import java.util.List;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;

public interface SparkTransformation {

  Dataset<Row> transform(Dataset<Row> dataset, List<String> fieldName, OrderedBag<Object> args);
}
