/**
 * @author Randy
 * @date 2011-11-25
 * @function ±àÒëÆ÷µÄ¹¤¾ßÀ¸
 */
package compilers.main_ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class MyToolBar extends JToolBar implements ActionListener
{
	// define components
	Operaor operaor = null;

	static String[] ToolIcon =
	{ "new.png", "open.gif", "select_grammar.png", "save.png", "undo.png", "redo.png", "font.png",
			"lineWrap.png", "compile.png", "run.png", "debug.png", "lex.png",
			 "LL1.png", "sem.png", "dualTable.gif", "pro.png", "firstset.png", "followset.png", 
			 "LL1Table1.png", "LL1Table2.png", "test.gif" };
	
	static String[] ToolText =
	{ "New File(Ctrl+N)", "Open File(Ctrl+O)", "Select Grammar File(Ctrl+1)", "Save File(Ctrl+S)",
			"Undo(Ctrl+Z)", "Redo(Ctrl+Y)", "Set Font", "Auto Line Wrap",
			"Compile(Ctrl+F9)", "Run(Ctrl+F10)", "Debug(Ctrl+F11)",
			"Lexical Analysis(Ctrl+L)", "Syntax Analysis by LL1 Method", 
			"Semantic Analysis", "Show Dual-expression Table",
			"Show Productions", "Show First Set", "Show Follow Set",
			"Show LL1 Table(normal)", "Show LL1 Table(program)",
			"For Testing When programming" };
	
	static String[] ToolCmds =
	{ "ctrl-new", "ctrl-open", "ctrl-select_grammar_file", "ctrl-save", "ctrl-undo", "ctrl-redo",
			"ctrl-font", "ctrl-lineWrap", "ctrl-compile", "ctrl-run",
			"ctrl-debug","ctrl-lexical_analysis", "ctrl-syntax_analysis", 
			"ctrl-semantic_analysis", "ctrl-dual-expression_table", 
			"ctrl-show_productions", "ctrl-show_first_set", "ctrl-show_follow_set",
			"ctrl-show_ll(1)_table_normal", "ctrl-show_ll(1)_table_program", "ctrl-color" };

	ArrayList<JButton> jbList = new ArrayList<JButton>();

	/**
	 * constructor
	 */
	public MyToolBar(Operaor operaor)
	{
		this.operaor = operaor;

		int len = ToolIcon.length;
		for (int i = 0; i < len; i++)
		{
			JButton jb = new JButton(new ImageIcon("images/icons/" + ToolIcon[i]));
			jb.addActionListener(this);
			jb.setActionCommand(ToolCmds[i]);
			jb.setToolTipText(ToolText[i]);
			this.add(jb);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		this.operaor.operateAction(e.getActionCommand());
	}
}
