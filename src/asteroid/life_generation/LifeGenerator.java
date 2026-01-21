package asteroid.life_generation;

import asteroid.controller.Controller;
import asteroid.controller.entity.CircularSize;
import asteroid.controller.entity.EntityType;
import asteroid.controller.entity.RectangularSize;
import asteroid.dto.EntityParamsDto;
import asteroid.model.body.Body;
import asteroid.model.body.BodyAsteroid;
import asteroid.model.body.BodyPlayer;
import asteroid.model.body.BodyShot;
import asteroid.physics.BasicPhysicsEngine;
import asteroid.physics.ShipPhysicsEngine;
import asteroid.view.renderable.RendAsteroid;
import asteroid.view.renderable.RendPlayer;
import asteroid.view.renderable.RendShot;
import asteroid.view.renderable.Renderable;
import config.simulation.AsteroidConfig;
import config.simulation.WorldConfig;
import helpers.ss_animation.Sprite;
import helpers.ss_animation.SpriteSheet;
import helpers.ss_animation.SpriteLoader;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

import static java.lang.Thread.sleep;

public class LifeGenerator implements Runnable {
    private Thread thread;
    private Controller controller;
    private boolean running;
    private int delay = 250;
    private final EntityParams entityParams;
    private static final AtomicLong idGenerator = new AtomicLong(0);
    private final HashMap<WorldConfig, Integer> worldConfigs;
    private final HashMap<AsteroidConfig, Integer> asteroidConfigs;

    private final HashMap<EntityType, Sprite> sprites;
    private final String imageFolderPath;
    private final String spriteSheetsFolderPath;

    private final Point2D.Double zero;

    public LifeGenerator(
            Controller controller,
            HashMap<WorldConfig, Integer> worldConfigs,
            HashMap<AsteroidConfig, Integer> asteroidConfigs) {

        this.thread = new Thread();
        this.controller = controller;
        this.running = false;
        this.worldConfigs = worldConfigs;
        worldConfigs.put(WorldConfig.HALF_WIDTH, worldConfigs.get(WorldConfig.WIDTH)/2);
        worldConfigs.put(WorldConfig.HALF_HEIGHT, worldConfigs.get(WorldConfig.HEIGHT)/2);
        this.asteroidConfigs = asteroidConfigs;
        this.entityParams = new EntityParams(
                asteroidConfigs.get(AsteroidConfig.MIN_DIAMETER),
                asteroidConfigs.get(AsteroidConfig.MAX_DIAMETER),
                asteroidConfigs.get(AsteroidConfig.MIN_SPEED),
                asteroidConfigs.get(AsteroidConfig.MAX_SPEED)
        );
        this.sprites = new HashMap<>();
        this.imageFolderPath = "src/img/";
        this.spriteSheetsFolderPath = imageFolderPath + "sprite_sheets/";
        this.zero = new Point2D.Double(0, 0);

        SpriteLoader loader = new SpriteLoader();
        loadImages(loader);
        loadSpriteSheets(loader);
    }

    public void startThread() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void stopThread(){
        running = false;
        if (thread!=null){
            thread.interrupt();
        }
    }

    public void setRunning(boolean b) {
        this.running = b;
    }

    public void setEntityParams(EntityParamsDto params) {
        this.entityParams.setMinDiameter(params.MIN_DIAMETER);
        this.entityParams.setMaxDiameter(params.MAX_DIAMETER);
        this.entityParams.setMinSpeed(params.MIN_SPEED);
        this.entityParams.setMaxDiameter(params.MAX_SPEED);
    }

    @Override
    public void run() {
        while (running) {
            generateRandomEntity();
            try {
                sleep(delay);
            } catch(Exception ignored) {}
        }
    }

