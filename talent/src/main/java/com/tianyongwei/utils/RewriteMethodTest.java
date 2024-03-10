package com.tianyongwei.utils;

public class RewriteMethodTest {
//  public static void main(String[] args) {
//    new Child();
//  }
public static synchronized void main(String[] a){
  Thread t=new Thread(){
    public void run(){
      System.out.print("sougo");
    }
  };
  t.start();
  System.out.print("Hello");
}
  static synchronized void Sogou(){
    System.out.print("Sogou");
  }
}

class Parent {
  //构造方法
  public Parent() {
    System.out.println("当前类：" + this);
    display();//等价 this.display();
  }

  void display() {
    System.out.println("父类同名普通方法执行");
  }

  @Override
  public String toString() {
    return "Parent{}";
  }
}

class Child extends  Parent{
  //构造方法
  Child() {
    super();//可以省略，不省略的话只能翻盖
    System.out.println("子类构造执行");
  }
  //普通方法
  void display() {
    System.out.println("子类同名普通方法执行");
  }

  @Override
  public String toString() {
    return "Child{}";
  }
}