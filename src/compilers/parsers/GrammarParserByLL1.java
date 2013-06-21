/**
 * @author Randy
 * @date 2013-05-30
 * @function 用 LL1法 分析文法
 */
package compilers.parsers;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Stack;

import compilers.javabean.ErrorMessageBean;
import compilers.javabean.IdentifierBean;
import compilers.javabean.LL1ProcessLog;
import compilers.javabean.Nonterminal;
import compilers.javabean.Production;
import compilers.javabean.Terminal;
import compilers.scanners.LexicalAnalysis;
import compilers.tool.HandleXMLFile;
import compilers.util.Tree;
import compilers.util.TreeElem;
import compilers.util.TreeNode;

public class GrammarParserByLL1
{
	// define member variable
	LexicalAnalysis lexicalAnalysis = null;
	ArrayList<Production> productions = new ArrayList<Production>();
	HandleXMLFile handleXMLFile = new HandleXMLFile(new File(
			"data/ErrorMessage.xml"));
	HashSet<Integer>[] firstSets; // 暂时不初始化，因为长度不知道
	ArrayList<String> firstSetLog = new ArrayList<String>();// 记录构造过程
	HashSet<Integer>[] followSets;
	ArrayList<String> followSetLog = new ArrayList<String>();// 记录构造过程
	int nullIndexInTerminals = -1;
	int[][] LL1Table = null;
	ArrayList<LL1ProcessLog> LL1ParseLog = new ArrayList<LL1ProcessLog>();// 记录LL1语法分析过程过程
	// 记录要分析的每条语句
	ArrayList<Integer[]> expressions = new ArrayList<Integer[]>();
	// 记录要分析的每条语句中含有ID的在标识符表中的索引
	ArrayList<Integer[]> expsIdIndex = new ArrayList<Integer[]>();
	Object[][] productionArray = null;
	public static int[][] LL1TableforShow = null;
	public static Object[][] productionArrayforShow = null;
	// 记录四元式
	ArrayList<String> quaternions = new ArrayList<String>();

	public ArrayList<String> getQuaternions()
	{
		return quaternions;
	}

	/**
	 * constructor
	 */
	public GrammarParserByLL1(LexicalAnalysis lexicalAnalysis)
	{
		this.lexicalAnalysis = lexicalAnalysis;
	}

	public ArrayList<Integer[]> getExpressions()
	{
		return expressions;
	}

	public Object[][] getProductionArray()
	{
		return productionArray;
	}

	public int[][] getLL1Table()
	{
		return LL1Table;
	}

	public ArrayList<Production> getProductions() {
		return productions;
	}

	public HashSet<Integer>[] getFirstSets()
	{
		return firstSets;
	}

	public HashSet<Integer>[] getFollowSets()
	{
		return followSets;
	}

	public ArrayList<String> getFirstSetLog() {
		return firstSetLog;
	}

	public ArrayList<String> getFollowSetLog() {
		return followSetLog;
	}

	public ArrayList<LL1ProcessLog> getlL1ParseLog() {
		return LL1ParseLog;
	}

	/**
	 * ========================================================================
	 * LL1文法分析第1步: 初始化产生式 productions
	 * 
	 * @return 如果文法正确，返回true；否则false，初始化productions失败！
	 */
	public boolean FindProductions()
	{
		// 1. 分析文法，找出终结符
		lexicalAnalysis.clearLexicalAnalysisResult(); // 清空词法分析相关结果集
		lexicalAnalysis.scanRows(); // 词法分析
		if (lexicalAnalysis.errorMessageBeans.size() != 0)
		{
			// 交给上级调用程序处理错误
			return false;
		}
		String[] duals = lexicalAnalysis.getDualExpression().split(" ");
		int nonterminalIndex = 0;
		if (duals[0].charAt(1) != '%' || duals[1].charAt(1) != '%')
		{
			// 报错，文法规则必须以"%%"开头
			this.addGrammarParserError("B0001", getDualLeft(duals[0])
					+ getDualLeft(duals[1]), lexicalAnalysis.errorMessageBeans);
			return false;			
		}
		// 匹配到 "%%" 终结符开始
		for (int i = 2; i < duals.length - 1; i++)
		{
			//						这两个%%是为了判断后面的产生式
			if (duals[i].charAt(1) == '%' && duals[i + 1].charAt(1) == '%')				
			{
				nonterminalIndex = i;
				break;
			}
			String dual = duals[i];
			int j = dual.lastIndexOf(",");
			String terminal = dual.substring(1, j);
			// 如果为标识符，把标识符找到，添加到终结符表中
			if (terminal.equals("ID"))
			{
				j = Integer.parseInt(dual.substring(j + 1,
						dual.indexOf(")")));
				terminal = lexicalAnalysis.identifierList.get(j)
						.getName();
			}
			Terminal.terminals.add(terminal);
		}

		// 2. 分析文法，找出非终结符
		if (nonterminalIndex == 0)
		{
			// 报错，产生式必须以"%%"开头
			this.addGrammarParserError("B0002", "",
					lexicalAnalysis.errorMessageBeans);
			return false;
		}
		// 把终结符添加到关键字表中
		for (int i = 0; i < Terminal.terminals.size(); i++)
		{
			lexicalAnalysis.keywordsHashMap.put(Terminal.get(i), -1);
		}
		// 清空 lexicalAnalysis.identifierList等
		lexicalAnalysis.clearLexicalAnalysisResult();
		lexicalAnalysis.scanRows(); // 词法分析(!!! 关键字表已经改变)
		duals = lexicalAnalysis.getDualExpression().split(" ");
		// 初始化非终结符
		for (int i = 0; i < lexicalAnalysis.identifierList.size(); i++)
		{
			Nonterminal.nonterminals.add(lexicalAnalysis.identifierList.get(i)
					.getName());
		}
		// 获得产生式
		nonterminalIndex += 2;
		for (int i = nonterminalIndex; i < duals.length; i++)
		{
			Production production = new Production();
			if (! this.getDualLeft(duals[i]).equals("ID"))
			{
				// 报错，产生式头部必须为非终结符
				this.addGrammarParserError("B0003", "",
						lexicalAnalysis.errorMessageBeans);
				return false;
			}
			production.head = this.getDualRight(duals[i]);
			i++;
			if (duals[i].charAt(1) != ':')
			{
				// 报错，产生式左部和右部必须用':'连接
				this.addGrammarParserError("B0004", "",
						lexicalAnalysis.errorMessageBeans);
				return false;				
			}
			i++;
			String dual = duals[i];
			while (dual.charAt(1) != ';')
			{
				if (this.getDualLeft(dual).equals("ID"))
				{
					production.production.add(new Nonterminal(this
							.getDualRight(dual)));
				}
				else
				{
					int index = Terminal
							.IndexOf(this.getDualLeft(dual));
					production.production.add(new Terminal(index));
				}
				// 判断产生式是否以';'结尾
				if (i + 1 < duals.length)
				{
					dual = duals[++i];
				}
				else
				{
					// 报错，产生式必须以';'结尾
					this.addGrammarParserError("B0005", "",
							lexicalAnalysis.errorMessageBeans);
					return false;
				}
			}
			
			productions.add(production);
		}
		return true;
	}

