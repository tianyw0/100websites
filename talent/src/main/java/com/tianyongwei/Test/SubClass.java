package com.tianyongwei.Test;

public class SubClass extends SuperClass{
  static {
    System.out.println("init SubClass");
  }

  static int a;

  public SubClass() {
    System.out.println("init SubClass");
  }
}
