package jborg.lightning.exceptions;


/**
 * Exception which gets thrown when something with
 * Lattice-Grid-Canvas went wrong.
 */
public class LTGCException extends Exception
{

	private static final long serialVersionUID = 8171992163754012686L;
	
	/**
	 * Info about what went wrong.
	 */
	String msg;
	
	/**
	 * Constructor.
	 * 
	 * @param msg Info.
	 */
	public LTGCException(String msg)
	{
		this.msg = msg;
		System.out.println(msg);
	}
}
