/**
 * @author Randy
 * @date 2013-04-27
 * @function Error Message Table
 */
package compilers.javabean;

public class ErrorMessageBean
{
	protected String errorCode = null;
	protected String errorWord = null;
	protected int errorLine = 0;
	
	public String getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(String errorCode)
	{
		this.errorCode = errorCode;
	}

	public String getErrorWord()
	{
		return errorWord;
	}

	public void setErrorWord(String errorWord)
	{
		this.errorWord = errorWord;
	}

	public int getErrorLine()
	{
		return errorLine;
	}

	public void setErrorLine(int errorLine)
	{
		this.errorLine = errorLine;
	}
	
	/**
	 * constructor
	 */
	public ErrorMessageBean(String errorCode, String errorWord, int errorLine)
	{
		this.errorCode = errorCode;
		this.errorWord = errorWord;
		this.errorLine = errorLine;
	}
}
