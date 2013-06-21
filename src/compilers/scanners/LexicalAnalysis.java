/**
 * @author Randy
 * @date 2013-05-25
 * @function Lexical Analysis(词法分析)
 */
package compilers.scanners;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compilers.javabean.ErrorMessageBean;
import compilers.javabean.IdentifierBean;
import compilers.tool.HandleXMLFile;

public class LexicalAnalysis
{
	String fileName = null;

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	String rowStr = "";
	public ArrayList<String> numberList = new ArrayList<String>();
	public ArrayList<String> stringList = new ArrayList<String>();
	public ArrayList<Character> charList = new ArrayList<Character>();
	public ArrayList<IdentifierBean> identifierList = new ArrayList<IdentifierBean>();
	public ArrayList<ErrorMessageBean> errorMessageBeans = new ArrayList<ErrorMessageBean>();
	boolean foundMultiComments = false; // 多行注释

	// 只存放标识符，用于检索标识符是否已存在，用于提高效率，但占用了一定的内存空间（以空间换时间）
	public HashMap<String, Integer> identifierHashMap = new HashMap<String, Integer>();
	public HashMap<String, Integer> keywordsHashMap = new HashMap<String, Integer>();
	public String dualExpression = "";

	// 为生成新的临时变量的索引(_Tn)
	int newTempIndex = 0;

	public String getDualExpression()
	{
		return dualExpression;
	}

	// 记录当前行数
	int countRows = 0;

	// 文件流
	FileReader fr = null;
	BufferedReader br = null;

	// 正则表达式 匹配
	Pattern pattern = null;
	Matcher matcher = null;

	// 正则表达式
	static String regexFirstWord = "[a-zA-Z\u4e00-\u9fa5_]"; //包括汉字
	static String regexRestWord = "[a-zA-Z0-9\u4e00-\u9fa5_]*";
	static String regexOperator = "[+\\-*/%=<>!！^&|.,，:：;；()（）\\[\\]【】{}]";
	static String regexContainIllegalWord = "[^+\\-*/%=<>!！^&|.,，:：;；()（）\\[\\]【】{} \t]*";
	static String regexNumber = "[0-9]";
	static String regexRestNumber = "[0-9]*(\\.[0-9]+)|([0-9]*)";
	static String regexContainIllegalNumber = "[\\w\u4e00-\u9fa5\\.]*";
	static String regexWord = "[a-zA-Z\u4e00-\u9fa5]";
	static String regexSpecialChar = "(([bfnrt\'\"\\\\])|(u[0-9A-Fa-f]{4}))['’‘]";
	static String regexNormalChar = ".['’‘]";
	static String regexNormalString = "(.*[^\\\\][\"“”])|([\"“”])";
	static String regexSpecialString = "[\\\\](([bfnrt\'\"\\\\])|(u[0-9A-Fa-f]{4}))";
	static String regexContainIllegalString = "([\\\\](([^bfnrt\'\"\\\\])|(^(u([0-9A-Fa-f]{4})))))+[\"]";

	/**
	 * Constructor
	 */
	public LexicalAnalysis(String fileName)
	{
		this.fileName = fileName;
		keywordsHashMap = new HandleXMLFile(new File("data/Keywords.xml"))
				.getKeywordsHashMap();
	}

	// 扫描每一行(every string line)
	public void scanRows()
	{
		try
		{
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);

			while ((rowStr = br.readLine()) != null)
			{
				countRows++;
				// 处理每一行的字符串
				this.handleRow();
				// TODO ????
			}
		}
		catch (Exception e2)
		{
			e2.printStackTrace();
		}
		finally
		{
			try
			{
				if (br != null)
				{
					fr.close();
				}
				if (br != null)
				{
					br.close();
				}
			}
			catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
	}

