package by.zadziarnouski.starter;

import by.zadziarnouski.starter.unsafe.DataExtractor;
import by.zadziarnouski.starter.unsafe.DataExtractorResolver;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

public class FirstLevelCacheService {

  private Map<Class<?>, Dataset<Row>> model2Dataset = new HashMap<>();

  @Autowired
  private DataExtractorResolver resolver;

  public List getDataFor(long ownerId, Class<?> modelClass, String foreignKeyName, String path, ConfigurableApplicationContext context) {
    if (!model2Dataset.containsKey(modelClass)) {
      DataExtractor dataExtractor = this.resolver.resolve(path);
      Dataset<Row> dataset = dataExtractor.readData(path, context).persist();
      model2Dataset.put(modelClass, dataset);
    }
    return model2Dataset.get(modelClass).filter(functions.col(foreignKeyName).equalTo(ownerId)).as(Encoders.bean(modelClass)).collectAsList();
  }
}
