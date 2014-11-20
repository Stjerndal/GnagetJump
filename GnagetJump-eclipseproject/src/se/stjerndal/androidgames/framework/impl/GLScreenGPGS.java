package se.stjerndal.androidgames.framework.impl;

import se.stjerndal.androidgames.framework.Game;
import se.stjerndal.androidgames.framework.Screen;

public abstract class GLScreenGPGS extends Screen {
    protected final GLGraphics glGraphics;
    protected final GLGameGPGS glGame;
    
    public GLScreenGPGS(Game game) {
        super(game);
        glGame = (GLGameGPGS)game;
        glGraphics = glGame.getGLGraphics();
    }
}
