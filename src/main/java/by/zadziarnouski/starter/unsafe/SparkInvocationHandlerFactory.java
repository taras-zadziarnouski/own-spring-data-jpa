package by.zadziarnouski.starter.unsafe;

import by.zadziarnouski.springdatajpa.Source;
import by.zadziarnouski.springdatajpa.SparkRepository;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
import scala.Tuple2;

@Component
@RequiredArgsConstructor
public class SparkInvocationHandlerFactory {

  private final DataExtractorResolver extractorResolver;
  private final Map<String, TransformationSpider> spiderMap;
  private final Map<String, Finalizer> finalizerMap;

  @Setter
  private ConfigurableApplicationContext realContext;

  public SparkInvocationHandler create(Class<? extends SparkRepository> repositoryInterface) {
    Class<?> modelClass = getModelClass(repositoryInterface);
    Set<String> fieldNames = getFieldNames(modelClass);
    String pathToData = modelClass.getAnnotation(Source.class).value();
    DataExtractor dataExtractor = extractorResolver.resolve(pathToData);

    Map<Method, List<Tuple2<SparkTransformation, List<String>>>> transformationChain = new HashMap<>();
    Map<Method, Finalizer> method2Finalizer = new HashMap<>();

    Method[] methods = repositoryInterface.getMethods();
    for (Method method : methods) {
      TransformationSpider currentSpider = null;
      List<Tuple2<SparkTransformation, List<String>>> transformations = new ArrayList<>();
      List<String> methodWords = new ArrayList(Arrays.asList(method.getName().split("(?=\\p{Upper})")));
      while (methodWords.size() > 1) {
        String strategyName = WordMatcher.findAndRemoveIfMatchingPiecesIfExists(spiderMap.keySet(), methodWords);
        if (!strategyName.isEmpty()) {
          currentSpider = spiderMap.get(strategyName);
        }
        transformations.add(currentSpider.createTransformation(methodWords, fieldNames));
      }
      String finalizerName = "collect";
      if (methodWords.size() == 1) {
        finalizerName = Introspector.decapitalize(methodWords.get(0));
      }
      transformationChain.put(method, transformations);
      method2Finalizer.put(method, finalizerMap.get(finalizerName));
    }

    return DefaultSparkInvocationHandler.builder()
        .modelClass(modelClass)
        .pathToData(pathToData)
        .finalizerMap(method2Finalizer)
        .transformationChain(transformationChain)
        .dataExtractor(dataExtractor)
        .postProcessor(new LazyCollectionSupportFinalizerPostProcessor(realContext))
        .context(realContext)
        .build();
  }

  private Class<?> getModelClass(Class<? extends SparkRepository> repositoryInterface) {
    ParameterizedType genericInterface = (ParameterizedType) repositoryInterface.getGenericInterfaces()[0];
    Class<?> modelClass = (Class<?>) genericInterface.getActualTypeArguments()[0];
    return modelClass;
  }

  private Set<String> getFieldNames(Class<?> modelClass) {
    return Arrays.stream(modelClass.getDeclaredFields())
        .filter(field -> !field.isAnnotationPresent(Transient.class))
        .filter(field -> !Collections.class.isAssignableFrom(field.getType()))
        .map(Field::getName)
        .collect(Collectors.toSet());
  }
}
