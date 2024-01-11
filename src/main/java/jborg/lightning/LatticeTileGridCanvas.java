package jborg.lightning;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import static jborg.lightning.LatticeGrid.*;

import java.awt.Point;
import java.util.function.Consumer;



public class LatticeTileGridCanvas extends Canvas
{

	private final int widthInTiles;
	private final int heightInTiles;
	
	private final int absolutWidthInPixels;
	private final int absolutHeightInPixels;
	
	private final int tileSize;
	private final double strokeWidthLattice;
	
	private Color[][] colorOfTile;
	private final Color latticeColor;

	private GraphicsContext gc2D;
	LatticeGrid lg;
	
	//TODO: Testing next!!!!!!!!!!!!!!!!!!!!!!
	
	public LatticeTileGridCanvas(int xTileWidth, int yTileHeight, int tileSize, double strokeWidthLattice, Color latticeColor) throws LTGCException
	{
		super(xTileWidth*tileSize, yTileHeight*tileSize);
		
		this.widthInTiles = xTileWidth;
		this.heightInTiles = yTileHeight;
		
		this.absolutWidthInPixels = xTileWidth*tileSize;
		this.absolutHeightInPixels = yTileHeight*tileSize;
		
		this.tileSize = tileSize;
		
		this.latticeColor = latticeColor;
		
		gc2D = this.getGraphicsContext2D();
		
		this.strokeWidthLattice = strokeWidthLattice;
	
		lg = new LatticeGrid(widthInTiles, heightInTiles);
		colorOfTile = new Color[widthInTiles][heightInTiles];
		initGrid();
		drawWholeCanvas();
	}
	
	public void initGrid()
	{
		walkThruTiles((p)->
		{
			try
			{
				setColorOnTile(Color.GREY, p.x, p.y);
				lg.setOneLatticeOnTile(p.x, p.y, 0);
			}
			catch (LTGCException e)
			{
				e.printStackTrace();
			}
		});
	}
	
	public void setOneLattice(int x, int y, int bitNr) throws LTGCException
	{
		lg.setOneLatticeOnTile(x, y, bitNr);
	}
	
	public void drawWholeCanvas() throws LTGCException
	{
		walkThruTiles((p)->
		{
			try
			{
				Color colorOfTile = getColorOfTile(p.x, p.y);
				setColorOnTile(colorOfTile, p.x, p.y);
				boolean[] latticeBits = lg.translateLatticeCodeToLatticeBits(lg.getLatticeCode(p.x, p.y));
				
				if(latticeBits[indexLatticeBitLeft])drawLattice(p.x, p.y, indexLatticeBitLeft);
				if(latticeBits[indexLatticeBitRight])drawLattice(p.x, p.y, indexLatticeBitRight);
				if(latticeBits[indexLatticeBitTop])drawLattice(p.x, p.y, indexLatticeBitTop);
				if(latticeBits[indexLatticeBitBottom])drawLattice(p.x, p.y, indexLatticeBitBottom);
			}
			catch (LTGCException e)
			{
				e.printStackTrace();
			}
		});
	}
	
	public void walkThruTiles(Consumer<Point> consumer)
	{
	
		for(int x=0;x<widthInTiles;x++)
		{
			for(int y=0;y<heightInTiles;y++)
			{
				consumer.accept(new Point(x,y));
			}
		}

	}
	
	public void setColorOnTile(Color c, int xPos, int yPos) throws LTGCException
	{
		
		if(xPos>widthInTiles-1||xPos<0)throw new LTGCException("X-Position out of Bounds.");
		if(yPos>heightInTiles-1||yPos<0)throw new LTGCException("Y-Position out of Bounds.");

		gc2D.setFill(c);
		gc2D.fillRect(xPos*tileSize, yPos*tileSize, tileSize, tileSize);
	
		colorOfTile[xPos][yPos] = c;
	}
	
	

	private void drawLattice(int xPosTile, int yPosTile, int latticeNr)
	{
		
		Double xStart = 0d;
		Double xEnd = 0d;
		Double yStart = 0d;
		Double yEnd = 0d;
		
		if(latticeNr==indexLatticeBitLeft)//Left of Tile
		{
			xStart= (double)(xPosTile*tileSize);
			xEnd = xStart;
			yStart = (double)(yPosTile*tileSize);
			yEnd = yStart + tileSize;
		}
		
		if(latticeNr==indexLatticeBitBottom)//downOfTile
		{
			xStart= (double)(xPosTile*tileSize);
			xEnd = xStart + tileSize;
			yStart = (double)(yPosTile+1)*tileSize;
			yEnd = yStart;
		}
		
		if(latticeNr==indexLatticeBitRight)//rightOfTile
		{
			xStart= (double)(xPosTile+1)*tileSize;
			xEnd = xStart;
			yStart = (double)(yPosTile*tileSize);
			yEnd = yStart + tileSize;
		}
		
		if(latticeNr==indexLatticeBitTop)//TopOfTile
		{
			xStart= (double)(xPosTile*tileSize);
			xEnd = xStart + tileSize;
			yStart = (double)(yPosTile*tileSize);
			yEnd = yStart;
		}
		
		gc2D.setStroke(latticeColor);
		gc2D.setLineWidth(strokeWidthLattice);
		gc2D.strokeLine(xStart, yStart, xEnd, yEnd);
	}

	
	public Color getColorOfTile(int xPos, int yPos)
	{
		return colorOfTile[xPos][yPos];
	}
	
	public int getWidthInTiles()
	{
		return widthInTiles;
	}

	public int getHeightInTiles()
	{
		return heightInTiles;
	}

	public int getAbsolutWidthInPixels()
	{
		return absolutWidthInPixels;
	}

	public int getAbsolutHeightInPixels()
	{
		return absolutHeightInPixels;
	}

	public int getTileSize()
	{
		return tileSize;
	}
	
	public int getLatticeCode(int x, int y) throws LTGCException
	{
		
		if(x<0||x>=widthInTiles) throw new LTGCException("Can't get Lattice Code. x is out of Bounds.");
		if(y<0||y>=heightInTiles) throw new LTGCException("Can't get Lattice Code. y is out of Bounds.");
		
		return lg.getLatticeCode(x, y);
	}
	
	public LatticeGrid getLatticeGrid()
	{
		return lg;
	}
}