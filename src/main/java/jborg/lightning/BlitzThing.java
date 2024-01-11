package jborg.lightning;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import someMath.CollectionManipulation;

import static jborg.lightning.LatticeGrid.*;


/**
 * JavaFX App
 */

public class BlitzThing extends Application
{

	LatticeTileGridCanvas canvas;

	int tileSize;
	int widthInTiles;
	int heightInTiles;
	double strokeWidthLattice;
	int nrOfLattices;
	Point start, end;
	Set<Snake> snakeSet = new HashSet<>();
	
	public BlitzThing()
	{
		super();
	}
		
    @Override
    public void start(Stage stage) throws LTGCException, SnakeException
    {
        
    	Parameters params = getParameters();
    	Map<String, String> namedParams = params.getNamed();
    	
        tileSize = Integer.parseInt(namedParams.get("tileSize"));      
        widthInTiles = Integer.parseInt(namedParams.get("width"));
        heightInTiles = Integer.parseInt(namedParams.get("height"));
        strokeWidthLattice = Double.parseDouble(namedParams.get("strokeWidth"));
        nrOfLattices = Integer.parseInt(namedParams.get("nrL"));
        int xStart = Integer.parseInt(namedParams.get("xStart"));
        int yStart = Integer.parseInt(namedParams.get("yStart"));
        int xEnd = Integer.parseInt(namedParams.get("xEnd"));
        int yEnd = Integer.parseInt(namedParams.get("yEnd"));
        
        start = new Point(xStart, yStart);
        end = new Point(xEnd, yEnd);
        
        canvas = new LatticeTileGridCanvas(widthInTiles, heightInTiles, tileSize, strokeWidthLattice, Color.BLUE);
       
        int absolutWidth = canvas.getAbsolutWidthInPixels();
        int absolutHeight = canvas.getAbsolutHeightInPixels();
        
        Group root = new Group();
        Scene scene = new Scene(root, absolutWidth, absolutHeight, Color.GRAY);
        
        
        root.getChildren().add(canvas);
        
        stage.setScene(scene);
        stage.show();
        
      	Snake firstSnake = new Snake(start, Snake.readyStatus);
        snakeSet.add(firstSnake);

        Platform.runLater(()->
        {
			try
			{
				setupLTGCanvas(nrOfLattices);
				canvas.drawWholeCanvas();
			}
			catch (LTGCException e)
			{
				e.printStackTrace();
			} catch (SnakeException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
    }

    private void setupLTGCanvas(int nrOfLattices) throws LTGCException, SnakeException
    {
    	markStartAndEnd();
    	chooseWhereToDrawLattice();
    }
    
    
    private void markStartAndEnd() throws LTGCException
    {
    	canvas.setColorOnTile(Color.GREEN, start.x, start.y);
    	canvas.setColorOnTile(Color.RED, end.x, end.y);
    }
    
    private void chooseWhereToDrawLattice() throws LTGCException
    {

    	int nrOfInternPossibleLattices = 2*widthInTiles*heightInTiles-widthInTiles-heightInTiles;
    	System.out.println("Nr. of Possible Intern Lattices: " + nrOfInternPossibleLattices);
    	System.out.println("Nr. of Factual Intern Lattices: " + nrOfLattices);
    	System.out.println("Percentage: " + ((double)(nrOfLattices)/(nrOfInternPossibleLattices)));

    	List<Integer> possibleLatticeNrs = new ArrayList<>();
    	for(int n=0;n<nrOfInternPossibleLattices;n++)possibleLatticeNrs.add(n);

    	List<Integer> actualLatticeNrs = new ArrayList<>();
    	for(int n=0;n<nrOfLattices;n++)
    	{
    		int k = CollectionManipulation.catchRandomElementOfList(possibleLatticeNrs);
    		int i = possibleLatticeNrs.indexOf(k);
    		possibleLatticeNrs.remove(i);
    		actualLatticeNrs.add(k);
    	}
    	System.out.println("ActualLattices: "+ actualLatticeNrs.size());
    	for(int n=0;n<actualLatticeNrs.size();n++)System.out.print(", " + actualLatticeNrs.get(n));
    	System.out.println("");
    	
    	int nrOfBottomLattices = widthInTiles*(heightInTiles-1);
    	System.out.println("Nr. of Bottoms: " + nrOfBottomLattices);
    	int nrOfRightLattices = (widthInTiles-1)*heightInTiles;
    	System.out.println("Nr. of Rights: " + nrOfRightLattices);
    	
    	int cnt = 0;
    	for(int n=0;n<actualLatticeNrs.size();n++)
    	{
    		
    		int k = actualLatticeNrs.get(n);
    		
    		if(k<nrOfBottomLattices)
    		{
    			int l = k % widthInTiles;
    			int h = (k/widthInTiles);
    			canvas.setOneLattice(l, h, indexLatticeBitBottom);
    			System.out.println("Cnt = " + cnt + "; Bottom(" + l + ", " + h +")");
    		}
    		else
    		{
    			int m = k-nrOfBottomLattices;
    			
    			int l = (m/heightInTiles);
    			int h = m % heightInTiles;
    			canvas.setOneLattice(l, h, indexLatticeBitRight);
    			System.out.println("Cnt = " + cnt + "; Right(" + l + ", " + h +")");
    		}
    		cnt++;
    	}
    	
    	System.out.println("Cnt: " + cnt);
    }
    

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}