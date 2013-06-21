/**
 * @author Randy
 * @date Mar 9, 2012
 * @function Test XML
 */
package compilers.unit_test;

import java.io.File;

import org.w3c.dom.*;

import compilers.tool.HandleXMLFile;
import compilers.tool.XMLConn;

public class TestXML
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// HandleXMLFile handleXMLFile = new HandleXMLFile();
		// System.out.println(handleXMLFile.getKeywordsString());
//		File xmlFile = new File("data/ErrorMessage.xml");
//
//		Document doc = new XMLConn(xmlFile).getDoc();
////		Element element= doc.getDocumentElement();
////		Element e = ((Document) element).getElementById("A0002");
////		NodeList node = doc.getElementsByTagName("errors_lexical_analysis");
////		Node n = node.item(0);
////		Element e = n.get
//		Element e = doc.getElementById("1");
//		if (e == null)
//		{
//			System.out.println("null");
//		}
//		else
//		{
//
//			System.out.println(e.getNodeName());
//		}
		HandleXMLFile handleXMLFile = new HandleXMLFile(new File("data/ErrorMessage.xml"));
		System.out.println(handleXMLFile.getErrorMsg("A0005"));

	}

}