	/**
	 * ========================================================================
	 * LL1文法分析第2步: 计算first集（FirstSets）
	 * 
	 * @return 正确计算得到了first集，返回true
	 */
	@SuppressWarnings("unchecked")
	public boolean ComputeFirstSets()
	{
		if (this.productions.size() == 0)
		{
			// 报错，产生式空，不能进行文法分析
			this.addGrammarParserError("B0006", "",
					lexicalAnalysis.errorMessageBeans);
			return false;
		}
		// 把'#'加入到终结符集
		Terminal.terminals.add("#");

		// 初始化 first集
		this.firstSets = new HashSet[Nonterminal.nonterminals.size()];
		for (int i = 0; i < firstSets.length; i++)
		{
			firstSets[i] = new HashSet<Integer>();
		}
		// 1. 根据规则一：X->a...
		for (int i = 0; i < productions.size(); i++)
		{
			Production production = productions.get(i);
			// 如果开头为终结符，加入first集
			if (isTerminalObject(production.production.get(0)))
			{
				Terminal terminal = (Terminal) production.production.get(0);
				firstSets[production.head].add(terminal.index);
				// write log
				String head = Nonterminal.get(production.head);
				String firstTerminal = Terminal.get(terminal.index);
				firstSetLog.add("根据规则一，把产生式[" + production.showProduction() 
						+ "]中的[" + firstTerminal + "]加入到First(" + head + "),则"
						+ this.getOneFirstSet(production.head));
			}
		}
		// 2. 根据规则二：X->Y...（Y为非终结符）
		nullIndexInTerminals = Terminal.IndexOf("null");
		// 循环寻找，直到每个first集中的个数不在增加
		int num = 0;
		int count=0;
		while (num != (num = this.getNumOfAllSetElements(firstSets)))
		{
			for (int i = 0; i < productions.size(); i++)
			{
				Production production = productions.get(i);
				boolean allContainNull = true;
				int j = 0;
				for (; !isTerminalObject(production.production.get(j))
						&& j < production.production.size(); j++)
				{
					Nonterminal nonterminal = (Nonterminal) production.production
							.get(j);
					// 如果first集中包含空
					if (firstSets[nonterminal.index]
							.contains(nullIndexInTerminals))
					{
						this.addNotContainsNullFirstSetToAnother(
								firstSets[nonterminal.index],
								firstSets[production.head],
								nullIndexInTerminals);
					}
					else
					{
						allContainNull = false;
						this.addOneSetToAnother(firstSets[nonterminal.index],
								firstSets[production.head]);
						break;
					}
				}
				// 如果 X->YZ...K，且YZ...K全部包含空，则把null添加到first(X)中
				if (allContainNull && j == production.production.size())
				{
					firstSets[production.head].add(nullIndexInTerminals);
				}
			}
		}
		return true;
	}

