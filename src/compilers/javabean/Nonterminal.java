/**
 * @author Randy
 * @date 2013-04-27
 * @function Nonterminal
 */
package compilers.javabean;

import java.util.ArrayList;

public class Nonterminal
{
	// define member variable
	public static ArrayList<String> nonterminals = new ArrayList<String>(); // 非终结符数组
	public int index;

	/**
	 * constructor
	 */
	public Nonterminal(int index)
	{
		this.index = index;
	}

	/**
	 * 初始化静态变量 nonterminals
	 * 
	 * @param terminal
	 */
	public static void setNonterminals(ArrayList<String> nonterminals)
	{
		Nonterminal.nonterminals = nonterminals;
	}

	/**
	 * 获得指定索引的 nonterminals 中的值
	 * 
	 * @param index
	 * @return Nonterminal.nonterminals[index];
	 */
	public static String get(int index)
	{
		return Nonterminal.nonterminals.get(index);
	}
}