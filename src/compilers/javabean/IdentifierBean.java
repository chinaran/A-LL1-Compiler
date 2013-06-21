/**
 * @author Randy
 * @date 2013-04-27
 * @function Identifier Table
 */
package compilers.javabean;

public class IdentifierBean
{
	String name = null;
	String type = null;
	int address = 0;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getType()
	{
		return type;
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public int getAddress()
	{
		return address;
	}

	public void setAddress(int address)
	{
		this.address = address;
	}

	/**
	 * Constructor
	 */
	public IdentifierBean(String name, String type, int address)
	{
		this.name = name;
		this.type = type;
		this.address = address;
	}
}
