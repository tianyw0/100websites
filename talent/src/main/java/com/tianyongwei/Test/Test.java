package com.tianyongwei.Test;

public class Test {
  static
  {
    i=0;
//    System.out.println(i);
  }
  static int i=1;

  public static void main(String args[])
  {
//    System.exit(2);
//    System.out.println(i);
//    System.exit(1);
    Runtime.getRuntime().exit(2);
  }

}
