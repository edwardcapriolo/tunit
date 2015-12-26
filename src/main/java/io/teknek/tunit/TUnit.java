package io.teknek.tunit;

import java.util.concurrent.Callable;

public class TUnit {
  public static <T> TUnitStub<T> assertThat(Callable<T> operation){
    return new TUnitStub<T>(operation);
  }
}