	/**
	 * ========================================================================
	 * LL1文法分析第3步: 计算follow集（followSets）
	 * 
	 * @return 正确计算得到了follow集，返回true
	 */
	@SuppressWarnings("unchecked")
	public boolean ComputeFollowSets()
	{
		if (this.productions.size() == 0)
		{
			// 报错，产生式空，不能进行文法分析
			this.addGrammarParserError("B0006", "",
					lexicalAnalysis.errorMessageBeans);
			return false;
		}

		// 初始化 follow集
		this.followSets = new HashSet[Nonterminal.nonterminals.size()];
		for (int i = 0; i < followSets.length; i++)
		{
			followSets[i] = new HashSet<Integer>();
		}
		// 1. 根据规则一：对于文法开始符号S，将'#'置于follow(S)中
		followSets[0].add(Terminal.IndexOf("#"));

		// 2. 根据规则二：若有A->...Bb，则将非空first（b）加到follow（B）中
		for (int i = 0; i < productions.size(); i++)
		{
			Production production = productions.get(i);
			for (int j = 0; j < production.production.size() - 1; j++)
			{
				// 只对非终结符操作
				if (!isTerminalObject(production.production.get(j)))
				{
					Nonterminal nonterminal = (Nonterminal) production.production
							.get(j);
					if (isTerminalObject(production.production.get(j + 1)))
					{
						// 把终结符添加到follow集中
						Terminal terminal = (Terminal) production.production
								.get(j + 1);
						followSets[nonterminal.index].add(terminal.index);
						j++;
					}
					else
					{
						// 将非空first（j+1）加到follow（j）中
						Nonterminal nonterminal2 = (Nonterminal) production.production
								.get(j + 1);
						if (firstSets[nonterminal2.index]
								.contains(nullIndexInTerminals))
						{
							this.addNotContainsNullFirstSetToAnother(
									firstSets[nonterminal2.index],
									followSets[nonterminal.index],
									nullIndexInTerminals);
						}
						else
						{
							this.addOneSetToAnother(
									firstSets[nonterminal2.index],
									followSets[nonterminal.index]);
						}
					}
				}// end if
			}
		}
		// 3. 根据规则三：若有A->...Bb，则将非空follow（b）加到follow（B）中
		nullIndexInTerminals = Terminal.IndexOf("null");
		// 循环寻找，直到每个follow集中的个数不在增加
		int num = 0;
		while (num != (num = this.getNumOfAllSetElements(followSets)))
		{
			for (int i = 0; i < productions.size(); i++)
			{
				Production production = productions.get(i);
				int productionEleNum = production.production.size();
				for (int j = 0; j < productionEleNum; j++)
				{
					// 只对非终结符操作
					if (!isTerminalObject(production.production.get(j)))
					{
						Nonterminal nonterminal = (Nonterminal) production.production
								.get(j);
						if (j == productionEleNum - 1)
						{
							addOneSetToAnother(followSets[production.head],
									followSets[nonterminal.index]);
							break;
						}
						if (isTerminalObject(production.production.get(j + 1)))
						{
							j++;
						}
						else
						{
							Nonterminal nonterminal2 = (Nonterminal) production.production
									.get(j + 1);
							// 如果b非空，
							if (firstSets[nonterminal2.index]
									.contains(nullIndexInTerminals))
							{
								addOneSetToAnother(followSets[production.head],
										followSets[nonterminal.index]);
							}
						}
					}// end if
				}// end inner for
			}// end outer for
		}// end while
		return true;
	}

	/**
	 * ========================================================================
	 * LL1文法分析第4步: 构建LL1预测分析表
	 * 
	 * @return 正确构建了 LL1Table ，返回true
	 */
	public boolean buildLL1Table()
	{
		if (this.productions.size() == 0)
		{
			// 报错，产生式空，不能进行文法分析
			this.addGrammarParserError("B0006", "",
					lexicalAnalysis.errorMessageBeans);
			return false;
		}

		// 初始化 LL1Table
		LL1Table = new int[Nonterminal.nonterminals.size()][Terminal.terminals.size()];
		// 把 LL1Table 中的每个元素初始化为-2（-2表示错误）
		for (int i = 0; i < LL1Table.length; i++)
		{
			for (int j = 0; j < LL1Table[i].length; j++)
			{
				LL1Table[i][j] = -2;
			}
		}
		productionArray = new Object[productions.size()][];
		nullIndexInTerminals = Terminal.IndexOf("null");
		for (int i = 0; i < productionArray.length; i++)
		{
			productionArray[i] = productions.get(i).production.toArray();
		}
		/**
		 * 对于每个产生式 A→α，进行如下操作
		 * 
		 * 1. 对于first（α）中的每个终结符号a，将A→a加入到LL1Table[A,a]中
		 * 
		 * 2. 如果null在first（α）中，那么对于follow（A）中的每个终结符号b，将A→α加入到LL1Table[A,b]中。
		 * ...如果null在first（α）中,且'#'在follow（A）中，也将A→α加入到LL1Table[A,#]中
		 * 
		 * 3. [错误恢复]将follow（A）的所有符号c放到LL1Table[A,c]中，用-1填充。
		 */
		for (int i = 0; i < productionArray.length; i++)
		{
			int head = productions.get(i).head;
			// 如果α为终结符
			if (isTerminalObject(productionArray[i][0]))
			{
				Terminal terminal = (Terminal) productionArray[i][0];
				// 如果终结符为空
				if (terminal.index == nullIndexInTerminals)
				{
					this.setLL1TableRow(head, followSets[head], i);
				}
				// 如果终结符不为空
				else
				{
					LL1Table[head][terminal.index] = i;

					// 3. [错误恢复]将follow（A）的所有符号c放到LL1Table[A,c]中，用-1填充。
					this.setLL1TableRow(head, followSets[head], -1);
				}
			}
			// 如果α为非终结符
			else
			{
				Nonterminal nonterminal = (Nonterminal) productionArray[i][0];
				// 对于first（α）中的每个终结符号a，将A→a加入到LL1Table[A,a]中
				this.setLL1TableRow(head, firstSets[nonterminal.index], i);
				// 如果null在first（α）中
				if (firstSets[nonterminal.index].contains("null"))
				{
					this.setLL1TableRow(head, followSets[head], i);
				}
				else
				{
					// 3. [错误恢复]将follow（A）的所有符号c放到LL1Table[A,c]中，用-1填充。
					this.setLL1TableRow(head, followSets[head], -1);
				}
			}
		}// end for
		return true;
	}

