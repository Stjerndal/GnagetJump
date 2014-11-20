package se.stjerndal.androidgames.gnagetjump;

import se.stjerndal.androidgames.framework.DynamicGameObject;

public class Gnagare extends DynamicGameObject{
    public static final int GNAGARE_STATE_JUMP = 0;
    public static final int GNAGARE_STATE_FALL = 1;
    public static final int GNAGARE_STATE_HIT = 2;
    public static final float GNAGARE_JUMP_VELOCITY = 11;    
    public static final float GNAGARE_MOVE_VELOCITY = 20;
    public static final float GNAGARE_WIDTH = 0.8f;
    public static final float GNAGARE_HEIGHT = 0.8f;

    int state;
    float stateTime;    

    public Gnagare(float x, float y) {
        super(x, y, GNAGARE_WIDTH, GNAGARE_HEIGHT);
        state = GNAGARE_STATE_FALL;
        stateTime = 0;        
    }

    public void update(float deltaTime) {     
        velocity.add(World.gravity.x * deltaTime, World.gravity.y * deltaTime);
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(bounds.width / 2, bounds.height / 2);
        
        if(velocity.y > 0 && state != GNAGARE_STATE_HIT) {
            if(state != GNAGARE_STATE_JUMP) {
                state = GNAGARE_STATE_JUMP;
                stateTime = 0;
            }
        }
        
        if(velocity.y < 0 && state != GNAGARE_STATE_HIT) {
            if(state != GNAGARE_STATE_FALL) {
                state = GNAGARE_STATE_FALL;
                stateTime = 0;
            }
        }
        
        if(position.x < 0)
            position.x = World.WORLD_WIDTH;
        if(position.x > World.WORLD_WIDTH)
            position.x = 0;
        
        stateTime += deltaTime;
    }

    public void hitMonkey() {
//    	if(state == GNAGARE_STATE_FALL) {
//        	hitPlatform();
//        } else {
        velocity.set(0,0);
        state = GNAGARE_STATE_HIT;        
        stateTime = 0;
//        }
        //TODO
        
    }
    
    public void hitPlatform() {
        velocity.y = GNAGARE_JUMP_VELOCITY;
        state = GNAGARE_STATE_JUMP;
        stateTime = 0;
    }

    public void hitSpring() {
        velocity.y = GNAGARE_JUMP_VELOCITY * 1.5f;
        state = GNAGARE_STATE_JUMP;
        stateTime = 0;   
    }
}
