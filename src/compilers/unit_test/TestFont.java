/**
 * @author Randy
 * @date Mar 11, 2012
 * @function 
 */
package compilers.unit_test;

import java.awt.GraphicsEnvironment;


public class TestFont
{// 获得当前系统字体

	public TestFont()
	{
	}// 构造器

	public void getfont()
	{//

		String[] fontnames = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getAvailableFontFamilyNames();// 获得当前系统字体

		for (int i = 0; i < fontnames.length; i++)
		{// 输出所有字体
			System.out.println(fontnames[i]);
		}
	}

	public static void main(String[] args)
	{
		TestFont f = new TestFont();
		f.getfont();
	}
}