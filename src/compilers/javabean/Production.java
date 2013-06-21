/**
 * @author Randy
 * @date 2013-04-27
 * @function 产生式的结构
 */
package compilers.javabean;

import java.util.ArrayList;

import compilers.parsers.GrammarParserByLL1;

public class Production
{
	// define member variable
	public ArrayList<Object> production = new ArrayList<Object>();
	public int head = 0;

	/**
	 * constructor 1
	 */
	public Production()
	{
		
	}

	/**
	 * constructor 2
	 */
	public Production(int head, ArrayList<Object> production)
	{
		this.production = production;
		this.head = head;
	}
	
	/**
	 * 显示一条产生式
	 */
	public String showProduction()
	{
		String out = "";
		out += Nonterminal.get(head) + "→";
		for (int k = 0; k < production.size(); k++)
		{
			if (GrammarParserByLL1
					.isTerminalObject(production.get(k)))
			{
				Terminal terminal = (Terminal) production.get(k);
				out += Terminal.get(terminal.index)
						+ " ";
			}
			else
			{
				Nonterminal nonterminal = (Nonterminal) production.get(k);
				out += Nonterminal
						.get(nonterminal.index) + " ";
			}
		}
		return out;
	}
	/**
	 * 显示一条产生式，html表格形式
	 */
	public String showProduction_html()
	{
		String out = "<tr><td>";
		out += Nonterminal.get(head) + "</td>" +
				"<td> → </td>" +
				"<td>";
		for (int k = 0; k < production.size(); k++)
		{
			if (GrammarParserByLL1
					.isTerminalObject(production.get(k)))
			{
				Terminal terminal = (Terminal) production.get(k);
				out += Terminal.get(terminal.index)
						+ " ";
			}
			else
			{
				Nonterminal nonterminal = (Nonterminal) production.get(k);
				out += Nonterminal
						.get(nonterminal.index) + " ";
			}
		}
		out += "</td></tr>";
		return out;
	}
	/**
	 * 判断产生式中是否包含null
	 */
	public boolean isContainNull()
	{
		if (production.get(production.size() - 1) instanceof Terminal) 
		{
			Terminal terminal = (Terminal)production.get(production.size() - 1);
			if (terminal.index == Terminal.IndexOf("null")) 
			{
				return true;
			}
		}
		return false;
	}
}