	/**
	 * ========================================================================
	 * LL1文法分析第5步: 表驱动的预测语法分析
	 * 
	 * @return 语法分析正确 ，返回true，否则返回false（错误信息包含在
	 *         lexicalAnalysis.errorMessageBeans 中）
	 */
	public boolean LL1Parser(String fileName)
	{
		lexicalAnalysis.setFileName(fileName);
		lexicalAnalysis.clearLexicalAnalysisResult();
		this.clearSyntaxAnalysisResult();
		lexicalAnalysis.scanRows();
		if (lexicalAnalysis.errorMessageBeans.size() != 0)
		{
			// 交给上级调用程序处理错误
			return false;
		}
		if (lexicalAnalysis.getDualExpression() == "") 
		{
			// 报错，句子（表达式）为空！
			this.addGrammarParserError("B0008", "",
					lexicalAnalysis.errorMessageBeans);
			return false;
		}
		String[] duals = lexicalAnalysis.getDualExpression().split(" ");
		// 1. 得到每个要解析的句子（表达式）
		Integer[] exp;
		int endIndex = Terminal.IndexOf("#"); // 得到结束符‘#’的索引
		for (int i = 0; i < duals.length; i++)
		{
			int j = i;
			while (duals[j].charAt(1) != ';')
			{
				if (++j == duals.length)
				{
					// 报错，句子（表达式）必须以';'结尾
					this.addGrammarParserError("B0007", "",
							lexicalAnalysis.errorMessageBeans);
					return false;
				}
			}
			exp = new Integer[j - i + 1];
			int k = 0;
			for (k = 0; k < exp.length - 1; k++)
			{
				String terminal = getDualLeft(duals[i++]);
				if (terminal.equals("ID"))
				{
					terminal = "id";
				}
				int index = Terminal.IndexOf(terminal);
				exp[k] = index;
			}
			exp[k] = endIndex; // 将‘#’的索引加到句子（表达式）末尾，表示结束
			expressions.add(exp);
		}
		// 2. 使用算法对每个句子（表达式）进行文法分析
		int len = expressions.size();
		for (int i = 0; i < len; i++)
		{
			// 定义栈
			exp = expressions.get(i);
			int j = 0;
			Stack<Object> stack = new Stack<Object>();
			stack.push(new Nonterminal(0));// 把开始符号S入栈
			
			LL1ProcessLog LL1Log = new LL1ProcessLog();
			LL1Log.symbolStack = getSymbolStack(stack);
			LL1Log.currentSymbol = "";
			LL1Log.inputString = getExpression(j, exp);
			LL1Log.instruction = "把开始符号压入栈";
			LL1ParseLog.add(LL1Log);
			
			while (! stack.empty())
			{
				// 如果栈顶为终结符
				if (isTerminalObject(stack.peek()))
				{
					// TODO 重写
					Terminal terminal = (Terminal) stack.peek();
					if (terminal.index == exp[j])
					{
						LL1ProcessLog LL1Log1 = new LL1ProcessLog();
						LL1Log1.symbolStack = getSymbolStack(stack);
						LL1Log1.currentSymbol = Terminal.get(exp[j]);
						LL1Log1.inputString = getExpression(j+1, exp);
						LL1Log1.instruction = "匹配，弹出栈顶符号" + Terminal.get(exp[j])
								+ "，并读入下一符号" + Terminal.get(exp[j+1]);
						LL1ParseLog.add(LL1Log1);
						
						stack.pop();
						j++; // TODO 是否要判断j的值，防止溢出？？？
					}
					else
					{
						stack.pop();
						// 报错，句子（表达式）中缺少当前终结符与文法匹配
						this.addGrammarParserError("B0009", "",
								lexicalAnalysis.errorMessageBeans);
					}
				}
				// 如果栈顶为非终结符
				else
				{
					Nonterminal nonterminal = (Nonterminal) stack.peek();
					if (LL1Table[nonterminal.index][exp[j]] == -2)
					{
						// 报错，句子（表达式）中当前终结符多余，可使串指针下移一个位置，继续分析
						this.addGrammarParserError("B0010", "",
								lexicalAnalysis.errorMessageBeans);
						
						LL1ProcessLog LL1Log4 = new LL1ProcessLog();
						LL1Log4.symbolStack = getSymbolStack(stack);
						LL1Log4.currentSymbol = Terminal.get(exp[j]);
						LL1Log4.inputString = getExpression(j+1, exp);
						LL1Log4.instruction = "错误，输入串中当前终结符" + Terminal.get(exp[j]) 
								+ "多余，使串指针下移一个位置，继续分析";
						LL1ParseLog.add(LL1Log4);
						
						j++;
					}
					else if (LL1Table[nonterminal.index][exp[j]] == -1)
					{
						// 报错，句子（表达式）中缺少A表示的结构，从栈中弹出A，继续分析
						this.addGrammarParserError("B0011", "",
								lexicalAnalysis.errorMessageBeans);
						
						LL1ProcessLog LL1Log5 = new LL1ProcessLog();
						LL1Log5.symbolStack = getSymbolStack(stack);
						LL1Log5.currentSymbol = Terminal.get(exp[j]);
						LL1Log5.inputString = getExpression(j+1, exp);
						Nonterminal nonterminal2 = (Nonterminal)stack.peek();
						String nontermialString = Nonterminal.get(nonterminal2.index);
						LL1Log5.instruction = "错误，输入串中中缺少" + nontermialString 
								+ "表示的结构，从栈中弹出" + nontermialString + "，继续分析";
						LL1ParseLog.add(LL1Log5);
						
						stack.pop();
					}
					// 没有出错，正常分析
					else
					{
						LL1ProcessLog LL1Log2 = new LL1ProcessLog();
						LL1Log2.symbolStack = getSymbolStack(stack);
						LL1Log2.currentSymbol = Terminal.get(exp[j]);
						LL1Log2.inputString = getExpression(j+1, exp);
						
						stack.pop();
						int proNum = LL1Table[nonterminal.index][exp[j]];
						Object[] production = productionArray[proNum];

						if (productions.get(proNum).isContainNull()) 
						{
							LL1Log2.instruction = "展开，当前产生式：" + productions.get(proNum).showProduction()
									+ "，仅弹出栈顶符号";
						}
						else
						{
							LL1Log2.instruction = "展开，当前产生式：" + productions.get(proNum).showProduction()
												+ "，并将其右部逆序入栈";
						}						
						LL1ParseLog.add(LL1Log2);

						if (!isTerminalObject(production[0])
								|| ((Terminal) production[0]).index != nullIndexInTerminals)
						{
							for (int k = production.length - 1; k > -1; k--)
							{
								stack.push(production[k]);
							}
						}
					}
				}// end else
			}// end while
			LL1ProcessLog LL1Log3 = new LL1ProcessLog();
			LL1Log3.symbolStack = getSymbolStack(stack);
			LL1Log3.currentSymbol = Terminal.get(exp[j]);
			LL1Log3.inputString = "";
			LL1Log3.instruction = "匹配，分析成功 ^_^";
			LL1ParseLog.add(LL1Log3);
			
			if (i < len) 
			{
				LL1ParseLog.add(new LL1ProcessLog());
			}
		}
		return (lexicalAnalysis.errorMessageBeans.size() == 0);
	}
	
