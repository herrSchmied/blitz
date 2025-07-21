package jborg.lightning;


import java.awt.Point;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class AnotherBlitz extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		
	    int width = 100;
	    int height = 100;

        Canvas canvas = new Canvas(width, height);
        ActOnCanvas aoc;
        
        HBox hBox = new HBox();
        hBox.getChildren().add(canvas);
        
         
        Scene scene = new Scene(hBox);

        primaryStage.setScene(scene);
        primaryStage.show();
        
        Point start = new Point(0, 0); Point end = new Point(width-1, height-1);
		/* 
		 * aoc = new ActOnCanvas(canvas, start, end);
		 */        
	}

    public static void main(String[] args)
    {
        Application.launch(args);
    }
}
