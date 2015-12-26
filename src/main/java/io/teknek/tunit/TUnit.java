package io.teknek.tunit;

import java.util.concurrent.Callable;

public class TUnit {
  public static <T> TUnitStubThatWaits<T> assertThat(Callable<T> operation){
    return new TUnitStubThatWaits<T>(operation);
  }
}
