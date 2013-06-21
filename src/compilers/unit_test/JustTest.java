/**
 * @author Randy
 * @date Mar 4, 2012
 * @function 
 */
package compilers.unit_test;

import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JustTest
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		
		Stack<Integer> s = new Stack<Integer>();
		s.push(0);
		s.push(1);
		s.push(2);
		s.push(3);
		for (int i = 0; i < 4; i++) {
			System.out.println(s.get(i));
		}
		
		/*int num = 0;
		if (num != (num = 0)) {
			System.out.println("1");
		} else {
			System.out.println("2");
		}*/
		
		
		/*AA testAA = new AA();
		testAA.a = 2;
		BB testBB = new BB(testAA);
		System.out.println("testBB.aa.a1=" + testBB.aa.a);
		testAA.a = 3;
		System.out.println("testBB.aa.a2=" + testBB.aa.a);*/
		
		
		
		/*Stack<Integer> stack = new Stack<Integer>();
		stack.push(1);
		stack.pop();
		stack.push(2);
		stack.push(3);
		stack.pop();
		stack.push(100);
		for (int i = 0; i < stack.size(); i++)
		{
			System.out.print(stack.get(i) + "\t");
		}
		System.out.print(stack.get(2) + "\t");*/
//		String string = "(+,_)";
//		System.out.println();
		
//		Pattern pattern = Pattern.compile("ab");
//		Matcher matcher = pattern.matcher("(+,_)");
//		matcher.matches();
//		System.out.println(matcher.group());
		
//		String string = "1 2 3 ";
//		String[] strings = string.split(" ");
//		System.out.println("strings' length = " + strings.length);
//		
//		long startTime=System.currentTimeMillis();   //获取开始时间

////		　　doSomeThing();  //测试的代码段
//		int sum = 0;
//		for (int i = 0; i < 10000; i++)
//		{
//			sum += i;
//		}
//
//		long endTime=System.currentTimeMillis(); //获取结束时间
//
//		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
		// String string = "\0";
		// string = "";
		// string = null;
		// string = "null";
		// System.out.println(string + "  length:" + string.length());
		// char c = ' ';
		// System.out.println('\u5218');

		// char i = '\"';

		// int i = Integer.valueOf("000A",16);
		// String string = "0x1230";
		// int t3 = 0x1234;
		// Integer.valueOf(Integer.toHexString(t3));
		// System.out.println(i);
		// String t1 = "1234H";
		// int t2 = Integer.valueOf(t1);
		// System.out.println("t2" + t2);
		// String f = "\\n";
//		 int i = '\n';
//		 System.out.println(i);
		// char c = (char) i;
		// System.out.println(c);
		// char h = f.charAt(1);
		// System.out.print("1");
		// System.out.print(h);
		// System.out.print("2");
		// String s1 = "12\r\n";
		// // s1 += "123";
		// System.out.println(s1.length());

		// String s = "你好吗？";
		// char c = '网';
		// System.out.println(s.charAt(1));
		// String string = "124.txt";
		// int index = string.lastIndexOf(".");
		// System.out.println(index);

		// String string = "	{";
		// String[] ss = string.split(" ");
		// System.out.println("ss.length : " + ss.length);
		// System.out.println("ss.content : ");
		// for (int i = 0; i < ss.length; i++)
		// {
		// System.out.println(ss[i]);
		// }

		// String string = "1	2 ";

		// for (int i = 0; i < string.length(); i++)
		// {
		// System.out.println(string.charAt(i));
		// }
		// String re = "\t| ";
		// String[] ss = string.split(re);
		// System.out.println("ss.length : " + ss.length);
		// for (int i = 0; i < ss.length; i++)
		// {
		// System.out.println(ss[i]);
		// }
		// System.out.println('\u4dff' + ";" + '\u4e00' + ";" + '\u4e01' + ";"
		// + '\u9fa5' + ";" + '\u9fa4' + ";" + '\u9fa6');
		//
		// Pattern p = null; // 正则表达式
		// Matcher m = null; // 操作的字符串
		// boolean b = false;
		// // 正则表达式表示汉字的一串字符串
		// p = Pattern.compile("^[\u4e00-\u9fa5]+$");
		// m = p.matcher("中文——名正确");
		// b = m.matches();
		// System.out.println("中文名正确：" + b); // 输出：true
		// //
		// p = Pattern.compile("^[\u4e00-\u9fa5]+$");
		// m = p.matcher("nick");// 错误 只能是中文
		// b = m.matches();
		// System.out.println("中文名错误：" + b); // 输出：false

		// int _s = 01;
		// int ______________ = 000000003;
		// System.out.println(______________);
		// if (c >= '\u4e00' && c <= '\u9fa5')
		// {
		// System.out.println("chinese");
		// }
		// else {
		// System.out.println("English");
		// }

		// String string2 ="sdfsfoodig983g8uhg";
		// [\\\\]([^b,f,n,r,t,',\",\\\\])|(u\\d{4})
		// char c = '’'; // （）{}【】[]{}'‘’“”‘’，,;:;|、、\、、|||||
		// System.out.println((int) c);
		// String t = "[\\]u[0-9,A-F,a-f]{4}";
//		String regex = "[a-zA-Z\u4e00-\u9fa5_]";
		// String regex = "[\\\\]u([0-9,A-F,a-f]{4})";
		// char ch = '-';
//		String goal = "然";
//		boolean b = Pattern.matches(regex, goal);
//		System.out.println("b : " + b);
//
//		String ms = "a,";
//		// System.out.println(ms.length());
//		Pattern p = Pattern.compile("[a-z,A-Z,0-9,\u4e00-\u9fa5,_]*");
//		// .compile("[^+,\\-,*,/,%,=,<,>,!,！,^,&,|,.,,,，,;,；,(,),（,）,\\[,\\],【,】,{,}, ,\t]*");
//		// p=Pattern.compile("[a-z]*");
//		Matcher m = p.matcher(ms);
//		m.find(1);
//		System.out.println("m.start()" + m.start());// 第一个循环返回3，第二个循环返回9
//		System.out.println("m.end()" + m.end());// 返回7,第二个循环返回14
//		System.out.println(m.group() + "\\0");// 返回2233，第二个返回11222
//		if (m.group() == null)
//		{
//			System.out.println("null");
//		}
//		else if (m.group().equals(""))
//		{
//			System.out.println("kong");
//		}
//		else
//		{
//			System.out.println("else");
//		}
//		System.out.println("END");

		// ms = "+-aa+-+-";
		// // m=p.matcher("+-aa+-+-");
		// m.find(2);
		// System.out.println("m.start()"+m.start());//第一个循环返回3，第二个循环返回9
		// System.out.println("m.end()"+m.end());//返回7,第二个循环返回14
		// System.out.print(m.group());//返回2233，第二个返回11222
		// System.out.println("END");

		// if (m.group().equals(""))
		// {
		// System.out.println("null");
		// }
		// else
		// {
		// System.out.println("not");
		// }

	}
}

class AA
{
	public int a = 0;
}

class BB
{
	public int b = 0;
	public AA aa = null;
	
	/**
	 * constructor 
	 */
	public BB(AA aa)
	{
		this.aa = aa;
	}
}
