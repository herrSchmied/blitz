package jborg.lightning;

/**
 * Info
 */
public class SystemInfo
{
	
	/**
	 * Empty Constructor
	 */
	public SystemInfo()
	{
		
	}

	/**
	 * Self Explanatory.
	 * @return java Version.
	 */
    public static String javaVersion()
    {
        return System.getProperty("java.version");
    }

    /**
     * Self Explanatory.
     * @return javaFX Version.
     */
    public static String javafxVersion()
    {
        return System.getProperty("javafx.version");
    }
}