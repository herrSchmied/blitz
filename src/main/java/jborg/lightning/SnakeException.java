package jborg.lightning;

public class SnakeException extends Exception 
{

	private static final long serialVersionUID = -8672725683143843637L;
	private final String msg;
	
	public SnakeException(String msg)
	{
		this.msg = msg;
		System.out.println(msg);
	}
	
	public String getMessage()
	{
		return msg;
	}
}
