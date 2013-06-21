/**
 * @author Randy
 * @date 2013-05-25
 * @function 编译器的文本部分
 */
package compilers.main_ui;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;

public class MyText extends JTextArea
{
	// define variable
	boolean isChanged = false;
	String fileName = null;
	UndoManager undoManager = null;

	/**
	 * constructor
	 */
	public MyText()
	{
		// 监听文本是否发生变化（可以优化，取消监听，以后再说。。。）
		this.getDocument().addDocumentListener(new DocumentListener()
		{
			@Override
			public void removeUpdate(DocumentEvent e)
			{
				// TODO Auto-generated method stub
				// System.out.println("remove");
				if (isChanged == false)
				{
					isChanged = true;
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e)
			{
				// TODO Auto-generated method stub
				// System.out.println("insert");
				if (isChanged == false)
				{
					isChanged = true;
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e)
			{
				// TODO Auto-generated method stub
				// System.out.println("changed");
				if (isChanged == false)
				{
					isChanged = true;
				}
			}
		});

		// 监听 undo and redo
		undoManager = new UndoManager();
		this.getDocument().addUndoableEditListener(undoManager);
	}
}
