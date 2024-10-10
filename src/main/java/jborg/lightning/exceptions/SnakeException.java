package jborg.lightning.exceptions;

/**
 * Exception which gets thrown when arguments for Snake Method or
 * Snake Constructor violate the Rules.
 */
public class SnakeException extends Exception 
{

	private static final long serialVersionUID = -8672725683143843637L;
	
	/**
	 * Info about what went wrong.
	 */
	private final String msg;
	
	/**
	 * Constructor
	 * 
	 * @param msg Info.
	 */
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
