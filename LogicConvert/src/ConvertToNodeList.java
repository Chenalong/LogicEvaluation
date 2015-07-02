import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.NotDirectoryException;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

public class ConvertToNodeList
{
	/*
	 * AND type = 1
	 * OR type = 2
	 * NOT type = 3
	 * => type = 4
	 * <=> type = 5
	 */
	public class nodeClass
	{
		public int type;//type = 1表示属于逻辑符号  type = 2 表示属于操作数
		public int index; //逻辑符号1-5  操作数从1开始标号  用map记录标号和字符的对应关系
		public nodeClass(int type,int index)
		{
			this.type = type;
			this.index = index;
		}
	}
	
	//定义一些变量
	public static HashMap<String, Integer> map = new HashMap<String,Integer>();
	public static HashMap<String, Integer> logicMap = new HashMap<String,Integer>();
	
	public static int mapMaxIndex = 1;
	public ArrayList<nodeClass> arrayList = new ArrayList<nodeClass>();
	
	public ConvertToNodeList()
	{
		logicMap.put("AND", 1);	logicMap.put("OR", 2);
		logicMap.put("NOT", 3);	logicMap.put("=>", 4);
		logicMap.put("<=>", 5);
		
	}
	
	public static String fromIndexToString(HashMap<String,Integer> map,int index)
	{
		for (String tmpString : map.keySet())
		{
			if(map.get(tmpString).intValue() == index)
				return tmpString;
		}
		return "hello_world";
	}
	
	public void PrintStatement()
	{
		for(int i = 0;i<arrayList.size();i++)
		{
			if(arrayList.get(i).type == 1)
				System.out.print(fromIndexToString(logicMap, arrayList.get(i).index) + " ");
			else
				System.out.print(fromIndexToString(map, arrayList.get(i).index) + " ");
		}
		System.out.println();
	}
	
	public void parseStringToNode(String parseString)
	{
		//System.out.println(parseString);
		int index = 0;
		for(int i = 0;i<parseString.length();)
		{
			if(parseString.charAt(i) == ')' || parseString.charAt(i) == '(' 
					|| parseString.charAt(i) == ' ')
			{
				i++;
			}
			else
			{
				int j = i;
				while(j < parseString.length() && parseString.charAt(j) != ' ' && parseString.charAt(j) != '(' && parseString.charAt(j) != ')')j++;
				String tmpString = parseString.substring(i, j);
				if(logicMap.containsKey(tmpString))
				{
					arrayList.add(new nodeClass(1, logicMap.get(tmpString)));
					i = j;
				}
				else 
				{
					if(map.containsKey(tmpString) == false)
						map.put(tmpString, mapMaxIndex++);
					arrayList.add(new nodeClass(2, map.get(tmpString)));
					i = j;
				}
			}
			
		}
	}

}
