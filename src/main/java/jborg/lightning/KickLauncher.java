package jborg.lightning;

import javafx.application.Application;

public class KickLauncher
{

	public static void main(String ...args)
	{
		
		String [] args2 = new String[9];
		args2[0]= "--width=2";
		args2[1]= "--height=2";
        args2[2]= "--tileSize=30";
        args2[3]= "--strokeWidth=3.5";
        args2[4]= "--nrL=2";
        args2[5]= "--xStart=0";
        args2[6]= "--yStart=0";
        args2[7]= "--xEnd=1";
        args2[8]= "--yEnd=1";
		
        Application.launch(BlitzThing.class, args2);
	}
}