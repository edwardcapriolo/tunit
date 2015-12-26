package io.teknek.tunit.integration;
import static io.teknek.tunit.TUnit.*;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.ComparisonFailure;
import org.junit.Test;
public class TUnitTests {

  @Test
  public void basicImmutableTest(){
    assertThat( new Callable<Integer>(){
      public Integer call() throws Exception {
        return 5;
      }} ).afterWaitingAtMost(1000, TimeUnit.MILLISECONDS).isEqualTo(5);
  }
  
  @Test
  public void equalsShortcut(){
    assertThat( new Callable<Integer>(){
      public Integer call() throws Exception {
        return 5;
      }} ).isEqualTo(5);
  }
  
  @Test(expected=ComparisonFailure.class)
  public void shouldAssertTest(){
    assertThat( new Callable<Integer>(){
      public Integer call() throws Exception {
        return 6;
      }} ).afterWaitingAtMost(1000, TimeUnit.MILLISECONDS).isEqualTo(5);
  }
  
}
