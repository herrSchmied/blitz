package jborg.lightning;

import static guiTools.Input.getIntInput;
import static guiTools.Output.errorAlert;

import java.awt.Point;
import java.io.IOException;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import someMath.exceptions.CollectionException;
import someMath.exceptions.LTGCException;
import someMath.exceptions.SnakeException;

public class AnotherBlitz extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		
	    int width = 50;
	    int height = 50;

        Canvas canvas = new Canvas(width, height);
        ActOnCanvas aoc;
        
        HBox hBox = new HBox();
        hBox.getChildren().add(canvas);
        
         
        Scene scene = new Scene(hBox);

        primaryStage.setScene(scene);
        primaryStage.show();
        
        Point start = new Point(0, 0);
        Point end = new Point(width-1, height-1);
        aoc = new ActOnCanvas(canvas, start, end);
        
	}

    public static void main(String[] args)
    {
        Application.launch(args);
    }

}
