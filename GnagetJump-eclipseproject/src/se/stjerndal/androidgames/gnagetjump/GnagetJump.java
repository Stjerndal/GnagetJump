package se.stjerndal.androidgames.gnagetjump;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import se.stjerndal.androidgames.framework.Screen;
import se.stjerndal.androidgames.framework.impl.GLGame;
import se.stjerndal.androidgames.framework.impl.GLGameGPGS;

public class GnagetJump extends GLGameGPGS {
    boolean firstTimeCreate = true;
    
    public Screen getStartScreen() {
        return new MainMenuScreen(this);
    }
    
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {         
        super.onSurfaceCreated(gl, config);
        if(firstTimeCreate) {
            Settings.load(getFileIO());
            Assets.load(this);
            firstTimeCreate = false;            
        } else {
            Assets.reload();
        }
    }     
    
    @Override
    public void onPause() {
        super.onPause();
        if(Tools.debugging)
        Tools.log("onPause");
        if(Settings.soundEnabled && Assets.music.isPlaying() && (!pausingFromGPGS || isFinishing() ))
            Assets.music.pause();
    }
    
    @Override
    public void onStop() {
    	
    	if(Assets.music.isPlaying()) {
    		Assets.music.pause();
    	}
    	super.onStop();
    }
    

//	@Override
	public void onSignInFailed() {
		// TODO Auto-generated method stub
		// TODO Move these methods to GLGameGPGS
		pausingFromGPGS = false;
		
	}

//	@Override
	public void onSignInSucceeded() {
		// TODO Auto-generated method stub
		pausingFromGPGS = false;
		super.submitNormalHighscore(Settings.highscores[0]);
	}
    
    //TODO
    //Fix Black screen which appears after a while, being paused
    //OnDestroy?
}