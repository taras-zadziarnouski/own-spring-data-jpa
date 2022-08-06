package by.zadziarnouski.starter.unsafe;

import by.zadziarnouski.springdatajpa.SparkRepository;
import java.beans.Introspector;
import java.lang.reflect.Proxy;

import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.SparkSession;
import org.reflections.Reflections;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SparkDataApplicationContextInitializer implements ApplicationContextInitializer {

  @Override
  public void initialize(ConfigurableApplicationContext context) {
    AnnotationConfigApplicationContext tempContext = new AnnotationConfigApplicationContext(InternalConf.class);
    SparkInvocationHandlerFactory factory = tempContext.getBean(SparkInvocationHandlerFactory.class);
    factory.setRealContext(context);

    DataExtractorResolver extractorResolver = tempContext.getBean(DataExtractorResolver.class);
    context.getBeanFactory().registerSingleton("extractorResolverForSpark", extractorResolver);
    tempContext.close();

    registerSparkBeans(context);
    Reflections scanner = new Reflections(context.getEnvironment().getProperty("spark.packages-to-scan"));

    scanner.getSubTypesOf(SparkRepository.class).forEach(sparkRepositoryInterface -> {
      Object crang = Proxy.newProxyInstance(sparkRepositoryInterface.getClassLoader()
          , new Class[]{sparkRepositoryInterface}
          , factory.create(sparkRepositoryInterface));
      context.getBeanFactory().registerSingleton(Introspector.decapitalize(sparkRepositoryInterface.getSimpleName()), crang);
    });
  }

  private void registerSparkBeans(ConfigurableApplicationContext context) {
    SparkSession sparkSession = SparkSession.builder().master("local[*]").appName(context.getEnvironment().getProperty("spark.app-name")).getOrCreate();
    JavaSparkContext sc = new JavaSparkContext(sparkSession.sparkContext());
    context.getBeanFactory().registerSingleton("sparkSession", sparkSession);
    context.getBeanFactory().registerSingleton("sparkContext", sc);
  }
}