	/**
	 * ========================================================================
	 * LL1文法分析第6步: 属性文法语义分析
	 * 
	 * @return 语义分析正确 ，返回true，否则返回false（错误信息包含在
	 *         lexicalAnalysis.errorMessageBeans 中）
	 */
	public boolean semanticAnalysis(String fileName)
	{
		lexicalAnalysis.setFileName(fileName);
		lexicalAnalysis.clearLexicalAnalysisResult();
		this.clearSyntaxAnalysisResult();
		lexicalAnalysis.scanRows();
		if (lexicalAnalysis.errorMessageBeans.size() != 0)
		{
			// 交给上级调用程序处理错误
			return false;
		}
		String[] duals = lexicalAnalysis.getDualExpression().split(" ");
		// 1. 得到每个要解析的句子（表达式）
		Integer[] exp;
		Integer[] expIdIndex;
		int endIndex = Terminal.IndexOf("#"); // 得到结束符‘#’的索引
		for (int i = 0; i < duals.length; i++)
		{
			int j = i;
			while (duals[j].charAt(1) != ';')
			{
				if (++j == duals.length)
				{
					// 报错，句子（表达式）必须以';'结尾
					this.addGrammarParserError("B0007", "",
							lexicalAnalysis.errorMessageBeans);
					return false;
				}
			}
			exp = new Integer[j - i + 1];
			expIdIndex = new Integer[j - i + 1];
			int k = 0;
			for (k = 0; k < exp.length - 1; k++)
			{
				String terminal = getDualLeft(duals[i++]);
				int idIndex = -1;
				if (terminal.equals("ID"))
				{
					terminal = "id";
					idIndex = getDualRight(duals[i - 1]);
				}
				int index = Terminal.IndexOf(terminal);
				exp[k] = index;
				expIdIndex[k] = idIndex;
			}
			exp[k] = endIndex; // 将‘#’的索引加到句子（表达式）末尾，表示结束
			expIdIndex[k] = -1;
			expressions.add(exp);
			expsIdIndex.add(expIdIndex);
		}
		// 2. 使用算法对每个句子（表达式）进行文法分析

		// 初始化终结符和非终结符与索引对应的关系，加快查找速度
		HashMap<String, Integer> terminalIndex = new HashMap<String, Integer>();
		HashMap<String, Integer> nonterminalIndex = new HashMap<String, Integer>();
		for (int i = 0; i < Terminal.terminals.size(); i++)
		{
			terminalIndex.put(Terminal.get(i), i);
		}
		for (int i = 0; i < Nonterminal.nonterminals.size(); i++)
		{
			nonterminalIndex.put(Nonterminal.get(i), i);
		}

		for (int i = 0; i < expressions.size(); i++)
		{
			// 定义栈
			exp = expressions.get(i);
			expIdIndex = expsIdIndex.get(i);
			int j = 0;
			Stack<Object> stack = new Stack<Object>(); // 语法栈
			Stack<Integer> semanStack = new Stack<Integer>(); // 语义栈
			stack.push(new Nonterminal(0));// 把开始符号S入栈

			// 开始建立语法树 并把根节点入栈
			Tree semanTree = new Tree();
			Nonterminal tempNonterminal = new Nonterminal(
					nonterminalIndex.get("G"));
			Terminal tempTerminal = null;
			int nodeIndex = semanTree.addNode(new TreeNode(new TreeElem(
					tempNonterminal), 0, -1, -1)); // father节点设为0，虽不合理，但为了下面程序，不受影响
			semanStack.push(nodeIndex);

			// 语法栈空结束
			while (!stack.empty())
			{
				// TODO 语义分析1， 当终结符出栈时，进行语义动作
				// 如果栈顶为终结符
				if (isTerminalObject(stack.peek()))
				{
					// TODO 重写
					Terminal terminal = (Terminal) stack.peek();
					if (terminal.index == exp[j])
					{
						stack.pop();
						j++; // TODO 是否要判断j的值，防止溢出？？？

						// 语义分析 int, float, id
						int currentNode = semanStack.pop();
						TreeNode currentTreeNode = semanTree.nodes
								.get(currentNode);
						TreeNode fatherTreeNode = semanTree.nodes
								.get(currentTreeNode.father);
						// int 出栈
						if (((Terminal) (currentTreeNode.elem.token)).index == terminalIndex
								.get("int"))
						{
							fatherTreeNode.elem.type = "int";
						}
						// float 出栈
						else if (((Terminal) (currentTreeNode.elem.token)).index == terminalIndex
								.get("float"))
						{
							fatherTreeNode.elem.type = "float";
						}
						// + 出栈
						else if (((Terminal) (currentTreeNode.elem.token)).index == terminalIndex
								.get("+"))
						{
							currentTreeNode.elem.val = lexicalAnalysis
									.newTempInIdentifierList();
						}
						// * 出栈
						else if (((Terminal) (currentTreeNode.elem.token)).index == terminalIndex
								.get("*"))
						{
							currentTreeNode.elem.val = lexicalAnalysis
									.newTempInIdentifierList();
						}
						// id 出栈
						else if (((Terminal) (currentTreeNode.elem.token)).index == terminalIndex
								.get("id"))
						{
							int id_entry = expIdIndex[j - 1];

							// 父节点 是 ‘F’： F → id
							if (((Nonterminal) (fatherTreeNode.elem.token)).index == nonterminalIndex
									.get("F"))
							{
								fatherTreeNode.elem.val = id_entry;
							}
							// 父节点 是 ‘C’或‘C1’： C→id C1 或 C1→, id C1
							else if (((Nonterminal) (fatherTreeNode.elem.token)).index == nonterminalIndex
									.get("C")
									|| ((Nonterminal) (fatherTreeNode.elem.token)).index == nonterminalIndex
											.get("C1"))
							{
								// 产生四元式 fill(id.type = c/c1.inh)
								IdentifierBean id = lexicalAnalysis.identifierList
										.get(id_entry);
								String type = fatherTreeNode.elem.type;
								quaternions.add("fill(" + id.getName()
										+ ".type = " + type + ")");
								id.setType(type);
							}
							// 父节点 是 ‘F’： F → id
							else if (((Nonterminal) (fatherTreeNode.elem.token)).index == nonterminalIndex
									.get("A"))
							{
								fatherTreeNode.elem.inh = id_entry;
							}
						}
					}
					else
					{
						stack.pop();
						semanStack.pop(); // TODO 暂时不处理语义错误
						// 报错，句子（表达式）中缺少当前终结符与文法匹配
						this.addGrammarParserError("B0009", "",
								lexicalAnalysis.errorMessageBeans);
					}
				}
				// 如果栈顶为非终结符
				else
				{
					Nonterminal nonterminal = (Nonterminal) stack.peek();
					if (LL1Table[nonterminal.index][exp[j]] == -2)
					{
						// 报错，句子（表达式）中当前终结符多余，可使串指针下移一个位置，继续分析
						this.addGrammarParserError("B0010", "",
								lexicalAnalysis.errorMessageBeans);
						j++;
					}
					else if (LL1Table[nonterminal.index][exp[j]] == -1)
					{
						// 报错，句子（表达式）中缺少A表示的结构，从栈中弹出A，继续分析
						this.addGrammarParserError("B0011", "",
								lexicalAnalysis.errorMessageBeans);
						stack.pop();
						semanStack.pop(); // TODO 暂时不处理语义错误
					}
					// 没有出错，正常分析
					else
					{
						// TODO 语义分析2， 当非终结符出栈时，进行的语义动作
						stack.pop(); // 语法出栈
						int currentNode = semanStack.pop(); // 语义出栈
						int proNum = LL1Table[nonterminal.index][exp[j]];
						Object[] production = productionArray[proNum];

						// 不把 null 入栈
						if (!isTerminalObject(production[0])
								|| ((Terminal) production[0]).index != nullIndexInTerminals)
						{
							for (int k = production.length - 1; k > -1; k--)
							{
								stack.push(production[k]);
								// 为语义树添加叶子
								TreeElem treeElem = new TreeElem(production[k]);
								TreeNode treeNode = new TreeNode(treeElem,
										currentNode, -1, -1);
								// add node
								int temp = semanTree.addNode(treeNode);
								// 设置 nextSibling
								if (production.length - k > 1)
								{
									semanTree.nodes.get(temp).nextSibling = temp - 1;
								}
								// 设置 currentNode 的 firstChild
								if (k == 0)
								{
									semanTree.nodes.get(currentNode).firstChild = temp;
								}
								semanStack.push(temp);
							}

							// 非终结符 出栈 语义动作
							TreeNode currentTreeNode = semanTree.nodes
									.get(currentNode);
							TreeNode fatherTreeNode = semanTree.nodes
									.get(currentTreeNode.father);
							// D→B C U : C.type = B.type
							if (((Nonterminal) (currentTreeNode.elem.token)).index == nonterminalIndex
									.get("C"))
							{
								currentTreeNode.elem.type = semanTree.nodes
										.get(fatherTreeNode.firstChild).elem.type;
							}
							// C→id C1 或 C1→, id C1 : C1.type = C/C1.type
							else if (((Nonterminal) (currentTreeNode.elem.token)).index == nonterminalIndex
									.get("C1"))
							{
								currentTreeNode.elem.type = semanTree.nodes
										.get(fatherTreeNode.index).elem.type;
							}
							else if (((Nonterminal) (currentTreeNode.elem.token)).index == nonterminalIndex
									.get("T1"))
							{
								// T→F T1 : T1.inh = F.val
								if (((Nonterminal) (fatherTreeNode.elem.token)).index == nonterminalIndex
										.get("T"))
								{
									currentTreeNode.elem.inh = semanTree.nodes
											.get(fatherTreeNode.firstChild).elem.val;
								}
							}
						}
						// 推出 null 时
						else
						{
							// null 出栈 语义动作
							TreeNode currentTreeNode = semanTree.nodes
									.get(currentNode);
							TreeNode fatherTreeNode = semanTree.nodes
									.get(currentTreeNode.father);

							// E1→null
							if (((Nonterminal) (currentTreeNode.elem.token)).index == nonterminalIndex
									.get("E1"))
							{
								int k = stack.size() - 1;
								for (; isTerminalObject(stack.get(k)); k--)
									;
								TreeNode topTreeNode = semanTree.nodes
										.get(semanStack.get(k));// 向上直到能推出null的节点

								// E1→+ T E1, E1→null
								if (((Nonterminal) (fatherTreeNode.elem.token)).index == nonterminalIndex
										.get("E1"))
								{ // (+, id, id, T1)
									currentTreeNode.elem.val = semanTree.nodes
											.get(fatherTreeNode.firstChild).elem.val; // '+'
																						// 生成的临时变量
									IdentifierBean id1 = lexicalAnalysis.identifierList
											.get(fatherTreeNode.elem.inh);
									IdentifierBean id2 = lexicalAnalysis.identifierList
											.get(currentTreeNode.elem.inh);
									IdentifierBean id3 = lexicalAnalysis.identifierList
											.get(currentTreeNode.elem.val);
									quaternions.add("(+, " + id1.getName()
											+ ", " + id2.getName() + ", "
											+ id3.getName() + ")"); // 生成(+, id,
																	// id, T1)

									while (fatherTreeNode.nextSibling != topTreeNode.index)
									{
										fatherTreeNode.elem.val = currentTreeNode.elem.val;
										currentTreeNode = fatherTreeNode;
										fatherTreeNode = semanTree.nodes
												.get(currentTreeNode.father);
									}
									fatherTreeNode.elem.val = currentTreeNode.elem.val;
									topTreeNode.elem.inh = fatherTreeNode.elem.val;
								}
								// E→T E1, E1→null : E1.inh = T.val , 向上继承
								else if (((Nonterminal) (fatherTreeNode.elem.token)).index == nonterminalIndex
										.get("E"))
								{
									currentTreeNode.elem.val = currentTreeNode.elem.inh;
									while (fatherTreeNode.nextSibling != topTreeNode.index)
									{
										fatherTreeNode.elem.val = currentTreeNode.elem.val;
										currentTreeNode = fatherTreeNode;
										fatherTreeNode = semanTree.nodes
												.get(fatherTreeNode.father);
									}
									fatherTreeNode.elem.val = currentTreeNode.elem.val;
									topTreeNode.elem.inh = fatherTreeNode.elem.val;
								}
							}
							// T1→null
							else if (((Nonterminal) (currentTreeNode.elem.token)).index == nonterminalIndex
									.get("T1"))
							{
								int k = stack.size() - 1;
								for (; isTerminalObject(stack.get(k)); k--)
									;
								TreeNode topTreeNode = semanTree.nodes
										.get(semanStack.get(k));// 向上直到能推出null的节点

								// T→F T1, T1→* F T1
								if (((Nonterminal) (fatherTreeNode.elem.token)).index == nonterminalIndex
										.get("T1"))
								{ // (*, id, id, T1)
									currentTreeNode.elem.val = semanTree.nodes
											.get(fatherTreeNode.firstChild).elem.val; // '*'
																						// 生成的临时变量
									TreeNode fathersFirstChild = semanTree.nodes
											.get(fatherTreeNode.firstChild);
									currentTreeNode.elem.inh = semanTree.nodes
											.get(fathersFirstChild.nextSibling).elem.val;
									IdentifierBean id1 = lexicalAnalysis.identifierList
											.get(fatherTreeNode.elem.inh);
									IdentifierBean id2 = lexicalAnalysis.identifierList
											.get(currentTreeNode.elem.inh);
									IdentifierBean id3 = lexicalAnalysis.identifierList
											.get(currentTreeNode.elem.val);
									quaternions.add("(*, " + id1.getName()
											+ ", " + id2.getName() + ", "
											+ id3.getName() + ")"); // 生成(*, id,
																	// id, T1)
									while (fatherTreeNode.nextSibling != topTreeNode.index)
									{
										fatherTreeNode.elem.val = currentTreeNode.elem.val;
										currentTreeNode = fatherTreeNode;
										fatherTreeNode = semanTree.nodes
												.get(currentTreeNode.father);
									}
									fatherTreeNode.elem.val = currentTreeNode.elem.val;
									topTreeNode.elem.inh = fatherTreeNode.elem.val;
								}
								// T→F T1, E1→null : T1.inh = F.val, T1.val =
								// T1.inh , 向上继承
								else if (((Nonterminal) (fatherTreeNode.elem.token)).index == nonterminalIndex
										.get("T"))
								{
									currentTreeNode.elem.inh = semanTree.nodes
											.get(fatherTreeNode.firstChild).elem.val;
									currentTreeNode.elem.val = currentTreeNode.elem.inh;
									while (fatherTreeNode.nextSibling != topTreeNode.index)
									{
										fatherTreeNode.elem.val = currentTreeNode.elem.val;
										currentTreeNode = fatherTreeNode;
										fatherTreeNode = semanTree.nodes
												.get(fatherTreeNode.father);
									}
									fatherTreeNode.elem.val = currentTreeNode.elem.val;
									topTreeNode.elem.inh = fatherTreeNode.elem.val;
								}
							}
							// A→id = E U, U→null
							else if (((Nonterminal) (currentTreeNode.elem.token)).index == nonterminalIndex
									.get("U"))
							{
								if (((Nonterminal) (fatherTreeNode.elem.token)).index == nonterminalIndex
										.get("A"))
								{
									// (=, id, _, T1)
									IdentifierBean id1 = lexicalAnalysis.identifierList
											.get(fatherTreeNode.elem.inh);
									IdentifierBean id3 = lexicalAnalysis.identifierList
											.get(currentTreeNode.elem.inh);
									quaternions.add("(=, " + id1.getName()
											+ ", _, " + id3.getName() + ")");
								}
							}
						}
					}
				}
			}
		}
		return (lexicalAnalysis.errorMessageBeans.size() == 0);
	}

