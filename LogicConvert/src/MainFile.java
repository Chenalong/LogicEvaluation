import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class MainFile
{
	public ArrayList<ConvertToNodeList> convertToNodeList = new ArrayList<ConvertToNodeList>();
	
	public ArrayList<ConvertToTree> convertToTreeList = new ArrayList<ConvertToTree>();
	
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
			convertToTreeList.get(convertToTreeList.size()-1).convertToTree(convertToNodeList.get(i).arrayList);
			convertToTreeList.get(convertToTreeList.size()-1).printTree(null);
			System.out.println();
			
		}
	}
	
	public static void main(String[] args) throws IOException
	{
		String fileName = "kb.txt";
		MainFile mainFile = new MainFile();
		mainFile.convertToList(fileName);
		mainFile.convertToTree();
		
		System.out.println("it is sucessful");
	}
}
