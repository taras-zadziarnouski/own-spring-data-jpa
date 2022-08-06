package by.zadziarnouski.starter.unsafe;

import java.util.List;
import java.util.Set;
import scala.Tuple2;

public interface TransformationSpider {

  Tuple2<SparkTransformation, List<String>> createTransformation(List<String> remainingWords, Set<String> fieldNames);
}
