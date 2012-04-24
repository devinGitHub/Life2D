package com.dev.Life2D;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

class GridCursor
{
	public Bitmap mCursorBM;

	private float mScaleFactor = 1.0f;
	public int mPosX, mPosY;
	
	private int mGridSizeX, mGridSizeY;
	
	public static final int CURSOR_BOX_HALFSIZE = 4; //4 points to any side of center
	
	public GridCursor( float scaleFactor, int gridSizeX, int gridSizeY )
	{
		mScaleFactor = scaleFactor;
		mGridSizeX = gridSizeX;
		mGridSizeY = gridSizeY;
		mPosX = 0;
		mPosY = 0;
		
		initCursor();
	}
	
	public void initCursor()
	{
		int cursorSize = CURSOR_BOX_HALFSIZE*2+1;
		mCursorBM = Bitmap.createBitmap(cursorSize, cursorSize, Bitmap.Config.ARGB_8888);
		//center
		mCursorBM.setPixel(	CURSOR_BOX_HALFSIZE, CURSOR_BOX_HALFSIZE, 
							Color.argb(128, 255, 255, 255) );
		//borders
		mCursorBM.setPixel(	0, 0, 
		           			Color.argb(64, 255, 255, 255) );
		mCursorBM.setPixel( 0, CURSOR_BOX_HALFSIZE*2, 
		           			Color.argb(64, 255, 255, 255) );
		mCursorBM.setPixel( CURSOR_BOX_HALFSIZE*2, 0, 
							Color.argb(64, 255, 255, 255) );
		mCursorBM.setPixel(	CURSOR_BOX_HALFSIZE*2, CURSOR_BOX_HALFSIZE*2, 
		           			Color.argb(64, 255, 255, 255) );
	}
	
	public void moveX(int deltaX)
	{
		mPosX = mPosX + deltaX;
	}

	public void moveY(int deltaY)
	{
		mPosY = mPosY + deltaY;
	}
	
	public void doDraw( Canvas canvas, Paint paint )
	{
		Matrix tempMatrix = new Matrix();
		tempMatrix.setScale( mScaleFactor, mScaleFactor );
		tempMatrix.preTranslate( mPosX-CURSOR_BOX_HALFSIZE, mPosY-CURSOR_BOX_HALFSIZE );
		
		canvas.drawBitmap( mCursorBM, tempMatrix, paint );
	}

}