	// 处理当前行的字符串（单个字符扫描）
	public void handleRow()
	{
		char singleChar = ' ';

		for (int i = 0; i < rowStr.length(); i++)
		{
			singleChar = rowStr.charAt(i);
			// 判断如果是 空格 或 制表符
			if (' ' == singleChar || '\t' == singleChar)
			{
				continue;
			}
			// 如果是 英文字母 || 汉字字符 || '_'
			else if (Pattern.matches(regexFirstWord, String.valueOf(singleChar)))
			{
				i = this.handleFirstWord(i, singleChar);
			}
			// 如果是 运算符（包括，；）
			else if (Pattern.matches(regexOperator, String.valueOf(singleChar)))
			{
				i = this.handleOperator(i, singleChar);
			}
			// 如果是 数字常量
			else if (Pattern.matches(regexNumber, String.valueOf(singleChar)))
			{
				// 可能为 数字
				i = this.handleNumber(i, singleChar);
			}
			// 如果是 字符常量
			else if ('\'' == singleChar || '‘' == singleChar
					|| '’' == singleChar)
			{
				i = this.handleChar(i, singleChar);
			}
			// 如果是 字符串常量
			else if ('"' == singleChar || '“' == singleChar)
			{
				i = this.handleString(i, singleChar);
			}
			// 非法字符
			else
			{
				// 报错
				errorMessageBeans.add(new ErrorMessageBean("A0001", singleChar
						+ "", countRows));
			}
		}// for loop end
	}

	// 处理首字母，可能是标识符或关键字的情况
	public int handleFirstWord(int i, char singleChar)
	{
		String singleWord = null;
		// 可能为 标识符 或 关键字
		pattern = Pattern.compile(regexRestWord);
		matcher = pattern.matcher(rowStr);
		matcher.find(i + 1);
		// set i
		int mayCorrect_i = matcher.end() - 1;
		singleWord = String.valueOf(singleChar) + matcher.group();
		// 需要判断为什么匹配结束的，是否为非法字符

		pattern = Pattern.compile(regexContainIllegalWord);
		matcher = pattern.matcher(rowStr);
		matcher.find(i + 1);
		int mayWrong_i = matcher.end() - 1;
		if (mayWrong_i != mayCorrect_i)
		{
			// 报错，存在非法字符
			i = mayWrong_i;
			String errorStr = String.valueOf(singleChar) + matcher.group();
			errorMessageBeans.add(new ErrorMessageBean("A0001", errorStr,
					countRows));
		}
		else
		{
			// 正确匹配，处理，判断 关键字，还是标识符
			i = mayCorrect_i;
			this.handleSingleWord(singleWord);
		}
		return i;
	}

	// 处理单个单词（标识符 或 关键字）
	public void handleSingleWord(String singleWord)
	{
		// 首先查找是否未关键字，否则插入标识符表
		// 判断是否为关键字
		if (keywordsHashMap.containsKey(singleWord))
		{
			dualExpression += "(" + singleWord + ",_) ";
		}
		// 否则为标识符（插入到标识符表）
		else
		{
			int index = isExistidentifier(singleWord);
			// 标识符不存在，插入标识符表
			if (-1 == index)
			{
				identifierList.add(new IdentifierBean(singleWord, null, 0));
				index = identifierList.size() - 1;
				identifierHashMap.put(singleWord, index);
			}
			// 添加二元流
			dualExpression += "(ID," + index + ") ";
		}
	}

	// 处理首字母为数字的情况
	public int handleNumber(int i, char singleChar)
	{
		String singleNumber = null;
		// 可能为 标识符 或 关键字
		pattern = Pattern.compile(regexRestNumber);
		matcher = pattern.matcher(rowStr);
		matcher.find(i + 1);
		// set i
		int mayCorrect_i = matcher.end() - 1;
		singleNumber = String.valueOf(singleChar) + matcher.group();

		// 需要判断为什么匹配结束的，是否为非法字符
		pattern = Pattern.compile(regexContainIllegalNumber);
		matcher = pattern.matcher(rowStr);
		matcher.find(i + 1);
		int mayWrong_i = matcher.end() - 1;
		// 数字有误，报错
		if (mayWrong_i != mayCorrect_i)
		{
			i = mayWrong_i;
			String errorStr = String.valueOf(singleChar) + matcher.group();
			// 判断错误类型
			if (errorStr.contains("."))
			{
				// 数据错误 // 但感觉有一些问题， eg: 6a.name, 算什么？也可以，反正是错的
				errorMessageBeans.add(new ErrorMessageBean("A0002", errorStr,
						countRows));
			}
			else
			{
				// 标识符不能以数字开头
				errorMessageBeans.add(new ErrorMessageBean("A0005", errorStr,
						countRows));
			}
		}
		// 正确匹配，把常数加入到数字表中
		// 并加入到二元流中
		else
		{
			i = mayCorrect_i;
			int index = isExistString(numberList, singleNumber);
			// 数字常数不存在，插入数字表
			if (-1 == index)
			{
				numberList.add(singleNumber);
				index = numberList.size() - 1;
			}
			// 添加二元流
			dualExpression += "(NUM," + index + ") ";
		}
		return i;
	}

