package com.tianyongwei.Test;

public class SuperClass extends SSClass {
  static {
    System.out.println("init SuperClass");
  }

  public static int value = 123;

  public SuperClass() {
    System.out.println("构造 superclass");
  }
}
