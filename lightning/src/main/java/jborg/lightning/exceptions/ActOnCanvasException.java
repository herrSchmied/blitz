package jborg.lightning.exceptions;


public class ActOnCanvasException extends Exception
{

	private static final long serialVersionUID = -8786907596154568723L;
	private final String msg;
	
	
	public ActOnCanvasException(String msg)
	{
		this.msg = msg;
	}
	
	public String getMessage()
	{
		return msg;
	}


}