	// 处理首字母为 ‘ || ' 的情况(字符串常量)
	public int handleChar(int i, char singleChar)
	{
		// 如果为特殊字符
		if (isNextCharExistAndEqual(i, '\\'))
		{
			boolean isError = false;
			char matchChar = ' ';
			pattern = Pattern.compile(regexSpecialChar);
			matcher = pattern.matcher(rowStr);
			if (rowStr.substring(i + 2, i + 4).equals("\'\'"))
			{
				i = i + 3;
				matchChar = '\'';
			}
			// 字符有误
			else if (rowStr.substring(i + 2, i + 4).equals("\\"))
			{
				isError = true;
				String errorStr = rowStr.substring(i);
				i = rowStr.length() - 1;
				errorMessageBeans.add(new ErrorMessageBean("A0006", errorStr,
						countRows));
			}
			else if (rowStr.substring(i + 2, i + 4).equals("\\\'"))
			{
				i = i + 3;
				matchChar = '\\';
			}
			else if (matcher.find(i + 1))
			{
				String temp = matcher.group();
				matchChar = handleSpecialChar(temp.substring(0,
						temp.length() - 1));
				i = matcher.end() - 1;
			}
			else
			{
				// 字符有误
				isError = true;
				String errorStr = rowStr.substring(i);
				i = rowStr.length() - 1;
				errorMessageBeans.add(new ErrorMessageBean("A0006", errorStr,
						countRows));
			}
			if (!isError)
			{
				int index = isExistChar(matchChar);
				// 字符常数不存在，插入字符表
				if (-1 == index)
				{
					charList.add(matchChar);
					index = charList.size();
				}
				// 添加二元流
				dualExpression += "(CHAR," + index + ") ";
			}
		}
		// 如果为普通字符
		else
		{
			char matchChar = ' ';
			pattern = Pattern.compile(regexNormalChar);
			matcher = pattern.matcher(rowStr);
			if (matcher.find(i + 1))
			{
				matchChar = rowStr.charAt(i + 1);
				i = matcher.end() - 1;
				int index = isExistChar(matchChar);
				// 字符常数不存在，插入字符表
				if (-1 == index)
				{
					charList.add(matchChar);
					index = charList.size() - 1;
				}
				// 添加二元流
				dualExpression += "(CHAR," + index + ") ";
			}
			else
			{
				// 字符有误
				String errorStr = rowStr.substring(i);
				i = rowStr.length() - 1;
				errorMessageBeans.add(new ErrorMessageBean("A0007", errorStr,
						countRows));
			}
		}
		return i;
	}

	// 处理特殊字符 ([b,f,n,r,t,',\",\\\\])
	public char handleSpecialChar(String specialStr)
	{
		switch (specialStr.charAt(0))
		{
		case 'b':
			return '\b';
		case 'f':
			return '\f';
		case 'n':
			return '\n';
		case 'r':
			return '\r';
		case 't':
			return '\t';
		case '\'':
			return '\'';
		case '\"':
			return '\"';
		case '\\':
			return '\\';
		default:
			int t = Integer.valueOf(specialStr.substring(1), 16);
			return (char) t;
		}
	}

	// 转换包含特殊字符的字符串常量
	public String convertSpecialString(String cs)
	{
		String convertedStr = cs;
		String temp = null;
		pattern = Pattern.compile(regexSpecialString);
		matcher = pattern.matcher(cs);
		while (matcher.find())
		{
			temp = matcher.group();
			convertedStr = convertedStr.replace(temp,
					handleSpecialChar(temp.substring(1)) + "");
		}
		return convertedStr;
	}

