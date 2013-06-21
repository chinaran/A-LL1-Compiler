/**
 * @author Randy
 * @date 2013-05-27
 * @function 树中元素的结构
 */
package compilers.util;

public class TreeElem
{	
	// 成员
	public int val = -1; // 综合属性，此为idTable中的index
	public int inh = -1; // 继承属性，同上
	public String type = null; // id 的类型，此为 int 或 floatS
	
	public Object token;
	
	/**
	 * constructor 
	 */
	public TreeElem()
	{
	}

	/**
	 * constructor @param token
	 */
	public TreeElem(Object token)
	{
		this.token = token;
	}

	/**
	 * constructor @param val
	 * constructor @param inh
	 * constructor @param type
	 * constructor @param token
	 */
	public TreeElem(int val, int inh, String type, Object token)
	{
		this.val = val;
		this.inh = inh;
		this.type = type;
		this.token = token;
	}
}
