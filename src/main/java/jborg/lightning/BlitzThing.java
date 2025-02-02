package jborg.lightning;


//*********************************************
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import static consoleTools.TerminalXDisplay.*;

import static guiTools.Input.*;
import static guiTools.Output.*;


import javafx.application.Application;
import javafx.application.Platform;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javafx.stage.Stage;
import jborg.lightning.exceptions.LTGCException;
import jborg.lightning.exceptions.SnakeException;
import someMath.exceptions.CollectionException;

import someMath.CollectionManipulation;


import static jborg.lightning.LatticeGrid.*;
//********************************************************


/**
 * JavaFX App
 * Provides a Stage with some Buttons and TextFields for
 * Lattice-Tile-Grid-Canvas.(LTGC).
 */

public class BlitzThing extends Application
{

	int tileSize = 20;
	int widthInTiles = 3;
	int heightInTiles = 3;
	double strokeWidthLattice = 3.5;
	int nrOfLattices;
	
	Point start = new Point(0, 0);
	Point end = new Point(widthInTiles-1, heightInTiles-1);
	
	Set<Snake> snakeSet = new HashSet<>();
	
	LatticeTileGridCanvas canvas;


    VBox questionBox = new VBox();

    HBox heightBox = new HBox();
    Label heightLbl = new Label("Height");
    TextField heightTxtField = new TextField("" + heightInTiles + "");

    HBox widthBox = new HBox();
    Label widthLbl = new Label("Width");
    TextField widthTxtField = new TextField("" + widthInTiles + "");

    int latticeNr = 2;
    HBox latticeBox = new HBox();
    Label latticeLbl = new Label("Nr. of Lattices");
    TextField latticeTxtField = new TextField("" + latticeNr + "");

    int minXStart = 0;
    int maxXStart = 0;
    int minYStart = 0;
    int maxYStart = 0;
    
    int minXEnd = 1;
    int maxXEnd = 1;
    int minYEnd = 1;
    int maxYEnd = 1;
  
    HBox startPointBox = new HBox();
    Button newStartPointBtn = new Button("Different start Point");
    Label startPointDisplay = new Label("Current start Point (" +  start.x + ", " + start.y + ")");

    HBox endPointBox = new HBox();
    Button newEndPointBtn = new Button("Different end Point");
    Label endPointDisplay = new Label("Current end Point (" +  (widthInTiles-1) + ", " + (heightInTiles-1) + ")");

    HBox startBox = new HBox();
    Button startBtn = new Button("Start");

    /**
     * Constructor
     * 
     */
	public BlitzThing()
	{
		super();
	}

	/**
	 * Responsible for setting up The primary-Stage.
	 */
    @Override
    public void start(Stage stage) throws LTGCException, SnakeException
    {
               
        int absolutWidth = 340;
        int absolutHeight = 200;
        	//questionBox = root
        Scene scene = new Scene(questionBox, absolutWidth, absolutHeight, Color.GRAY);

        heightBox.setMaxWidth(Double.MAX_VALUE);
        
        heightLbl.setPrefWidth(300);
        heightTxtField.setPrefWidth(40);
        heightBox.getChildren().add(heightLbl);
        heightBox.getChildren().add(heightTxtField);

        widthBox.setMaxWidth(Double.MAX_VALUE);
        widthLbl.setPrefWidth(300);
        widthTxtField.setPrefWidth(40);
        widthBox.getChildren().add(widthLbl);
        widthBox.getChildren().add(widthTxtField);
        
        latticeBox.setMaxWidth(Double.MAX_VALUE);
        latticeLbl.setPrefWidth(300);
        latticeTxtField.setPrefWidth(40);
        latticeBox.getChildren().add(latticeLbl);
        latticeBox.getChildren().add(latticeTxtField);

        startPointBox.setMaxWidth(Double.MAX_VALUE);
        newStartPointBtn.setOnAction((e)->
        {
        	getInput();
        	maxXStart = widthInTiles-1;
        	maxYStart = heightInTiles-1;
        	start.x = getIntInput("X-Coordinate", "Give it an Integer Value.", minXStart, maxXStart);
        	start.y = getIntInput("Y-Coordinate", "Integer Value.", minYStart, maxYStart);
        	
        	startPointDisplay.setText("Current start Point (" +  start.x + ", " + start.y + ")");
        });
        
        newStartPointBtn.setPrefWidth(150);
        startPointDisplay.setPrefWidth(190);
        startPointBox.getChildren().add(newStartPointBtn);
        startPointBox.getChildren().add(startPointDisplay);
        
        endPointBox.setMaxWidth(Double.MAX_VALUE);
        newEndPointBtn.setOnAction((e)->
        {
        	getInput();
        	maxXEnd = widthInTiles-1;
        	maxYEnd = heightInTiles-1;
        	        	
        	end.x = getIntInput("X-Coordinate", "Give it an Integer Value.", minXEnd, maxXEnd);
        	end.y = getIntInput("Y-Coordinate", "Integer Value.", minYEnd, maxYEnd);
        	
        	endPointDisplay.setText("Current start Point (" +  end.x + ", " + end.y + ")");
        });
        
        newEndPointBtn.setPrefWidth(150);
        endPointDisplay.setPrefWidth(190);
        endPointBox.getChildren().add(newEndPointBtn);
        endPointBox.getChildren().add(endPointDisplay);
        
        startBox.setMaxWidth(Double.MAX_VALUE);
        startBtn.setPrefWidth(340);
        startBox.getChildren().add(startBtn);
        
        questionBox.getChildren().add(heightBox);
        questionBox.getChildren().add(widthBox);
        questionBox.getChildren().add(latticeBox);
        questionBox.getChildren().add(startPointBox);
        questionBox.getChildren().add(endPointBox);
        questionBox.getChildren().add(startBox);
        
        startBtn.setOnAction((actionEvent)->
        {
        	
        	if(!getInput())return;

        	int maxLattices = 2*widthInTiles*heightInTiles-widthInTiles-heightInTiles;
        		
       		if(nrOfLattices>maxLattices)
       		{
       			errorAlert("To many Lattices.");
       			return;
       		}
        		
       		if(nrOfLattices<0)
       		{
       			errorAlert("Nr. of Lattices can't be below Zero");
        		return;
        	}
        	
        	try
        	{
				showCanvasStage(widthInTiles, heightInTiles, nrOfLattices);

				Thread.sleep(250);				
			}
        	catch (LTGCException | SnakeException | CollectionException | InterruptedException | IOException e)
        	{
				e.printStackTrace();
			}
        });

        stage.setScene(scene);
        stage.show();
        
    }    
    