	// 处理首字母为 " || “ 的情况(字符串常量)
	public int handleString(int i, char singleChar)
	{
		pattern = Pattern.compile(regexNormalString);
		matcher = pattern.matcher(rowStr);
		// 匹配到了 右引号
		if (matcher.find(i + 1))
		{
			boolean isError = false;
			String correctString = "";

			int mayCorrect_i = matcher.end() - 1;
			String mayCorrectString = matcher.group().substring(0,
					matcher.group().length() - 1);

			// 虽然找到字符串结束符，但仍然需要判断反斜杠(\)的问题
			pattern = Pattern.compile("\\\\");
			matcher = pattern.matcher(mayCorrectString);
			if (matcher.find(0))
			{
				// 是否匹配特殊字符
				pattern = Pattern.compile(regexSpecialString);
				matcher = pattern.matcher(mayCorrectString);
				if (matcher.find(0))
				{
					correctString = convertSpecialString(mayCorrectString);
				}
				else
				{
					// 报错，错误特殊字符
					isError = true;
					String errorStr = rowStr.substring(i);
					i = rowStr.length() - 1;
					errorMessageBeans.add(new ErrorMessageBean("A0008",
							errorStr, countRows));
				}
			}
			// 如果是空串，形如 String s = ""; 返回一个空串
			else if (mayCorrectString.equals("\""))
			{
				correctString = "";
			}
			// 没有找到反斜杠，正常字符串
			else
			{
				correctString = mayCorrectString;
			}
			// 如果没有出现任何错误，正常操作
			if (!isError)
			{
				i = mayCorrect_i;
				int index = isExistString(stringList, correctString);
				// 字符常数不存在，插入字符表
				if (-1 == index)
				{
					stringList.add(correctString);
					index = stringList.size() - 1;
				}
				// 添加二元流
				dualExpression += "(STRING," + index + ") ";
			}
		}
		// 报错，缺少右引号与之匹配
		else
		{
			String errorStr = rowStr.substring(i);
			i = rowStr.length() - 1;
			errorMessageBeans.add(new ErrorMessageBean("A0009", errorStr,
					countRows));
		}
		return i;
	}

	/**
	 * 判断标识符是否出现
	 * 
	 * @return -1（没有），index（存在，在标识符表中的索引[从0开始]）
	 */
	public int isExistidentifier(String singleWord)
	{
		if (this.identifierHashMap.containsKey(singleWord))
		{
			return this.identifierHashMap.get(singleWord);
		}
		return -1;
	}

	/**
	 * 判断数字常数，字符串常量 是否出现过
	 * 
	 * @return -1（没有），index（存在，在arraylist中的 [索引]）
	 */
	public int isExistString(ArrayList<String> stringList, String singleString)
	{
		for (int i = 0; i < stringList.size(); i++)
		{
			if (stringList.get(i).equals(singleString))
			{
				return i;
			}
		}
		return -1;
	}

	/**
	 * backslash 判断字符常数是否出现过
	 * 
	 * @return -1（没有），index（存在，在arraylist中的 [索引 + 1]）
	 */
	public int isExistChar(char singleChar)
	{
		for (int i = 0; i < charList.size(); i++)
		{
			if (charList.get(i) == singleChar)
			{
				return i;
			}
		}
		return -1;
	}

