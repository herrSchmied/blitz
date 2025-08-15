package jborg.lightning.exceptions;

public class LSException extends Exception
{

	private final String msg;
	
	public LSException(String msg)
	{
		this.msg = msg;
	}
	
	public String getMessage()
	{
		return msg;
	}
}
