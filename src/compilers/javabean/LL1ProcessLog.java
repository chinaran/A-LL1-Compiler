/**
 * @author Randy
 * @date 2013-06-02
 * @function 记录LL1分析过程而产生的表格
 */
package compilers.javabean;

public class LL1ProcessLog 
{
	public String symbolStack = null;
	public String currentSymbol = null;
	public String inputString = null;
	public String instruction = null;
	

	// constructor
	public LL1ProcessLog(String symbolStack, String currentSymbol,
			String inputString, String instruction) {
		this.symbolStack = symbolStack;
		this.currentSymbol = currentSymbol;
		this.inputString = inputString;
		this.instruction = instruction;
	}
	
	public LL1ProcessLog() {
		this.symbolStack = "";
		this.currentSymbol = "";
		this.inputString = "";
		this.instruction = "";
	}
	
	public String getSymbolStack() {
		return symbolStack;
	}
	public void setSymbolStack(String symbolStack) {
		this.symbolStack = symbolStack;
	}
	public String getCurrentSymbol() {
		return currentSymbol;
	}
	public void setCurrentSymbol(String currentSymbol) {
		this.currentSymbol = currentSymbol;
	}
	public String getInputString() {
		return inputString;
	}
	public void setInputString(String inputString) {
		this.inputString = inputString;
	}
	public String getInstruction() {
		return instruction;
	}
	public void setInstruction(String instruction) {
		this.instruction = instruction;
	}
	
	// 返回 html 表格形式的 LL1 Log
	public String getLL1Log_html()
	{
		String out = "<tr>";
		out += "<td>&nbsp;" + symbolStack + "</td>";
		out += "<td align=center>" + currentSymbol + "</td>";
		out += "<td align=right>" + inputString + " </td>";
		out += "<td>&nbsp;" + instruction + "</td>";
		out += "</tr>";
		return out;
	}
	// 返回 html 表格形式的 LL1 Log 的表头
	public static String getLL1Log_tablehead()
	{
		return "<tr><th> 符号栈 </th><th>当前输入符号</th><th> 输入串 </th><th> 说明 </th></tr>";
	}
}
