package uk.co.jawz;

import org.cocos2d.layers.CCScene;
import org.cocos2d.nodes.CCDirector;
import org.cocos2d.opengl.CCGLSurfaceView;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

public class SneakyInputDemo extends Activity {
	
	public static final String LOG_TAG = "SneakyInput";

	private CCGLSurfaceView mGLSurfaceView;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     
        // set the window status, no tile, full screen and don't sleep
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        					 WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
        				 	 WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        mGLSurfaceView = new CCGLSurfaceView(this);
        
        setContentView(mGLSurfaceView);
    }
    
    @Override
    public void onStart() {
    	super.onStart();
    
    	CCDirector director = CCDirector.sharedDirector();
    	director.attachInView(mGLSurfaceView);
    	director.setDeviceOrientation(CCDirector.kCCDeviceOrientationPortrait);
    	director.setDisplayFPS(true);
    	director.setAnimationInterval(1.0f / 60);

    	CCScene scene = CCScene.node();
    	HelloWorldLayer layer = HelloWorldLayer.node();
		scene.addChild(layer);
    	director.runWithScene(scene);  
    }

    @Override
    public void onPause() {
    	super.onPause();
    	CCDirector.sharedDirector().pause();
    }

    @Override
    public void onResume() {
    	super.onResume();
    	CCDirector.sharedDirector().resume();
    }

    @Override
    public void onStop() {
    	super.onStop();
    	CCDirector.sharedDirector().end();
    }
}