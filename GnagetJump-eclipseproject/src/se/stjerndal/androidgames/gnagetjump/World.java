package se.stjerndal.androidgames.gnagetjump;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import se.stjerndal.androidgames.framework.math.OverlapTester;
import se.stjerndal.androidgames.framework.math.Vector2;

public class World {
    public interface WorldListener {
        public void jump();
        public void highJump();
        public void hit();
        public void coin();
    }

    public static final float WORLD_WIDTH = 10;
    public static final float WORLD_HEIGHT = 15 * 20;    
    public static final int WORLD_STATE_RUNNING = 0;
    public static final int WORLD_STATE_NEXT_LEVEL = 1;
    public static final int WORLD_STATE_GAME_OVER = 2;
    public static final Vector2 gravity = new Vector2(0, -12);

    public final Gnagare gnagarn;           
    public final List<Platform> platforms;
    public final List<Spring> springs;
    public final List<Monkey> monkeys;
    public final List<Coin> coins;
    public List<Projectile> activeProjectiles;
    public Castle castle;    
    public final WorldListener listener;
    public final Random rand;
    
    public float heightSoFar;
    public int score;    
    public int state;
    public int ammo;
    

    public World(WorldListener listener) {
        this.gnagarn = new Gnagare(5, 1);        
        this.platforms = new ArrayList<Platform>();
        this.springs = new ArrayList<Spring>();
        this.monkeys = new ArrayList<Monkey>();
        this.coins = new ArrayList<Coin>();
        this.activeProjectiles = new ArrayList<Projectile>();
        this.listener = listener;
        rand = new Random();
        generateLevel();
        
        this.heightSoFar = 0;
        this.score = 0;
        this.state = WORLD_STATE_RUNNING;
        this.ammo = 0;
    }

    private void generateLevel() {
        float y = Platform.PLATFORM_HEIGHT / 2;
        float maxJumpHeight = Gnagare.GNAGARE_JUMP_VELOCITY * Gnagare.GNAGARE_JUMP_VELOCITY
                / (2 * -gravity.y);
        while (y < WORLD_HEIGHT - WORLD_WIDTH / 2) {
            int type = rand.nextFloat() > 0.8f ? Platform.PLATFORM_TYPE_MOVING
                    : Platform.PLATFORM_TYPE_STATIC;
            float x = rand.nextFloat()
                    * (WORLD_WIDTH - Platform.PLATFORM_WIDTH)
                    + Platform.PLATFORM_WIDTH / 2;

            Platform platform = new Platform(type, x, y);
            platforms.add(platform);

            if (rand.nextFloat() > 0.9f
                    && type != Platform.PLATFORM_TYPE_MOVING) {
                Spring spring = new Spring(platform.position.x,
                        platform.position.y + Platform.PLATFORM_HEIGHT / 2
                                + Spring.SPRING_HEIGHT / 2);
                springs.add(spring);
            }

            if (y > WORLD_HEIGHT / 4 && rand.nextFloat() > 0.8f) {
//            if (rand.nextFloat() > 0.8f) {
                Monkey monkey = new Monkey(platform.position.x
                        + rand.nextFloat(), platform.position.y
                        + Monkey.MONKEY_HEIGHT + rand.nextFloat() * 2);
                monkeys.add(monkey);
            }

            if (rand.nextFloat() > 0.8f) {
                Coin coin = new Coin(platform.position.x + rand.nextFloat(),
                        platform.position.y + Coin.COIN_HEIGHT
                                + rand.nextFloat() * 3);
                coins.add(coin);
            }

            y += (maxJumpHeight - 0.5f);
            y -= rand.nextFloat() * (maxJumpHeight / 3);
        }

        castle = new Castle(WORLD_WIDTH / 2, y);
    }
    
    public void shoot() {
    	if(ammo > 0) {
    		Projectile projectile = new Projectile(gnagarn.position.x, gnagarn.position.y+gnagarn.GNAGARE_HEIGHT);
    		activeProjectiles.add(projectile);
    		ammo--;
    	}
    }

    public void update(float deltaTime, float accelX) {
        updateGnagare(deltaTime, accelX);
        updatePlatforms(deltaTime);
        updateMonkeys(deltaTime);
        updateCoins(deltaTime);
        updateActiveProjectiles(deltaTime);
        if (gnagarn.state != Gnagare.GNAGARE_STATE_HIT)
            checkCollisions();
        checkGameOver();
    }

    private void updateGnagare(float deltaTime, float accelX) {
        if (gnagarn.state != Gnagare.GNAGARE_STATE_HIT && gnagarn.position.y <= 0.5f)
            gnagarn.hitPlatform();
        if (gnagarn.state != Gnagare.GNAGARE_STATE_HIT)
            gnagarn.velocity.x = -accelX / 10 * Gnagare.GNAGARE_MOVE_VELOCITY;
        gnagarn.update(deltaTime);
        heightSoFar = Math.max(gnagarn.position.y, heightSoFar);
    }

