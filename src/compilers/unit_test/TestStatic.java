/**
 * @author Randy
 * @date Apr 6, 2012
 * @function 
 */
package compilers.unit_test;

import javax.swing.text.AbstractDocument.BranchElement;

import compilers.javabean.Nonterminal;
import compilers.javabean.Terminal;

public class TestStatic
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// String[] terminal = {"1","2","3"}; // 终结符数组
		// String[] nonterminal = {"4","5","6"}; // 非终结符数组
		// Production production = new Production(terminal, terminal);
		// System.out.println("Production.Terminal[0]="+Production.Terminal[0]);
		// Production production1 = new Production(nonterminal, nonterminal);

		// System.out.println("Production.Terminal[0]="+Production.Terminal[0]);
		// A a = new A();
		// System.out.println("a=" + a.a);
		// a.addA(a);
		// System.out.println("a=" + a.a);
		// System.out.println("a=" + A.a);
		// A.setA(5);
		// System.out.println("a=" + A.a);
		// Terminal terminal = new Terminal(1);
		// // Nonterminal nonterminal = new Nonterminal(1);
		// Object a = terminal;
		// System.out.println(a instanceof Terminal);
//		int num = 2;
//		int c = 1;
//		A a = new A();
//		while (num > (num = c))
//		{
//			c++;
//			System.out.println("inin");
//			if (c > 5)
//			{
//				break;
//			}
//		}
//		System.out.println("out");
		
		Object[][] objects = new Object[2][3];
		objects[1][2] = "string1111";
		System.out.println(objects[1][2]);
		if (objects[0][0] == null)
		{
			System.out.println("null");
		}
	}
}

class A
{
	static int a = 0;
	int b = 0;

	public static void setA(int a)
	{
		A.a = a;
	}

	public void addB()
	{
		b++;
	}
}
