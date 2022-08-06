package by.zadziarnouski.starter.unsafe;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import scala.Tuple2;

@Component("findBy")
@RequiredArgsConstructor
public class FilterTransformationSpider implements TransformationSpider {

  private final Map<String, FilterTransformation> transformationMap;

  @Override
  public Tuple2<SparkTransformation, List<String>> createTransformation(List<String> remainingWords, Set<String> fieldNames) {
    String fieldName = WordMatcher.findAndRemoveIfMatchingPiecesIfExists(fieldNames, remainingWords);
    String filterName = WordMatcher.findAndRemoveIfMatchingPiecesIfExists(transformationMap.keySet(), remainingWords);
    return new Tuple2<>(transformationMap.get(filterName), List.of(fieldName));
  }
}