    private void chooseWhereToDrawLattice(int width, int height, int latticeNr) throws LTGCException, CollectionException
    {

    	int nrOfInternPossibleLattices = 2*width*height-width-height;
    	if(latticeNr>=nrOfInternPossibleLattices)throw new IllegalArgumentException("Much to many Lattices!");

    	printBoldAndBlue("Nr. of Possible Intern Lattices: " + nrOfInternPossibleLattices);
    	printBoldAndBlue("Nr. of Factual Intern Lattices: " + nrOfLattices);
    	printBoldAndBlue("Percentage: " + ((double)(latticeNr)/(nrOfInternPossibleLattices)));

    	List<Integer> possibleLatticeNrs = new ArrayList<>();
    	for(int n=0;n<nrOfInternPossibleLattices;n++)possibleLatticeNrs.add(n);

    	List<Integer> actualLatticeNrs = new ArrayList<>();
    	for(int n=0;n<latticeNr;n++)
    	{
    		int k = CollectionManipulation.catchRandomElementOfList(possibleLatticeNrs);
    		int i = possibleLatticeNrs.indexOf(k);
    		possibleLatticeNrs.remove(i);
    		actualLatticeNrs.add(k);
    	}

    	printBoldAndBlue("ActualLattices: "+ actualLatticeNrs.size());
    	for(int n=0;n<actualLatticeNrs.size();n++)System.out.print(", " + actualLatticeNrs.get(n));
    	System.out.println("");
    	
    	int nrOfTopLattices = width*(height-1);
    	int nrOfRightLattices = (width-1)*height;
    	
    	int cnt = 0;
    	for(int n: actualLatticeNrs)
    	{

    		if(n<nrOfTopLattices)
    		{
    			
    			int x = (int)Math.floor(n/width);
    			int y = n % (width-1)+1;
    			
				canvas.setOneLattice(x, y, indexLatticeBitTop);

    		}
    		else
    		{
    			int m = n-nrOfTopLattices;
    			
    			int x = m % (height-1);
    			int y = (int)Math.floor(m/height);

    			canvas.setOneLattice(x, y, indexLatticeBitRight);
   
    		}
    		cnt++;
    	}
	
    	printBoldAndBlue("Count: " + cnt);
    }


    /**
     * Graphic and LTGC Setup.
     * 
     * @param width Canvas width in Tiles.
     * @param height Canvas height in Tiles.
     * @param latticeNr How many Lattices on the Grid/Canvas?
     * @throws LTGCException Shouldn't.
     * @throws SnakeException Shouldn't.
     * @throws CollectionException Shouldn't.
     * @throws InterruptedException Shouldn't.
     * @throws IOException Shouldn't
     */
    public void showCanvasStage(int width, int height, int latticeNr) throws LTGCException, SnakeException, CollectionException, InterruptedException, IOException
    {

    	Group root = new Group();

        Snake snake = new Snake(start, Snake.readyStatus);
        canvas = new LatticeTileGridCanvas(width, height, end, snake);
        root.getChildren().add(canvas);
        chooseWhereToDrawLattice(width, height, latticeNr);

        SnakeAndLatticeGrid snlGrid = canvas.getSNLGrid();
        snlGrid.setFinalSnakes();
        Set<Snake> successSnakes = snlGrid.filterSuccesses();

        Snake sSnake = null;

        try
        {
			sSnake = CollectionManipulation.catchRandomElementOfSet(successSnakes);
		}
        catch(CollectionException e)
        {
			System.out.println(e.getMessage());
		}

        if(sSnake!=null)System.out.println("one of them:\n" + sSnake);

        System.out.println("Successes: " + successSnakes.size());
        Stage stage = new Stage();
        Scene scene = new Scene(root, canvas.getAbsolutWidthInPixels(), canvas.getAbsolutHeightInPixels(), Color.GREY);
        stage.setScene(scene);

        canvas.drawWholeCanvas();

        stage.show();
    }

    private boolean getInput()
    {
    	
    	try
    	{
    		widthInTiles = Integer.parseInt(widthTxtField.getText());
			heightInTiles = Integer.parseInt(heightTxtField.getText());
			nrOfLattices = Integer.parseInt(latticeTxtField.getText());
    	}
    	catch(NumberFormatException e)
    	{
       		errorAlert("Nr's aint valide");
    		return false;
    	}
    	
		return true;
    }

    /**
     * Entry Point. It starts here.
     * @param args Theoretical Input.
     */
    public static void main(String[] args)
    {
        Application.launch(args);
    }
}