/**
 * @author Randy
 * @date 2013-03-27
 * @function to connect xml parse
 */
package compilers.tool;

import org.w3c.dom.*;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class XMLConn
{
	// define variable
	File xmlFile = null;
	Document doc = null;

	/**
	 * constructor
	 */
	public XMLConn(File xmlFile)
	{
		this.xmlFile = xmlFile;
	}
	
	/**
	 * get document and return it
	 * 
	 * @return xml document
	 */
	public Document getDoc()
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = null;
		try
		{
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(xmlFile);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return doc;
	}
}
