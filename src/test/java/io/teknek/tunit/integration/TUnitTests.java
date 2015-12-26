package io.teknek.tunit.integration;
import static io.teknek.tunit.TUnit.*;

import java.util.concurrent.Callable;

import org.junit.Test;
public class TUnitTests {

  @Test
  public void aTest(){
    assertThat( new Callable<Integer>(){
      public Integer call() throws Exception {
        return 5;
      }} ).isEqualTo(5).afterWaitingAtMost(1000);
  }
}
