package by.zadziarnouski.starter.unsafe;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.springframework.context.ConfigurableApplicationContext;
import scala.Tuple2;

@Builder
public class DefaultSparkInvocationHandler implements SparkInvocationHandler {

  private Class<?> modelClass;
  private String pathToData;
  private DataExtractor dataExtractor;
  private Map<Method, List<Tuple2<SparkTransformation, List<String>>>> transformationChain;
  private Map<Method, Finalizer> finalizerMap;
  private FinalizerPostProcessor postProcessor;

  private ConfigurableApplicationContext context;

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    OrderedBag<Object> orderedBag = new OrderedBag<>(args);
    Dataset<Row> dataset = dataExtractor.readData(pathToData, context);
    List<Tuple2<SparkTransformation, List<String>>> tuple2List = transformationChain.get(method);
    for (Tuple2<SparkTransformation, List<String>> tuple : tuple2List) {
      SparkTransformation sparkTransformation = tuple._1;
      List<String> fieldNames = tuple._2;
      dataset = sparkTransformation.transform(dataset, fieldNames, orderedBag);
    }
    Finalizer finalizer = finalizerMap.get(method);
    Object retVal = finalizer.doAction(dataset, modelClass, orderedBag);
    return postProcessor.postFinalizer(retVal);
  }
}
