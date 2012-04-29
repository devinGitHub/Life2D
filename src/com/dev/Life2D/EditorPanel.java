package com.dev.Life2D;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;

public class EditorPanel extends View
{
    Paint paint;
	public Bitmap mPanelBM;
	public WorldGrid mWorldGrid;
	
	private boolean mDoWrite = false;
	private int mWriteToCoordX = 0;
	private int mWriteToCoordY = 0;
	
    
	public EditorPanel( Context context, WorldGrid worldGrid )
    {
        super(context);
        paint = new Paint();
        
        mWorldGrid = worldGrid;
        int gridEditorSize = mWorldGrid.mGridCursor.getCursorSize();
        
        mPanelBM = Bitmap.createBitmap( gridEditorSize, gridEditorSize, Bitmap.Config.ARGB_8888 ); 
        		//this.getHeight(), this.getWidth(), Bitmap.Config.ARGB_8888 );
        
        setOnTouchListener(mTouchListener);
    }

    OnTouchListener mTouchListener = new OnTouchListener() {
        public boolean onTouch(View v, MotionEvent event)
        {
        	if( event.getActionMasked() != MotionEvent.ACTION_DOWN )
        		return false; //we only care about the down
        	
        	float fX = event.getX();
        	float fY = event.getY();
        	int x = mWorldGrid.mGridCursor.minIndexX() + (int)(fX*mPanelBM.getWidth()/getWidth());
        	int y = mWorldGrid.mGridCursor.minIndexY() + (int)(fY*mPanelBM.getHeight()/getHeight());
        	//mWorldGrid.togglePoint(x, y, true);	
        	mDoWrite = true;
        	mWriteToCoordX = x;
        	mWriteToCoordY = y;
        	return true;
        }
    };
    
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
    			if( mWorldGrid.getValue( curX+x, curY+y, false ) > 0 )
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
    
    public void updatePhysics()
    {
    	if(mDoWrite)
    	{
    		mDoWrite = false;
    		mWorldGrid.togglePoint(mWriteToCoordX, mWriteToCoordY, true);	
    	}
    }
    
}
