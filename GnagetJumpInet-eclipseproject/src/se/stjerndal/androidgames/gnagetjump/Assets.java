package se.stjerndal.androidgames.gnagetjump;

import se.stjerndal.androidgames.framework.Music;
import se.stjerndal.androidgames.framework.Sound;
import se.stjerndal.androidgames.framework.glgpgs.Animation;
import se.stjerndal.androidgames.framework.glgpgs.Font;
import se.stjerndal.androidgames.framework.glgpgs.Texture;
import se.stjerndal.androidgames.framework.glgpgs.TextureRegion;
import se.stjerndal.androidgames.framework.impl.GLGameGPGS;

public class Assets {
    public static Texture background;
    public static TextureRegion backgroundRegion;
    public static Texture background1;
    public static Texture background1Region;
    public static Texture background2;
    public static Texture background2Region;
    
    public static Texture items;        
    public static TextureRegion mainMenu;
    public static TextureRegion pauseMenu;
    public static TextureRegion ready;
    public static TextureRegion gameOver;
    public static TextureRegion highScoresRegion;
    public static TextureRegion logo;
    public static TextureRegion soundOn;
    public static TextureRegion soundOff;
    public static TextureRegion arrow;
    public static TextureRegion pause;
    public static TextureRegion gplusSignIn;
    public static TextureRegion signOut;
    public static TextureRegion spring;
    public static TextureRegion lennart;
    public static TextureRegion projectile;
    public static Animation bombAnim;
    public static Animation explosionAnim;
    public static Animation coinAnim;
    public static Animation gnagarnJump;
    public static Animation gnagarnFall;
    public static TextureRegion gnagarnHit;
    public static Animation monkeyFly;
    public static TextureRegion platform;
    public static Animation breakingPlatform;    
    public static Font font;
    
    public static Music music;
    public static Sound jumpSound;
    public static Sound highJumpSound;
    public static Sound hitSound;
    public static Sound coinSound;
    public static Sound clickSound;
    public static Sound shootSound;

    public static void load(GLGameGPGS game) {
        background = new Texture(game, "background-main.png");
        backgroundRegion = new TextureRegion(background, 0, 0, 320, 480);
        
        items = new Texture(game, "items.png");        
        mainMenu = new TextureRegion(items, 0, 312, 352, 160);
        pauseMenu = new TextureRegion(items, 116, 128, 177, 66);
        ready = new TextureRegion(items, 105, 0, 175, 29);
        gameOver = new TextureRegion(items, 0, 128, 116, 65);
        highScoresRegion = new TextureRegion(Assets.items, 32, 400, 289, 26);
        logo = new TextureRegion(items, 0, 194, 226, 90);
        soundOff = new TextureRegion(items, 431, 64, 64, 64);
        soundOn = new TextureRegion(items, 303, 64, 64, 64);
        arrow = new TextureRegion(items, 367, 64, 64, 64);
        pause = new TextureRegion(items, 111, 64, 64, 64);
        gplusSignIn = new TextureRegion(items, 0, 64, 111, 48);
        signOut = new TextureRegion(items, 0, 0, 105, 15);
        
        
        spring = new TextureRegion(items, 440, 0, 32, 32);
        lennart = new TextureRegion(items, 239, 64, 64, 64);
        projectile = new TextureRegion(items, 0, 32, 32, 32);
        
//        coinAnim = new Animation(0.2f,                                 
//                                 new TextureRegion(items, 288, 32, 32, 32),
//                                 new TextureRegion(items, 320, 32, 32, 32),
//                                 new TextureRegion(items, 352, 32, 32, 32),
//                                 new TextureRegion(items, 320, 32, 32, 32));
        gnagarnJump = new Animation(0.2f,
                                new TextureRegion(items, 312, 0, 32, 32),
                                new TextureRegion(items, 376, 0, 32, 32));
        gnagarnFall = new Animation(0.2f,
                                new TextureRegion(items, 256, 32, 32, 32),
                                new TextureRegion(items, 408, 0, 32, 32));
        gnagarnHit = new TextureRegion(items, 384, 32, 32, 32);
        monkeyFly = new Animation(0.2f, 
                                    new TextureRegion(items, 280, 0, 32, 32),
                                    new TextureRegion(items, 344, 0, 32, 32));
        platform = new TextureRegion(items, 175, 64, 64, 16);
        breakingPlatform = new Animation(0.2f,
                                     new TextureRegion(items, 175, 64, 64, 16),
                                     new TextureRegion(items, 175, 80, 64, 16),
                                     new TextureRegion(items, 175, 96, 64, 16),
                                     new TextureRegion(items, 175, 112, 64, 16));
        
        
        bombAnim = new Animation(0.1f,                                 
                new TextureRegion(items, 0, 32, 32, 32),
                new TextureRegion(items, 32, 32, 32, 32),
                new TextureRegion(items, 64, 32, 32, 32),
                new TextureRegion(items, 96, 32, 32, 32),
      			new TextureRegion(items, 128, 32, 32, 32),
      			new TextureRegion(items, 160, 32, 32, 32),
      			new TextureRegion(items, 192, 32, 32, 32),
      			new TextureRegion(items, 224, 32, 32, 32));
        
        font = new Font(items, 226, 194, 16, 16, 20);
        
        music = game.getAudio().newMusic("music.ogg");
        music.setLooping(true);
        music.setVolume(0.5f);
        if(Settings.soundEnabled)
            music.play();
        jumpSound = game.getAudio().newSound("jump.ogg");
        highJumpSound = game.getAudio().newSound("highjump.ogg");
        hitSound = game.getAudio().newSound("hit.ogg");
        coinSound = game.getAudio().newSound("coin.ogg");
        clickSound = game.getAudio().newSound("click.ogg");
        shootSound = game.getAudio().newSound("shoot.ogg");
    }       

    public static void reload() {
        background.reload();
        items.reload();
        if(Settings.soundEnabled)
            music.play();
    }

    public static void playSound(Sound sound) {
        if(Settings.soundEnabled)
            sound.play(1);
    }
}
