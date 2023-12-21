package jborg.lightning;

import javafx.application.Application;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

import javafx.stage.Stage;


/**
 * JavaFX App
 */
public class BlitzThing extends Application
{

    @Override
    public void start(Stage stage) throws LTGCException 
    {
        
        int tileSize = 50;
        
        int widthInTiles = 5;
        int heightInTiles = 5;
        double strokeWidthLattice = 3.5;
        
        LatticeTileGridCanvas canvas = new LatticeTileGridCanvas(widthInTiles, heightInTiles, tileSize, strokeWidthLattice);

        int absolutWidth = canvas.getAbsolutWidthInPixels();
        int absolutHeight = canvas.getAbsolutHeightInPixels();
        
        Group root = new Group();
        Scene scene = new Scene(root, absolutWidth, absolutHeight, Color.GRAY);
        
        
        canvas.setColorOnTile(Color.GREEN, 0, 0);
        canvas.setColorOnTile(Color.YELLOW, 1, 1);
        canvas.setColorOnTile(Color.BLUE, 2, 2);
        canvas.setLatticeOnTile(2, 2, 15, Color.BROWN);
        canvas.setLatticeOnTile(0, 0, 7, Color.RED);
        canvas.setLatticeOnTile(1, 1, 2, Color.BROWN);
        
        root.getChildren().add(canvas);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}