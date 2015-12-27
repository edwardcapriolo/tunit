# tunit
Time/Transition Unit testing extensions ![Build status](https://travis-ci.org/edwardcapriolo/tunit.svg?branch=master)

Motivation
-----
At times complex async applications become hard to test. This is especially true for 3rd party applications, async applications, and background threads inside applications. TUnit (Time/Transition/Teknek Unit) is designed to help test these types of applications.

Usage
-----

```java
     TUnit.assertThat( new Callable<Integer>() {
      public Integer call() throws Exception {
        //something mutable
      }}).afterWaitingAtMost(2000, TimeUnit.MILLISECONDS).isEqualTo(40);
```

Motivation
-----

To understand how TUnit might be useful, look at an application that has a class like the one below:

```java
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

```

Because of the way threads work we can generally assume that the value of x will reach 10 in about 1 second. However, we do not know for sure when threads will get scheduled. Also what if we are not using a hardcoded sleep(10) ms? How might we test that?

```java
  @Test
  public void aStatefulThingThatChanges() throws InterruptedException{
    //The Enemy
    Changing c = new Changing();
    new Thread(c).start();
    Thread.sleep(1000);
    Assert.assertEquals(10, c.x);
  }
```

Besides the code being ugly, the test suite runs as long as all the sleep statements. (If control everything you may not have to test like this, but that is usually not the case.) 

The TUnit way
-----

TUnit samples the changing object at 1 ms intervals. If the value of the item monitorsed ever equals the expected value the framework returns normally. 

```java
  @Test
  public void aStatefulThingThatChanges() throws InterruptedException{
    final Changing c = new Changing();
    new Thread(c).start();
    TUnit.assertThat( new Callable<Integer>() {
      public Integer call() throws Exception {
        return c.x;
      }}).afterWaitingAtMost(2000, TimeUnit.MILLISECONDS).isEqualTo(10);
  }
```

In the worst case scenario TUnit will wait no more than a specified amount of time (default 1 second).

```java
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
```

Fluent style
-----

There also is some hooks here for Fluent style stuff if you are interested.

```java
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
```
