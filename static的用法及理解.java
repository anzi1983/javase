package com.prictise;

public class StaticDemo {
	/**
	 * static 的用法和含义
	 * 
	 */
	 private static int count = 0;
	/* 上面的代码起到了给StaticDemo的对象创建一个唯一id以及记录总数的作用，
	 其中count由static修饰，是Person类的成员属性，每次创建一个StaticDemo对象，就会使该属性自加1然后赋给对象的id属性，
	 这样，count属性记录了创建StaticDemo对象的总数，由于count使用了private修饰，所以从类外面无法随意改变。
	 这样，不同对象都共享了一个id属性，但通过构造方法使其自动+1再赋值，这样每个对象调用时就不会覆盖之前的id的值
	 */
	  int id;
	String name;
	 int age;				
/*	给age属性加了static关键字之后，StaticDemo对象就不再拥有age属性了
	age属性会统一交给StaticDemo类去管理，即多个StaticDemo对象只会对应一个age属性，
	一个对象如果对age属性做了改变，其他的对象都会受到影响。我们看到此时的age和toString()方法一样，都是交由类去管理*/
	
	 //无参构造方法   new 一个StaticDemo的时候默认调用无参的构造方法
	 public StaticDemo() {
	        id = ++count;
	    }
	 //自定义方法
	/* public String toString() {
	        return "Name:" + name + ", Age:" + age;
	    }*/
	 public String toString() {
	        return "Id:" + id + ", Name:" + name + ", Age:" + age;
	    }
	 //测试用的主方法
	 public static void main(String[] args) {
		 	/*---------没加static
		 	 * 在未加static之前，定义两个对象s1和s2，并进行赋值，此时类的属性(name,age)属于new的对象
		 	 * 而他们共同调用的toString()方法是属于类的
		 	 * 分别对name和age进行赋值，打印
		 	 * 此时打印结果为：
		 	 * Name:zhangsan, Age:10
			 * Name:lisi, Age:12
			 * 
			 * --------加了static
			 * 在给成员变量age加了static之后，这时age属于类，和toString()方法一样，是不同对象共享的一个属性
			 * 就是每new一个对象.age时都会对StaticDemo里的静态属性age进行赋值
			 * 再次进行打印，打印结果为：
			 * Name:zhangsan, Age:12
			 * Name:lisi, Age:12
		 	 */
		 	StaticDemo s1 = new StaticDemo();
		 	s1.name = "zhangsan";
		 	s1.age = 10;
	        StaticDemo s2 = new StaticDemo();
	        s2.name = "lisi";
	        s2.age = 12;
	        System.out.println(s1);
	        System.out.println(s2);
	        
	        System.out.println(count);
		}
}
