import java.util.ArrayList;

import javax.swing.tree.TreeNode;


public class JudgeClause
{
	ArrayList<Clause> KBClause,ClauseClause;
	public JudgeClause(ArrayList<Clause> KBClause, ArrayList<Clause> ClauseClause)
	{
		this.KBClause = KBClause;
		this.ClauseClause = ClauseClause;
	}
	//conString 标志语句是否蕴含在KB库中
	public static String[] conString = new String[]{"ENTAILED","NOT ENTAILED"};
	ArrayList<ArrayList<ConvertToTree.TreeNode> >totalList = new ArrayList<>();
	
	//两个子句进行规约
	public ArrayList<ArrayList<ConvertToTree.TreeNode>> PL_Resolve(ArrayList<ConvertToTree.TreeNode> clause1,ArrayList<ConvertToTree.TreeNode> clause2)
	{
		ArrayList<ArrayList<ConvertToTree.TreeNode>> tmp = new ArrayList<ArrayList<ConvertToTree.TreeNode>>();
		//ArrayList<ConvertToTree.TreeNode> arr = new ArrayList<ConvertToTree.TreeNode>();
		for (ConvertToTree.TreeNode treeNode1 : clause1)
		{
			for (ConvertToTree.TreeNode treeNode2 : clause2)
			{
				if(treeNode1.index == treeNode2.index &&(treeNode1.type ^ treeNode2.type) == 1)
				{
					ArrayList<ConvertToTree.TreeNode> New = new ArrayList<ConvertToTree.TreeNode>();
					for (ConvertToTree.TreeNode treeNode3 : clause1)
					{
						if(treeNode3.type != treeNode1.type || treeNode3.index != treeNode1.index)
							New.add(treeNode3);
					}
					
					for (ConvertToTree.TreeNode treeNode3 : clause2)
					{
						if(treeNode3.type != treeNode2.type || treeNode3.index != treeNode2.index)
							New.add(treeNode3);
					}
					tmp.add(New);
				}
			}
		}
		return tmp;
	}
	
	//判断语句中是否有空子句
	public boolean hasEmptyClause(ArrayList<ArrayList<ConvertToTree.TreeNode>> list)
	{
		for (ArrayList<ConvertToTree.TreeNode> arrayList : list)
		{
			if(arrayList.size() == 0)
				return true;
		}
		return false;
	}
	
	//比较两个子句是否是同一个
	public boolean compareTwoVector(ArrayList<ConvertToTree.TreeNode> vector1,ArrayList<ConvertToTree.TreeNode> vector2)
	{	
		for (ConvertToTree.TreeNode treeNode1 : vector1)
		{
			boolean b = false;
			for (ConvertToTree.TreeNode treeNode2 : vector2)
			{
				if(treeNode1.index == treeNode2.index && treeNode1.type == treeNode2.type)
				{
					b = true;
					break;
				}
			}
			if(b == false)
				return b;
		}
		
		for (ConvertToTree.TreeNode treeNode1 : vector2)
		{
			boolean b = false;
			for (ConvertToTree.TreeNode treeNode2 : vector1)
			{
				if(treeNode1.index == treeNode2.index && treeNode1.type == treeNode2.type)
				{
					b = true;
					break;
				}
			}
			if(b == false)
				return b;
		}
		return true;
	}
	
	//把两个语句合并
	public boolean combine(ArrayList<ArrayList<ConvertToTree.TreeNode>>list1,ArrayList<ArrayList<ConvertToTree.TreeNode>>list2)
	{
		int s = list2.size();
		for (ArrayList<ConvertToTree.TreeNode> arrayList1 : list1)
		{
			boolean b = false;
			for(int i = 0;i<s;i++)
			{
				if(compareTwoVector(arrayList1, list2.get(i)) == true)
				{
					b = true;
					break;
				}
			}
			if(b == false)
				list2.add(arrayList1);
		}
		if(s == list2.size())
			return false;
		return true;
	}
	
	//归并算法
	public boolean PL_Resolution()
	{
		totalList.clear();
		for (Clause clause : KBClause)
		{
			for (ArrayList<ConvertToTree.TreeNode> arrayList : clause.clause)
			{
				totalList.add(arrayList);
			}
		}
		
		while(true)
		{
			ArrayList<ArrayList<ConvertToTree.TreeNode>> New = new ArrayList<ArrayList<ConvertToTree.TreeNode>>();
			for(int i = 0;i<totalList.size();i++)
				for(int j = i + 1;j<totalList.size();j++)
				{
					ArrayList<ArrayList<ConvertToTree.TreeNode>> tmp = PL_Resolve(totalList.get(i),totalList.get(j));
					if(hasEmptyClause(tmp))
						return true;
					combine(tmp,New);
				}
			if(combine(New,totalList) == false)
				return false;
		}
	}
	
	
	public void judge()
	{
		for (Clause clause : ClauseClause)
		{
			KBClause.add(clause);
			if(PL_Resolution())
				System.out.println(conString[0]);
			else
				System.out.println(conString[1]);
			KBClause.remove(KBClause.size()-1);
		}
	}
}
