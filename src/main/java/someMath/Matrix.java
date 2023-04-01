package someMath;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

//It is important that the values of Type E have a good overwritten toString Method. 
//and the Type E must overwrite equals Too.
//I'm looking for a way to enforce that E is of Type: "Mathematical Body."
public class Matrix <E extends MultiplyableAndAddable<E>> implements MultiplyableAndAddable<Matrix<E>>
{

	private final E valArr [][];
	private final int rows;
	private final int columns;
	private final boolean isQuadratic;
	
	@SuppressWarnings("unchecked")
	public Matrix(int rows, int columns, List<E> values)
	{
	
		if(columns*rows!=values.size()) throw new IllegalArgumentException("Values don't Fit");
		if(values.remove(null)) throw new IllegalArgumentException("Please no Null values.");
		this.rows = rows;
		this.columns = columns;
		isQuadratic = (rows==columns);
		
		/*
		 * Cannot create a Array of Generic Type: "MultiplyableAndAddable<E>[][]" 
		 * The line below causes the Suppress Warning. How can i get rid of This?
		 */
		valArr = (E[][]) new MultiplyableAndAddable[rows][columns];
		
		BiConsumer<Integer, Integer> bic = (n,m)-> valArr[n][m] = values.get(n*columns+m);
		MatrixOps.walkThrouMatrix(this, bic);
	}
	
	public Matrix(E[][] valArr)
	{
		
		if(valArr==null)throw new IllegalArgumentException("Can't create Matrix with null Array.");
		int homogeneLengthStndrt = 1;
		for(int n=0;n<valArr.length;n++)
		{
			int l = valArr[n].length;
			if(n==0)homogeneLengthStndrt = l;
			if(l!=homogeneLengthStndrt)throw new IllegalArgumentException("Row's aren't all of same Length.");
			
			for(int m=0;m<l;m++)
			{
				if(valArr[n][m]==null)throw new IllegalArgumentException("Null Values at row: "+n+" column:"+m);
			}
		}
		
		this.valArr = valArr;
		this.rows = valArr.length;
		this.columns = valArr[0].length;
		isQuadratic = (rows==columns);
	}

	public int getRows()
	{ return rows;}
	
	public int getColumns()
	{ return columns;}
	
	public E getValue(int row, int column)
	{
		return valArr[row][column];
	}
		
	

	public Matrix<E> getColumn(int m)
	{
		
		List<E> list = new ArrayList<>();
		
		for(int i=0;i<rows;i++)list.add(valArr[i][m]);
		
		Matrix<E> outputRowMatrix = new Matrix<E>(rows, 1, list);
		
		return outputRowMatrix;
	}

	public Matrix<E> getRow(int n)
	{
		
		List<E> list = new ArrayList<>();
		
		for(int i=0;i<columns;i++)list.add(valArr[n][i]);
		
		Matrix<E> outputRowMatrix = new Matrix<E>(1, columns, list);
		
		return outputRowMatrix;
	}
	
	public boolean isQuadratic() {return isQuadratic;}
	//It is important that the values of Type E have a good overwritten toString Method.
	public String toString()
	{
		
		String output = "";
		
		int [] longesValue = new int[1];
		longesValue[0]= 1;
		BiConsumer<Integer, Integer> bic = (n,m)->
		{
			int valueLength = valArr[n][m].toString().length();
			if(valueLength>longesValue[0])longesValue[0]=valueLength; 
		};
		MatrixOps.walkThrouMatrix(this, bic);
		
		for(int n=0;n<rows;n++)
		{
			for(int m=0;m<columns;m++)
			{
				
				int l = valArr[n][m].toString().length();
				int d = longesValue[0]-l+1;
				String whiteSpace = StringManipulation.customWhiteSpace(d);
				
				output = output.concat(whiteSpace+valArr[n][m].toString());
			}
			output = output.concat("\n");
		}

		return output;
	}

	@Override
	public boolean hasNeutralOne() 
	{
		return isQuadratic;
	}

