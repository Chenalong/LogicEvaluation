import java.util.ArrayList;



public class Clause
{
	ArrayList<ArrayList<ConvertToTree.TreeNode> > clause = new ArrayList<ArrayList<ConvertToTree.TreeNode> >();
	
	ArrayList<ConvertToTree.TreeNode> tmpArrayList = new ArrayList<ConvertToTree.TreeNode>();
	
	public void construcClause(ConvertToTree.TreeNode root,int type)  // 表示是否带NOT
	{
		System.out.printf("int the construClause the type is %d and the index is %d\n", root.type,root.index);
		if(root.type == 2)
		{
			root.type = type;
			tmpArrayList.add(root);
			return ;
		}
		if(root.left !=null)
		{
			if(root.index == 3)
				construcClause(root.left,1);
			else
				construcClause(root.left, 0);
		}
		if(root.right!=null)
		{
			if(root.index == 3)
				construcClause(root.right,1);
			else
				construcClause(root.right, 0);
		}
	}
	
	public void fromTreeToClause(ConvertToTree.TreeNode root)
	{
		//System.out.printf("the type is %d and the index is %d\n", root.type,root.index);
		if(root.type!=1 || root.index != 1)
		{
			tmpArrayList.clear();
			construcClause(root,0); //0： 表示不带NOT 1：表示带NOT
			ArrayList<ConvertToTree.TreeNode> tmp = new ArrayList<ConvertToTree.TreeNode>();
			for(ConvertToTree.TreeNode convertToTree:tmpArrayList)
			{
				tmp.add(convertToTree);
			}
			clause.add(tmp);
		}
		else 
		{
			if(root.left !=null)
				fromTreeToClause(root.left);
			if(root.right!=null)
				fromTreeToClause(root.right);
		}
	}
	
	public boolean clauseIsTrue(int ValueTable)
	{
		int index,val;
		for (ArrayList<ConvertToTree.TreeNode> arrayList : clause)
		{
			boolean b = false;
			for(ConvertToTree.TreeNode tmp:arrayList)
			{
				index = tmp.index - 1;
				val = (ValueTable&(1<<index));
				if(val != 0) val = 1;
				if((val^ tmp.type) == 1)
				{
					b = true;
					break;
				}
			}
			if(b == false)
				return false;
		}
		return true;
	}
}
