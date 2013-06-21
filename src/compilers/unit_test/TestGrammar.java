/**
 * @author Randy
 * @date Apr 6, 2012
 * @function 
 */
package compilers.unit_test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.naming.ldap.SortControl;

import compilers.javabean.Nonterminal;
import compilers.javabean.Terminal;
import compilers.parsers.GrammarParserByLL1;
import compilers.scanners.LexicalAnalysis;

public class TestGrammar
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// 获取开始时间
		long startTime = System.currentTimeMillis();

		LexicalAnalysis lexicalAnalysis = new LexicalAnalysis(
				"data/gra.grammar");
		GrammarParserByLL1 grammarParserByLL1 = new GrammarParserByLL1(
				lexicalAnalysis);
		grammarParserByLL1.FindProductions();
		grammarParserByLL1.ComputeFirstSets();

		System.out.println("\nTerminal.terminals:");
		for (int i = 0; i < Terminal.terminals.size(); i++)
		{
			System.out.print(Terminal.get(i) + ", ");
		}
		System.out.println("\nNonterminal.nonterminals:");
		for (int i = 0; i < Nonterminal.nonterminals.size(); i++)
		{
			System.out.print(Nonterminal.get(i) + ", ");
		}
		System.out.println("\n\nFirstSets:");
		HashSet<Integer>[] firstsets = grammarParserByLL1.getFirstSets();
		for (int i = 0; i < firstsets.length; i++)
		{
			System.out.println("\nHashSet<Integer>[" + i + "]:");
			Iterator<Integer> iterator = firstsets[i].iterator();
			while (iterator.hasNext())
			{
				System.out.print(iterator.next() + " ");
			}
		}
		grammarParserByLL1.ComputeFollowSets();
		System.out.println("\n\nFollowSets:");
		HashSet<Integer>[] followSets = grammarParserByLL1.getFollowSets();
		for (int i = 0; i < followSets.length; i++)
		{
			System.out.println("\nHashSet<Integer>[" + i + "]:");
			Iterator<Integer> iterator = followSets[i].iterator();
			while (iterator.hasNext())
			{
				System.out.print(Terminal.get(iterator.next()) + " ");
			}
		}

		grammarParserByLL1.buildLL1Table();
//		System.out.println("\nLL1Table:");
//		int[][] LL1Table = grammarParserByLL1.getLL1Table();
//		for (int i = 0; i < LL1Table.length; i++)
//		{
//			for (int j = 0; j < LL1Table[i].length; j++)
//			{
//				System.out.print(LL1Table[i][j] + "\t");
//			}
//			System.out.println(Nonterminal.get(i));
//		}

		System.out.println("111");
		boolean isRight = grammarParserByLL1.LL1Parser("data/test.ran");
		System.out.println("\nisRight=" + isRight);
		System.out.println("222");
		System.out.println("\nlexicalAnalysis.errorMessageBeans.size()="
				+ lexicalAnalysis.errorMessageBeans.size());
		for (int i = 0; i < lexicalAnalysis.errorMessageBeans.size(); i++)
		{
			System.out.println("error code: "
					+ lexicalAnalysis.errorMessageBeans.get(i).getErrorCode());
		}
		// 获取结束时间
		long endTime = System.currentTimeMillis();
		System.out.println("运行时间=" + (endTime - startTime) + " 毫秒");
		// ArrayList<Integer[]> expressions =
		// grammarParserByLL1.getExpressions();
		// System.out.println("\nArrayList<Integer[]> expressions:");
		// for (int i = 0; i < expressions.size(); i++)
		// {
		// Integer[] exp = expressions.get(i);
		// for (int j = 0; j < exp.length; j++)
		// {
		// System.out.print(exp[j] + "\t");
		// }
		// System.out.println();
		// }

		// System.out.println("\nlexicalAnalysis.keywordsHashMap:");

		// Iterator iterator =
		// lexicalAnalysis.keywordsHashMap.keySet().iterator();
		// while (iterator.hasNext())
		// {
		// String key = iterator.next().toString();
		// System.out.println("key:" + key);
		// System.out.println("value:" +
		// lexicalAnalysis.keywordsHashMap.get(key));
		// }
		// Production production = new Production();
		// HashSet<Integer> hashSet = new HashSet<Integer>();
		// hashSet.add(1);
		// hashSet.add(1);
		// System.out.println("hashSet.size()=" + hashSet.size());

		// int b = 8;
		// int[] a = null;
		// a = new int[b];
		// System.out.println("a.length=");
		// System.out.println(a.length);
	}

}
