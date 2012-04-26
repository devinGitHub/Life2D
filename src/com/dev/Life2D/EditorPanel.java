package com.dev.Life2D;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.View;

public class EditorPanel extends View
{
    Paint paint;
	public Bitmap mPanelBM;
	public WorldGrid mWorldGrid;
	
    public EditorPanel( Context context, WorldGrid worldGrid )
    {
        super(context);
        paint = new Paint();
        
        mWorldGrid = worldGrid;
        int gridEditorSize = mWorldGrid.mGridCursor.getCursorSize();
        
        mPanelBM = Bitmap.createBitmap( gridEditorSize, gridEditorSize, Bitmap.Config.ARGB_8888 ); 
        		//this.getHeight(), this.getWidth(), Bitmap.Config.ARGB_8888 );
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
    	updateBitmap();
        doDraw(canvas,paint);
    }
    
    public void doDraw( Canvas canvas, Paint paint )
    {    	
		Matrix tempMatrix = new Matrix();
		
		tempMatrix.setScale( this.getWidth()/mPanelBM.getWidth(), this.getHeight()/mPanelBM.getHeight() );
		
		canvas.drawBitmap( mPanelBM, tempMatrix, paint );
    }
    
    public void updateBitmap()
    {
    	int curX = mWorldGrid.mGridCursor.minIndexX();//mWorldGrid.mGridCursor.mPosX;//
    	int curY = mWorldGrid.mGridCursor.minIndexY();//mWorldGrid.mGridCursor.mPosY;//
    	
    	for( int x=0; x<mPanelBM.getWidth(); x++ )
    	{
    		for( int y=0; y<mPanelBM.getHeight(); y++ )
    		{
    			if( mWorldGrid.getValue( curX+x, curY+y ) > 0 )
    			{
    				mPanelBM.setPixel( x, y, Color.GREEN );
    			}
    			else
    			{
    				mPanelBM.setPixel( x, y, Color.BLACK );    				
    			}
    		}
    	}
    }
    
}
