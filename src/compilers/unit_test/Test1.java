/**
 * @author Randy
 * @date Mar 9, 2012
 * @function 
 */
package compilers.unit_test;

import java.util.HashSet;
import java.util.Hashtable;

import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import javax.swing.text.Keymap;

public class Test1
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		HashSet<String> hashSet = new HashSet<String>();
		hashSet.contains(null);
		Hashtable<String, String> hashtable = new Hashtable<String, String>();
		String s1 = hashtable.put("1", "1");
		String s2 = hashtable.put("1", "2");
		System.out.println("s1=" + s1);
		System.out.println("s2=" + s2);
		System.out.println("hashtable.size()=" + hashtable.size());



	}

	public static void addIndentFunction(final JTextArea textArea)
	{
		Keymap parent = textArea.getKeymap();
		Keymap newMap = JTextComponent.addKeymap(textArea.getName(), parent);
		// 增加缩进newMap.addActionForKeyStroke(KeyStroke.getKeyStroke("pressed TAB"),
		// new
		// AbstractAction(){@Overridepublic
		// void
		// actionPerformed(
		// ActionEvent
		// e
		// ){//
		// 获得选中文本String
		// selection
		// =
		// textArea.getSelectedText();//
		// 未曾选中文本直接插入制表符if(
		// selection
		// ==
		// null
		// )textArea.insert("/t",
		// textArea.getCaretPosition());//
		// 有选中文本对选中行举行缩进else{//
		// 将选中文本按行瓜分String[]
		// lines
		// =
		// selection.split("/r?/n");if(
		// lines.length
		// ==
		// 0
		// )return;StringBuilder
		// builder
		// =
		// new
		// StringBuilder();//
		// 先为第一行以外的每行行首增加一个缩进builder.append(lines[0]);for(
		// int
		// i
		// =
		// 1;
		// i
		// <
		// lines.length;
		// i++
		// )builder.append(NEWLINE).append("/t").append(lines[i]);//
		// 缩进后选中局部的开始位置:
		// 坚持选中本来开始位置的字符//
		// 因为行首将会多一个缩进,
		// 因而位置后移一位来到达收获int
		// selectionStart
		// =
		// textArea.getSelectionStart()
		// +
		// 1;//
		// 缩进后选中局部的告终位置:
		// 坚持选中本来告终位置的字符//
		// 把开始位置向后偏移
		// 选中局部添置缩进后的长度int
		// selectionEnd
		// =
		// selectionStart
		// +
		// builder.length();try{//
		// ---
		// 处理选中局部第一行行首的缩进
		// ://
		// 此变量用于存储第一行的行首位置,
		// 初始值:
		// 选中局部开始位置的前一个位置int
		// lineHead
		// =
		// textArea.getSelectionStart()
		// -
		// 1;//
		// 寻找上一行行末;
		// 并将第一行中未选中局部增加进来,
		// 以缩进第一行while(
		// lineHead
		// >=
		// 0
		// &&
		// !离子风机textArea.getText(lineHead,
		// 1).equals("/n")
		// ){builder.insert(0,
		// textArea.getText(lineHead,
		// 1));lineHead--;}//
		// 确定行首位置lineHead++;//
		// 为第一行行首增加缩进builder.insert(0,
		// "/t");//
		// 将
		// 第一行行首到原选中局部告终位置
		// 轮换为已增加缩进的处理收获textArea.replaceRange(builder.toString(),
		// lineHead,
		// textArea.getSelectionEnd());//
		// 设置处理后的选中局部textArea.setSelectionStart(selectionStart);textArea.setSelectionEnd(selectionEnd);}catch(
		// BadLocationException
		// ex
		// ){Logger.getLogger(TextComponentUtils.class.getName()).log(Level.SEVERE,
		// null,
		// ex);}}}});//
		// 剔除缩进newMap.addActionForKeyStroke(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
		// Event.SHIFT_MASK),
		// new
		// AbstractAction(){@Overridepublic
		// void
		// actionPerformed(
		// ActionEvent
		// e
		// ){//
		// 获得选中文本String
		// selection
		// =
		// textArea.getSelectedText();//
		// 未曾选中文本什么事也不干if(
		// selection
		// ==
		// null
		// )return;//
		// 将选中文本按行瓜分String[]
		// lines
		// =
		// selection.split("/r?/n");if(
		// lines.length
		// ==
		// 0
		// )return;StringBuilder
		// builder
		// =
		// new
		// StringBuilder();//
		// 先剔除第一行以外每行行首se.seopu.com的一个缩进for(
		// int
		// i
		// =
		// 1;
		// i
		// <
		// lines.length;
		// i++
		// ){builder.append(NEWLINE);if(
		// lines[i].startsWith("/t")
		// )builder.append(lines[i].substring(1));else
		// if(
		// lines[i].startsWith(" ")
		// ){int
		// start
		// =
		// 0;//
		// 四个空格算作一个缩进while(
		// start
		// <
		// lines[i].length()
		// &&
		// lines[i].charAt(start)
		// ==
		// ' '
		// &&
		// start
		// <
		// 4
		// )start++;builder.append(lines[i].substring(start));}elsebuilder.append(lines[i]);}try{//
		// ---
		// 处理选中局部第一行行首的缩进
		// ://
		// 此变量用于存储第一行的行首位置,
		// 初始值:
		// 选中局部开始位置的前一个位置int
		// lineHead
		// =
		// textArea.getSelectionStart()
		// -
		// 1;//
		// 寻找上一行行末;
		// 并将第一行中未选中局部增加进来,
		// 以剔除第一行的缩进while(
		// lineHead
		// >=
		// 0
		// &&
		// !textArea.getText(lineHead,
		// 1).equals("/n")
		// )lineHead--;//
		// 确定行首位置lineHead++;//
		// 剔除缩进后选中局部的开始位置:
		// 坚持选中本来开始位置的字符int
		// selectionStart
		// =
		// textArea.getSelectionStart();//
		// 缩进后选中局部的告终位置:
		// 坚持选中本来告终位置的字符//
		// 把开始位置向后偏移
		// 选中局部剔除缩进后的长度int
		// selectionEnd
		// =
		// selectionStart
		// +
		// builder.length()
		// +
		// lines[0].length();String
		// firstLine
		// =
		// textArea.getText(lineHead,
		// textArea.getSelectionStart()
		// -
		// lineHead)
		// +
		// lines[0];//
		// 为第一行行首剔除缩进if(
		// firstLine.startsWith("/t")
		// ){//
		// 依据剔除的缩进调剂选中局部位置selectionStart--;selectionEnd--;builderhttp://www.soyes168.com.insert(0,
		// firstLine.substring(1));}else
		// if(
		// firstLine.startsWith(" ")
		// ){int
		// start
		// =
		// 0;//
		// 四个空格算作一个缩进while(
		// start
		// <
		// firstLine.length()
		// &&
		// firstLine.charAt(start)
		// ==
		// ' '
		// &&
		// start
		// <
		// 4
		// ){//
		// 依据剔除的缩进调剂选中局部位置selectionStart--;selectionEnd--;start++;}builder.insert(0,
		// firstLine.substring(start));}elsebuilder.insert(0,
		// firstLine);//
		// 将
		// 第一行行首到原选中局部告终位置
		// 轮换为已增加缩进的处理收获textArea.replaceRange(builder.toString(),
		// lineHead,
		// textArea.getSelectionEnd());//
		// 设置处理后的选中局部textArea.setSelectionStart(selectionStart);textArea.setSelectionEnd(selectionEnd);}catch(
		// BadLocationException
		// ex
		// ){Logger.getLogger(TextComponentUtils.class.getName()).log(Level.SEVERE,
		// null,
		// ex);}}});textArea.setKeymap(newMap);}
	}

}


