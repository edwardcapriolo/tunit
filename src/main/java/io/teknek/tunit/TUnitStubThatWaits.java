package io.teknek.tunit;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class TUnitStubThatWaits<T>{
  private Callable<T> operation;
  private long waitInMillis = 1000;

  public TUnitStubThatWaits(Callable<T> operation){
    this.operation = operation;
  }
  
  public TUnitStubThatWaits<T> afterWaitingAtMost(long ammount, TimeUnit unit){
    waitInMillis = TimeUnit.MILLISECONDS.convert(ammount, unit);
    return this;
  }

  public void isEqualTo(T expected){
    long start = System.currentTimeMillis();
    T result = null;
    try {
      result = operation.call();
    } catch (Exception e1) {
      throw new RuntimeException(e1);
    }
    if (result.equals(expected)){
      return;
    }
    for (long now = System.currentTimeMillis(); now < start + waitInMillis ; now = System.currentTimeMillis()){
      try {
        result = operation.call();
        if (result.equals(expected)){
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
    throw new org.junit.ComparisonFailure("ComparisonFailure", expected.toString(), result.toString());
  }
  
  public Callable<T> getOperation() {
    return operation;
  }

  public long getWaitInMillis() {
    return waitInMillis;
  }
  
}