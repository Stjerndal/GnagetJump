package se.stjerndal.androidgames.gnagetjump;

import se.stjerndal.androidgames.framework.DynamicGameObject;

public class Monkey extends DynamicGameObject {
    public static final float MONKEY_WIDTH = 1;
    public static final float MONKEY_HEIGHT = 0.6f;
    public static final float MONKEY_VELOCITY = 3f;
    
    float stateTime = 0;
    
    public Monkey(float x, float y) {
        super(x, y, MONKEY_WIDTH, MONKEY_HEIGHT);
        velocity.set(MONKEY_VELOCITY, 0);
    }
    
    public void update(float deltaTime) {
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);
        bounds.lowerLeft.set(position).sub(MONKEY_WIDTH / 2, MONKEY_HEIGHT / 2);
        
        if(position.x < MONKEY_WIDTH / 2 ) {
            position.x = MONKEY_WIDTH / 2;
            velocity.x = MONKEY_VELOCITY;
        }
        if(position.x > World.WORLD_WIDTH - MONKEY_WIDTH / 2) {
            position.x = World.WORLD_WIDTH - MONKEY_WIDTH / 2;
            velocity.x = -MONKEY_VELOCITY;
        }
        stateTime += deltaTime;
    }
}
