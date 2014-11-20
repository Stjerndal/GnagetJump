package se.stjerndal.androidgames.gnagetjump;

import java.util.List;

import javax.microedition.khronos.opengles.GL10;

import se.stjerndal.androidgames.framework.Game;
import se.stjerndal.androidgames.framework.Input.TouchEvent;
import se.stjerndal.androidgames.framework.glgpgs.Camera2D;
import se.stjerndal.androidgames.framework.glgpgs.SpriteBatcher;
import se.stjerndal.androidgames.framework.impl.GLGameGPGS;
import se.stjerndal.androidgames.framework.impl.GLScreenGPGS;
import se.stjerndal.androidgames.framework.math.OverlapTester;
import se.stjerndal.androidgames.framework.math.Rectangle;
import se.stjerndal.androidgames.framework.math.Vector2;

public class MainMenuScreen extends GLScreenGPGS {
    Camera2D guiCam;
    SpriteBatcher batcher;
    Rectangle soundBounds;
    Rectangle playBounds;
    Rectangle highscoresBounds;
    Rectangle achievementsBounds;
    Rectangle helpBounds;
    Rectangle gPlusBounds;
    Vector2 touchPoint;

    public MainMenuScreen(Game game) {
        super(game);
        guiCam = new Camera2D(glGraphics, 320, 480);
        batcher = new SpriteBatcher(glGraphics, 100);
        soundBounds = new Rectangle(0, 0, 64, 64);
        playBounds = new Rectangle(160 - 150, 272, 300, 40);
        highscoresBounds = new Rectangle(160 - 150, 240, 300, 32);
        achievementsBounds = new Rectangle(160 - 150, 208, 300, 32);
        helpBounds = new Rectangle(160 - 150, 168, 300, 40);
        gPlusBounds = new Rectangle(320-135, 0, 135, 100);
        touchPoint = new Vector2();
    }       

    @Override
    public void update(float deltaTime) {
        List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
        game.getInput().getKeyEvents();
        
        int len = touchEvents.size();
        for(int i = 0; i < len; i++) {
            TouchEvent event = touchEvents.get(i);                        
            if(event.type == TouchEvent.TOUCH_UP) {
                touchPoint.set(event.x, event.y);
                guiCam.touchToWorld(touchPoint);
                
                if(OverlapTester.pointInRectangle(playBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new GameScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(highscoresBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    if(Tools.useGPGSLeaderboards && glGame.isSignedIn()) {
                    	glGame.showLeaderboards();
                    } else {
                    	game.setScreen(new HighscoreScreen(game));
                    }
                    return;
                }
                if(OverlapTester.pointInRectangle(achievementsBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    if(glGame.isSignedIn()) {
                    	glGame.showAchievements();
                    } else {
                    	//TODO Show error message
                    }
                    return;
                }
                if(OverlapTester.pointInRectangle(helpBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    game.setScreen(new HelpScreen(game));
                    return;
                }
                if(OverlapTester.pointInRectangle(soundBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    Settings.soundEnabled = !Settings.soundEnabled;
                    if(Settings.soundEnabled) 
                        Assets.music.play();
                    else
                        Assets.music.pause();
                }
                if(OverlapTester.pointInRectangle(gPlusBounds, touchPoint)) {
                    Assets.playSound(Assets.clickSound);
                    if(glGame.isSignedIn()) {
                    	glGame.signOut();
                    } else {
                    	glGame.signIn();
                    }
                }
            }
        }
    }

    @Override
    public void present(float deltaTime) {
        GL10 gl = glGraphics.getGL();        
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        guiCam.setViewportAndMatrices();
        
        gl.glEnable(GL10.GL_TEXTURE_2D);
        
        batcher.beginBatch(Assets.background);
        batcher.drawSprite(160, 240, 320, 480, Assets.backgroundRegion);
        batcher.endBatch();
        
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);               
        
        batcher.beginBatch(Assets.items);                 
        
        batcher.drawSprite(160, 480 - 32 - 32 - 32, 256, 32, Assets.logo);
        batcher.drawSprite(160, 240, 300, 136, Assets.mainMenu);
        batcher.drawSprite(32, 32, 64, 64, Settings.soundEnabled?Assets.soundOn:Assets.soundOff);
        if(glGame.isSignedIn()) {
        	batcher.drawSprite(320 - 64, 32, 128, 32, Assets.signOut);
        } else {
        	batcher.drawSprite(320 - 64, 32, 128, 64 * guiCam.getYNormalizer(), Assets.gplusSignIn);
        }
                
        batcher.endBatch();
        
        gl.glDisable(GL10.GL_BLEND);
    }

    @Override
    public void pause() {        
        Settings.save(game.getFileIO());
    }

    @Override
    public void resume() {        
    }       

    @Override
    public void dispose() {        
    }
}
