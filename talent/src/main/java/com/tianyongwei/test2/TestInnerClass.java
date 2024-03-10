package com.tianyongwei.test2;

public class TestInnerClass {

  private int privatevalue;

  int defaultvalue;

  protected int protectedvalue;

  public int publicvalue;

  private void privatemethod() {

  }

  void defaultmethod() {

  }

  protected void protectedmethod() {

  }

  public void publicmethod() {

  }

  public void test() {
    // 1. 编译成功，类自身可以任意访问四种权限的方法
    privatemethod();
    defaultmethod();
    protectedmethod();
    publicmethod();

    InnerClass subclass = new InnerClass();

    subclass.sub_privatemethod();
    subclass.sub_defaultmethod();
    subclass.sub_protectedmethod();
    subclass.sub_publicmethod();


    // 2. 编译成功，类自身可以任意访问四种权限的变量
    System.out.println(privatevalue);
    System.out.println(defaultvalue);
    System.out.println(protectedvalue);
    System.out.println(publicvalue);

    System.out.println(subclass.sub_privatevalue);
    System.out.println(subclass.sub_defaultvalue);
    System.out.println(subclass.sub_protectedvalue);
    System.out.println(subclass.sub_publicvalue);
  }

  class InnerClass {

    private int sub_privatevalue;

    int sub_defaultvalue;

    protected int sub_protectedvalue;

    public int sub_publicvalue;

    private void sub_privatemethod() {

    }

    void sub_defaultmethod() {

    }

    protected void sub_protectedmethod() {

    }

    public void sub_publicmethod() {

    }

    private void testInner() {
      privatemethod();
      defaultmethod();
      protectedmethod();
      publicmethod();

      sub_privatemethod();
      sub_defaultmethod();
      sub_protectedmethod();
      sub_publicmethod();

      System.out.println(sub_privatevalue);
      System.out.println(sub_defaultvalue);
      System.out.println(sub_protectedvalue);
      System.out.println(sub_publicvalue);

      System.out.println(privatevalue);
      System.out.println(defaultvalue);
      System.out.println(protectedvalue);
      System.out.println(publicvalue);
    }

  }

  public static void main(String[] args) {
    TestInnerClass testInnerClass = new TestInnerClass();
    testInnerClass.test();

    // 理解内部类，外部类
    // 理解父类和子类
    // 理解接口和实现类
    // 理解抽象类和子类
    // 匿名内部类

    Testinterface testinterface = new Testinterface() {
      @Override
      public void test() {
        System.out.println("");
      }

      @Override
      public void test2() {

      }
    };

    Testabstract testabstract = new Testabstract() {
      @Override
      void abstracttest() {
        System.out.println("");
      }

      @Override
      void abstracttest2() {

      }
    };

    Testabstract testabstract1 = new Testabstract() {
      @Override
      void abstracttest() {

      }

      @Override
      void abstracttest2() {

      }
    };


  }


}
