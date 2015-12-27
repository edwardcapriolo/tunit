package io.teknek.tunit.integration;
import static io.teknek.tunit.TUnit.*;

import io.teknek.tunit.Function1;
import io.teknek.tunit.TUnit;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
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
  
  class Changing implements Runnable {
    public volatile int x;
    public void run() {
      while (x < 10){
        x++;
        try {
          Thread.sleep(10);
        } catch (InterruptedException e) { }
      }
    }
  }
  
  @Test
  public void aStatefulThingThatChanges() throws InterruptedException{
    /*
    //The Enemy
    Changing c = new Changing();
    new Thread(c).start();
    Thread.sleep(1000);
    Assert.assertEquals(10, c.x);
    */
    final Changing c = new Changing();
    new Thread(c).start();
    TUnit.assertThat( new Callable<Integer>() {
      public Integer call() throws Exception {
        return c.x;
      }}).afterWaitingAtMost(2000, TimeUnit.MILLISECONDS).isEqualTo(10);
  }
  
  @Test
  public void aStatefulThingThatChangesButNeverPasss() throws InterruptedException{
    final Changing c = new Changing();
    new Thread(c).start();
    long start = System.currentTimeMillis();
    boolean threw = false;
    try { 
      TUnit.assertThat( new Callable<Integer>() {
      public Integer call() throws Exception {
        return c.x;
      }}).afterWaitingAtMost(2000, TimeUnit.MILLISECONDS).isEqualTo(40);
    } catch (ComparisonFailure ex){
      threw = true;
    }
    Assert.assertTrue(threw);
    Assert.assertTrue((System.currentTimeMillis() - start ) >= 2000 );
  }
  
  @Test
  public void callableAssert() throws InterruptedException{
    final Changing c = new Changing();
    new Thread(c).start();
    TUnit.assertThat( new Callable<Integer>() {
      public Integer call() throws Exception {
        return c.x;
      }}).afterWaitingAtMost(2000, TimeUnit.MILLISECONDS).isNotEqualTo(11);
  }
  

  @Test
  public void callableAssertToFail1() throws InterruptedException{
    final Changing c = new Changing();
    new Thread(c).start();
    TUnit.assertThat( new Callable<Integer>() {
      public Integer call() throws Exception {
        return c.x;
      }}).afterWaitingAtMost(2000, TimeUnit.MILLISECONDS).isNotEqualTo(10);
  }
  
  @Test
  public void callableAssertToFail2() throws InterruptedException{
    TUnit.assertThat( new Callable<Integer>() {
      public Integer call() throws Exception {
        return 5;
      }}).afterWaitingAtMost(2000, TimeUnit.MILLISECONDS).isNotEqualTo(10);
  }
  
  @Test(expected=ComparisonFailure.class)
  public void callableAssertToFail3() throws InterruptedException{
  TUnit.assertThat( new Callable<Integer>() {
    public Integer call() throws Exception {
      return 5;
    }}).afterWaitingAtMost(2000, TimeUnit.MILLISECONDS).isNotEqualTo(5);
  }
  
  
  @Test
  public void callableAssertIs() throws InterruptedException{
    final Changing c = new Changing();
    new Thread(c).start();
    TUnit.assertThat( new Callable<Integer>() {
      public Integer call() throws Exception {
        return c.x;
      }}).afterWaitingAtMost(2000, TimeUnit.MILLISECONDS)
      .is(new Function1<Boolean,Integer>(){
        public Boolean function(Integer x) {
          return x == 10;
        }});
  }
  
  @Test(expected=ComparisonFailure.class)
  public void callableAssertIsBlowsUp() throws InterruptedException{
    final Changing c = new Changing();
    new Thread(c).start();
    TUnit.assertThat( new Callable<Integer>() {
      public Integer call() throws Exception {
        return c.x;
      }}).afterWaitingAtMost(2000, TimeUnit.MILLISECONDS)
      .is(new Function1<Boolean,Integer>(){
        public Boolean function(Integer x) {
          return x == 11;
        }});
  }

  public static class IsEqualTo implements Function1<Boolean,Integer>{
    private Integer n;
    public IsEqualTo(Integer n){
      this.n = n;
    }
    public Boolean function(Integer x) {
      return n.equals(x);
    }
  }
  
  public static IsEqualTo isEqualTo(int x){
    return new IsEqualTo(x);
  }
  
  @Test
  public void callableAssertIsFunc() throws InterruptedException{
    final Changing c = new Changing();
    new Thread(c).start();
    TUnit.assertThat( new Callable<Integer>() {
      public Integer call() throws Exception {
        return c.x;
      }}).is(isEqualTo(10));
  }
  
}