	public void setLL1TableRow(int row, HashSet<Integer> set, int value)
	{
		Iterator<Integer> iterator = set.iterator();
		while (iterator.hasNext())
		{
			LL1Table[row][iterator.next()] = value;
		}
	}

	/**
	 * 返回hashSets中所有元素的个数
	 * 
	 * @param hashSets
	 * @return
	 */
	public int getNumOfAllSetElements(HashSet<Integer>[] hashSets)
	{
		int num = 0;
		for (int i = 0; i < hashSets.length; i++)
		{
			num += hashSets[i].size();
		}
		return num;
	}

	/**
	 * 把一个first集中的元素，全部添加到另一个first集中
	 * 
	 * @param fromSet
	 * @param toSet
	 */
	public void addOneSetToAnother(HashSet<Integer> fromSet,
			HashSet<Integer> toSet)
	{
		Iterator<Integer> iterable = fromSet.iterator();
		while (iterable.hasNext())
		{
			toSet.add(iterable.next());
		}
	}

	/**
	 * 把一个含空first集中的非空元素，全部添加到另一个first集中
	 * 
	 * @param fromSet
	 * @param toSet
	 */
	public void addNotContainsNullFirstSetToAnother(HashSet<Integer> fromSet,
			HashSet<Integer> toSet, int nullIndexInTerminals)
	{
		Iterator<Integer> iterable = fromSet.iterator();
		while (iterable.hasNext())
		{
			int terminalIndex = iterable.next();
			if (terminalIndex != nullIndexInTerminals)
			{
				toSet.add(terminalIndex);
			}
		}
	}

