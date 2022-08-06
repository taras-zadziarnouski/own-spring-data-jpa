package by.zadziarnouski.starter;

import java.util.List;
import lombok.Data;
import lombok.experimental.Delegate;

@Data
public class LazySparkList implements List {

  @Delegate
  private List content;

  private long ownerId;
  private Class<?> modelClass;
  private String ForeignKeyName;
  private String pathToSource;

  public boolean initialized() {
    return (content != null && !content.isEmpty());
  }
}
