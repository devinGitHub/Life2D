package com.dev.Life2D;
import android.graphics.*;

public class WorldGrid {
	public int mGrid[][][];
	public int mSizeX, mSizeY, mCurFrame;
	
	public Bitmap mGridBitmap;
	
	public static final int DRAW_SCALE = 3;
	public WorldGrid( int sizeX, int sizeY )
	{
		init( sizeX, sizeY );	
	}
	
	public void init( int sizeX, int sizeY )
	{
		mGrid = new int[2][sizeX][sizeY];
	
		for( int i=0; i<sizeX; i++ )
		{
			for( int j=0; j<sizeY; j++ )
			{
				mGrid[0][i][j] = 0;
			}
		}
		
		mGridBitmap = Bitmap.createBitmap(sizeX, sizeY, Bitmap.Config.ARGB_8888);
		mSizeX = sizeX;
		mSizeY = sizeY;
		mCurFrame = 0;
	}
	
	public void doDraw( Canvas canvas, Paint paint )
	{
		int newFrame = (mCurFrame+1)%2;
		for( int i=0; i<mSizeX; i++ )
		{
			for( int j=0; j<mSizeY; j++ )
			{
				calcNewFramePt(i, j, mCurFrame, newFrame);
			    if( mGrid[mCurFrame][i][j] > 0 )
			    {
				    mGridBitmap.setPixel(i, j, Color.GREEN);
			    }
			    else
			    {
				    mGridBitmap.setPixel(i,  j, Color.BLACK);
 			    }
				
				/*
				canvas.save();
				   canvas.clipRect(i*DRAW_SCALE,j*DRAW_SCALE,(i+1)*DRAW_SCALE,(j+1)*DRAW_SCALE);
		           if( mGrid[i][j] > 0 )
		           {
		        	   canvas.drawColor(Color.GREEN);
		           }
		           else
		           {
		        	   canvas.drawColor(Color.BLACK);
		           }
		        canvas.restore();
		        */
			}
		}
		Matrix tempScaleMatrix = new Matrix();
		tempScaleMatrix.setScale(2.0f, 2.0f);
		
		canvas.drawBitmap(mGridBitmap, tempScaleMatrix, paint);
		mCurFrame = newFrame;
	}
	
	public void calcNewFramePt( int i, int j, int curFrame, int newFrame )
	{
		//mGrid[newFrame][i][j] = mGrid[curFrame][i][j];
		int count = neighbourCount(curFrame, i, j);
		
		if( mGrid[curFrame][i][j] > 0 ) //pt is currently alive
		{
			if( (count < 2) || (count > 3) )
			{
				mGrid[newFrame][i][j] = 0;
			}
			else
			{
				mGrid[newFrame][i][j] = mGrid[curFrame][i][j];
			}
		}
		else //pt is currently dead
		{
			if( count == 3 )
			{
				mGrid[newFrame][i][j] = 1;				
			}
			else
			{
				mGrid[newFrame][i][j] = 0;				
			}
		}
	}
	
	public int neighbourCount( int frame, int i, int j )
	{
		int count = 0;
		
		if( mGrid[frame][getWrapCoordX(i-1)][getWrapCoordY(j-1)] > 0 ) count++;
		if( mGrid[frame][getWrapCoordX(i-1)][getWrapCoordY(j)] > 0 ) count++;
		if( mGrid[frame][getWrapCoordX(i-1)][getWrapCoordY(j+1)] > 0 ) count++;

		if( mGrid[frame][getWrapCoordX(i)][getWrapCoordY(j-1)] > 0 ) count++;
		if( mGrid[frame][getWrapCoordX(i)][getWrapCoordY(j+1)] > 0 ) count++;
		
		if( mGrid[frame][getWrapCoordX(i+1)][getWrapCoordY(j-1)] > 0 ) count++;
		if( mGrid[frame][getWrapCoordX(i+1)][getWrapCoordY(j)] > 0 ) count++;
		if( mGrid[frame][getWrapCoordX(i+1)][getWrapCoordY(j+1)] > 0 ) count++;
		
		return count;
	}
	
    public int getWrapCoordX( int coordX )
    {
    	if(coordX < 0)
    		return (coordX + mSizeX);
    	if(coordX >= mSizeX)
    		return (coordX - mSizeX);
    	return coordX;
    }

    public int getWrapCoordY( int coordY )
    {
    	if(coordY < 0)
    		return (coordY + mSizeY);
    	if(coordY >= mSizeY)
    		return (coordY - mSizeY);
    	return coordY;
    }

    public void exampleSeed2020()
	{
		init(125,125);
        /*
		//box
		mGrid[0][3][4] = 1; 
		mGrid[0][3][5] = 1; 

		mGrid[0][4][4] = 1; 
		mGrid[0][4][5] = 1; 
		
		//blinker
		mGrid[0][30][4] = 1;
		mGrid[0][30][5] = 1;
		mGrid[0][30][6] = 1;
        */
		
		/*
		//beacon
		mGrid[0][0][0] = 1; 
		mGrid[0][0][1] = 1; 

		mGrid[0][1][0] = 1; 
		mGrid[0][1][1] = 1; 

		mGrid[0][2][2] = 1; 
		mGrid[0][2][3] = 1; 

		mGrid[0][3][2] = 1; 
		mGrid[0][3][3] = 1; 
        */
		
		/*
		//glider
		mGrid[0][40][4] = 1;
		mGrid[0][40][5] = 1;
		mGrid[0][40][6] = 1;
		mGrid[0][41][6] = 1;
		mGrid[0][42][5] = 1;	
		//*/

		//glider 2
		mGrid[0][3][3] = 1;
		mGrid[0][4][3] = 1;
		mGrid[0][2][2] = 1;
		mGrid[0][3][2] = 1;
		mGrid[0][4][1] = 1;	
		
	}
	
}
