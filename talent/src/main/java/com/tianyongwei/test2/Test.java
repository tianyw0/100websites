package com.tianyongwei.test2;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Test {
  public static void main(String[] args) {
//    Super s = new Sub();
//    System.out.println(s.gretting() + s.name());

    List<Future<String>> futureList = new ArrayList<>();

    ExecutorService ex = Executors.newCachedThreadPool();
    for (int i = 0; i < 10; i++) {
      futureList.add(ex.submit(new CallableTest("" + i)));
    }
    for (Future<String> stringFuture : futureList) {
      try {
        System.out.println(stringFuture.get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
    ex.shutdown();
  }

}

class CallableTest implements Callable<String> {

  private String id;

  public CallableTest(String id) {
    this.id = id;
  }

  @Override
  public String call() throws Exception {
    return "hello," + id;
  }
}

class Super {
  static String gretting() {
    return "hello,I'm super.";
  }

  String name() {
    System.out.println(this);
    return "-super.";
  }
}

class Sub extends Super {
  static String gretting() {
    return "hello,I'm sub.";
  }

  String name() {
    System.out.println(this);
    return "-sub";
  }
}
