/**
 * @author Randy
 * @date 2013-05-27
 * @function 树节点
 */
package compilers.util;

public class TreeNode
{
	// 成员
	public TreeElem elem;
	
	// -1 代表无
	public int index;
	public int father;
	public int firstChild;
	public int nextSibling;
	
	/**
	 * constructor 
	 */
	public TreeNode()
	{
		elem = new TreeElem();
	}
	
	/**
	 * constructor @param index
	 * constructor @param father
	 * constructor @param firstChild
	 * constructor @param nextSibling
	 */
	public TreeNode(int father, int firstChild,
			int nextSibling)
	{
		elem = new TreeElem();
		this.father = father;
		this.firstChild = firstChild;
		this.nextSibling = nextSibling;
	}

	/**
	 * constructor @param elem
	 * constructor @param father
	 * constructor @param firstChild
	 * constructor @param nextSibling
	 */
	public TreeNode(TreeElem elem, int father, int firstChild, int nextSibling)
	{
		this.elem = elem;
		this.father = father;
		this.firstChild = firstChild;
		this.nextSibling = nextSibling;
	}

	public TreeElem getElem()
	{
		return elem;
	}

	public void setElem(TreeElem elem)
	{
		this.elem = elem;
	}

	public int getFather()
	{
		return father;
	}

	public void setFather(int father)
	{
		this.father = father;
	}

	public int getFirstChild()
	{
		return firstChild;
	}

	public void setFirstChild(int firstChild)
	{
		this.firstChild = firstChild;
	}

	public int getNextSibling()
	{
		return nextSibling;
	}

	public void setNextSibling(int nextSibling)
	{
		this.nextSibling = nextSibling;
	}
}
