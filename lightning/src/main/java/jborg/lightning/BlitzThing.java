package jborg.lightning;


//*********************************************
import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


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
import javafx.util.Pair;
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

	Point start = new Point(0, 0);
	Point end;
	
    int minXStart = 0;
    int maxXStart = 0;
    int minYStart = 0;
    int maxYStart = 0;
    
    int minXEnd = 1;
    int maxXEnd = 1;
    int minYEnd = 1;
    int maxYEnd = 1;
  

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
        
    	//For Tests.
    	Integer widthInTiles = 5;
    	Integer heightInTiles = 5;
    	Integer nrOfLattices = 2;
    	
        HBox heightBox = new HBox();
        Label heightLbl = new Label("Height");
        TextField heightTxtField = new TextField("" + heightInTiles + "");
        
        HBox widthBox = new HBox();
        Label widthLbl = new Label("Width");
        TextField widthTxtField = new TextField("" + widthInTiles + "");

        int absolutWidth = 340;
        int absolutHeight = 200;

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
        
        int latticeNr = 2;
        HBox latticeBox = new HBox();
        Label latticeLbl = new Label("Nr. of Lattices");
        TextField latticeTxtField = new TextField("" + latticeNr + "");

        latticeBox.setMaxWidth(Double.MAX_VALUE);
        latticeLbl.setPrefWidth(300);
        latticeTxtField.setPrefWidth(40);
        latticeBox.getChildren().add(latticeLbl);
        latticeBox.getChildren().add(latticeTxtField);

        HBox startPointBox = new HBox();
        Button newStartPointBtn = new Button("Different start Point");
        Label startPointDisplay = new Label("Current start Point (" +  start.x + ", " + start.y + ")");

        newStartPointBtn.setOnAction((e)->
        {
        	try
        	{
        		setupInput(widthInTiles, heightInTiles, nrOfLattices, widthTxtField, heightTxtField, latticeTxtField);
         	}
        	catch(NumberFormatException nfe)
        	{
           		errorAlert("Nr's aint valide");

        	}

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
        
        HBox endPointBox = new HBox();
        Button newEndPointBtn = new Button("Different end Point");
        end = new Point(widthInTiles-1, heightInTiles-1);

        Label endPointDisplay = new Label("Current end Point (" +  (widthInTiles-1) + ", " + (heightInTiles-1) + ")");
        
        newEndPointBtn.setOnAction((e)->
        {
        	try
        	{
        		setupInput(widthInTiles, heightInTiles, nrOfLattices, widthTxtField, heightTxtField, latticeTxtField);
                end = new Point(widthInTiles-1, heightInTiles-1);
        	}
        	catch(NumberFormatException nfe)
        	{
           		errorAlert("Nr's aint valide");
        	}

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
        
        HBox startBox = new HBox();
        Button startBtn = new Button("Start");
        startPointBox.setMaxWidth(Double.MAX_VALUE);
        endPointBox.setMaxWidth(Double.MAX_VALUE);
        startBox.setMaxWidth(Double.MAX_VALUE);

        startBtn.setPrefWidth(340);
        startBox.getChildren().add(startBtn);
        
        VBox questionBox = new VBox();

        
        questionBox.getChildren().add(heightBox);
        questionBox.getChildren().add(widthBox);
        questionBox.getChildren().add(latticeBox);
        questionBox.getChildren().add(startPointBox);
        questionBox.getChildren().add(endPointBox);
        questionBox.getChildren().add(startBox);
        
        startBtn.setOnAction((actionEvent)->
        {
        	
        	try
        	{
        		setupInput(widthInTiles, heightInTiles, nrOfLattices, widthTxtField, heightTxtField, latticeTxtField);
        	}
        	catch(NumberFormatException e)
        	{
           		errorAlert("Nr's aint valide");
        	}


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

			Platform.runLater(()->
			{
				
				try
				{
					
					//For Tests.
					showCanvasStage(widthInTiles, heightInTiles, nrOfLattices);
				}
				catch(LTGCException | SnakeException | CollectionException | InterruptedException
							| IOException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			});

        });

    	//questionBox = root/
        Scene scene = new Scene(questionBox, absolutWidth, absolutHeight, Color.GRAY);

        stage.setScene(scene);
        stage.show();
        
    }    
    
    private void setupInput(Integer widthInTiles, Integer heightInTiles, Integer nrOfLattices, 
    		TextField widthTxtField, TextField heightTxtField, TextField latticeTxtField)
    {
   			widthInTiles = Integer.parseInt(widthTxtField.getText());
			heightInTiles = Integer.parseInt(heightTxtField.getText());
			nrOfLattices = Integer.parseInt(latticeTxtField.getText());

    }

    private void setupLattices(int width, int height, int latticeNr, LTGCS canvas) throws LTGCException, CollectionException
    {

    	int nrOfInternPossibleLattices = 2*width*height-width-height;
    	if(latticeNr>=nrOfInternPossibleLattices)throw new IllegalArgumentException("Much to many Lattices!");

    	System.out.println(formatBashStringBoldAndBlue("Nr. of Possible Intern Lattices: " + nrOfInternPossibleLattices));
    	System.out.println(formatBashStringBoldAndBlue("Nr. of Factual Intern Lattices: " + latticeNr));
    	System.out.println(formatBashStringBoldAndBlue("Percentage: " + ((double)(latticeNr)/(nrOfInternPossibleLattices))));

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

    	System.out.println(formatBashStringBoldAndBlue("ActualLattices: "+ actualLatticeNrs.size()));
    	for(int n=0;n<actualLatticeNrs.size();n++)System.out.print(", " + actualLatticeNrs.get(n));
    	System.out.println("");
    	
    	
    	Set<Pair<Point, Integer>> allPositions = poolOfPossibleLatticePositions(canvas.getLatticeGrid());
    	
    	Set<Pair<Point, Integer>> chosenPositions = new HashSet<>();
    	
    	while(chosenPositions.size()<latticeNr)
    	{
    		Pair<Point, Integer> position = CollectionManipulation.catchRandomElementOfCollection(allPositions);
    		chosenPositions.add(position);
    	}
    	
    	for(Pair<Point, Integer> position: chosenPositions)
    	{
    		Point p = position.getKey();
    		int bitNr= position.getValue();
    		
    		canvas.setOneLattice(p, bitNr);
    	}

    	int cnt = 0;
    	
	
    	System.out.println(formatBashStringBoldAndBlue("Count: " + cnt));
    }


    public Set<Pair<Point, Integer>> poolOfPossibleLatticePositions(LatticeGrid lg)
    {
    	
    	Set<Pair<Point, Integer>> pool = new HashSet<>();
    	int leftBitNr = 0;
    	int bottomBitNr = 1;
    	lg.walkThruTiles((p)->
    	{
    		
    		Pair<Point, Integer> position;
    		if(p.x>0)
    		{
   				position = new Pair(p, leftBitNr);
   				pool.add(position);
   			}
      				
    		if(p.y>0)
    		{
   				position = new Pair(p, bottomBitNr);
   				pool.add(position);
   			}
    	});

    	return pool;
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
    	
    	LTGCS canvas = new LTGCS(width, height, end, snake);
        root.getChildren().add(canvas);
        setupLattices(width, height, latticeNr, canvas);
       
        Stage stage = new Stage();
        Scene scene = new Scene(root, canvas.getAbsolutWidthInPixels(), canvas.getAbsolutHeightInPixels(), Color.GREY);
        stage.setScene(scene);

       	canvas.drawEverythingExeceptAnySnake();
       	stage.show();
    }

    public void setupTheSnakes(int width, int height, int latticeNr, LTGCS canvas) throws LTGCException, CollectionException, SnakeException
    {
    	
        setupLattices(width, height, latticeNr, canvas);

        canvas.setFinalSnakes();
        Set<Snake> successSnakes = canvas.filterSuccesses();
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