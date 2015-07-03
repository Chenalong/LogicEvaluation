import java.util.ArrayList;
import java.util.Stack;

import javax.swing.plaf.basic.BasicBorders.RadioButtonBorder;

public class ConvertToTree
{
	ArrayList<ConvertToNodeList.nodeClass> arrayList;
		
	
	public class TreeNode
	{
		public TreeNode left, right;
		public int type; // type = 1表示属于逻辑符号 type = 2 表示属于操作数
		public int index; // 逻辑符号1-5 操作数从1开始标号 用map记录标号和字符的对应关系

		public TreeNode(TreeNode left, TreeNode right, int type, int index)
		{
			this.left = left;
			this.right = right;
			this.type = type;
			this.index = index;
		}
	}
	boolean isTrue = true;
	public TreeNode root = null;
	public int cnt = 0;
	//前缀表达式生成子句数递归的写法
	public TreeNode dfs()
	{
		TreeNode tmp = new TreeNode(null, null, arrayList.get(cnt).type, arrayList.get(cnt).index);
		if(arrayList.get(cnt).type==1)
		{
			if(arrayList.get(cnt).index == 3)
			{
				cnt++;
				tmp.left = dfs();
			}
			else
			{
				cnt++;
				tmp.left = dfs();
				cnt++;
				tmp.right = dfs();
			}
		}
		return tmp;
	}
	
	public void converToTree(ArrayList<ConvertToNodeList.nodeClass> arrayList)
	{
		this.arrayList = arrayList;
		root = dfs();
	}
	
	//打印树的结构
	public void printTree(TreeNode father)
	{
		if(father == null)
			father = root;
		if(father.type == 1)
			System.out.print(ConvertToNodeList.fromIndexToString(ConvertToNodeList.logicMap, father.index) + " ");
		else
			System.out.print(ConvertToNodeList.fromIndexToString(ConvertToNodeList.map, father.index) + " ");
		
		if(father.left !=null)
			printTree(father.left);
		
		if(father.right != null)
			printTree(father.right);
	}
	
	//copy整颗以root的子树
	public TreeNode copyNode(TreeNode root)
	{
		TreeNode tmp = new TreeNode(root.left, root.right, root.type, root.index);
		if(tmp.left!=null)
			tmp.left = copyNode(tmp.left);
		if(tmp.right !=null)
			tmp.right = copyNode(tmp.right);
		return tmp;
	}
	
	//把<=>转换为=>
	
	//把逻辑表达式转换为合取式
	public void changeToConjunctionFunction()
	{
		change5(null, root, 1);
		change4(null, root, 1);
		change3(null, root, 1);
		//前缀逻辑表达式AND OR AND A B AND A1 B1 OR AND A2 B2 AND A3 B3
		//转换后的结果AND AND AND OR A1 A OR B1 A AND OR A1 B OR B1 B AND AND OR A3 A2 OR B3 A2 AND OR A3 B2 OR B3 B2
		
		//前缀逻辑表达式OR OR AND A B AND A1 B1 OR AND A2 B2 AND A3 B3
		//转换后的结果AND AND AND AND OR OR A3 A2 OR A1 A OR OR B3 A2 OR A1 A AND OR OR A3 B2 OR A1 A OR OR B3 B2 OR A1 A AND AND OR OR A3 A2 OR B1 A OR OR B3 A2 OR B1 A AND OR OR A3 B2 OR B1 A OR OR B3 B2 OR B1 A AND AND AND OR OR A3 A2 OR A1 B OR OR B3 A2 OR A1 B AND OR OR A3 B2 OR A1 B OR OR B3 B2 OR A1 B AND AND OR OR A3 A2 OR B1 B OR OR B3 A2 OR B1 B AND OR OR A3 B2 OR B1 B OR OR B3 B2 OR B1 B 
		
		while(isTrue)
		{
			isTrue = false;
			change2(null, root, 1);
		}
		
		
	}
	
	public void change5(TreeNode father,TreeNode current,int direct)
	{
		if(current.type != 1 || current.index!=5)
		{
			if(current.left !=null)
				change5(current, current.left,1);
			if(current.right != null)
				change5(current, current.right,2);
		}
		else 
		{
			TreeNode tmp = new TreeNode(null, null, 1, 1);
			TreeNode leftNode = new TreeNode(null, null, 1, 4);
			TreeNode rightNode = new TreeNode(null, null, 1, 4);
			TreeNode copyA = copyNode(current.left);
			TreeNode copyB = copyNode(current.right);
			//更新father节点
			if(father == null)
				root = tmp;
			else
			{
				if(direct ==1)
					father.left = tmp;
				else
					father.right = tmp;
			}
			
			//更新and节点
			tmp.left = leftNode;
			tmp.right = rightNode;
			
			//更新左边=>节点
			leftNode.left = current.left;
			leftNode.right = current.right;
			
			//更新右边=>节点
			rightNode.left = copyB;
			rightNode.right = copyA;
			
			if(tmp.left !=null)
				change5(tmp, tmp.left, 1);
			if(tmp.right !=null)
				change5(tmp, tmp.right, 2);
		}	
	}

