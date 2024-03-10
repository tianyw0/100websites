package com.tianyongwei.Test;

public class SSClass {
  static {
    System.out.println("init SSClass");
  }


  public static void main(String[] args) {
    int a = 11;
    int b = 11;
    Integer aa = 11;
    Integer bb = 11;
    Integer aaa = new Integer(11);
    Integer bbb = new Integer(11);
    System.out.println(a);
    System.out.println(aaa.byteValue());
    System.out.println(a == b);
    System.out.println(aa == bb);
    System.out.println(aaa == bbb);
  }
}
