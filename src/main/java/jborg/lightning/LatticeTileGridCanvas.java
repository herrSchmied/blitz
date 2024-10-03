package jborg.lightning;


	
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import jborg.lightning.exceptions.LTGCException;

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
	
	private final Point startPoint;
	private final Point finalPoint;
	private Color[][] colorOfTile;
	private final Color latticeColor;

	private GraphicsContext gc2D;
	LatticeGrid lg;
	
	private final SnakeAndLatticeGrid snlGrid;
	
	public static final double standartStrokeWidth = 3.6;
	public static final int standartTileWidth = 36;
	
	public final Snake initialSnake;
	
	public LatticeTileGridCanvas(int width, int height, Point finalPoint, Snake snake) throws LTGCException
	{
		this(width, height, standartTileWidth, finalPoint, snake, standartStrokeWidth);
	}
	
	public LatticeTileGridCanvas(int xTileWidth, int yTileHeight, int tileSize, Point finalPoint, Snake snake, double strokeWidthLattice) throws LTGCException
	{

		super(xTileWidth*tileSize, yTileHeight*tileSize);
				
		if(snake.getLength()!=1)throw new LTGCException("Snake needs to be of size One.");

		this.widthInTiles = xTileWidth;
		this.heightInTiles = yTileHeight;
		
		this.absolutWidthInPixels = xTileWidth*tileSize;
		this.absolutHeightInPixels = yTileHeight*tileSize;
		
		this.tileSize = tileSize;
		
		this.latticeColor = Color.BLACK;
		
		this.startPoint = snake.getHead();
		this.finalPoint = finalPoint;

		gc2D = this.getGraphicsContext2D();
		
		this.strokeWidthLattice = strokeWidthLattice;
	
		lg = new LatticeGrid(widthInTiles, heightInTiles);
		colorOfTile = new Color[widthInTiles][heightInTiles];
		
		this.initialSnake = snake;
		snlGrid = new SnakeAndLatticeGrid(initialSnake, lg, finalPoint);
		
		initGrid();
	}
	
	public void initGrid() throws LTGCException
	{
		
		walkThruTiles((p)->
		{
			try
			{
				setColorOnTile(Color.GREY, p.x, p.y);
				lg.setLatticesOnTile(p, 0);
			}
			catch (LTGCException e)
			{
				e.printStackTrace();
			}
		});
	}
	
	public void setOneLattice(Point p, int bitNr) throws LTGCException
	{
		setOneLattice(p.x, p.y, bitNr);
	}
	
	public void setOneLattice(int x, int y, int bitNr) throws LTGCException
	{
		if(x>widthInTiles-1||x<0)throw new LTGCException("X-Position out of Bounds.");
		if(y>heightInTiles-1||y<0)throw new LTGCException("Y-Position out of Bounds.");
		if(bitNr>nrOfLatticeBits||bitNr<0)throw new LTGCException("Bit Nr. not valide.");

		lg.setOneLatticeOnTile(x, y, bitNr);
	}
	
	public void drawWholeCanvas() throws LTGCException
	{
		
		setColorOnTile(Color.GREEN, startPoint);
		setColorOnTile(Color.RED, finalPoint);
		walkThruTiles((p)->
		{
			try
			{
					
				Color colorOfTile = getColorOfTile(p);
				setColorOnTile(colorOfTile, p);
				boolean[] latticeBits = lg.translateLatticeCodeToLatticeBits(lg.getLatticeCode(p));
				
				if(latticeBits[indexLatticeBitLeft])drawLattice(p, indexLatticeBitLeft);
				if(latticeBits[indexLatticeBitTop])drawLattice(p, indexLatticeBitTop);
				
				Thread.sleep(750);
			}
			catch (LTGCException | InterruptedException e)
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
	
	public void setColorOnTile(Color c, Point p) throws LTGCException
	{
		setColorOnTile(c, p.x, p.y);
	}

	public void setColorOnTile(Color c, int xPos, int yPos) throws LTGCException
	{
		
		if(xPos>widthInTiles-1||xPos<0)throw new LTGCException("X-Position out of Bounds.");
		if(yPos>heightInTiles-1||yPos<0)throw new LTGCException("Y-Position out of Bounds.");

		gc2D.setFill(c);
		gc2D.fillRect(xPos*tileSize, yPos*tileSize, tileSize, tileSize);
	
		colorOfTile[xPos][yPos] = c;
	}
	
	private void drawLattice(Point p, int bitNr) throws LTGCException
	{
		drawLattice(p.x, p.y, bitNr);
	}

	private void drawLattice(int xPosTile, int yPosTile, int bitNr) throws LTGCException
	{
		
		if(xPosTile>widthInTiles-1||xPosTile<0)throw new LTGCException("X-Position out of Bounds.");
		if(yPosTile>heightInTiles-1||yPosTile<0)throw new LTGCException("Y-Position out of Bounds.");
		if(bitNr>nrOfLatticeBits||bitNr<0)throw new LTGCException("Bit Nr. not valide.");

		Double xStart = 0d;
		Double xEnd = 0d;
		Double yStart = 0d;
		Double yEnd = 0d;
		
		if(bitNr==indexLatticeBitLeft)//Left of Tile
		{
			xStart= (double)(xPosTile*tileSize);
			xEnd = xStart;
			yStart = (double)(yPosTile*tileSize);
			yEnd = yStart + tileSize;
		}
		
		if(bitNr==indexLatticeBitBottom)//downOfTile
		{
			xStart= (double)(xPosTile*tileSize);
			xEnd = xStart + tileSize;
			yStart = (double)(yPosTile+1)*tileSize;
			yEnd = yStart;
		}
		
		if(bitNr==indexLatticeBitRight)//rightOfTile
		{
			xStart= (double)(xPosTile+1)*tileSize;
			xEnd = xStart;
			yStart = (double)(yPosTile*tileSize);
			yEnd = yStart + tileSize;
		}
		
		if(bitNr==indexLatticeBitTop)//TopOfTile
		{
			xStart= (double)(xPosTile*tileSize);
			xEnd = xStart + tileSize;
			yStart = (double)(yPosTile*tileSize);
			yEnd = yStart;
		}
		
		gc2D.setStroke(latticeColor);
		gc2D.setLineWidth(strokeWidthLattice);
		gc2D.strokeLine(xStart, yStart, xEnd, yEnd);
		
		String latticeBitStr = "";
		String posStr = "P("+xPosTile+", "+yPosTile+")";
		if(bitNr==indexLatticeBitLeft) latticeBitStr = "Left ";
		if(bitNr==indexLatticeBitTop) latticeBitStr = "Top ";
		if(bitNr==indexLatticeBitRight) latticeBitStr = "Right ";
		if(bitNr==indexLatticeBitBottom) latticeBitStr = "Bottom ";

		if(!latticeBitStr.equals(""))System.out.println("Drawing LatticeBit " + latticeBitStr + posStr);
	}

	public Color getColorOfTile(Point p) throws LTGCException
	{
		return getColorOfTile(p.x, p.y);
	}

	public Color getColorOfTile(int xPos, int yPos) throws LTGCException
	{
		if(xPos>widthInTiles-1||xPos<0)throw new LTGCException("X-Position out of Bounds.");
		if(yPos>heightInTiles-1||yPos<0)throw new LTGCException("Y-Position out of Bounds.");
		
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
	
	public int getLatticeCode(Point p) throws LTGCException
	{
		
		return getLatticeCode(p.x, p.y);
	}
	
	public int getLatticeCode(int x, int y) throws LTGCException
	{
		
		if(x<0||x>=widthInTiles) throw new LTGCException("Can't get Lattice Code. x is out of Bounds.");
		if(y<0||y>=heightInTiles) throw new LTGCException("Can't get Lattice Code. y is out of Bounds.");
		
		return lg.getLatticeCode(x, y);
	}
	
	public SnakeAndLatticeGrid getSNLGrid()
	{
		return snlGrid;
	}
}