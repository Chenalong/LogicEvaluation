import java.util.ArrayList;
import java.util.Stack;

public class ConvertToTree
{

	public class TreeNode
	{
		public TreeNode left, right;
		public int type; // type = 1表示属于逻辑符号 type = 2 表示属于操作数
		public int index; // 逻辑符号1-5 操作数从1开始标号 用map记录标号和字符的对应关系
		public int ti;

		public TreeNode(TreeNode left, TreeNode right, int type, int index, int ti)
		{
			this.left = left;
			this.right = right;
			this.type = type;
			this.index = index;
			this.ti = ti;
		}
	}

	public TreeNode root = null;

	public void convertToTree(ArrayList<ConvertToNodeList.nodeClass> arrayList)
	{
		Stack<TreeNode> logicStack = new Stack<TreeNode>();
		Stack<TreeNode> operateStack = new Stack<TreeNode>();
		TreeNode tmp = null;
		int type, index;
		TreeNode logicNodeTmp = null;
		TreeNode operator1 = null;
		TreeNode operator2 = null;
		for (int i = 0; i < arrayList.size(); i++)
		{
			type = arrayList.get(i).type;
			index = arrayList.get(i).index;
			tmp = new TreeNode(null, null, type, index, i);
			if (type == 1) // 是逻辑运算符
			{
				logicStack.push(tmp);
			} 
			else
			{
				operateStack.push(tmp);
				while (logicStack.size()>0)
				{
					logicNodeTmp = logicStack.lastElement();
					if (logicNodeTmp.index == 3 && operateStack.size() >= 1)
					{
						operator1 = operateStack.pop();
						logicNodeTmp = logicStack.pop();
						if (logicNodeTmp.ti < operator1.ti)
						{
							logicNodeTmp.left = operator1;
							logicNodeTmp.ti = operator1.ti;
							operateStack.push(logicNodeTmp);
						} 
						else
						{
							operateStack.push(operator1);
							logicStack.push(logicNodeTmp);
							break;
						}
					} 
					else if (logicNodeTmp.index != 3 && operateStack.size() >= 2)
					{
						operator1 = operateStack.pop();
						operator2 = operateStack.pop();
						logicNodeTmp = logicStack.pop();
						if (logicNodeTmp.ti < operator1.ti && logicNodeTmp.ti < operator2.ti)
						{
							logicNodeTmp.left = operator1;
							logicNodeTmp.right = operator2;
							logicNodeTmp.ti = operator1.ti;
							operateStack.push(logicNodeTmp);
						} 
						else
						{
							operateStack.push(operator2);
							operateStack.push(operator1);
							logicStack.push(logicNodeTmp);
							break;
						}
					} 
					else
					{
						break;
					}
				}
			}
		}
		root = operateStack.pop();

		if (operateStack.isEmpty() == false || logicStack.isEmpty() == false)
		{
			System.out.println("----------it is false");
			//System.out.println();
		}
}
	
	public void printTree(TreeNode father)
	{
		if(father == null)
			father = root;
		if(father.type == 1)
			System.out.print(ConvertToNodeList.fromIndexToString(ConvertToNodeList.logicMap, father.index) + " ");
		else
			System.out.print(ConvertToNodeList.fromIndexToString(ConvertToNodeList.map, father.index) + " ");
		
		if(father.right !=null)
			printTree(father.right);
		
		if(father.left != null)
			printTree(father.left);
	}
}
