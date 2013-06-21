/**
 * @author Randy
 * @date 2013-03-27
 * @function Handle XML Files 
 */
package compilers.tool;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.*;

import compilers.javabean.ErrorMessageBean;

public class HandleXMLFile
{
	// define variables
	Document doc = null;

	/**
	 * constructor
	 */
	public HandleXMLFile(File xmlFile)
	{
		doc = new XMLConn(xmlFile).getDoc();
	}

	/**
	 * 得到关键字表中的关键字流
	 * 
	 * @return keywords String
	 */
	public String getKeywordsString()
	{
		String keywordsStr = ",";

		NodeList nodeList = doc.getElementsByTagName("word");
		int len = nodeList.getLength();
		for (int i = 0; i < len; i++)
		{
			Element element = (Element) nodeList.item(i);
			keywordsStr += element.getFirstChild().getNodeValue() + ",";
		}
		return keywordsStr;
	}

	/**
	 * 得到关键字表(HashMap)
	 * 
	 * @return keywordsHashMap
	 */
	public HashMap<String, Integer> getKeywordsHashMap()
	{
		HashMap<String, Integer> keywordsHashMap = new HashMap<String, Integer>();
		NodeList kwNodeList = doc.getElementsByTagName("kw");
		int kwLen = kwNodeList.getLength();
		String keyword = null;
		int kwId = 0;
		Element element = null;
		for (int i = 0; i < kwLen; i++)
		{
			element = (Element) kwNodeList.item(i);
			keyword = element.getElementsByTagName("word").item(0)
					.getFirstChild().getNodeValue();
			kwId = Integer.parseInt(element.getElementsByTagName("id").item(0)
					.getFirstChild().getNodeValue());
			keywordsHashMap.put(keyword, kwId);
		}
		return keywordsHashMap;
	}

	/**
	 * 根据errorCode，得到具体的错误信息，可以用 getElementById（）优化
	 * 
	 * @return error_info
	 */
	public String getErrorMsg(String errorCode)
	{
		NodeList nodeList = doc.getElementsByTagName("error");
		int len = nodeList.getLength();
		for (int i = 0; i < len; i++)
		{
			Element e1 = (Element) nodeList.item(i);
			Node e2 = e1.getElementsByTagName("code").item(0);
			if (e2.getFirstChild().getNodeValue().equals(errorCode))
			{
				Node e3 = e1.getElementsByTagName("error_info").item(0);
				return e3.getFirstChild().getNodeValue();
			}
		}
		return "错误，没有此类型错误的详细信息";
	}

	/**
	 * 得到一个XMl文件的指定节点下的元素（用于显示）
	 * 
	 * @return 拼成 HTML table 的字符串
	 */
	public String getElementTable(String element, String[] subElements)
	{
		String tableStr = "<table border=1 style=\"font-size: 16;margin-left:25;border-color: lightblue;background-color:#CCE8CF\">";
		tableStr += "<tr><th> " + subElements[0] + " </th><th> "
				+ subElements[1] + " </th></tr>";
		NodeList nodeList = doc.getElementsByTagName(element);
		int len = nodeList.getLength();
		for (int i = 0; i < len; i++)
		{
			Element e = (Element) nodeList.item(i);
			tableStr += "<tr>";
			for (int j = 0; j < subElements.length; j++)
			{
				Node n = e.getElementsByTagName(subElements[j]).item(0);
				tableStr += "<td> &nbsp;" + n.getFirstChild().getNodeValue()
						+ " &nbsp; </td>";
			}
		}
		tableStr += "</table>";
		return tableStr;
	}
}
