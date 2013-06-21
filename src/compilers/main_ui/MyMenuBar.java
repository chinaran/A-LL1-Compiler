/**
 * @author Randy
 * @date 2013-05-25
 * @function 编译器的菜单栏
 */
package compilers.main_ui;

import java.awt.Toolkit;
import java.awt.event.*;
import javax.swing.*;

import java.util.*;

public class MyMenuBar extends JMenuBar implements ActionListener
{
	// define components
	Operaor operaor = null;

	// 一级菜单
	JMenu jmFile, jmEdit, jmProject, jmDebug, jmWindow, jmStyle, jmHelp;
	ArrayList<JMenuItem> jmiFile = new ArrayList<JMenuItem>();
	ArrayList<JMenuItem> jmiEdit = new ArrayList<JMenuItem>();
	ArrayList<JMenuItem> jmiStyle = new ArrayList<JMenuItem>();
	JCheckBoxMenuItem jcbmiLineWrap = new JCheckBoxMenuItem("Line Wrap(W)");
	JCheckBoxMenuItem jcbmiToolbar = new JCheckBoxMenuItem("Toolbar(T)");
	ArrayList<JMenuItem> jmiProject = new ArrayList<JMenuItem>();
	ArrayList<JMenuItem> jmiDebug = new ArrayList<JMenuItem>();
	ArrayList<JMenuItem> jmiWindow = new ArrayList<JMenuItem>();
	ArrayList<JMenuItem> jmiHelp = new ArrayList<JMenuItem>();

	// 二级菜单（菜单[Window]中的）
	JMenu jmW2LexicalAnalysis, jmW2SyntaxAnalysis, jmW2SemanticAnalysis,
			jmW2Optimize, jmW2GenerateTargetCode;
	JMenuItem jmiPreferences;
	// 词法分析
	ArrayList<JMenuItem> jmiW2LexicalAnalysis = new ArrayList<JMenuItem>();
	// 语法分析
	ArrayList<JMenuItem> jmiW2SyntaxAnalysis = new ArrayList<JMenuItem>();
	// 语义分析
	ArrayList<JMenuItem> jmiW2SemanticAnalysis = new ArrayList<JMenuItem>();
	// 优化
	ArrayList<JMenuItem> jmiW2Optimize = new ArrayList<JMenuItem>();
	// 目标代码生成
	ArrayList<JMenuItem> jmiW2GenerateTargetCode = new ArrayList<JMenuItem>();

	// for jmenu jmfile
	static String[] FILEITEMS =
	{ "New(N)", "Open(O)...", "Select Grammar File...", "Save(S)", "Save As(A)...", "Print(P)...",
			"Quit(Q)" };
	static String[] FILEICON =
	{ "new.png", "open.gif", "select_grammar.png", "save.png", "save.png", "print.png", "quit.png" };
	static String[] FILECMDS =
	{ "ctrl-new", "ctrl-open", "ctrl-select_grammar_file", "ctrl-save", "ctrl-saveAs", "ctrl-print",
			"ctrl-quit" };
	char[] fileMnemonic =
	{ 'N', 'O', 'G', 'S', 'A', 'P', 'Q' };
	char[] fileShortcuts =
	{ 'N', 'O', '1', 'S', 'E', 'P', 'Q' };

	// for jmenu jmedit
	static String[] EDITITEMS =
	{ "Undo(U)", "Redo(Y)", "Select All(A)", "Cut(T)", "Copy(C)", "Paste(P)",
			"Delete(D)", "Find(F)", "Find Next(N)", "Replace(R)" };
	static String[] EDITICON =
	{ "undo.png", "redo.png", "selectAll.png", "cut.png", "copy.png",
			"paste.png", "delete.png", "find.png", "findNext.png",
			"replace.png" };
	static String[] EDITCMDS =
	{ "ctrl-undo", "ctrl-redo", "ctrl-selectAll", "ctrl-cut", "ctrl-copy",
			"ctrl-paste", "ctrl-delete", "ctrl-find", "ctrl-findNext",
			"ctrl-replace" };
	char[] editMnemonic =
	{ 'U', 'Y', 'A', 'T', 'C', 'P', 'D', 'F', 'N', 'R' };
	char[] editShortcuts =
	{ 'Z', 'Y', 'A', 'X', 'C', 'V', 'D', 'F', 'I', 'R' };

