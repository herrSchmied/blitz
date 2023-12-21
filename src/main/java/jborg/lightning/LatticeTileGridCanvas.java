package jborg.lightning;


import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class LatticeTileGridCanvas extends Canvas
{

	private final int widthInTiles;
	private final int heightInTiles;
	
	private final int absolutWidthInPixels;
	private final int absolutHeightInPixels;
	
	private final int tileSize;
	private final double strokeWidthLattice;
	
	private Color[][] colorOfTile;
	private int[][] latticeCodeOfTile;
	
	private GraphicsContext gc2D;
	
	public LatticeTileGridCanvas(int xTileWidth, int yTileHeight, int tileSize, double strokeWidthLattice)
	{
		super(xTileWidth*tileSize, yTileHeight*tileSize);
		
		this.widthInTiles = xTileWidth;
		this.heightInTiles = yTileHeight;
		
		this.absolutWidthInPixels = xTileWidth*tileSize;
		this.absolutHeightInPixels = yTileHeight*tileSize;
		
		this.tileSize = tileSize;
		
		gc2D = this.getGraphicsContext2D();
		
		this.strokeWidthLattice = strokeWidthLattice;
		
		colorOfTile = new Color[xTileWidth][yTileHeight];
		latticeCodeOfTile = new int[xTileWidth][yTileHeight];
	}
	
	public void setColorOnTile(Color c, int xPos, int yPos) throws LTGCException
	{
		
		if(xPos>widthInTiles-1||xPos<0)throw new LTGCException("X-Position out of Bounds.");
		if(yPos>heightInTiles-1||yPos<0)throw new LTGCException("Y-Position out of Bounds.");

		gc2D.setFill(c);
		gc2D.fillRect(xPos*tileSize, yPos*tileSize, tileSize, tileSize);
	
		colorOfTile[xPos][yPos] = c;
	}
	
	public void setLatticeOnTile(int xPosOfTile, int yPosOfTile, int posOfLatticeCode, Color latticeColor) throws LTGCException
	{

		if(xPosOfTile>widthInTiles-1||xPosOfTile<0)throw new LTGCException("X-Position out of Bounds.");
		if(yPosOfTile>heightInTiles-1||yPosOfTile<0)throw new LTGCException("Y-Position out of Bounds.");

		if(posOfLatticeCode>15||posOfLatticeCode<0)throw new LTGCException("Position of Lattic is out of Bounds");
		latticeCodeOfTile[xPosOfTile][yPosOfTile]=posOfLatticeCode;
		
		if(posOfLatticeCode==0)return;

		boolean[]latticeBits = new boolean[4];
		
		int tempLatticeCode = posOfLatticeCode;
		
		if(tempLatticeCode%2==0)latticeBits[0]=false;
		else latticeBits[0]= true;
		
		tempLatticeCode = tempLatticeCode/2;
		if(tempLatticeCode%2==0)latticeBits[1]=false;
		else latticeBits[1]= true;

		tempLatticeCode = tempLatticeCode/2;
		if(tempLatticeCode%2==0)latticeBits[2]=false;
		else latticeBits[2]= true;

		tempLatticeCode = tempLatticeCode/2;
		if(tempLatticeCode%2==0)latticeBits[3]=false;
		else latticeBits[3]= true;

		for(int n=0;n<4;n++)if(latticeBits[n])
			drawLattice(xPosOfTile, yPosOfTile, n, latticeColor);
	}
	
	private void drawLattice(int xPosTile, int yPosTile, int latticeNr, Color latticeColor)
	{
		
		Double xStart = 0d;
		Double xEnd = 0d;
		Double yStart = 0d;
		Double yEnd = 0d;
		
		if(latticeNr==0)//Left of Tile
		{
			xStart= (double)(xPosTile*tileSize);
			xEnd = xStart;
			yStart = (double)(yPosTile*tileSize);
			yEnd = yStart + tileSize;
		}
		
		if(latticeNr==1)//downOfTile
		{
			xStart= (double)(xPosTile*tileSize);
			xEnd = xStart + tileSize;
			yStart = (double)(yPosTile+1)*tileSize;
			yEnd = yStart;
		}
		
		if(latticeNr==2)//rightOfTile
		{
			xStart= (double)(xPosTile+1)*tileSize;
			xEnd = xStart;
			yStart = (double)(yPosTile*tileSize);
			yEnd = yStart + tileSize;
		}
		
		if(latticeNr==3)//TopOfTile
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
}