/**
 * @author Randy
 * @date 2013-04-27
 * @function Terminal
 */
package compilers.javabean;

import java.util.ArrayList;

public class Terminal
{
	// define member variable
	public static ArrayList<String> terminals = new ArrayList<String>(); // 终结符数组
	public int index;

	/**
	 * constructor
	 */
	public Terminal(int index)
	{
		this.index = index;
	}

	/**
	 * 初始化静态变量 terminals
	 * 
	 * @param terminal
	 */
	public static void setTerminals(ArrayList<String> terminals)
	{
		Terminal.terminals = terminals;
	}

	/**
	 * 获得指定索引的 terminals 中的值
	 * 
	 * @param index
	 * @return Terminal.terminals[index];
	 */
	public static String get(int index)
	{
		return Terminal.terminals.get(index);
	}

	// TODO containTerminal
	/**
	 * 返回指定终结符的索引，若不存在，返回-1
	 * @param terminal
	 * @return
	 */
	public static int IndexOf(String terminal)
	{
		for (int i = 0; i < Terminal.terminals.size(); i++)
		{
			if (Terminal.get(i).equals(terminal))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * Returns the index of the terminal in Terminal.terminals
	 * 
	 * </blockquote> If no such value of <i>k</i> exists, then {@code -1} is
	 * returned.
	 * 
	 * @param str
	 *            想要搜索的终结符.
	 * @return the index of the the terminal, or {@code -1} if there is no such
	 *         occurrence.
	 */
	public int indexOf(String str)
	{
		for (int i = 0; i < Terminal.terminals.size(); i++)
		{
			if (Terminal.terminals.get(i).equals(str))
			{
				return i;
			}
		}
		return -1;
	}
}