	/**
	 * 判断对象是否为 Terminal 对象
	 * 
	 * @param testObject
	 * @return
	 */
	public static boolean isTerminalObject(Object testObject)
	{
		return testObject instanceof Terminal;
	}

	/**
	 * 添加错误到错误列表 lexicalAnalysis.errorMessageBeans
	 * 
	 * @param errorCode
	 * @param errorMessageBeans
	 */
	public void addGrammarParserError(String errorCode, String errorWord,
			ArrayList<ErrorMessageBean> errorMessageBeans)
	{
		ErrorMessageBean e = new ErrorMessageBean(errorCode, errorWord, -1);
		errorMessageBeans.add(e);
	}

	/**
	 * 返回 形如(ID,2) 的数字值(右部)
	 * 
	 * @param dual
	 * @return 返回数字值， 例如 (ID,2) 返回2
	 */
	public int getDualRight(String dual)
	{
		int last = dual.lastIndexOf(",");
		return Integer.parseInt(dual.substring(last + 1,
				dual.indexOf(")", last)));
	}

	/**
	 * 返回 形如(ID,2) 的左部值
	 * 
	 * @param dual
	 * @return 返回左部值， 例如 (ID,2) 返回 "ID"
	 */
	public String getDualLeft(String dual)
	{
		return dual.substring(1, dual.lastIndexOf(","));
	}

