/**
 * @author Randy 
 * @date 2013-05-25
 * @function 对各个窗体元素的操作
 */
package compilers.main_ui;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import compilers.javabean.IdentifierBean;
import compilers.javabean.LL1ProcessLog;
import compilers.javabean.Nonterminal;
import compilers.javabean.Production;
import compilers.javabean.Terminal;
import compilers.parsers.GrammarParserByLL1;
import compilers.scanners.LexicalAnalysis;
import compilers.tool.HandleXMLFile;

public class Operaor
{
	// define variable
	MainUI mainUI = null;
	MyText myText = null;
	LexicalAnalysis lexicalAnalysis = null;
	boolean isInitSeman = false; // 是否在语义分析时把文法等解析好
	boolean isSemanticAnalysis = false; // 是否进行了语义分析
	GrammarParserByLL1 grammarParserByLL1 = null;
	String grammarFilePath = "data/gra.grammar";

	/**
	 * constructor
	 */
	public Operaor(MyText myText, MainUI mainUI)
	{
		this.myText = myText;
		this.mainUI = mainUI;
	}

	public void saveFile(String filePath, String text)
	{
		FileWriter fw = null;
		BufferedWriter bw = null;
		try
		{
			fw = new FileWriter(new File(filePath));
			bw = new BufferedWriter(fw);

			String[] ws = text.split("\n");
			for (int i = 0; i < ws.length - 1; i++)
			{
				bw.write(ws[i] + "\n");
			}
			bw.write(ws[ws.length - 1]);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		finally
		{	// TODO 对异常的处理
			try
			{
				if (bw != null)
				{
					bw.close();
				}
				if (fw != null)
				{
					fw.close();
				}
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	// save the file when know the file path
	public void save(String filePath)
	{
		this.saveFile(filePath, myText.getText());
		mainUI.setTitle(filePath + " - Ran Compiler");
		myText.isChanged = false;
	}

	// 执行接收“ctrl-save”后的总操作
	public void ctrlSave()
	{
		if (myText.fileName == null)
		{
			this.saveAs();
		}
		else
		{
			this.save(myText.fileName);
			myText.requestFocus(true);
		}
	}

	// save as the contents to another file
	public boolean saveAs()
	{
		JFileChooser jfc = new JFileChooser("userdata");
		jfc.setDialogTitle("Save as ...");

		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"Ran Language File", "ran");
		jfc.setFileFilter(filter);

		int result = jfc.showSaveDialog(null);
		jfc.setVisible(true);
		
		// 判断对话框返回类型
		if(result != JFileChooser.APPROVE_OPTION)
		{
			return false;
		}

		File saveFile = jfc.getSelectedFile();
		if (saveFile != null)
		{
			myText.fileName = saveFile.getAbsolutePath();
			// 判断文件类型（默认为 *.ran）
			int index = myText.fileName.lastIndexOf(".");
			if (-1 == index)
			{
				myText.fileName += ".ran";
			}
			this.save(myText.fileName);
		}
		return true;
	}

	// judge if save the contents
	public int judgeSaveAs()
	{
		int op = 0;
		op = JOptionPane.showInternalConfirmDialog(mainUI.myMenuBar,
				"Do you want to save after changing ?", "information",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.INFORMATION_MESSAGE);
		switch (op)
		{
		case JOptionPane.YES_OPTION:
			if (myText.fileName == null)
			{
				this.saveAs();
			}
			else
			{
				this.save(myText.fileName);
			}
			break;

		case JOptionPane.NO_OPTION:
			break;
		case JOptionPane.CANCEL_OPTION:
			break;
		}
		return op;

	}

	// open a file
	public boolean openFile()
	{
		JFileChooser jfcOpen = new JFileChooser("userdata");
		jfcOpen.setDialogTitle("Please select the file ...");
		int result = jfcOpen.showOpenDialog(null);
		jfcOpen.setVisible(true);

		if(result != JFileChooser.APPROVE_OPTION)
		{
			return false;
		}
		
		File selectedFile = jfcOpen.getSelectedFile();

		if (selectedFile != null)
		{
			myText.fileName = selectedFile.getAbsolutePath();

			FileReader fr = null;
			BufferedReader br = null;

			try
			{
				myText.setText("");//初始化清空

				fr = new FileReader(myText.fileName);
				br = new BufferedReader(fr);

				String string = "";

				while ((string = br.readLine()) != null)
				{
					myText.append(string + "\n");
				}
				String jtaString = myText.getText();
				if (jtaString.length() - 1 >= 0)
				{
					myText.setText(jtaString.substring(0,
							jtaString.length() - 1));
				}
				myText.isChanged = false;
			}
			catch (Exception e2)
			{
				e2.printStackTrace();
			}
			finally
			{
				try
				{
					fr.close();
					br.close();
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
		}
		return true;
	}

	// quit the compiler
	public void quit()
	{
		if (myText.isChanged == true)
		{
			int op = this.judgeSaveAs();
			if (op == JOptionPane.CANCEL_OPTION)
			{
				myText.requestFocus(true);
			}
			else
			{
				System.exit(0);
			}
		}
		else
		{
			System.exit(0);
		}
	}

	// 响应各种事件的操作函数（通过 action command）
	public void operateAction(String cmd)
	{
		// create a new notepad
		if (cmd.equals("ctrl-new"))
		{
			int op = 0;
			if (myText.isChanged == true)
			{
				op = this.judgeSaveAs();
			}
			if (op != JOptionPane.CANCEL_OPTION)
			{
				myText.setText("");
				myText.isChanged = false;
				myText.requestFocus(true);
				mainUI.setTitle("null - Ran Compiler");
			}
		}

		// open a file
		else if (cmd.equals("ctrl-open"))
		{
			int op = 0;
			if (myText.isChanged == true)
			{
				op = this.judgeSaveAs();
			}
			if (op != JOptionPane.CANCEL_OPTION)
			{
				if(! this.openFile())
					return;
				myText.requestFocus(true);
			}
			mainUI.setTitle(myText.fileName + " - Ran Compiler");
		}
		
		// 选择用户自定义的语法文件
		else if (cmd.equals("ctrl-select_grammar_file"))
		{
			JFileChooser jfcSelect = new JFileChooser("userdata");
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
			        "Grammar File", "grammar");
			jfcSelect.setFileFilter(filter);
			jfcSelect.setDialogTitle("Please select the grammar file ...");
			int result = jfcSelect.showOpenDialog(null);
			jfcSelect.setVisible(true);

			if(result != JFileChooser.APPROVE_OPTION)
			{
				return ;
			}
			
			File selectedFile = jfcSelect.getSelectedFile();

			if (selectedFile != null)
			{
				this.grammarFilePath = selectedFile.getAbsolutePath();
				this.isInitSeman = false;
				this.isSemanticAnalysis = false;
				// 提示：选择了新的自定义语法
				JOptionPane.showMessageDialog(mainUI, "\n您选择了新的自定义语法，可以进行相应的分析了。\n", 
						grammarFilePath, JOptionPane.INFORMATION_MESSAGE);
			}
		}

		// save the contents to a file
		else if (cmd.equals("ctrl-save"))
		{
			this.ctrlSave();
		}

		// save the contents to a another file
		else if (cmd.equals("ctrl-saveAs"))
		{
			if(! this.saveAs())
				return ;
		}

		// quit the notepad
		else if (cmd.equals("ctrl-quit"))
		{
			this.quit();
		}

		// undo
		else if (cmd.equals("ctrl-undo"))
		{
			if (myText.undoManager.canUndo())
			{
				myText.undoManager.undo();
			}
		}
		// redo
		else if (cmd.equals("ctrl-redo"))
		{
			if (myText.undoManager.canRedo())
			{
				myText.undoManager.redo();
			}
		}
		else if (cmd.equals("ctrl-selectAll"))
		{
			myText.selectAll();
		}
		else if (cmd.equals("ctrl-cut"))
		{
			myText.cut();
		}
		else if (cmd.equals("ctrl-copy"))
		{
			myText.copy();
		}
		else if (cmd.equals("ctrl-paste"))
		{
			myText.paste();
		}
		// delete 所选的
		else if (cmd.equals("ctrl-delete"))
		{
			myText.replaceSelection("");
		}
		else if (cmd.equals("ctrl-find"))
		{
			// TODO find
		}
		else if (cmd.equals("ctrl-findNext"))
		{
			// TODO findNext
		}
		else if (cmd.equals("ctrl-replace"))
		{
			// TODO replace
		}
		// auto line wrap
		else if (cmd.equals("ctrl-lineWrap"))
		{
			myText.setLineWrap(!myText.getLineWrap());
			mainUI.myMenuBar.jcbmiLineWrap
					.setSelected(!mainUI.myMenuBar.jcbmiLineWrap.isSelected());
		}
		// set toolbar visible
		else if (cmd.equals("ctrl-toolbar"))
		{
			mainUI.myToolBar.setVisible(mainUI.myMenuBar.jcbmiToolbar
					.isSelected());
		}
		else if (cmd.equals("ctrl-font"))
		{
			// TODO font
		}
		else if (cmd.equals("ctrl-color"))
		{
			// TODO color
		}
		else if (cmd.equals("ctrl-helpContent"))
		{
			// TODO helpContent
		}
		else if (cmd.equals("ctrl-about"))
		{
			JOptionPane.showMessageDialog(mainUI, "\n编译原理教学辅助软件 v4.5\n"
					+ "作者：Randy\n" + "Email：gchinaran@gmail.com\n", 
					"关于软件", JOptionPane.INFORMATION_MESSAGE);
		}
		/*********** 编译命令 词法分析 ***********/
		else if (cmd.equals("ctrl-lexical_analysis"))
		{
			this.ctrlSave();
			// 执行词法分析
			if (myText.fileName != null)
			{
				// 获取开始时间
				long startTime = System.currentTimeMillis();

				lexicalAnalysis = new LexicalAnalysis(myText.fileName);
				// 开始 词法分析
				lexicalAnalysis.scanRows();
				String showErrorMsg = "> 开始词法分析 (" + myText.fileName + ")\n";
				showErrorMsg += lexicalAnalysis.handleErrorMessage();
				if (lexicalAnalysis.errorMessageBeans.size() == 0)
				{
					showErrorMsg += ">\n> 词法分析 成功。\n";
				}
				else
				{
					showErrorMsg += ">\n> 词法分析 失败。\n";
				}
				// 获取结束时间
				long endTime = System.currentTimeMillis();
				showErrorMsg += "> 已用时间: " + (endTime - startTime) + " (毫秒)\n";
				showErrorMsg += "================    词法分析 阶段    ================";
				mainUI.myMainPanel.errorText.setText(showErrorMsg);

				// 将二元流 写入文件（"data/dual.lex"）
				this.saveFile("data/dual.lex", lexicalAnalysis.getDualExpression());
						
				// 如果词法分析成功，在右面板显示词法分析结果
				if (lexicalAnalysis.errorMessageBeans.size() == 0)
				{
					this.operateAction("ctrl-dual-expression_table");
					mainUI.myMainPanel.jstRight.setDividerLocation(0.5);
				}
				else
				{
					mainUI.myMainPanel.jstRight.setDividerLocation(0.9);
				}
			}
		}
		// 显示词法分析结果（二元流表） ctrl-dual-expression_table
		else if (cmd.equals("ctrl-dual-expression_table"))
		{
			if (isLexicalAnalysis())
			{
				// 通过Label内嵌拼接HTML的方式。没有使用java的table，感觉得不方便
				String dualTable = "<html><body><center>";
				dualTable += "<h2>Dual-expression Table</h2><br>";
				String[] dualExpression = lexicalAnalysis.getDualExpression().split(" ");
						
				int tdNum = 10;//初始化列数
				int trNum = dualExpression.length / tdNum;//计算总行数
				if (dualExpression.length % tdNum != 0)
				{
					trNum++;
				}
				dualTable += "<table border=1 style=\"font-size: 16;margin-left:25;border-color: lightblue;background-color:#CCE8CF\">";
				for (int i = 0; i < trNum; i++)
				{
					dualTable += "<tr>";
					for (int j = 0; (j < tdNum) && (i * tdNum + j < dualExpression.length); j++)
					{
						dualTable += "<td> " + dualExpression[i * tdNum + j] + "</td>";
					}
					dualTable += "</tr>";
				}
				dualTable += "</table></center></body></html>";
				mainUI.myMainPanel.jstRight.setDividerLocation(0.2);
				mainUI.myMainPanel.jlMsg.setText(dualTable);
			}
		}
		// 显示xml中的关键字表 ctrl-show_Keywords_table
		else if (cmd.equals("ctrl-show_Keywords_table"))
		{
			HandleXMLFile handleXMLFile = new HandleXMLFile(new File("data/Keywords.xml"));
			String keywordsTable = "<html><body><center>";
			keywordsTable += "<h2>KeyWords Table</h2><br>";
			String[] subElements = { "id", "word" };
			
			keywordsTable += handleXMLFile.getElementTable("kw", subElements);
			keywordsTable += "</center></body></html>";
			mainUI.myMainPanel.jstRight.setDividerLocation(0.5);
			mainUI.myMainPanel.jlMsg.setText(keywordsTable);
		}
		// 显示xml中的详细错误信息表 ctrl-show_error_messages_table
		else if (cmd.equals("ctrl-show_error_messages_table"))
		{
			HandleXMLFile handleXMLFile = new HandleXMLFile(new File(
					"data/ErrorMessage.xml"));
			String errorMsgTable = "<html><body><center>";
			errorMsgTable += "<h2>Error Messages Table</h2><br>";
			String[] subElements =
			{ "code", "error_info" };
			errorMsgTable += handleXMLFile
					.getElementTable("error", subElements);
			errorMsgTable += "</center></body></html>";
			mainUI.myMainPanel.jstRight.setDividerLocation(0.2);
			mainUI.myMainPanel.jlMsg.setText(errorMsgTable);
		}
		// 显示标识符表 ctrl-show_identifier_table ctrl-show_constant_table"
		else if (cmd.equals("ctrl-show_identifier_table"))
		{
			if (isLexicalAnalysis())
			{
				String identifierTable = "<html><body><center>";
				identifierTable += "<h2>Identifier Table</h2><br>";
				identifierTable += "<table border=1 style=\"font-size: 16;margin-left:25;border-color: lightblue;background-color:#CCE8CF\">";
				identifierTable += "<tr><th> index </th><th> name </th><th> type </th><th> address </th></tr>";
				int len = lexicalAnalysis.identifierList.size();
				for (int i = 0; i < len; i++)
				{
					IdentifierBean identifierBean = lexicalAnalysis.identifierList
							.get(i);
					identifierTable += "<tr>";
					identifierTable += "<td> &nbsp;" + i + " &nbsp; </td>";
					identifierTable += "<td> &nbsp;" + identifierBean.getName()
							+ " &nbsp; </td>";
					identifierTable += "<td> &nbsp;" + identifierBean.getType()
							+ " &nbsp; </td>";
					identifierTable += "<td> &nbsp;"
							+ identifierBean.getAddress() + " &nbsp; </td>";
					identifierTable += "</tr>";
				}
				identifierTable += "</table></center></body></html>";
				mainUI.myMainPanel.jstRight.setDividerLocation(0.4);
				mainUI.myMainPanel.jlMsg.setText(identifierTable);
			}
		}
		// 显示常量表（数字，字符，字符串） ctrl-show_constant_table
		else if (cmd.equals("ctrl-show_constant_table"))
		{
			if (isLexicalAnalysis())
			{
				String constantTable = "<html><body>";

				// 设置3个div，盛放3个常量表
				constantTable += "<div style=\"float: left; margin-left: 25\">";
				constantTable += "<h2>Number List</h2><br>";
				constantTable += this.constantTable(lexicalAnalysis.numberList);
				constantTable += "</div>";

				constantTable += "<div style=\"float: left; margin-left: 25\">";
				constantTable += "<h2>String List</h2><br>";
				constantTable += this.constantTable(lexicalAnalysis.stringList);
				constantTable += "</div>";

				constantTable += "<div style=\"float: left; margin-left: 25\">";
				constantTable += "<h2>Char List</h2><br>";
				constantTable += this.constantTable(lexicalAnalysis.charList);
				constantTable += "</div>";
				
				constantTable += "</body></html>";
				mainUI.myMainPanel.jstRight.setDividerLocation(0.5);
				mainUI.myMainPanel.jlMsg.setText(constantTable);
			}
		}
		// 对当前文本进行文法分析，通过LL1预测分析法
		else if (cmd.equals("ctrl-syntax_analysis"))
		{
			// 获取开始运行的时间
			long startTime = System.currentTimeMillis();

			// 先保存
			this.ctrlSave();
			
			// -TODO 可以优化，不必每次都从头分析
			if (!isInitSeman)
			{
				// 把终结符集和非终结符集清空
				Terminal.terminals.clear();
				Nonterminal.nonterminals.clear();

				// 先用词法分析器解析文法
				lexicalAnalysis = new LexicalAnalysis(grammarFilePath);
				grammarParserByLL1 = new GrammarParserByLL1(
						lexicalAnalysis);
				// 1. LL1文法分析第1步: 初始化产生式 productions
				if (!grammarParserByLL1.FindProductions())
				{
					this.showGrammarParserResult(lexicalAnalysis, startTime);
					return;
				}
				// 2. LL1文法分析第2步: 计算first集（FirstSets)
				if (!grammarParserByLL1.ComputeFirstSets())
				{
					this.showGrammarParserResult(lexicalAnalysis, startTime);
					return;
				}
				// 3. LL1文法分析第3步: 计算follow集（followSets)
				if (!grammarParserByLL1.ComputeFollowSets())
				{
					this.showGrammarParserResult(lexicalAnalysis, startTime);
					return;
				}
				// 4. LL1文法分析第4步: 构建LL1预测分析表
				if (!grammarParserByLL1.buildLL1Table())
				{
					this.showGrammarParserResult(lexicalAnalysis, startTime);
					return;
				}
				isInitSeman = true;
			}
			// 5. LL1文法分析第5步: 表驱动的预测语法分析
			if (!grammarParserByLL1.LL1Parser(myText.fileName))
			{
				this.showGrammarParserResult(lexicalAnalysis, startTime);
				return;
			}
			this.showGrammarParserResult(lexicalAnalysis, startTime);
			// 初始化 LL1TableforShow，productionArrayforShow，为了显示LL1预测表
			GrammarParserByLL1.LL1TableforShow = grammarParserByLL1
					.getLL1Table();
			GrammarParserByLL1.productionArrayforShow = grammarParserByLL1
					.getProductionArray();
			
			showLL1ParseLog(grammarParserByLL1.getlL1ParseLog(), mainUI);
		}
		// 对当前文本进行语义分析，通过属性文法语义分析
		else if (cmd.equals("ctrl-semantic_analysis"))
		{
			if(this.grammarFilePath != "data/gra.grammar")
			{
				JOptionPane.showMessageDialog(mainUI, "\n语义分析只适用于[data/gra.grammar]文件下\n" +
						"系统定义的默认文法！\n", 
						"警告：文法不匹配", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			// 获取开始运行的时间
			long startTime = System.currentTimeMillis();
			
			// 先保存
			this.ctrlSave();
			
			if (!isInitSeman)
			{
				// 把终结符集和非终结符集清空
				Terminal.terminals.clear();
				Nonterminal.nonterminals.clear();
				
				// 先用词法分析器解析文法
				lexicalAnalysis = new LexicalAnalysis(grammarFilePath);
				grammarParserByLL1 = new GrammarParserByLL1(
						lexicalAnalysis);
				// 1. LL1文法分析第1步: 初始化产生式 productions
				if (!grammarParserByLL1.FindProductions())
				{
					this.showSemanticAnalysisResult(lexicalAnalysis, startTime, grammarParserByLL1);
					return;
				}
				// 2. LL1文法分析第2步: 计算first集（FirstSets)
				if (!grammarParserByLL1.ComputeFirstSets())
				{
					this.showSemanticAnalysisResult(lexicalAnalysis, startTime, grammarParserByLL1);
					return;
				}
				// 3. LL1文法分析第3步: 计算follow集（followSets)
				if (!grammarParserByLL1.ComputeFollowSets())
				{
					this.showSemanticAnalysisResult(lexicalAnalysis, startTime, grammarParserByLL1);
					return;
				}
				// 4. LL1文法分析第4步: 构建LL1预测分析表
				if (!grammarParserByLL1.buildLL1Table())
				{
					this.showSemanticAnalysisResult(lexicalAnalysis, startTime, grammarParserByLL1);
					return;
				}
				isInitSeman = true;
			}
			// 5. LL1文法分析第5步: 属性文法语义分析
			if (!grammarParserByLL1.semanticAnalysis(myText.fileName))
			{
				isSemanticAnalysis = true;
				this.showSemanticAnalysisResult(lexicalAnalysis, startTime, grammarParserByLL1);
				return;
			}
			this.showSemanticAnalysisResult(lexicalAnalysis, startTime, grammarParserByLL1);
			// 初始化 LL1TableforShow，productionArrayforShow，为了显示LL1预测表
			GrammarParserByLL1.LL1TableforShow = grammarParserByLL1
					.getLL1Table();
			GrammarParserByLL1.productionArrayforShow = grammarParserByLL1
					.getProductionArray();
			
			showQuaternionType(grammarParserByLL1.getQuaternions(), mainUI);
		}
		// 显示LL1预测分析法生成的LL1预测表（正常版，和课本显示的一样）
		else if (cmd.equals("ctrl-show_ll(1)_table_normal"))
		{
			if (! isSyntaxAnalysis())
			{
				return;
			}
			int nullTerminalIndex = Terminal.IndexOf("null");
			String LL1Table = "<html><body><center>";
			LL1Table += "<h2>LL1 Table (normal)</h2><br>";
			LL1Table += "<table border=1 style=\"font-size: 16;margin-left:25;border-color: lightblue;background-color:#CCE8CF\">";
			LL1Table += "<tr>";
			LL1Table += "<th width=\"50px\">  </th>";
			int terminalsLength = Terminal.terminals.size();
			for (int i = 0; i < terminalsLength; i++)
			{
				if (i != nullTerminalIndex)
				{
					LL1Table += "<th width=\"50px\"> " + Terminal.get(i)
							+ " </th>";
				}
			}
			LL1Table += "</tr>";
			int[][] LL1TableforShow = GrammarParserByLL1.LL1TableforShow;
			for (int i = 0; i < LL1TableforShow.length; i++)
			{
				String nonTerminalString = Nonterminal.get(i);
				LL1Table += "<tr>";
				LL1Table += "<td align=\"center\"><b> " + nonTerminalString
						+ " </b></td>";
				nonTerminalString += "→";
				for (int j = 0; j < terminalsLength; j++)
				{
					if (j != nullTerminalIndex)
					{
						LL1Table += "<td align=\"center\"> ";
						// get 产生式
						int proIndex = LL1TableforShow[i][j];
						if (proIndex == -2 || proIndex == -1)
						{
							LL1Table += "";
						}
						else
						{
							Object[] production = GrammarParserByLL1.productionArrayforShow[proIndex];
							String proStr = nonTerminalString;
							for (int k = 0; k < production.length; k++)
							{
								if (GrammarParserByLL1
										.isTerminalObject(production[k]))
								{
									Terminal terminal = (Terminal) production[k];
									proStr += Terminal.get(terminal.index)
											+ " ";
								}
								else
								{
									Nonterminal nonterminal = (Nonterminal) production[k];
									proStr += Nonterminal
											.get(nonterminal.index) + " ";
								}
							}
							LL1Table += proStr;
						}
						LL1Table += " </td>";
					}
				}
				LL1Table += "</tr>";
			}
			LL1Table += "</table>";
			LL1Table += "</center></body></html>";
			mainUI.myMainPanel.jstRight.setDividerLocation(0.2);
			mainUI.myMainPanel.jlMsg.setText(LL1Table);
		}
		// 显示LL1预测分析法生成的LL1预测表（程序版，程序中真正使用的LL1表）
		else if (cmd.equals("ctrl-show_ll(1)_table_program"))
		{
			if (! isSyntaxAnalysis())
			{
				return;
			}
			int nullTerminalIndex = Terminal.IndexOf("null");
			String LL1Table = "<html><body><center>";
			LL1Table += "<h2>LL1 Table (program)</h2><br>";
			LL1Table += "<table border=1 style=\"font-size: 16;margin-left:25;border-color: lightblue;background-color:#CCE8CF\">";
			LL1Table += "<tr>";
			LL1Table += "<th width=\"50px\">  </th>";
			int terminalsLength = Terminal.terminals.size();
			for (int i = 0; i < terminalsLength; i++)
			{
				if (i != nullTerminalIndex)
				{
					LL1Table += "<th width=\"50px\"> " + Terminal.get(i)
							+ " </th>";
				}
			}
			LL1Table += "</tr>";
			int[][] LL1TableforShow = GrammarParserByLL1.LL1TableforShow;
			for (int i = 0; i < LL1TableforShow.length; i++)
			{
				String nonTerminalString = Nonterminal.get(i);
				LL1Table += "<tr>";
				LL1Table += "<td align=\"center\"><b> " + nonTerminalString
						+ " </b></td>";
				nonTerminalString += "→";
				for (int j = 0; j < terminalsLength; j++)
				{
					if (j != nullTerminalIndex)
					{
						LL1Table += "<td align=\"center\"> ";
						// get 产生式
						int proIndex = LL1TableforShow[i][j];
						if (proIndex == -2)
						{
							LL1Table += "-2";
						}
						else if (proIndex == -1)
						{
							LL1Table += "-1";
						}
						else
						{
							Object[] production = GrammarParserByLL1.productionArrayforShow[proIndex];
							String proStr = nonTerminalString;
							for (int k = 0; k < production.length; k++)
							{
								if (GrammarParserByLL1
										.isTerminalObject(production[k]))
								{
									Terminal terminal = (Terminal) production[k];
									proStr += Terminal.get(terminal.index)
											+ " ";
								}
								else
								{
									Nonterminal nonterminal = (Nonterminal) production[k];
									proStr += Nonterminal
											.get(nonterminal.index) + " ";
								}
							}
							LL1Table += proStr;
						}
						LL1Table += " </td>";
					}
				}
				LL1Table += "</tr>";
			}
			LL1Table += "</table>";
			LL1Table += "<br>注：程序中使用 “-2”代表错误标识，“-1”代表错误恢复标识。";
			LL1Table += "</center></body></html>";
			mainUI.myMainPanel.jstRight.setDividerLocation(0.2);
			mainUI.myMainPanel.jlMsg.setText(LL1Table);
		}
		// 显示解析出来的产生式
		else if (cmd.equals("ctrl-show_productions"))
		{
			if (! isSyntaxAnalysis())
			{
				return;
			}
			String showProductions = "<html><body><center>";

			// 设置1个div，盛放productions
			showProductions += "<div style=\"float: left; margin-left: 30\">";
			showProductions += "<h2>Used Productions</h2>";
			
			showProductions += "<table style=\"float: left; margin-left: 10;font-size: 14; \">";
			ArrayList<Production> productions = grammarParserByLL1.getProductions();
			for (int i = 0; i < productions.size(); i++) 
			{
				showProductions += productions.get(i).showProduction_html();
			}
			showProductions += "</table>";
			showProductions += "</div>";

			showProductions += "</center></body></html>";
			mainUI.myMainPanel.jstRight.setDividerLocation(0.4);
			mainUI.myMainPanel.jlMsg.setText(showProductions);
		}
		// 显示First集 及其 构造过程
		else if (cmd.equals("ctrl-show_first_set"))
		{
			if (! isSyntaxAnalysis())
			{
				return;
			}
			String showFirstSet = "<html><body><center>";
			
			// 设置2个div，盛放 firstset 和 构造过程
			showFirstSet += "<div style=\"float: left; margin-left: 30\">";
			showFirstSet += "<h2>First Set</h2>";
			
			showFirstSet += "<table style=\"float: left; margin-left: 10;font-size: 14; \">";
			HashSet<Integer>[] firstSets = grammarParserByLL1.getFirstSets();
			for (int i = 0; i < firstSets.length; i++) 
			{
				showFirstSet += "<tr>";
				showFirstSet += "<td>First(" + Nonterminal.get(i) + ")</td>";
				showFirstSet += "<td> = </td>";
				showFirstSet += "<td>{ ";
				
				Iterator<Integer> iterator = firstSets[i].iterator();
				while (iterator.hasNext())
				{
					showFirstSet += Terminal.get(iterator.next()) + " ";
				}
				
				showFirstSet += "}</td>";
				showFirstSet += "</tr>";
			}
			showFirstSet += "</table>";
			showFirstSet += "</div>";
			
			/*showFirstSet += "<div style=\"float: left; margin-left: 25; \">";
			showFirstSet += "<h2>The Generate Process</h2>";
			
			// 构造First集的规则。
			showFirstSet += "规则一：如果存在产生式X->a…，且a∈Vt，则把a加入到FIRST（X）中；<br>" +
					"&nbsp;&nbsp;&nbsp;&nbsp;若存在X->ε,则将ε也加入到FIRST（X）中。<br><br>";
			showFirstSet += "规则二：如有X->Y…，且Y∈Vn，则将FIRST（Y）中的所有非ε终结符加入到FITST（X）中；<br>" +
					"&nbsp;&nbsp;&nbsp;&nbsp;若有X->Y1Y2…Yk，且Y1～Y2都是非终结符，而Y1～Yi-1的候选式都有ε存在，<br>" +
					"&nbsp;&nbsp;&nbsp;&nbsp;则把FIRST（Yj）（j=1,2，…i）的所有非ε终结符加入到FIRST（X）中；<br>" +
					"&nbsp;&nbsp;&nbsp;&nbsp;特别是当Y1～Yk都含有ε产生式时，应把ε也加入FIRST（X）中。<br><br>";
			// 显示构造过程。
			showFirstSet += "<hr>";
			showFirstSet += "<table>";
			ArrayList<String> firstSetLogs = grammarParserByLL1.getFirstSetLog();
			for (String log : firstSetLogs) {
				showFirstSet += "<tr><td>" + log + "</td></tr>";
			}
			showFirstSet += "</table>";
			
			showFirstSet += "</div>";*/
			
			showFirstSet += "</center></body></html>";
			mainUI.myMainPanel.jstRight.setDividerLocation(0.4);
			mainUI.myMainPanel.jlMsg.setText(showFirstSet);
		}
		// 显示Follow集 及其 构造过程
		else if (cmd.equals("ctrl-show_follow_set"))
		{
			if (! isSyntaxAnalysis())
			{
				return;
			}
			String showFollowSet = "<html><body><center>";
			
			// 设置2个div，盛放followset 和 构造过程
			showFollowSet += "<div style=\"float: left; margin-left: 30\">";
			showFollowSet += "<h2>Follow Set</h2>";
			
			showFollowSet += "<table style=\"float: left; margin-left: 10;font-size: 14; \">";
			HashSet<Integer>[] followSets = grammarParserByLL1.getFollowSets();
			for (int i = 0; i < followSets.length; i++) 
			{
				showFollowSet += "<tr>";
				showFollowSet += "<td>First(" + Nonterminal.get(i) + ")</td>";
				showFollowSet += "<td> = </td>";
				showFollowSet += "<td>{ ";

				Iterator<Integer> iterator = followSets[i].iterator();
				while (iterator.hasNext())
				{
					showFollowSet += Terminal.get(iterator.next()) + " ";
				}
				
				showFollowSet += "}</td>";
				showFollowSet += "</tr>";
			}
			showFollowSet += "</table>";
			showFollowSet += "</div>";

			/*showFollowSet += "<div style=\"float: left; margin-left: 25\">";
			showFollowSet += "<h2>The Generate Process</h2><br>";
			
			
			
			showFollowSet += "</div>";*/

			showFollowSet += "</center></body></html>";
			mainUI.myMainPanel.jstRight.setDividerLocation(0.4);
			mainUI.myMainPanel.jlMsg.setText(showFollowSet);
		}
		// 显示LL（1）语法分析过程
		else if (cmd.equals("ctrl-show_parse_process"))
		{
			if (! isSyntaxAnalysis())
			{
				return;
			}
			showLL1ParseLog(grammarParserByLL1.getlL1ParseLog(), mainUI);
		}
		// 显示语义分析产生的四元式
		else if (cmd.equals("ctrl-show_quaternion_type"))
		{
			if (! isSemanticAnalysis)
			{
				JOptionPane.showMessageDialog(null,
						"Please do the Semantic Analysis Firstly !", "alert",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			showQuaternionType(grammarParserByLL1.getQuaternions(), mainUI);
		}
	}

	public void showGrammarParserResult(LexicalAnalysis lexicalAnalysis,
			long startTime)
	{
		String showResult = "> 开始语法分析 (" + myText.fileName + ")\n";
		showResult += lexicalAnalysis.handleErrorMessage();
		if (lexicalAnalysis.errorMessageBeans.size() == 0)
		{
			showResult += ">\n> 语法分析 成功。\n";
		}
		else
		{
			showResult += ">\n> 语法分析 失败。\n";
		}
		// 获取结束时间
		long endTime = System.currentTimeMillis();
		showResult += "> 已用时间: " + (endTime - startTime) + " (毫秒)\n";
		showResult += "================    文法分析 阶段    ================";
		mainUI.myMainPanel.errorText.setText(showResult);
	}
	
	public void showSemanticAnalysisResult(LexicalAnalysis lexicalAnalysis,
			long startTime, GrammarParserByLL1 grammarParserByLL1)
	{
		String showResult = "> 开始语义分析 (" + myText.fileName + ")\n";
		showResult += lexicalAnalysis.handleErrorMessage();
		if (lexicalAnalysis.errorMessageBeans.size() == 0)
		{
			showResult += ">\n> 语义分析 成功。\n";
		}
		else
		{
			showResult += ">\n> 语义分析 失败。\n";
		}
		// 获取结束时间
		long endTime = System.currentTimeMillis();
		showResult += "> 已用时间: " + (endTime - startTime) + " (毫秒)\n";
		mainUI.myMainPanel.errorText.setText(showResult);
	}

	// 判断是否 进行了词法分析
	public boolean isLexicalAnalysis()
	{
		if (lexicalAnalysis == null)
		{
			JOptionPane.showMessageDialog(null,
					"Please do the Lexical Analysis Firstly !", "alert",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}
	
	// 判断是否 进行了语法分析
	public boolean isSyntaxAnalysis()
	{
		//if (GrammarParserByLL1.LL1TableforShow == null)
		if (isInitSeman == false)
		{
			JOptionPane.showMessageDialog(null,
					"Please do the Syntax Analysis Firstly !", "alert",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	//
	public String constantTable(ArrayList constantList)
	{
		String constantTable = "<table border=1 style=\"font-size: 16;margin-left:25;border-color: lightblue;background-color:#CCE8CF\">";
		constantTable += "<tr><th> index </th><th> value </th></tr>";
		int len = constantList.size();
		for (int i = 0; i < len; i++)
		{
			constantTable += "<tr>";
			constantTable += "<td> &nbsp;" + i + " &nbsp; </td>";
			constantTable += "<td> &nbsp;" + constantList.get(i)
					+ " &nbsp; </td>";
			constantTable += "</tr>";
		}
		constantTable += "</table>";
		return constantTable;
	}
	
	// 显示LL1语法分析过程
	public String showLL1ParseLog(ArrayList<LL1ProcessLog> LL1ParseLog, MainUI mainUI)
	{
		String out = "<html><body><center>";
		out += "<h2>LL(1) Parse Process</h2>";
		out += "<table border=1 style=\"font-size: 16;margin-left:25;border-color: lightblue;background-color:#CCE8CF\">";
		out += LL1ProcessLog.getLL1Log_tablehead();
		int len = LL1ParseLog.size();
		for (int i = 0; i < len; i++)
		{
			out += LL1ParseLog.get(i).getLL1Log_html();
		}
		out += "</table>";
		out += "</center></body></html>";
		
		mainUI.myMainPanel.jstRight.setDividerLocation(0.3);
		mainUI.myMainPanel.jlMsg.setText(out);
		return out;
	}
	
	// 显示语义分析产生的四元式
	public String showQuaternionType(ArrayList<String> quaternions, MainUI mainUI)
	{
		String out = "<html><body><center>";
		out += "<h2>&nbsp;&nbsp;Quaternion Type（四元式）</h2><br>";
		out += "<table style=\"font-size: 16;margin-left:25;\">";

		int len = quaternions.size();
		for (int i = 0; i < len; i++)
		{
			out += "<tr><td>" + quaternions.get(i) + "</td></tr>";
		}
		
		out += "</table>";
		out += "</center></body></html>";
		
		mainUI.myMainPanel.jstRight.setDividerLocation(0.5);
		mainUI.myMainPanel.jlMsg.setText(out);
		return out;
	}
}
