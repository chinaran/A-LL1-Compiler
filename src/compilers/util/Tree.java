/**
 * @author Randy
 * @date 2013-05-27
 * @function 树结构
 */
package compilers.util;

import java.util.ArrayList;

public class Tree
{
	public ArrayList<TreeNode> nodes = null;

	/**
	 * constructor
	 */
	public Tree()
	{
		nodes = new ArrayList<TreeNode>();
	}

	/**
	 * 添加节点
	 * 
	 * @param node
	 * @return 节点索引 index
	 */
	public int addNode(TreeNode node)
	{
		nodes.add(node);
		int index = nodes.size() - 1;
		node.index = index;
		return index;
	}
}
