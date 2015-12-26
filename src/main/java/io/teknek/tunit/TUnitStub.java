package io.teknek.tunit;

import java.util.concurrent.Callable;

public class TUnitStub<T> {
  private Callable<T> operation;
  public TUnitStub(Callable<T> operation){
    this.operation = operation;
  }
      
  public TUnitStubThatWaits<T> isEqualTo(T otherThing){
    return new TUnitStubThatWaits<T>(this, otherThing);
  }
  public Callable<T> getOperation() {
    return operation;
  }
  
}