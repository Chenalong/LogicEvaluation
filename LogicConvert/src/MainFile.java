import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;


public class MainFile
{
	public ArrayList<ConvertToNodeList> convertToNodeList = new ArrayList<ConvertToNodeList>();
	
	public ArrayList<ConvertToTree> convertToTreeList = new ArrayList<ConvertToTree>();
	
	public ArrayList<Clause> clauseList = new ArrayList<Clause>();
	
	public void convertToList(String fileName) throws IOException
	{
		File file = new File(fileName);
		BufferedReader buffer = new BufferedReader(new FileReader(file));
		String tmpString;
		while((tmpString = buffer.readLine())!= null)
		{
			if(tmpString.length() >=1)
			{
				convertToNodeList.add(new ConvertToNodeList());
				convertToNodeList.get(convertToNodeList.size()-1).parseStringToNode(tmpString);
				convertToNodeList.get(convertToNodeList.size()-1).PrintStatement();
				
			}
		}
	}
	
	public void convertToTree()
	{
		for(int i= 0;i<convertToNodeList.size();i++)
		{
			convertToTreeList.add(new ConvertToTree());
			convertToTreeList.get(convertToTreeList.size()-1).converToTree(convertToNodeList.get(i).arrayList);
			convertToTreeList.get(convertToTreeList.size()-1).changeToConjunctionFunction();
			convertToTreeList.get(convertToTreeList.size()-1).printTree(null);
			System.out.println("");
			//clauseList.add(new Clause());
			//clauseList.get(clauseList.size()-1).fromTreeToClause(convertToTreeList.get(convertToTreeList.size()-1).root);
		}
	}
	
	public void convertToClause()
	{
		for(ConvertToTree tmp:convertToTreeList)
		{
			clauseList.add(new Clause());
			clauseList.get(clauseList.size()-1).fromTreeToClause(tmp.root);
			//clauseList.get(clauseList.size()-1).deleteSameAtom();
		}
	}
	//VALID、SATISFIABLE和UNSATISFIABLE
	public void JudgeType()
	{
		int maxIndexMap = ConvertToNodeList.MaxIndexForMap();
		System.out.printf("-----the maxIndexMap is %d\n", maxIndexMap);
		boolean VALID = true;
		boolean SATISFIABLE = false;
		for(int i = 0;i<(1<<maxIndexMap);i++)
		{
			boolean b = true;
			for(Clause tmp:clauseList)
			{
				if(tmp.clauseIsTrue(i) == false)
				{
					b = false;
					break;
				}	
			}
			if(b == false)
				VALID = false;
			if(b == true)
				SATISFIABLE = true;
		}
		if(VALID)
			System.out.println("The KB is VALID");
		else if(SATISFIABLE)
			System.out.println("The KB is SATISFIABLE");
		else 
			System.out.println("The KB is UNSATISFIABLE");
			
	}
	
	public void PrintClause()
	{
		for (Clause clause : clauseList)
		{
			clause.printClause();
		}
	}
	public static void main(String[] args) throws IOException
	{
		
		//对于知识库的处理
//		String KBFileName = "kbTest.txt";
		String KBFileName = "kb.txt";
		MainFile KBMainFile = new MainFile();
		KBMainFile.convertToList(KBFileName);
		KBMainFile.convertToTree();
		KBMainFile.convertToClause();
		//System.out.printf("the clause num is %d\n", KBMainFile.clauseList.get(0).clause.size());
		KBMainFile.JudgeType();
		KBMainFile.PrintClause();
		System.out.println("KB it is sucessful");
		
		//对于知识库的处理
		String ClauseFileName = "clause.txt";
//		String ClauseFileName = "testClause.txt";
		MainFile ClauseMainFile = new MainFile();
		ClauseMainFile.convertToList(ClauseFileName);
		ClauseMainFile.convertToTree();
		ClauseMainFile.convertToClause();
		//System.out.printf("the clause num is %d\n", KBMainFile.clauseList.get(0).clause.size());
		ClauseMainFile.JudgeType();
		ClauseMainFile.PrintClause();
		System.out.println("Clause it is sucessful");
		
		JudgeClause judgeClause = new JudgeClause(KBMainFile.clauseList, ClauseMainFile.clauseList);
		
		judgeClause.judge();
		
	}
}
