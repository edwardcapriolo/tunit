package io.teknek.tunit;

public class TUnitStubThatWaits<T>{
  private TUnitStub<T> stub;
  private T expected;
  
  public TUnitStubThatWaits(TUnitStub<T> stub, T expected){
    this.stub = stub;
    this.expected = expected;
  }
  
  public void afterWaitingAtMost(long millis){
    long start = System.currentTimeMillis();
    T result = null;
    try {
      result = stub.getOperation().call();
    } catch (Exception e1) {
      throw new RuntimeException(e1);
    }
    if (result.equals(expected)){
      return;
    }
    for (long now = System.currentTimeMillis(); start + millis < now; now = System.currentTimeMillis()){
      try {
        result = stub.getOperation().call();
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
}