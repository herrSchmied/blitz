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
        
        int tileSize = 15;
        
        int widthInTiles = 20;
        int heightInTiles = 20;
        double strokeWidthLattice = 2.5;
        
        LatticeTileGridCanvas canvas = new LatticeTileGridCanvas(widthInTiles, heightInTiles, tileSize, strokeWidthLattice);

        int absolutWidth = canvas.getAbsolutWidthInPixels();
        int absolutHeight = canvas.getAbsolutHeightInPixels();
        
        Group root = new Group();
        Scene scene = new Scene(root, absolutWidth, absolutHeight, Color.GRAY);
        
        boolean latticeBits[] = new boolean[4];
        latticeBits[LatticeTileGridCanvas.indexLatticeBitLeft] = true;
        latticeBits[LatticeTileGridCanvas.indexLatticeBitBottom] = false;
        latticeBits[LatticeTileGridCanvas.indexLatticeBitRight] = true;
        latticeBits[LatticeTileGridCanvas.indexLatticeBitTop] = false;
        
        canvas.setColorOnTile(Color.GREEN, 0, 0);
        canvas.setColorOnTile(Color.YELLOW, 1, 1);
        canvas.setColorOnTile(Color.BLUE, 2, 2);
        canvas.setLatticesOnTile(2, 2, 15, Color.BROWN);
        canvas.setLatticesOnTile(1, 1, 4, Color.RED);
        canvas.setLatticesOnTile(0, 0, latticeBits, Color.BROWN);
        
        root.getChildren().add(canvas);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}