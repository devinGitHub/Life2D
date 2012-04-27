
   package com.dev.Life2D;

   import com.dev.Life2D.R;
   import android.app.Activity;
   import android.os.Bundle;
   import android.view.*;

import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
   import android.widget.Button;
   import android.graphics.*;
   import android.content.*;
   import android.content.pm.PackageManager.NameNotFoundException;

   /**
 * @author Mike
 *
 */
public class Skeleton2D extends Activity
   {
       public static final int DIRECTION_RIGHT = 0, DIRECTION_LEFT = 1;
       static final private int BACK_ID = Menu.FIRST;

       private Panel mMainPanel;
       private View mEditPanel;
      // private EditorPanel mEditPanel;

       public boolean mbStart = true;
       private volatile boolean running = true;//false;
       private int direction = DIRECTION_RIGHT;

       public static final int CANVAS_X = 320, CANVAS_Y = 480; //no longer used
       public static final int WORLD_X = 250, WORLD_Y = 250;
       public static final int EDITOR_X = 150, EDITOR_Y = 150;
       public static final int BOX_SIZE = 10;
       
       private int mBoxX = 0;
       
       private WorldGrid mWorldGrid;
       private EditorPanel mEditorPanel;
       
       private Bitmap mCursorBitmap;
       
       @Override
       public void onCreate(Bundle savedInstanceState)
       {
           super.onCreate(savedInstanceState);
           mWorldGrid = new WorldGrid(1,1);
           mWorldGrid.exampleSeed2020();

           mMainPanel = new Panel(this);
           mMainPanel.setFocusableInTouchMode(true);
           
           try{
               Context c =  createPackageContext("com.dev.Life2D", 0);
               mEditorPanel = new EditorPanel( c, mWorldGrid ); 
               mEditPanel = mEditorPanel;//mEditPanel = new View(c);
               mEditPanel.setFocusableInTouchMode(true);
              
           }
           catch(NameNotFoundException ex)
           {
        	   int ii=0; // place holder
           }
           
           setContentView(R.layout.main);
           
           ViewGroup vgMain = (ViewGroup)findViewById(R.id.WorldWrapper);
           mMainPanel.setFocusable(true);

           vgMain.addView(mMainPanel,new ViewGroup.LayoutParams(WORLD_X,WORLD_Y));
           mMainPanel.setOnTouchListener(mTouchListener);           

           ViewGroup vgEdit = (ViewGroup)findViewById(R.id.EditorWrapper);
           mEditPanel.setBackgroundColor(0xFF303030);
           mEditPanel.setFocusable(true);
           vgEdit.addView(mEditPanel,new ViewGroup.LayoutParams(EDITOR_X,EDITOR_Y));
 
           // Hook up button presses to the appropriate event handler.
           ((Button) findViewById(R.id.back)).setOnClickListener(mBackListener); 
           
           setOffscreenBitmap();

           (new Thread(new AnimationLoop())).start();
       }

       private void setOffscreenBitmap()
       {
            mCursorBitmap = Bitmap.createBitmap(10,10,Bitmap.Config.ARGB_8888);
            mCursorBitmap.eraseColor(Color.argb(128, 255, 0, 0));
       }

       private synchronized void updatePhysics()
       {
           if(mBoxX < 1)
           {
               direction = DIRECTION_RIGHT;
           }
           else if(mBoxX > (WORLD_X-BOX_SIZE))
           {
               direction = DIRECTION_LEFT;
           }

           if(direction == DIRECTION_RIGHT)
           {
        	   mBoxX += 1;
           }
           else
           {
        	   mBoxX -= 1;
           }
           mEditorPanel.updatePhysics();
           mWorldGrid.updatePhysics();
       }
       

       private synchronized void doDraw(Canvas canvas, Paint paint)
       {
           if(mbStart)
           {
               mbStart = false;
           }
           else
           {
	           canvas.clipRect(0,0,WORLD_X,WORLD_Y);
               canvas.drawColor(Color.DKGRAY);
	           canvas.save();
	           
	           mWorldGrid.doDraw( canvas, paint );
	           canvas.drawBitmap(mCursorBitmap,mBoxX,20,paint);
	
	           canvas.restore();
           }
       }

       /**
        * Called when a menu item is selected.
        */
       @Override
       public boolean onOptionsItemSelected(MenuItem item) {
           switch (item.getItemId()) {
           case BACK_ID:
               finish();
               return true;
           }

           return super.onOptionsItemSelected(item);
       }

       /**
        * A call-back for when the user presses the back button.
        */
       OnClickListener mBackListener = new OnClickListener() {
           public void onClick(View v) {
               finish();
           }
       };
       

	   OnTouchListener mTouchListener = new OnTouchListener() 
	   {
			public boolean onTouch(View v, MotionEvent event)
			{
				
				float fX = event.getX();
				float fY = event.getY();
				int x = (int)(fX*mWorldGrid.mSizeX/mMainPanel.getWidth());
				int y = (int)(fY*mWorldGrid.mSizeY/mMainPanel.getHeight());
				
				mWorldGrid.mGridCursor.mPosX = x;
				mWorldGrid.mGridCursor.mPosY = y;
				
				return true;
			}
		};

       
       @Override
       public boolean onKeyDown(int keyCode, KeyEvent event)
       {
           if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
           {
        	   mWorldGrid.togglePoint( 	mWorldGrid.mGridCursor.mPosX, mWorldGrid.mGridCursor.mPosY, true );
           }
           if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT)
           {
        	   mWorldGrid.mGridCursor.moveX(1);
           }
           if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT)
           {
        	   mWorldGrid.mGridCursor.moveX(-1);
           }
           if(keyCode == KeyEvent.KEYCODE_DPAD_UP)
           {
        	   mWorldGrid.mGridCursor.moveY(-1);
           }
           if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
           {
        	   mWorldGrid.mGridCursor.moveY(1);
           }
           
           if(keyCode == KeyEvent.KEYCODE_SPACE)
           {
               if(running)
               {
                   running = false;
               }
               else
               {
                   running = true;
               }
           }
           else if (keyCode == KeyEvent.KEYCODE_Q)//KeyEvent.KEYCODE_DPAD_DOWN)
           {
               finish();
           }

           return true;
       }

       class Panel extends View
       {
           Paint paint;
           
           public Panel(Context context)
           {
               super(context);
               paint = new Paint();
           }

           @Override
           protected void onDraw(Canvas canvas)
           {
               doDraw(canvas,paint);
           }
       }

       class AnimationLoop implements Runnable
       {
           public void run()
           {
               while(true)
               {
                   while(running)
                   {
                       try
                       {
                           Thread.sleep(15);//30
                       }
                       catch(InterruptedException ex) 
                       {
                    	   int ii=0;//for breakpoint
                    	   ii++;    //breakpoint info
                       }

                       updatePhysics();
                       mMainPanel.postInvalidate();
                       mEditorPanel.postInvalidate();
                       
                       //running = false; //temp, step-wise
                   }
                   while(!running)
                   {
                       try
                       {
                           Thread.sleep(15);//30
                       }
                       catch(InterruptedException ex) {}
                	   
                       mEditorPanel.updatePhysics();
                       
                	   mMainPanel.postInvalidate();
                       mEditorPanel.postInvalidate();
                   }               
               }
           }
       }
   }
   
