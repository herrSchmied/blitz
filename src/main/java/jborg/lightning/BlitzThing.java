package jborg.lightning;

import java.awt.Point;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import guiTools.Output;

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
import someMath.CollectionException;
import someMath.CollectionManipulation;

import static jborg.lightning.LatticeGrid.*;


/**
 * JavaFX App
 */

public class BlitzThing extends Application
{

	LatticeTileGridCanvas canvas;

	int tileSize = 20;
	int widthInTiles = 3;
	int heightInTiles = 3;
	double strokeWidthLattice = 3.5;
	int nrOfLattices;
	
	Point start = new Point(0, 0);
	Point end;
	
	Set<Snake> snakeSet = new HashSet<>();
	
	public BlitzThing()
	{
		super();
	}
		
    @Override
    public void start(Stage stage) throws LTGCException, SnakeException
    {
               
        int absolutWidth = 340;
        int absolutHeight = 200;
        	//questionBox = root
        VBox questionBox = new VBox();
        Scene scene = new Scene(questionBox, absolutWidth, absolutHeight, Color.GRAY);
        

        HBox heightBox = new HBox();
        heightBox.setMaxWidth(Double.MAX_VALUE);
        Label heightLbl = new Label("Height");
        heightLbl.setPrefWidth(300);
        TextField heightTxtField = new TextField("" + heightInTiles + "");
        heightTxtField.setPrefWidth(40);
        heightBox.getChildren().add(heightLbl);
        heightBox.getChildren().add(heightTxtField);


        HBox widthBox = new HBox();
        widthBox.setMaxWidth(Double.MAX_VALUE);
        Label widthLbl = new Label("Width");
        widthLbl.setPrefWidth(300);
        TextField widthTxtField = new TextField("" + widthInTiles + "");
        widthTxtField.setPrefWidth(40);
        widthBox.getChildren().add(widthLbl);
        widthBox.getChildren().add(widthTxtField);
        
        int latticeNr = 2;
        HBox latticeBox = new HBox();
        latticeBox.setMaxWidth(Double.MAX_VALUE);
        Label latticeLbl = new Label("Nr. of Lattices");
        latticeLbl.setPrefWidth(300);
        TextField latticeTxtField = new TextField("" + latticeNr + "");
        latticeTxtField.setPrefWidth(40);
        latticeBox.getChildren().add(latticeLbl);
        latticeBox.getChildren().add(latticeTxtField);

        HBox startPointBox = new HBox();
        startPointBox.setMaxWidth(Double.MAX_VALUE);
        Button newStartPointBtn = new Button("Different start Point");
        newStartPointBtn.setPrefWidth(150);
        Label startPointDisplay = new Label("Current start Point (" +  start.x + ", " + start.y + ")");
        startPointDisplay.setPrefWidth(190);
        startPointBox.getChildren().add(newStartPointBtn);
        startPointBox.getChildren().add(startPointDisplay);
        
        HBox endPointBox = new HBox();
        endPointBox.setMaxWidth(Double.MAX_VALUE);
        Button newEndPointBtn = new Button("Different end Point");
        newEndPointBtn.setPrefWidth(150);
        Label endPointDisplay = new Label("Current end Point (" +  (widthInTiles-1) + ", " + (heightInTiles-1) + ")");
        endPointDisplay.setPrefWidth(190);
        endPointBox.getChildren().add(newEndPointBtn);
        endPointBox.getChildren().add(endPointDisplay);
        
        HBox startBox = new HBox();
        startBox.setMaxWidth(Double.MAX_VALUE);
        Button startBtn = new Button("Start");
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
        	
        	try
        	{
        		widthInTiles = Integer.parseInt(widthTxtField.getText());
        		heightInTiles = Integer.parseInt(heightTxtField.getText());
        		nrOfLattices = Integer.parseInt(latticeTxtField.getText());
        	}
        	catch(NumberFormatException nfExce)
        	{
        		Output.errorAlert("Nr's aint valide");
        		return;
        	}


        	int maxLattices = 2*widthInTiles*heightInTiles-widthInTiles-heightInTiles;
        		
       		if(nrOfLattices>maxLattices)
       		{
       			Output.errorAlert("To many Lattices.");
       			return;
       		}
        		
       		if(nrOfLattices<0)
       		{
       			Output.errorAlert("Nr. of Lattices can't be below Zero");
        		return;
        	}
        	
        	start = new Point(0,0);
        	end = new Point(widthInTiles-1, heightInTiles-1);
        	
        	try
        	{
				showCanvasStage(widthInTiles, heightInTiles, nrOfLattices);
			}
        	catch (LTGCException | SnakeException | CollectionException e)
        	{
				e.printStackTrace();
			}
        });
        
        stage.setScene(scene);
        stage.show();
        
    }    
    
    private void markStartAndEnd() throws LTGCException
    {
    	canvas.setColorOnTile(Color.GREEN, start);
    	canvas.setColorOnTile(Color.RED, end);
    }

    private void chooseWhereToDrawLattice(int width, int height, int latticeNr) throws LTGCException, CollectionException
    {

    	int nrOfInternPossibleLattices = 2*width*height-width-height;
    	System.out.println("Nr. of Possible Intern Lattices: " + nrOfInternPossibleLattices);
    	System.out.println("Nr. of Factual Intern Lattices: " + nrOfLattices);
    	System.out.println("Percentage: " + ((double)(latticeNr)/(nrOfInternPossibleLattices)));

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
    	System.out.println("ActualLattices: "+ actualLatticeNrs.size());
    	for(int n=0;n<actualLatticeNrs.size();n++)System.out.print(", " + actualLatticeNrs.get(n));
    	System.out.println("");
    	
    	int nrOfTopLattices = width*(height-1);
    	System.out.println("Nr. of Tops: " + nrOfTopLattices);
    	int nrOfRightLattices = (width-1)*height;
    	System.out.println("Nr. of Rights: " + nrOfRightLattices);
    	
    	int cnt = 0;
    	for(int n: actualLatticeNrs)
    	{
    		
    		
    		if(n<nrOfTopLattices)
    		{
    			int x = n % width;
    			int y = (n/width);
    			
				canvas.setOneLattice(x, y, indexLatticeBitTop);
	    		System.out.println("Top(" + x + ", " + y +")");

    		}
    		else
    		{
    			int m = n-nrOfTopLattices;
    			
    			int x = m % height;
    			int y = (m/height);
    			
    			canvas.setOneLattice(x, y, indexLatticeBitRight);
    			System.out.println("Right(" + x + ", " + y +")");
    		}
    		cnt++;
    	}
    	
    	System.out.println("Cnt: " + cnt);
    }
    

    public void showCanvasStage(int width, int height, int latticeNr) throws LTGCException, SnakeException, CollectionException
    {
    	
    	Group root = new Group();


        
        Snake snake = new Snake(start, Snake.readyStatus);
        canvas = new LatticeTileGridCanvas(width, height, end, snake);
        root.getChildren().add(canvas);
        markStartAndEnd();
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
        
        stage.show();
        
    	Thread dhCanvasThrd = new Thread(()->
    	{
				
			for(int n=0;n<7;n++)
			{
				Platform.runLater(()->
				{
					try
					{
						canvas.drawWholeCanvas();
						Thread.sleep(350);
					}
					catch(LTGCException | InterruptedException e)
					{
						e.printStackTrace();
					}
				});
			}
		});
    	dhCanvasThrd.start();
    }
    
    public static void main(String[] args)
    {
        Application.launch(args);
    }
}