package jborg.lightning;

import static consoleTools.TerminalXDisplay.formatBashStringBoldAndBlue;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import CollectionTools.CollectionManipulation;
import javafx.util.Pair;
import someMath.exceptions.CollectionException;
import someMath.exceptions.LTGCException;
import someMath.pathFinder.LatticeGrid;

public class LatticeSetup
{

	private final LatticeGrid lg;
	
	private final int width;
	private final int height;
	
	public LatticeSetup(LatticeGrid lg)
	{

		this.lg = lg;
		this.width = lg.getWidth();
		this.height = lg.getHeight();
	}

	private Set<Pair<Point, Integer>> poolOfPossibleLatticePositions(LatticeGrid lg)
	{
		
		Set<Pair<Point, Integer>> pool = new HashSet<>();
		int leftBitNr = 0;
		int bottomBitNr = 1;
		
		for(int x=0;x<width-1;x++)
		{
			for(int y=0;y<height-1;y++)
			{	
				
				Point p = new Point(x,y);
				Pair<Point, Integer> position;
				if(p.x>0)
				{
					position = new Pair<>(p, leftBitNr);
					pool.add(position);
				}
	  				
				if(p.y>0)
				{
					position = new Pair<>(p, bottomBitNr);
					pool.add(position);
				}
			}
		}
	
		return pool;
	}

	public void setupLattices(int width, int height, int latticeNr, LTGCS canvas) throws LTGCException, CollectionException
	{
	
		int nrOfInternPossibleLattices = 2*width*height-width-height;
		if(latticeNr>=nrOfInternPossibleLattices)throw new IllegalArgumentException("Much to many Lattices!");
	
		System.out.println(formatBashStringBoldAndBlue("Nr. of Possible Intern Lattices: " + nrOfInternPossibleLattices));
		System.out.println(formatBashStringBoldAndBlue("Nr. of Factual Intern Lattices: " + latticeNr));
		System.out.println(formatBashStringBoldAndBlue("Percentage: " + ((double)(latticeNr)/(nrOfInternPossibleLattices))));
	
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
	
		System.out.println(formatBashStringBoldAndBlue("ActualLattices: "+ actualLatticeNrs.size()));
		for(int n=0;n<actualLatticeNrs.size();n++)System.out.print(", " + actualLatticeNrs.get(n));
		System.out.println("");
		
		
		Set<Pair<Point, Integer>> allPositions = poolOfPossibleLatticePositions(canvas.getLatticeGrid());
		
		Set<Pair<Point, Integer>> chosenPositions = new HashSet<>();
		
		while(chosenPositions.size()<latticeNr)
		{
			Pair<Point, Integer> position = CollectionManipulation.catchRandomElementOfCollection(allPositions);
			chosenPositions.add(position);
		}
		
		for(Pair<Point, Integer> position: chosenPositions)
		{
			Point p = position.getKey();
			int bitNr= position.getValue();
			
			canvas.setOneLattice(p, bitNr);
		}
	
		int cnt = 0;
		
	
		System.out.println(formatBashStringBoldAndBlue("Count: " + cnt));
	}
}