	// 处理操作符
	public int handleOperator(int i, char singleChar)
	{
		switch (singleChar)
		{
		case '+':
		case '-':
			if (isNextCharExistAndEqual(i, singleChar))
			{
				dualExpression += "(" + singleChar + singleChar + ",_) ";
				i++;
			}
			else if (isNextCharExistAndEqual(i, '='))
			{
				dualExpression += "(" + singleChar + "=,_) ";
				i++;
			}
			else
			{
				dualExpression += "(" + singleChar + ",_) ";
			}
			break;
		case '%':
			if (isNextCharExistAndEqual(i, '='))
			{
				dualExpression += "(" + singleChar + "=,_) ";
				i++;
			}
			else
			{
				dualExpression += "(" + singleChar + ",_) ";
			}
			break;
		// 单独处理，注释问题
		case '*':
			if (isNextCharExistAndEqual(i, '='))
			{
				dualExpression += "(" + singleChar + "=,_) ";
				i++;
			}
			// 如果只有 */ ，报错
			else if (isNextCharExistAndEqual(i, '/'))
			{
				errorMessageBeans.add(new ErrorMessageBean("A0004",
						"comments error", countRows));
				i++;
			}
			else
			{
				dualExpression += "(" + singleChar + ",_) ";
			}
			break;
		// 单独处理，注释问题
		case '/':
			if (isNextCharExistAndEqual(i, '='))
			{
				dualExpression += "(/=,_) ";
				i++;
			}
			// 若为 // ，后面全部忽略
			else if (isNextCharExistAndEqual(i, '/'))
			{
				i = rowStr.length() - 1;
			}
			// 若为 /* ，处理跨行注释
			else if (isNextCharExistAndEqual(i, '*'))
			{
				foundMultiComments = true;

				pattern = Pattern.compile("[*][/]");
				matcher = pattern.matcher(rowStr);
				// 在本行找到le */
				if (i + 2 < rowStr.length() && matcher.find(i + 2))
				{
					foundMultiComments = false;
					i = matcher.end() - 1;
				}
				// 在本行没有找到 */
				else
				{
					try
					{
						while ((rowStr = br.readLine()) != null)
						{
							countRows++;
							// 处理当前行的字符串 判断是否为 */
							matcher = pattern.matcher(rowStr);
							// 在本行找到le */
							if (matcher.find(0))
							{
								foundMultiComments = false;
								i = matcher.end() - 1;// 因为外面的循环还要加一
								break;
							}
							// 在本行没有找到 "*/", then continue;
						}
						if (rowStr == null)
						{
							rowStr = "";
							i = -1;
						}
						if (foundMultiComments)
						{
							errorMessageBeans.add(new ErrorMessageBean("A0003",
									"comments error", countRows));
						}
					}
					catch (IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			else
			{
				dualExpression += "(/,_) ";
			}
			break;
		case '=':
			if (isNextCharExistAndEqual(i, '='))
			{
				dualExpression += "(relop," + singleChar + "=) ";
				i++;
			}
			else
			{
				dualExpression += "(=,_) ";
			}
			break;
		case '<':
		case '>':
		case '!':
		case '！':
			if (isNextCharExistAndEqual(i, '='))
			{
				dualExpression += "(relop," + singleChar + "=) ";
				i++;
			}
			else
			{
				dualExpression += "(relop," + singleChar + ") ";
			}
			break;
		case '^':
			dualExpression += "(relop,_) ";
			break;
		case '&':
		case '|':
			if (isNextCharExistAndEqual(i, singleChar))
			{
				dualExpression += "(relop," + singleChar + singleChar + ") ";
				i++;
			}
			else
			{
				dualExpression += "(relop," + singleChar + ") ";
			}
			break;
		// case '}':case '{':case '】':case '【':case ']':case '[':case ',':case
		// '，':case ';':case '；':case '(':case ')':case '（':case '）':
		default:
			dualExpression += "(" + singleChar + "," + "_) ";
			break;
		}
		return i;
	}

	// 判断下一个字符是否存在，且等于要匹配的字符
	public boolean isNextCharExistAndEqual(int i, char operator)
	{
		if (i + 1 < rowStr.length() && rowStr.charAt(i + 1) == operator)
		{
			return true;
		}
		return false;
	}

	// 处理二元流信息
	public void handleDualExpression()
	{
		// 暂时这样处理
		System.out.println(dualExpression);
	}

	// 处理错误信息
	public String handleErrorMessage()
	{
		// 如果没有错误，显示 词法分析 成功
		String showLexicalMsg = "";
		if (errorMessageBeans.size() != 0)
		{
			HandleXMLFile handleXMLFile = new HandleXMLFile(new File(
					"data/ErrorMessage.xml"));
			String errorCode = null;
			for (int i = 0; i < errorMessageBeans.size(); i++)
			{
				ErrorMessageBean er = errorMessageBeans.get(i);
				errorCode = er.getErrorCode();
				showLexicalMsg += "> Line(" + er.getErrorLine() + "): error "
						+ errorCode + ": [" + er.getErrorWord() + "]: "
						+ handleXMLFile.getErrorMsg(errorCode) + "\n";
			}
		}
		return showLexicalMsg;
	}

	/**
	 * 清除词法分析够的一些存放结果的变量值，以便进行下一次词法分析
	 * 包括：numberList，stringList，charList，identifierList，
	 * ....identifierHashMap，errorMessageBeans，dualExpression
	 * 不包括：关键字表（keywordsHashMap）
	 */
	public void clearLexicalAnalysisResult()
	{
		numberList.clear();
		stringList.clear();
		charList.clear();
		identifierList.clear();
		identifierHashMap.clear();
		errorMessageBeans.clear();
		dualExpression = "";
		newTempIndex = 0;
	}

	public int newTempInIdentifierList()
	{
		String newTemp = "_T" + (++newTempIndex);
		identifierList.add(new IdentifierBean(newTemp, null, 0));
		return (identifierList.size() - 1);
	}
}
