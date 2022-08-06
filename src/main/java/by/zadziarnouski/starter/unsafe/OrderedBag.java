package by.zadziarnouski.starter.unsafe;

import java.util.ArrayList;
import java.util.List;

public class OrderedBag<T> {

  private List<T> list;

  public OrderedBag(T[] args) {
    this.list = new ArrayList<T>(List.of(args));
  }

  public T takeAndRemove() {
    return list.remove(0);
  }

  public int size() {
    return list.size();
  }
}