	// for jmenu jmstyle
	static String[] STYLEITEMS =
	{ "Font(F)...", "Color(C)...", "Line Wrap(W)", "Toolbar(T)" };
	static String[] STYLECMDS =
	{ "ctrl-font", "ctrl-color", "ctrl-lineWrap", "ctrl-toolbar" };
	static String[] STYLEICON =
	{ "font.png", "color.png", "lineWrap.png", "toolbar.png" };
	char[] styleMnemonic =
	{ 'F', 'C', 'W', 'T' };
	// ImageIcon iilineWrap_true = new ImageIcon("images/lineWrap_true.png");
	// ImageIcon iilineWrap_false = new ImageIcon("images/lineWrap_false.png");

	// for jmenu jmProject
	static String[] PROJECTITEMS =
	{ "Compile(C)", "Build(B)", "Run(R)", "Debug(D)", "Propeties(P)..." };
	static String[] PROJECTCMDS =
	{ "ctrl-compile", "ctrl-build", "ctrl-run", "ctrl-debug", "ctrl-propeties" };
	static String[] PROJECTICON =
	{ "compile.png", "build.png", "run.png", "debug.png", "propeties.png" };
	char[] ProjectMnemonic =
	{ 'C', 'B', 'R', 'D', 'P' };
	// char[] ProjectShortcuts =
	// { 'N', 'O', 'S', 'E', 'P', 'Q' };

	// for jmenu jmDebug
	static String[] DEBUGTITEMS =
	{ "Debug(D)", "Lexical analysis(L)", "Syntax analysis(S)",
			"Semantic analysis(M)", "Optimize(O)", "Generate target code(G)" };
	static String[] DEBUGCMDS =
	{ "ctrl-debug", "ctrl-lexical_analysis", "ctrl-syntax_analysis",
			"ctrl-semantic_analysis", "ctrl-optimize",
			"ctrl-generate_target_code" };
	static String[] DEBUGICON =
	{ "debug.png", "lex.png", "LL1.png", "sem.png",
			"optimize.png", "generate_target_code.png" };
	char[] DebugMnemonic =
	{ 'D', 'L', 'S', 'M', 'O', 'G' };

	// for jmenu help
	static String[] HELPITEMS =
	{ "Help Content(H)...", "About(A)..." };
	static String[] HELPCMDS =
	{ "ctrl-helpContent", "ctrl-about" };
	static String[] HELPICON =
	{ "help.png", "about.png" };
	char[] helpMnemonic =
	{ 'H', 'A' };

	/********* for jmenu jmWindow（二级菜单） ***********/
	// for jmenu jmW2LexicalAnalysis
	// 分别为 关键字表，标识符表，常数表，错误信息查询表,二元式表
	static String[] LexicalAnalysisITEMS =
	{ "Keywords Table", "Identifier Table", "Constant Table",
			"Error Messages Table", "Dual-expression Table" };
	static String[] LexicalAnalysisCMDS =
	{ "ctrl-show_Keywords_table", "ctrl-show_identifier_table",
			"ctrl-show_constant_table", "ctrl-show_error_messages_table",
			"ctrl-dual-expression_table" };
	
	// for jmenu jmW2SyntaxAnalysis
	static String[] SyntaxAnalysisITEMS = 
	{ "Show Productions", "First Set", "Follow Set", "LL(1) Table(normal)", "LL(1) Table(program)", "Parse Process" };
	static String[] SyntaxAnalysisCMDS = 
	{ "ctrl-show_productions", "ctrl-show_first_set", "ctrl-show_follow_set",
			"ctrl-show_ll(1)_table_normal", "ctrl-show_ll(1)_table_program", "ctrl-show_parse_process" };

	// for jmenu jmW2SemanticAnalysis
	static String[] SemanticAnalysisITEMS = { "Quaternion Type" };
	static String[] SemanticAnalysisCMDS = { "ctrl-show_quaternion_type" };

	// for jmenu jmW2Optimize
	static String[] OptimizeITEMS = {};// 暂时空
	static String[] OptimizeCMDS = {};

	// for jmenu jmW2GenerateTargetCode
	static String[] GenerateTargetCodeITEMS = {};// 暂时空
	static String[] GenerateTargetCodeCMDS = {};

