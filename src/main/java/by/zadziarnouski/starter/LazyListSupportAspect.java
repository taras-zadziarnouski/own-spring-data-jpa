package by.zadziarnouski.starter;

import java.util.List;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;

@Aspect
public class LazyListSupportAspect {

  @Autowired
  private FirstLevelCacheService cacheService;
  @Autowired
  private ConfigurableApplicationContext context;

  @Before("execution(* by.zadziarnouski.starter.LazySparkList.*(..)) && execution(* java.util.*.*(..))")
  public void beforeEachMethodInvocationCheckAndFillContent(JoinPoint jp) {
    LazySparkList lazyList = (LazySparkList) jp.getTarget();
    if (!lazyList.initialized()) {
      List list = cacheService.getDataFor(lazyList.getOwnerId(), lazyList.getModelClass(), lazyList.getForeignKeyName(), lazyList.getPathToSource(), context);
      lazyList.setContent(list);
    }
  }
}