	//把=>转换为not(a) and b
	public void change4(TreeNode father,TreeNode current,int direct)
	{
		if(current.type != 1 || current.index !=4)
		{
			if(current.left !=null)
				change4(current, current.left,1);
			if(current.right != null)
				change4(current, current.right,2);
		}
		else 
		{
			TreeNode tmp = new TreeNode(null, null, 1, 2);
			TreeNode notNode = new TreeNode(null, null, 1, 3);
			
			//更新father节点
			if(father == null)
				root = tmp;
			else
			{
				if(direct ==1)
					father.left = tmp;
				else
					father.right = tmp;
			}
			
			//更新or节点
			tmp.left = notNode;
			tmp.right = current.right;
			
			//更新not=>节点
			notNode.left = current.left;
			
			if(tmp.left !=null)
				change4(tmp, tmp.left, 1);
			if(tmp.right !=null)
				change4(tmp, tmp.right, 2);
		}	
	}

	//把not内移
	public void change3(TreeNode father,TreeNode current,int direct)
	{
		if(current.type != 1 || current.index != 3 || current.left.type==2)
		{
			if(current.left !=null)
				change3(current, current.left,1);
			if(current.right != null)
				change3(current, current.right,2);
		}
		else 
		{
			if(current.left.index == 3)
			{
				if(father == null)
					root = current.left.left;
				else
				{
					if(direct == 1)
						father.left = current.left.left;
					else 	
						father.right = current.left.left;	
				}
				change3(father, current.left.left, direct);
			}
			else 
			{
				TreeNode tmp = new TreeNode(null, null, 1, 2);
				if(current.left.index == 1)
					tmp.index = 2;
				else
					tmp.index = 1;
				
				TreeNode notANode = new TreeNode(null, null, 1, 3);
				TreeNode notBNode = new TreeNode(null, null, 1, 3);
				//更新father节点
				if(father == null)
					root = tmp;
				else
				{
					if(direct ==1)
						father.left = tmp;
					else
						father.right = tmp;
				}
				
				//更新tmp节点
				tmp.left = notANode;
				tmp.right = notBNode;
				//更新notA节点
				notANode.left = current.left.left;
				
				//更新notB节点
				notBNode.left = current.left.right;
				
				if(tmp.left !=null)
					change3(tmp, tmp.left, 1);
				if(tmp.right !=null)
					change3(tmp, tmp.right, 2);
			}
			
		}	
	}
	
	//把or在子句树中下移
	
	public void change2(TreeNode father,TreeNode current,int direct)
	{
		if(current.type != 1 || current.index !=2 ||
				((current.left.type !=1 || current.left.index!=1) && (current.right.type !=1 || current.right.index!=1)))
		{
			if(current.left !=null)
				change2(current, current.left,1);
			if(current.right != null)
				change2(current, current.right,2);
		}
		else 
		{
			isTrue = true;
			TreeNode tmp = new TreeNode(null, null, 1, 1);
			TreeNode orANode = new TreeNode(null, null, 1, 2);
			TreeNode orBNode = new TreeNode(null, null, 1, 2);
			TreeNode NodeA = null;
			TreeNode NodeB = null;
			TreeNode entity = null;
			if(current.left.type ==1 && current.left.index == 1)
			{
				NodeA = current.left.left;
				NodeB = current.left.right;
				entity = current.right;
			}
			else
			{
				NodeA = current.right.left;
				NodeB = current.right.right;
				entity = current.left;
			}
			
			TreeNode copyEntity = copyNode(entity);
			//更新father节点
			if(father == null)
				root = tmp;
			else
			{
				if(direct ==1)
					father.left = tmp;
				else
					father.right = tmp;
			}
			
			//更新tmp节点
			tmp.left = orANode;
			tmp.right = orBNode;
			
			//更新orAnode节点
			orANode.left = NodeA;
			orANode.right = entity;
			
			//更新orBnode节点
			orBNode.left = NodeB;
			orBNode.right = copyEntity;
			
			if(tmp.left !=null)
				change2(tmp, tmp.left, 1);
			if(tmp.right !=null)
				change2(tmp, tmp.right, 2);
		}	
	}
}








