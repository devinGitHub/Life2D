
   package com.dev.Life2D;

import com.dev.Life2D.R;
import android.app.Activity;
   import android.os.Bundle;
   import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
   import android.graphics.*;
import android.content.*;

   public class Skeleton2D extends Activity
   {
       public static final int DIRECTION_RIGHT = 0, DIRECTION_LEFT = 1;
       static final private int BACK_ID = Menu.FIRST;

       private Panel mMainPanel;

       public boolean mbStart = true;
       private volatile boolean running = true;
       private int direction = DIRECTION_RIGHT;

       public static final int CANVAS_X = 320, CANVAS_Y = 480;
       public static final int WORLD_X = 250, WORLD_Y = 250;
       public static final int BOX_SIZE = 10;
       
       private int mBoxX = 0;
       
       @Override
       public void onCreate(Bundle savedInstanceState)
       {
           super.onCreate(savedInstanceState);

           mMainPanel = new Panel(this);
           
           /*
           mMainPanel.addFocusables(1);
           mMainPanel.addFocusables(R.layout.main);
           */
           setContentView(mMainPanel,new ViewGroup.LayoutParams(CANVAS_X,CANVAS_Y));
           //setContentView(R.layout.main);
           // Hook up button presses to the appropriate event handler.
           //((Button) findViewById(R.id.back)).setOnClickListener(mBackListener); 
           
           (new Thread(new AnimationLoop())).start();
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
       }

       private synchronized void doDraw(Canvas canvas, Paint paint)
       {
           if(mbStart)
           {
	           //canvas.clipRect(0,0,250,250);
               //canvas.drawColor(Color.DKGRAY);
               mbStart = false;
           }
           else
           {
	           canvas.clipRect(0,0,WORLD_X,WORLD_Y);
               canvas.drawColor(Color.DKGRAY);
	           canvas.save();
	           canvas.clipRect(mBoxX,20,mBoxX+BOX_SIZE,30);
	           canvas.drawColor(Color.RED);
	
	            //canvas.drawBitmap(scratch,mBoxX,10,paint);
	
	           canvas.restore();
           }
       }

       /**
        * Called when your activity's options menu needs to be created.
        */
       @Override
       public boolean onCreateOptionsMenu(Menu menu) {
           super.onCreateOptionsMenu(menu);

           menu.add(0, BACK_ID, 0, R.string.back).setShortcut('0', 'b');
           return true;
       }
    
       /**
        * Called right before your activity's option menu is displayed.
        */
       @Override
       public boolean onPrepareOptionsMenu(Menu menu) {
           super.onPrepareOptionsMenu(menu);

           return true;
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
       
       @Override
       public boolean onKeyDown(int keyCode, KeyEvent event)
       {
           if(keyCode == KeyEvent.KEYCODE_DPAD_CENTER)
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
           else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
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
                           Thread.sleep(30);
                       }
                       catch(InterruptedException ex) {}

                       updatePhysics();
                       mMainPanel.postInvalidate();
                   }
               }
           }
       }
   }