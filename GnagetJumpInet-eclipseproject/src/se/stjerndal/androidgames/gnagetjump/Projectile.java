package se.stjerndal.androidgames.gnagetjump;

import se.stjerndal.androidgames.framework.DynamicGameObject;

public class Projectile extends DynamicGameObject {
    public static final float PROJECTILE_WIDTH = 0.5f;
    public static final float PROJECTILE_HEIGHT = 0.8f;
    public static final float PROJECTILE_VELOCITY = 15f;
    public static final int PROJECTILE_SCORE = 20;
    public static final float PROJECTILE_TTL = 2f;
    
    float stateTime = 0;
    
    public Projectile(float x, float y) {
        super(x, y, PROJECTILE_WIDTH, PROJECTILE_HEIGHT);
        velocity.set(0, PROJECTILE_VELOCITY);
    }
    
    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(PROJECTILE_WIDTH / 2, PROJECTILE_HEIGHT / 2);
        
        stateTime += deltaTime;
    }
}