    public void generateRandomizedEntityOfType(EntityType entityType) {
        long id = idGenerator.incrementAndGet();
        Body generatedBody = null;
        Renderable generatedRenderable = null;

        if (entityType == EntityType.ASTEROID) {
            CircularSize size = generateRandomCircularSize();
            Point2D.Double speed = generateRandomSpeed();
            Point2D.Double position = generateRandomPositionInMapBorder(speed);

            BasicPhysicsEngine engine = new BasicPhysicsEngine(position, speed, 0, zero, zero);

            generatedBody = new BodyAsteroid(id, EntityType.ASTEROID, size, engine);
            generatedRenderable = new RendAsteroid(id, EntityType.ASTEROID, sprites.get(EntityType.ASTEROID));

        } else if (entityType == EntityType.PLAYER) {
            RectangularSize size = new RectangularSize(new Point2D.Double(30, 25));
            Point2D.Double speed = zero;
            Point2D.Double position = new Point2D.Double(worldConfigs.get(WorldConfig.HALF_WIDTH), worldConfigs.get(WorldConfig.HALF_HEIGHT));
            ShipPhysicsEngine engine = new ShipPhysicsEngine(position, speed, 0, zero, zero, 0.980);

            generatedBody = new BodyPlayer(id, EntityType.PLAYER, size, engine);
            generatedRenderable = new RendPlayer(id, EntityType.PLAYER, sprites.get(EntityType.PLAYER));
        }

        controller.addEntity(generatedBody, generatedRenderable);
    }

    public void generateRandomEntity() {
        // Logic for deciding what kind of entity will be generated
        // Chance (%) of generating an asteroid
        int astChance = 100;
        int result = (int)(Math.random() * 100) +1;
        EntityType decision = null;

        if (0 < result && result <= astChance) {
            decision = EntityType.ASTEROID;
        }

        if (decision == null) {
            System.out.println("Couldn't take a decision while generating an entity in LifeGenerator");

        } else {
            generateRandomizedEntityOfType(decision);
        }
    }

    private int generateRandomNumberBetween2Values(int lower, int higher) {
        return (int) (Math.random() * (higher - lower + 1)) + lower;
    }

    private CircularSize generateRandomCircularSize() {
        return new CircularSize(generateRandomNumberBetween2Values(
                asteroidConfigs.get(AsteroidConfig.MIN_DIAMETER),
                asteroidConfigs.get(AsteroidConfig.MAX_DIAMETER)));
    }

    private Point2D.Double generateRandomSpeed() {
        return new Point2D.Double(
                generateRandomNumberBetween2Values(
                        asteroidConfigs.get(AsteroidConfig.MIN_SPEED),
                        asteroidConfigs.get(AsteroidConfig.MAX_SPEED)
                ),
                generateRandomNumberBetween2Values(
                        asteroidConfigs.get(AsteroidConfig.MIN_SPEED),
                        asteroidConfigs.get(AsteroidConfig.MAX_SPEED)
                )
        );
    }

    private Point2D.Double generateRandomPositionInMapBorder(Point2D.Double speed) {
        Point2D.Double position;
        boolean positiveSpeedX;
        boolean positiveSpeedY;

        positiveSpeedX = (speed.getX() >= 0);
        positiveSpeedY = (speed.getY() >= 0);

        boolean b = Math.random() < 0.5;
        if (positiveSpeedX) {
            if (positiveSpeedY) {
                // (+w, +h)
                position = generateRandomPositionInTopLeftMapBorder(b);
            } else {
                // (+w, -h)
                position = generateRandomPositionInBottomLeftMapBorder(b);
            }
        } else {
            if (positiveSpeedY) {
                // (-w, +h)
                position = generateRandomPositionInTopRightMapBorder(b);
            } else {
                // (-w, -h)
                position = generateRandomPositionInBottomRightMapBorder(b);
            }
        }
        return position;
    }

    private Point2D.Double generateRandomPositionInTopLeftMapBorder(boolean b) {
        int w, h;

        if (b) {
            w = 0;
            h = generateRandomNumberBetween2Values(0, worldConfigs.get(WorldConfig.HALF_HEIGHT));
        } else {
            w = generateRandomNumberBetween2Values(0, worldConfigs.get(WorldConfig.HALF_WIDTH));
            h = 0;
        }
        return new Point2D.Double(w, h);
    }

    private Point2D.Double generateRandomPositionInBottomLeftMapBorder(boolean b) {
        int w, h;

        if (b) {
            w = 0;
            h = generateRandomNumberBetween2Values(worldConfigs.get(WorldConfig.HALF_HEIGHT), worldConfigs.get(WorldConfig.HEIGHT));
        } else {
            w = generateRandomNumberBetween2Values(0, worldConfigs.get(WorldConfig.HALF_WIDTH));
            h = worldConfigs.get(WorldConfig.HEIGHT);
        }
        return new Point2D.Double(w, h);
    }

