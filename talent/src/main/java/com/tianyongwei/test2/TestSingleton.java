package com.tianyongwei.test2;

public class TestSingleton {
  private static TestSingleton ourInstance = new TestSingleton();

  public static TestSingleton getInstance() {
    return ourInstance;
  }

}