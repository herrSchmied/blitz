package jborg.lightning;


	
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import jborg.lightning.exceptions.LTGCException;
import jborg.lightning.exceptions.SnakeException;

import static jborg.lightning.LatticeGrid.*;

import java.awt.Point;
import java.util.List;
import java.util.Set;



/**
 * It is a Canvas. It inherits from javafx.scene.canvas.Canvas.
 * Receives width, height, a Snake and a final-Point. It 
 * creates a Lattice-Grid. From that a SnakeAndLatticeGrid.
 * The Stage where this Canvas is displayed is in BlitzThing.
 * If visuals happen than thru this class.
 * Lattice Tile Grid Canvas Snake. L. T. G. C. S.
 */
public class LTGCS extends Canvas
{

	/**
	 * Canvas width in Tiles.
	 */
	private final int widthInTiles;

	/**
	 * Canvas height in Tiles.
	 */
	private final int heightInTiles;
	
	/**
	 * Canvas width in Pixels.
	 */
	private final int absolutWidthInPixels;
	
	/**
	 * Canvas height in Pixels.
	 */
	private final int absolutHeightInPixels;
	
	/**
	 * The Tiles are quadratic. This side length
	 * of Tiles in Pixels.
	 */
	private final int tileSize;
	
	/**
	 * Lattice Stroke width in Pixels.
	 */
	private final double strokeWidthLattice;
	
	/**
	 * Start point of Snake growth. Is identical
	 * with Snake-Head at the Start.
	 */
	private final Point startPoint;
	
	/**
	 * final Destination for successful Snakes.
	 */
	private final Point finalPoint;
	
	/**
	 * Color of each Tile.
	 */
	private Color[][] colorOfTile;
	
	/**
	 * Lattice Color.
	 */
	private final Color latticeColor;

	/**
	 * This Canvas GrapicsContext.
	 */
	private GraphicsContext gc2D;
	
	/**
	 * LatticeGrid.
	 */
	LatticeGrid lg;
	
	/**
	 * Snake and Lattice-Grid.
	 * Calculation Basis for The Canvas.
	 */
	private final SnakeAndLatticeGrid snlGrid;
	
	/**
	 * Standard stroke width Lattices.
	 */
	public static final double standartStrokeWidth = 3.6;

	/**
	 * Standard Tile Width.
	 */
	public static final int standartTileWidth = 36;
	
	/**
	 * Initial Snake.
	 */
	public final Snake initialSnake;
	