    private Point2D.Double generateRandomPositionInTopRightMapBorder(boolean b) {
        int w, h;

        if (b) {
            w = worldConfigs.get(WorldConfig.WIDTH);
            h = generateRandomNumberBetween2Values(0, worldConfigs.get(WorldConfig.HALF_HEIGHT));
        } else {
            w = generateRandomNumberBetween2Values(worldConfigs.get(WorldConfig.HALF_WIDTH), worldConfigs.get(WorldConfig.WIDTH));
            h = 0;
        }
        return new Point2D.Double(w, h);
    }

    private Point2D.Double generateRandomPositionInBottomRightMapBorder(boolean b) {
        int w, h;

        if (b) {
            w = worldConfigs.get(WorldConfig.WIDTH);
            h = generateRandomNumberBetween2Values(worldConfigs.get(WorldConfig.HALF_HEIGHT), worldConfigs.get(WorldConfig.HEIGHT));
        } else {
            w = generateRandomNumberBetween2Values(worldConfigs.get(WorldConfig.HALF_WIDTH), worldConfigs.get(WorldConfig.WIDTH));
            h = worldConfigs.get(WorldConfig.HEIGHT);
        }
        return new Point2D.Double(w, h);
    }

    public void generatePlayerShot(long shooterId, Point2D.Double position, double rotationAngle, int totalBulletSpeed) {
        long id = idGenerator.incrementAndGet();
        Point2D.Double speed = calculateSpeedVector(rotationAngle, totalBulletSpeed);
        RectangularSize size = new RectangularSize(new Point2D.Double(3, 3));

        BasicPhysicsEngine engine = new BasicPhysicsEngine(position, speed, rotationAngle, zero, zero);

        BodyShot bodyShot = new BodyShot(id, shooterId, EntityType.SHOT, size, engine);
        RendShot rendShot = new RendShot(id, EntityType.SHOT, sprites.get(EntityType.SHOT));
        controller.addEntity(bodyShot, rendShot);
    }

    public Point2D.Double calculateSpeedVector(double rotationAngle, int bulletSpeed) {
        double velX = bulletSpeed * Math.cos(rotationAngle);
        double velY = bulletSpeed * Math.sin(rotationAngle);

        return new Point2D.Double(velX, velY);
    }

    // =================================== IMAGE PREPARATIONS ===================================

    private void loadImages(SpriteLoader loader) {
        //String asteroidImagePath = imageFolderPath + "asteroid.png";
        //String playerImagePath = imageFolderPath + "spaceship2.png";
        String shotImagePath = imageFolderPath + "yellow_pium_pium.png";

        //sprites.put(EntityType.ASTEROID, loader.loadImage(asteroidImagePath));
        //sprites.put(EntityType.PLAYER, loader.loadImage(playerImagePath));
        sprites.put(EntityType.SHOT, loader.loadImage(shotImagePath));
    }

    // =================================== SPRITE SHEET PREPARATIONS ===================================

    private void loadSpriteSheets(SpriteLoader loader) {
        String ssPath;

        // Explosion
        ssPath = spriteSheetsFolderPath + "ss_explosion.png";
        loadSpriteSheet(loader, EntityType.EXPLOSION, ssPath, 1, 9, 100);

        // Player
        ssPath = spriteSheetsFolderPath + "ss_spaceship_reducted.png";
        loadSpriteSheet(loader, EntityType.PLAYER, ssPath, 1, 5, 100);

        // Asteroid
        ssPath = spriteSheetsFolderPath + "ss_asteroid2.png";
        loadSpriteSheet(loader, EntityType.ASTEROID, ssPath, 5, 4, 100);
    }

    private void loadSpriteSheet(SpriteLoader loader, EntityType entityType, String ssPath,
                                 int ssRows, int ssCols, int ssDelay) {
        SpriteSheet ss = loader.loadSpriteSheet(ssPath, ssRows, ssCols, ssDelay);
        sprites.put(entityType, ss);
    }
}
