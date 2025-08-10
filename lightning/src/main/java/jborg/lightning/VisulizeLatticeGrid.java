package jborg.lightning;



import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import jborg.lightning.LTGCS;
import jborg.lightning.LatticeSetup;
import someMath.exceptions.LTGCException;
import someMath.exceptions.SnakeException;
import someMath.pathFinder.LatticeGrid;

public class VisulizeLatticeGrid extends Application
{



	public void Horizontal() throws LTGCException, SnakeException
	{
		
		LTGCS ltgcs = LTGCS.getCorneredVersion(5, 5);

		LatticeGrid lg = ltgcs.getLatticeGrid();

		LatticeSetup ls = new LatticeSetup(lg);;

		
		Group root = new Group();
		
		ls.setHorizontalBarsWithOneHole();
		ltgcs.drawEverythingExeceptAnySnake();
		ltgcs.drawLattices();
		
        root.getChildren().add(ltgcs);
       
        Stage stage = new Stage();
        Scene scene = new Scene(root, ltgcs.getAbsolutWidthInPixels(), ltgcs.getAbsolutHeightInPixels(), Color.GREY);
        stage.setScene(scene);


       	stage.show();
	}

	public void Vertical() throws LTGCException, SnakeException
	{
		
		LTGCS ltgcs = LTGCS.getCorneredVersion(5, 5);

		LatticeGrid lg = ltgcs.getLatticeGrid();

		LatticeSetup ls = new LatticeSetup(lg);;
		

		Group root = new Group();
		
		ls.setVerticalBarsWithOneHole();
		ltgcs.drawEverythingExeceptAnySnake();
		
        root.getChildren().add(ltgcs);
       
        Stage stage = new Stage();
        Scene scene = new Scene(root, ltgcs.getAbsolutWidthInPixels(), ltgcs.getAbsolutHeightInPixels(), Color.GREY);
        stage.setScene(scene);


       	stage.show();
	}

	@Override
	public void start(Stage primaryStage) throws Exception
	{

		Canvas canvas = new Canvas(10, 10);
		Group root = new Group();		

        root.getChildren().add(canvas);
       
        Scene scene = new Scene(root, 10, 10, Color.GREY);
        primaryStage.setScene(scene);


       	primaryStage.show();
       	
       	Horizontal();
       	Vertical();
	}
	
	public static void main(String[] args)
	{
		Application.launch(args);
	}
}