	/*
	 * public boolean isNodeEqual(Object node1) { if (isTerminalObject(node)) {
	 * 
	 * } }
	 */
	
	// 返回指定的First集
	public String getOneFirstSet(int head) 
	{
		String out = "";
		out += "First(" + Nonterminal.get(head) + ") = ";
		out += "{ ";
		Iterator<Integer> iterator = firstSets[head].iterator();
		while (iterator.hasNext())
		{
			out += Terminal.get(iterator.next()) + " ";
		}
		out += "}";
		return out;
	}
	
	// 返回指定的Follow集
	public String getOneFollowSet(int head) 
	{
		String out = "";
		out += "Follow(" + Nonterminal.get(head) + ") = ";
		out += "{ ";
		Iterator<Integer> iterator = followSets[head].iterator();
		while (iterator.hasNext())
		{
			out += Terminal.get(iterator.next()) + " ";
		}
		out += "}";
		return out;
	}
	
	// 获得表达式的字符串显示形式 expression
	public String getExpression(int startIndex, Integer[] expression) 
	{
		String out = "";
		for (int i = startIndex; i < expression.length; i++) 
		{
			out += Terminal.get(expression[i]) + " ";
		}		
		return out;
	}
	
	// 获得符号栈中的字符串显示形式
	public String getSymbolStack(Stack<Object> stack)
	{
		String out = "# ";
		for (int i = 0; i < stack.size(); i++) 
		{
			if (isTerminalObject(stack.get(i))) 
			{
				Terminal terminal = (Terminal)stack.get(i);
				out += Terminal.get(terminal.index) + " ";
			}
			else
			{
				Nonterminal nonterminal = (Nonterminal)stack.get(i);
				out += Nonterminal.get(nonterminal.index) + " ";
			}
		}
		return out;
	}
	
	// 清除语法分析结果
	public void clearSyntaxAnalysisResult() 
	{
		this.LL1ParseLog.clear();
		this.expressions.clear();
		this.quaternions.clear();
		this.expressions.clear();
		this.expsIdIndex.clear();
	}
}