	@Override
	public Matrix<E> multiplyWith(Matrix<E> a)
	{
		
		if(!(this.columns==a.rows)) throw new IllegalArgumentException("Can't Multiply these Matrixes.");
		int newRows = this.rows;
		int newColumns = a.columns;
		E someValue = MatrixOps.rowAsList(this, 0).get(0);
		
		@SuppressWarnings("unchecked")
		E[][] newValues = (E[][]) new MultiplyableAndAddable[newRows][newColumns];
		
		for(int n=0;n<newRows;n++) 
		{
			for(int m=0;m<newColumns;m++)
			{
				E currentValue = someValue.getNeutralZero();
			
				for(int i=0;i<this.columns;i++)
					currentValue = currentValue.addWith(this.valArr[n][i].multiplyWith(a.valArr[i][m]));

				newValues[n][m] = currentValue;
			}
		}
		
		return new Matrix<E>(newValues);
	}

	@Override
	public Matrix<E> getNeutralOne() 
	{
		if(!hasNeutralOne()) throw new IllegalArgumentException("This Kind of Matrix has no Neutral Multiplicator.");
		
		List<E> listOfValues = new ArrayList<>();
		E someValue = MatrixOps.rowAsList(this, 0).get(0);//I just need a value. Doesn't matter it's State as long it isn't Null.
		
		BiConsumer<Integer, Integer> bic = (n,m)->
		{
			if(n==m)listOfValues.add(someValue.getNeutralOne());
			else listOfValues.add(someValue.getNeutralZero());
		};
		MatrixOps.walkThrouMatrix(this, bic); 
		
		return new Matrix<E>(rows, columns, listOfValues);
	}

	@Override
	public boolean hasNeutralZero() 
	{
		//No matter the kind of Matrix.
		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Matrix<E> addWith(Matrix<E> a)
	{
		
		E[][] newValArr;
		newValArr = (E[][]) new MultiplyableAndAddable[rows][columns];
		
		BiConsumer<Integer, Integer> bic = (n,m)-> 
		newValArr[n][m] = valArr[n][m].addWith(a.valArr[n][m]);
		MatrixOps.walkThrouMatrix(this, bic);
	
		return new Matrix<E>(newValArr);
	}

	@Override
	public Matrix<E> getNeutralZero() 
	{
		List<E> listOfValues = new ArrayList<>();
		E someValue = MatrixOps.rowAsList(this, 0).get(0);//I just need a value. Doesn't matter its State as long it isn't Null.
		
		BiConsumer<Integer, Integer> bic = (n,m)->listOfValues.add(someValue.getNeutralZero());
		MatrixOps.walkThrouMatrix(this, bic);

		return new Matrix<E>(rows, columns, listOfValues);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Matrix<E> subtractArg(Matrix<E> a) 
	{
		E[][] newValArr;
		newValArr = (E[][]) new MultiplyableAndAddable[rows][columns];
		
		BiConsumer<Integer, Integer> bic = (n,m)-> 
		newValArr[n][m] = valArr[n][m].subtractArg(a.valArr[n][m]);
		MatrixOps.walkThrouMatrix(this, bic);
	
		return new Matrix<E>(newValArr);
	}
	
	@SuppressWarnings("unchecked")
	public Class<E> getEnclosedType()
	{
		
		E e = MatrixOps.rowAsList(this,0).get(0);
		
		return (Class<E>) e.getClass();
	}
	
	
	public boolean equals(Object obj)
	{
		if (obj == this) return true;
		
	    if (!(obj instanceof Matrix)) return false;
	    
	    @SuppressWarnings("rawtypes")
		Matrix other = (Matrix)obj;//TODO: Must be raw?
	    if(!(other.getEnclosedType()== this.getEnclosedType()))
	    {
	    	
	    	System.out.println("Matrixes aren't Enclosing same Type.");
	    	return false;
	    }
	    
	    boolean[] check = new boolean[1];
	    check[0]= true;
	    BiConsumer<Integer, Integer>bic=(n,m)->
	    {
	    	if(!other.valArr[n][m].equals(this.valArr[n][m]))check[0] = false;
	    };
	    MatrixOps.walkThrouMatrix(this, bic);
	    
	    return check[0];
	}
}