	/**
	 * Constructor.
	 * 
	 * @param width Grid width.
	 * @param height Grid height.
	 * @param finalPoint success Coordinates.
	 * @param snake initial Snake.
	 * @throws LTGCException If Given Snake is not of length 1.
	 */
	public LTGCS(int width, int height, Point finalPoint, Snake snake) throws LTGCException
	{
		this(width, height, standartTileWidth, finalPoint, snake, standartStrokeWidth);
	}
	
	
	/**
	 * Constructor.
	 * 
	 * @param xTileWidth custom width in Tiles.
	 * @param yTileHeight custom height in Tiles.
	 * @param tileSize Tile side lenght in Pixel.
	 * @param finalPoint Destination of successful Snakes.
	 * @param snake initial Snake.
	 * @param strokeWidthLattice custom stroke for lattices in Pixel.
	 * @throws LTGCException If the given Snake has more parts then just one.
	 */
	public LTGCS(int xTileWidth, int yTileHeight, int tileSize, Point finalPoint, Snake snake, double strokeWidthLattice) throws LTGCException
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
		
		
	}
	
	/**
	 * Sets a Plain and clear Canvas and Lattic-Grid.
	 */
	public void colorAllTilesWhite()
	{
		lg.walkThruTiles((p)->
		{
			
			try
			{
				setColorOnTile(Color.WHITE, p);
			}
			catch(LTGCException e)
			{
				e.printStackTrace();
			}
		});
	}

	/**
	 * Sets one Lattice on a Tile Border.
	 * @param p Tile Coordinates.
	 * @param bitNr See Lattice-Grid Documentation.
	 * @throws LTGCException See Lattice-Grid Documentation.
	 */
	public void setOneLattice(Point p, int bitNr) throws LTGCException
	{
		setOneLattice(p.x, p.y, bitNr);
	}
	
	/**
	 * Sets one Lattice on Tile Border.
	 * @param x Tile x-Coordinate.
	 * @param y Tile y-Coordinate.
	 * @param bitNr See Lattice-Grid Documentation.
	 * @throws LTGCException See Lattice-Grid Documentation.
	 */
	public void setOneLattice(int x, int y, int bitNr) throws LTGCException
	{
		lg.setOneLatticeOnTile(new Point(x, y), bitNr);
	}
	
	/**
	 * Sets Lattices on all sides of a Tile.
	 * @param x Tile x-Coordinate.
	 * @param y Tile y-Coordinate.
	 * @throws LTGCException See Lattice-Grid Documentation.
	 */
	public void setAllLatticesOnTile(int x, int y) throws LTGCException
	{
		lg.setAllLatticesOnTile(new Point(x,y));
	}
	
	/**
	 * Sets Lattices on all Sides of a Tile.
	 * @param p Tile Coordinates.
	 * @throws LTGCException See Lattice-Grid Documentation.
	 */
	public void setAllLatticesOnTile(Point p) throws LTGCException
	{
		lg.setAllLatticesOnTile(p);
	}
	
	
	public void drawEverythingExeceptAnySnake() throws LTGCException
	{
		colorAllTilesWhite();
		drawStartAndEndTile();
		drawLattices();
	}
	/**
	 * draws Lattices except the Snakes.
	 * @throws LTGCException Shouldn't
	 */
	public void drawLattices() throws LTGCException
	{
	    	
		lg.walkThruTiles((p)->
		{
			try
			{
					
				Color c= Color.GREY;
				setColorOnTile(c, p);
				
				if(lg.hasLatticeOnTheLeft(p))drawLattice(p, indexLatticeBitLeft);
				if(lg.hasLatticeOnTheTop(p))drawLattice(p, indexLatticeBitTop);
				if(lg.hasLatticeOnTheRight(p))drawLattice(p, indexLatticeBitRight);				if(lg.hasLatticeOnTheRight(p))drawLattice(p, indexLatticeBitRight);				if(lg.hasLatticeOnTheRight(p))drawLattice(p, indexLatticeBitRight);				if(lg.hasLatticeOnTheBottom(p))drawLattice(p, indexLatticeBitBottom);
				if(lg.hasLatticeOnTheBottom(p))drawLattice(p, indexLatticeBitBottom);
				Thread.sleep(750);
			}
			catch (LTGCException | InterruptedException e)
			{
				e.printStackTrace();
			}
		});
	}

	public void drawStartAndEndTile() throws LTGCException
	{
		setColorOnTile(Color.GREEN, startPoint);
		setColorOnTile(Color.RED, finalPoint);

	}

	/**
	 * Changes Color of a Tile. Also the Color Data.
	 *
	 * @param c Color the Tile will have.
	 * @param p Tile Coordinate.
	 * @throws LTGCException Shouldn't
	 */
	public void setColorOnTile(Color c, Point p) throws LTGCException
	{
		setColorOnTile(c, p.x, p.y);
	}

	/**
	 * Changes Color of a Tile. Also the Color Data.
	 *
	 * @param c Color the Tile will have.
	 * @param xPos x-Coordinate of Tile.
	 * @param yPos y-Coordinate of Tile.
	 * @throws LTGCException Shouldn't
	 */
	public void setColorOnTile(Color c, int xPos, int yPos) throws LTGCException
	{
		
		if(xPos>widthInTiles-1||xPos<0)throw new LTGCException("X-Position out of Bounds.");
		if(yPos>heightInTiles-1||yPos<0)throw new LTGCException("Y-Position out of Bounds.");

		gc2D.setFill(c);
		int startX = xPos*tileSize+1;
		int startY = yPos*tileSize+1;
		int xSpan = tileSize-1;
		int ySpan = xSpan;
		
		gc2D.fillRect(startX, startY, xSpan, ySpan);
	
		colorOfTile[xPos][yPos] = c;
	}
	
	/**
	 * Draws one Lattice on a quadratic Tile.
	 * @param p Tile Coordinates.
	 * @param bitNr See Lattice-Grid Documentation.
	 * @throws LTGCException Shouldn't
	 */
	private void drawLattice(Point p, int bitNr) throws LTGCException
	{
		drawLattice(p.x, p.y, bitNr);
	}

	/**
	 * Draws one Lattice on a quadratic Tile.
	 * @param xPosTile x-Coordinate of Tile.
	 * @param yPosTile y-Coordinate of Tile.
	 * @param bitNr See Lattice-Grid Documentation.
	 * @throws LTGCException Shouldn't.
	 */
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

	/**
	 * Which Color has the Tile on Position p?
	 * 
	 * @param p Tile Coordinates.
	 * @return Color
	 * @throws LTGCException Shouldn't.
	 */
	public Color getColorOfTile(Point p) throws LTGCException
	{
		return getColorOfTile(p.x, p.y);
	}

	/**
	 * Which Color has the Tile on Position (xPos, yPos)?
	 * 
	 * @param xPos Tile x-Coordinate.
	 * @param yPos Tile y-Coordinate.
	 * @return Color.
	 * @throws LTGCException Shouldn't.
	 */
	public Color getColorOfTile(int xPos, int yPos) throws LTGCException
	{
		if(xPos>widthInTiles-1||xPos<0)throw new LTGCException("X-Position out of Bounds.");
		if(yPos>heightInTiles-1||yPos<0)throw new LTGCException("Y-Position out of Bounds.");
		
		return colorOfTile[xPos][yPos];
	}
	
	/**
	 * Canvas width in Tiles.
	 * 
	 * @return width.
	 */
	public int getWidthInTiles()
	{
		return widthInTiles;
	}

	/**
	 *  Canvas height in Tiles.
	 *  
	 * @return height.
	 */
	public int getHeightInTiles()
	{
		return heightInTiles;
	}

	/**
	 * Canvas width in Pixels.
	 * 
	 * @return width.
	 */
	public int getAbsolutWidthInPixels()
	{
		return absolutWidthInPixels;
	}

	/**
	 * Canvas height in Pixels.
	 * 
	 * @return height.
	 */
	public int getAbsolutHeightInPixels()
	{
		return absolutHeightInPixels;
	}

	/**
	 * Tile side length in Pixels.
	 * 
	 * @return side length.
	 */
	public int getTileSize()
	{
		return tileSize;
	}
	
	public void setFinalSnakes() throws LTGCException, SnakeException
	{
		snlGrid.setFinalSnakes();
	}

	public Set<Snake> filterSuccesses()
	{
		return snlGrid.filterSuccesses();
	}
	
	public List<Point> getOptions(Snake snake) throws LTGCException, SnakeException
	{
		return snlGrid.getOptions(snake);
	}
	
	public Set<Snake> getSnakeSet()
	{
		return snlGrid.getSnakeSet();
	}
	
	public Set<Snake> theDivergence(Snake snake) throws SnakeException, LTGCException
	{
		return snlGrid.theDivergence(snake);
	}
	
	public Point getFinalPoint()
	{
		return snlGrid.getFinalPoint();
	}
	
	public void setOneLatticeOnTile(Point p, int bitNr) throws LTGCException
	{
		lg.setOneLatticeOnTile(p, bitNr);
	}

	/**
	 * Positions, Lattices and more. All in SNL-Grid.
	 * @return Data.
	 */
	/*
	 * public SnakeAndLatticeGrid getSNLGrid() { return snlGrid; }
	 */	

	public boolean hasLatticeOnTheLeft(int x, int y) throws LTGCException
	{
		return lg.hasLatticeOnTheLeft(new Point(x,y));
	}

	public boolean hasLatticeOnTheRight(int x, int y) throws LTGCException
	{
		return lg.hasLatticeOnTheRight(new Point(x,y));
	}

	public boolean hasLatticeOnTheTop(int x, int y) throws LTGCException
	{
		return lg.hasLatticeOnTheTop(new Point(x,y));
	}

	public boolean hasLatticeOnTheBottom(int x, int y) throws LTGCException
	{
		return lg.hasLatticeOnTheBottom(new Point(x,y));
	}

	public boolean isSurounded(int x, int y) throws LTGCException
	{
		return lg.isSurounded(x, y);
	}
	
	public int howManyLatticesHasTile(int x, int y) throws LTGCException
	{
		return lg.howManyLatticesHasTile(new Point(x, y));
	}
	
	public boolean hasLatticeSomeWhere(int x, int y) throws LTGCException
	{
		return lg.hasLatticeSomeWhere(new Point(x, y));
	}
}