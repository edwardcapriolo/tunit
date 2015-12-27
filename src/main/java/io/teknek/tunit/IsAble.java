package io.teknek.tunit;

import java.util.concurrent.Callable;

//You know your overdesigning when you have a class called isAble :)
public class IsAble<T> {

  private final Callable<T> operation;
  private final Function1<Boolean,T> compare;
  private final long waitInMillis;
  
  public IsAble(Callable<T> operation, Function1<Boolean,T> compare, long  waitInMillis){
    this.operation = operation;
    this.compare = compare;
    this.waitInMillis =  waitInMillis;
  }
  
  public void doIs(){
    long start = System.currentTimeMillis();
    T result = null;
    try {
      result = operation.call();
    } catch (Exception e1) {
      throw new RuntimeException(e1);
    }
    if (compare.function(result)){
      return;
    }
    for (long now = System.currentTimeMillis(); now < start + waitInMillis ; now = System.currentTimeMillis()){
      try {
        result = operation.call();
        if (compare.function(result)){
          return;
        }
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
      try {
        Thread.sleep(1);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
    //TODO this could be better
    throw new org.junit.ComparisonFailure("Comparison Returned", String.valueOf(compare.function(result)), result.toString());
  }
}
