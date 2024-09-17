package jborg.lightning.exceptions;

public class LTGCException extends Exception
{

	private static final long serialVersionUID = 8171992163754012686L;
	
	String msg;
	
	public LTGCException(String msg)
	{
		this.msg = msg;
		System.out.println(msg);
	}
}