	/**
	 * constructor
	 */
	public MyMenuBar(Operaor operaor)
	{
		this.operaor = operaor;

		// initial JMenu
		jmFile = new JMenu("File(F)");
		jmEdit = new JMenu("Edit(E)");
		jmStyle = new JMenu("Style(S)");
		jmProject = new JMenu("Project(P)");
		jmDebug = new JMenu("Debug(D)");
		jmWindow = new JMenu("Window(W)");
		jmHelp = new JMenu("Help(H)");

		jmFile.setMnemonic(KeyEvent.VK_F);
		jmEdit.setMnemonic(KeyEvent.VK_E);
		jmStyle.setMnemonic(KeyEvent.VK_S);
		jmProject.setMnemonic(KeyEvent.VK_P);
		jmDebug.setMnemonic(KeyEvent.VK_D);
		jmWindow.setMnemonic(KeyEvent.VK_W);
		jmHelp.setMnemonic(KeyEvent.VK_H);

		// add JMenu to JMenuBar
		this.add(jmFile);
		this.add(jmEdit);
		this.add(jmStyle);
		this.add(jmProject);
		this.add(jmDebug);
		this.add(jmWindow);
		this.add(jmHelp);
		// new ImageIcon("images/icons/"+ ToolIcon[i])
		/********************** add JMenuItem and set mnemonic ********************/
		// add "File" JMenuItem and set mnemonic
		for (int i = 0; i < FILEITEMS.length; i++)
		{
			JMenuItem jmi = new JMenuItem(FILEITEMS[i], new ImageIcon(
					"images/icons/" + FILEICON[i]));
			jmiFile.add(jmi);
			jmi.setMnemonic(fileMnemonic[i]);// Alt
			jmi.setAccelerator(KeyStroke.getKeyStroke(fileShortcuts[i], Toolkit
					.getDefaultToolkit().getMenuShortcutKeyMask(), false));// Ctrl
			jmi.addActionListener(this);
			jmi.setActionCommand(FILECMDS[i]);
			jmFile.add(jmi);
		}
		jmFile.insertSeparator(4);
		jmFile.insertSeparator(6);

		// add "Edit" JMenuItem and set mnemonic
		for (int i = 0; i < EDITITEMS.length; i++)
		{
			JMenuItem jmi = new JMenuItem(EDITITEMS[i], new ImageIcon(
					"images/icons/" + EDITICON[i]));
			jmiEdit.add(jmi);
			jmi.setMnemonic(editMnemonic[i]);// Alt
			jmi.setAccelerator(KeyStroke.getKeyStroke(editShortcuts[i], Toolkit
					.getDefaultToolkit().getMenuShortcutKeyMask(), false));// Ctrl
			jmi.addActionListener(this);
			jmi.setActionCommand(EDITCMDS[i]);
			jmEdit.add(jmi);
		}
		jmEdit.insertSeparator(3);
		jmEdit.insertSeparator(8);

		// add "Style" JMenuItem and set mnemonic
		for (int i = 0; i < STYLEITEMS.length - 2; i++)
		{
			JMenuItem jmi = new JMenuItem(STYLEITEMS[i], new ImageIcon(
					"images/icons/" + STYLEICON[i]));
			jmiStyle.add(jmi);
			jmi.setMnemonic(styleMnemonic[i]);// Alt
			jmi.addActionListener(this);
			jmi.setActionCommand(STYLECMDS[i]);
			jmStyle.add(jmi);
		}
		// 设置 那两个 JCheckBoxMenuItem
		jcbmiLineWrap.setMnemonic(styleMnemonic[2]);
		jcbmiLineWrap.addActionListener(this);
		jcbmiLineWrap.setActionCommand(STYLECMDS[2]);
		jcbmiLineWrap.setSelected(false);
		jmStyle.add(jcbmiLineWrap);

		jcbmiToolbar.setMnemonic(styleMnemonic[3]);
		jcbmiToolbar.addActionListener(this);
		jcbmiToolbar.setActionCommand(STYLECMDS[3]);
		jcbmiToolbar.setSelected(true);
		jmStyle.add(jcbmiToolbar);

		// add "Project" JMenuItem and set mnemonic
		for (int i = 0; i < PROJECTITEMS.length; i++)
		{
			JMenuItem jmi = new JMenuItem(PROJECTITEMS[i], new ImageIcon(
					"images/icons/" + PROJECTICON[i]));
			jmiProject.add(jmi);
			jmi.setMnemonic(ProjectMnemonic[i]);// Alt
			jmi.addActionListener(this);
			jmi.setActionCommand(PROJECTCMDS[i]);
			jmProject.add(jmi);
		}
		// Ctrl compiler
		jmiProject.get(0).setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F9, Toolkit
						.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		// Ctrl run
		jmiProject.get(2).setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F10, Toolkit
						.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		// Ctrl debug
		jmiProject.get(3).setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_F11, Toolkit
						.getDefaultToolkit().getMenuShortcutKeyMask(), false));

		jmProject.insertSeparator(2);
		jmProject.insertSeparator(5);

		// add "Debug" JMenuItem and set mnemonic
		for (int i = 0; i < DEBUGTITEMS.length; i++)
		{
			JMenuItem jmi = new JMenuItem(DEBUGTITEMS[i], new ImageIcon(
					"images/icons/" + DEBUGICON[i]));
			jmiDebug.add(jmi);
			jmi.setMnemonic(DebugMnemonic[i]);// Alt
			jmi.addActionListener(this);
			jmi.setActionCommand(DEBUGCMDS[i]);
			jmDebug.add(jmi);
		}
		jmiDebug.get(1).setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_L, Toolkit
						.getDefaultToolkit().getMenuShortcutKeyMask(), false));
		jmDebug.insertSeparator(1);

		// add "Help" JMenuItem and set mnemonic
		for (int i = 0; i < HELPITEMS.length; i++)
		{
			JMenuItem jmi = new JMenuItem(HELPITEMS[i], new ImageIcon(
					"images/icons/" + HELPICON[i]));
			jmiHelp.add(jmi);
			jmi.setMnemonic(helpMnemonic[i]);// Alt
			jmi.addActionListener(this);
			jmi.setActionCommand(HELPCMDS[i]);
			jmHelp.add(jmi);
		}
		/******************* set Window 二级菜单 ******************************/
		// add "Window" JMenuItem
		jmW2LexicalAnalysis = new JMenu("Lexical Analysis");
		jmW2SyntaxAnalysis = new JMenu("Syntax Analysis");
		jmW2SemanticAnalysis = new JMenu("Semantic Analysis");
		jmW2Optimize = new JMenu("Optimize");
		jmW2GenerateTargetCode = new JMenu("Generate Target Code");

		jmWindow.add(jmW2LexicalAnalysis);
		jmWindow.add(jmW2SyntaxAnalysis);
		jmWindow.add(jmW2SemanticAnalysis);
		jmWindow.add(jmW2Optimize);
		jmWindow.add(jmW2GenerateTargetCode);

		jmiPreferences = new JMenuItem("Preferences(P)...");
		jmiPreferences.addActionListener(this);
		jmiPreferences.setActionCommand("ctrl-preferences");

		jmWindow.add(jmiPreferences);

		// add "LexicalAnalysis" JMenuItem
		for (int i = 0; i < LexicalAnalysisITEMS.length; i++)
		{
			JMenuItem jmi = new JMenuItem(LexicalAnalysisITEMS[i]);
			jmiW2LexicalAnalysis.add(jmi);
			jmi.addActionListener(this);
			jmi.setActionCommand(LexicalAnalysisCMDS[i]);
			jmW2LexicalAnalysis.add(jmi);
		}
		// add "Syntax analysis" JMenuItem
		for (int i = 0; i < SyntaxAnalysisITEMS.length; i++)
		{
			JMenuItem jmi = new JMenuItem(SyntaxAnalysisITEMS[i]);
			jmiW2SyntaxAnalysis.add(jmi);
			jmi.addActionListener(this);
			jmi.setActionCommand(SyntaxAnalysisCMDS[i]);
			jmW2SyntaxAnalysis.add(jmi);
		}
		// add "Semantic analysis" JMenuItem
		for (int i = 0; i < SemanticAnalysisITEMS.length; i++)
		{
			JMenuItem jmi = new JMenuItem(SemanticAnalysisITEMS[i]);
			jmiW2SemanticAnalysis.add(jmi);
			jmi.addActionListener(this);
			jmi.setActionCommand(SemanticAnalysisCMDS[i]);
			jmW2SemanticAnalysis.add(jmi);
		}
	}

	// 对于事件响应的处理
	@Override
	public void actionPerformed(ActionEvent e)
	{
		operaor.operateAction(e.getActionCommand());
	}

}