    private void updatePlatforms(float deltaTime) {
        int len = platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = platforms.get(i);
            platform.update(deltaTime);
            if (platform.state == Platform.PLATFORM_STATE_PULVERIZING
                    && platform.stateTime > Platform.PLATFORM_PULVERIZE_TIME) {
                platforms.remove(platform);
                len = platforms.size();
            }
        }
    }

    private void updateMonkeys(float deltaTime) {
        int len = monkeys.size();
        for (int i = 0; i < len; i++) {
            Monkey monkey = monkeys.get(i);
            monkey.update(deltaTime);
        }
    }

    private void updateCoins(float deltaTime) {
        int len = coins.size();
        for (int i = 0; i < len; i++) {
            Coin coin = coins.get(i);
            coin.update(deltaTime);
        }
    }
    
    private void updateActiveProjectiles(float deltaTime) {
        int len = activeProjectiles.size();
        if(Tools.debugging && len > 0)
        	Tools.log("Trying to update " + len + " projectiles");
        for (int i = 0; i < len; i++) {
            Projectile projectile = activeProjectiles.get(i);
            projectile.update(deltaTime);
            if(projectile.stateTime > Projectile.PROJECTILE_TTL) {
            	activeProjectiles.remove(projectile);
            	len = activeProjectiles.size();
            }
        }
    }

    private void checkCollisions() {
        checkPlatformCollisions();
        checkMonkeyCollisions();
        checkItemCollisions();
        checkCastleCollisions();
        checkActiveProjectilesCollisions();
    }

    private void checkPlatformCollisions() {
        if (gnagarn.velocity.y > 0)
            return;

        int len = platforms.size();
        for (int i = 0; i < len; i++) {
            Platform platform = platforms.get(i);
            if (gnagarn.position.y > platform.position.y) {
                if (OverlapTester
                        .overlapRectangles(gnagarn.bounds, platform.bounds)) {
                    gnagarn.hitPlatform();
                    listener.jump();
                    if (rand.nextFloat() > 0.5f) {
                        platform.pulverize();
                    }
                    break;
                }
            }
        }
    }

	private void checkMonkeyCollisions() {
		int len = monkeys.size();
		for (int i = 0; i < len; i++) {
			Monkey monkey = monkeys.get(i);
			if (OverlapTester.overlapRectangles(monkey.bounds, gnagarn.bounds)) {
				if (gnagarn.state == Gnagare.GNAGARE_STATE_FALL) {
					gnagarn.hitPlatform();
					listener.highJump();
					// TODO make own soundeffect & method
					// Also move removal to updateMonkeys(), create monkey state: dying.
					monkeys.remove(monkey);
					len = monkeys.size();
					score += Monkey.MONKEY_SCORE;
				} else {
					gnagarn.hitMonkey();

					listener.hit();
				}
			}
		}
	}

    private void checkItemCollisions() {
        int len = coins.size();
        for (int i = 0; i < len; i++) {
            Coin coin = coins.get(i);
            if (OverlapTester.overlapRectangles(gnagarn.bounds, coin.bounds)) {
                coins.remove(coin);
                len = coins.size();
                listener.coin();
                score += Coin.COIN_SCORE;
                ammo++;
                //TODO
                //Break here? Won't take 2 coins same update?
            }

        }

        if (gnagarn.velocity.y > 0)
            return;

        len = springs.size();
        for (int i = 0; i < len; i++) {
            Spring spring = springs.get(i);
            if (gnagarn.position.y > spring.position.y) {
                if (OverlapTester.overlapRectangles(gnagarn.bounds, spring.bounds)) {
                    gnagarn.hitSpring();
                    listener.highJump();
                }
            }
        }
    }
    
	private void checkActiveProjectilesCollisions() {
		int len = activeProjectiles.size();
		int len2 = monkeys.size();
		for (int i = 0; i < len; i++) {
			Projectile projectile = activeProjectiles.get(i);
			for (int j = 0; j < len2; j++) {
				Monkey monkey = monkeys.get(j);
				if (OverlapTester.overlapRectangles(projectile.bounds,
						monkey.bounds)) {
					// TODO make own soundeffect & method
					// Also move removal to updateMonkeys(),
					//create monkey state: dying.
					//change remove(monkey) to remove(j), stampa monkey as well
					activeProjectiles.remove(projectile);
					len = activeProjectiles.size();
					monkeys.remove(monkey);
					len2 = monkeys.size();
					score += Monkey.MONKEY_SCORE;
					listener.hit();
				}
			}
		}
	}
	

    private void checkCastleCollisions() {
        if (OverlapTester.overlapRectangles(castle.bounds, gnagarn.bounds)) {
            state = WORLD_STATE_NEXT_LEVEL;
        }
    }

    private void checkGameOver() {
        if (heightSoFar - 7.5f > gnagarn.position.y) {
            state = WORLD_STATE_GAME_OVER;
        }
    }
}
