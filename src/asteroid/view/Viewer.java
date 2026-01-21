package asteroid.view;

import asteroid.controller.entity.CircularSize;
import asteroid.controller.entity.EntityType;
import asteroid.controller.entity.RectangularSize;
import asteroid.dto.BodyDto;
import asteroid.dto.ShipMovementDto;
import asteroid.view.renderable.RenderableSS;
import asteroid.view.renderable.SelfUpdatingRenderableSS;
import config.player.ControlConfig;
import asteroid.view.renderable.Renderable;
import config.simulation.WorldConfig;
import helpers.CardinalDirection;
import helpers.ss_animation.Sprite;
import helpers.ss_animation.SpriteLoader;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class Viewer extends Canvas implements Runnable, KeyListener, MouseMotionListener, MouseListener {
    private Thread thread;
    private final View view;
    private final int mapWidth;
    private final int height;
    private long lastSecondMillisecond;
    private int timesIteratedInLastSecond;
    private volatile boolean running=false;
    private BufferStrategy bufferStrategy;
    private final HashMap<EntityType, CopyOnWriteArrayList<Renderable>> renderables;
    private final Sprite backgroundImage;
    private final HashMap<ControlConfig, String> playerControlConfigs;
    private final HashMap<WorldConfig, Integer> worldConfigs;

    public Viewer(View view,
                  HashMap<ControlConfig, String> playerControlConfigs,
                  HashMap<WorldConfig, Integer> worldConfigs) {
        this.view = view;
        this.playerControlConfigs = playerControlConfigs;
        this.worldConfigs = worldConfigs;

        this.renderables = new HashMap<>();
        this.renderables.put(EntityType.ASTEROID, new CopyOnWriteArrayList<>());
        this.renderables.put(EntityType.PLAYER, new CopyOnWriteArrayList<>());
        this.renderables.put(EntityType.PLANET, new CopyOnWriteArrayList<>());
        this.renderables.put(EntityType.SHOT, new CopyOnWriteArrayList<>());

        setIgnoreRepaint(true);
        this.mapWidth = worldConfigs.get(WorldConfig.WIDTH);
        this.height = worldConfigs.get(WorldConfig.HEIGHT);
        this.addKeyListener(this);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);

        this.setFocusable(true);
        this.requestFocus();

        SpriteLoader sl = new SpriteLoader();
        backgroundImage = sl.loadImage("src/img/purple_space.png");
    }

    // ---------------------------------------- VIEWER WORKING LOGIC ----------------------------------------
    // ------------ CANVAS PAINTING

    @Override
    public void run() {
        this.lastSecondMillisecond = System.currentTimeMillis();
        while(getBufferStrategy() == null){
            createBufferStrategy(2);
            bufferStrategy = getBufferStrategy();
        }
        while (running) {
            Graphics g = null;
            try {
                g = bufferStrategy.getDrawGraphics();
                if(g!=null) {
                    Graphics2D g2 = (Graphics2D) g;

                    // Paint background (clear the frame)
                    g2.drawImage(backgroundImage.getSprite(), 0, 0, worldConfigs.get(WorldConfig.WIDTH), worldConfigs.get(WorldConfig.HEIGHT), null);

                    // Draw all asteroids
                    ArrayList<BodyDto> bodiesForPaint;
                    bodiesForPaint = view.getAllBodyDtosByType(EntityType.ASTEROID);
                    paintBodies(g2, bodiesForPaint);

                    // Draw all players
                    bodiesForPaint = view.getAllBodyDtosByType(EntityType.PLAYER);
                    paintBodies(g2, bodiesForPaint);

                    // Draw all shots
                    bodiesForPaint = view.getAllBodyDtosByType(EntityType.SHOT);
                    paintBodies(g2, bodiesForPaint);
                }
            } catch (IllegalArgumentException e) {
                System.err.println(e);
            } finally {
                if (g != null) g.dispose();
                bufferStrategy.show();
            }

            try{
                Thread.sleep(10);
            } catch (InterruptedException ignored){
            }
            timesIteratedInLastSecond += 1;
            updateDataPanelIfShould();
        }
    }

    private void paintBodies(Graphics2D g, ArrayList<BodyDto> bodies) {
        if (bodies.isEmpty()) return;

        for (BodyDto body : bodies) {
            Graphics2D g2 = (Graphics2D) g.create();

            // Image position
            double px = body.position.x;
            double py = body.position.y;

            //Calculate rotation angle for the image of the body
            double rotation = body.rotationAngle + Math.toRadians(90);

            // Rotate around the center of the body
            g2.rotate(rotation, px, py);

            // Ship size
            int w = 0;
            int h = 0;

            switch (body.size.getType()) {
                case RECTANGULAR:
                    w = (int) ((RectangularSize) body.size).getValues().getX();
                    h = (int) ((RectangularSize) body.size).getValues().getY();
                    break;

                case CIRCULAR:
                    w = (int) ((CircularSize) body.size).getDiameter();
                    h = w;
                    break;

                case VECTORIAL:
                    System.out.println("VECTORIAL sizes not implemented");
                    break;

                case COMPOSED:
                    System.out.println("COMPOSED sizes not implemented");
                    break;

            }
            double x = px - w/2;
            double y = py - h/2;

            Renderable renderableOfBody = null;
            CopyOnWriteArrayList<Renderable> renderablesOfBodyType = this.renderables.get(body.type);
            for (Renderable renderable : renderablesOfBodyType) {
                if (body.entityId == renderable.getEntityId()) {
                    renderableOfBody = renderable;
                    break;
                }
            }


            try {
                Image bodyImage = renderableOfBody.getImage();
                g2.drawImage(bodyImage, (int) x, (int) y, w, h,null);
            } catch (NullPointerException e) {
                System.out.println("Couldn't draw entity because it got deleted");
            }

            g2.dispose();
        }
    }

    // ----------- DATA PANEL UPDATE

    private void updateDataPanelIfShould() {
        if (this.lastSecondMillisecond +1000 < System.currentTimeMillis()) {
            //If the second has changed
            view.updateFPS(timesIteratedInLastSecond);
            view.updateRenderTime((double) Math.round(((double) 1000 /timesIteratedInLastSecond) * 1000.0) / 1000.0);
            view.updateAsteroidCount(view.getAllBodyDtosByType(EntityType.ASTEROID).size());

            timesIteratedInLastSecond = 0;
            this.lastSecondMillisecond = System.currentTimeMillis();

        }
    }

    // ================================== CONTROLS ==================================
    // ============ KEYBOARD

    private int getKeyCodeSafe(ControlConfig action) { // Asumo que es PlayerAction, o ControlConfig
        String value = playerControlConfigs.get(action);

        if (value == null || value.startsWith("MOUSE")) {
            return -1;
        }

        // If it is a simple letter (Ex: "W", "D", "A")
        if (value.length() == 1) {
            char caracter = value.charAt(0);
            return KeyEvent.getExtendedKeyCodeForChar(caracter);
        }

        // If it's a numeric code (Ex: "32" (SPACE)), "10" (ENTER))
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!this.running) return;
        int code = e.getKeyCode();

        int moveUpCode = getKeyCodeSafe(ControlConfig.MOVE_UP);
        int moveDownCode = getKeyCodeSafe(ControlConfig.MOVE_DOWN);
        int moveLeftCode = getKeyCodeSafe(ControlConfig.MOVE_LEFT);
        int moveRightCode = getKeyCodeSafe(ControlConfig.MOVE_RIGHT);
        int shootCode = getKeyCodeSafe(ControlConfig.SHOOT);

        if (code == moveUpCode) {
            view.setPlayerMoving(1, true, CardinalDirection.NORTH);

        } else if (code == moveDownCode) {
            view.setPlayerMoving(1, true, CardinalDirection.SOUTH);

        } else if (code == moveLeftCode) {
            view.setPlayerMoving(1, true, CardinalDirection.WEST);

        } else if (code == moveRightCode) {
            view.setPlayerMoving(1, true, CardinalDirection.EAST);

        } else if (code == shootCode) {
            view.setPlayerShooting(1, true);

        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!this.running) return;
        int code = e.getKeyCode();

        int moveUpCode = getKeyCodeSafe(ControlConfig.MOVE_UP);
        int moveDownCode = getKeyCodeSafe(ControlConfig.MOVE_DOWN);
        int moveLeftCode = getKeyCodeSafe(ControlConfig.MOVE_LEFT);
        int moveRightCode = getKeyCodeSafe(ControlConfig.MOVE_RIGHT);
        int shootCode = getKeyCodeSafe(ControlConfig.SHOOT);

        if (code == moveUpCode) {
            view.setPlayerMoving(1, false, CardinalDirection.NORTH);

        } else if (code == moveDownCode) {
            view.setPlayerMoving(1, false, CardinalDirection.SOUTH);

        } else if (code == moveLeftCode) {
            view.setPlayerMoving(1, false, CardinalDirection.WEST);

        } else if (code == moveRightCode) {
            view.setPlayerMoving(1, false, CardinalDirection.EAST);

        } else if (code == shootCode) {
            view.setPlayerShooting(1, false);

        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    // ============= MOUSE

    @Override
    public void mouseDragged(MouseEvent e) {
        if (!running) return;

        int x = e.getX();
        int y = e.getY();

        Point2D.Double mouseP = new Point2D.Double(x, y);
        view.calcRotationAngleOfPlayer(mouseP, 1);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (!running) return;

        int x = e.getX();
        int y = e.getY();

        Point2D.Double mouseP = new Point2D.Double(x, y);
        view.calcRotationAngleOfPlayer(mouseP, 1);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (!running) return;

        for (Map.Entry<ControlConfig, String> entry : playerControlConfigs.entrySet()) {
            if (entry.getValue().equals("MOUSE1")) {
                switch (entry.getKey()) {
                    case MOVE_UP -> view.setPlayerMoving(1, true, CardinalDirection.NORTH);
                    case MOVE_DOWN -> view.setPlayerMoving(1, true, CardinalDirection.SOUTH);
                    case MOVE_LEFT -> view.setPlayerMoving(1, true, CardinalDirection.WEST);
                    case MOVE_RIGHT -> view.setPlayerMoving(1, true, CardinalDirection.EAST);
                    case SHOOT -> view.setPlayerShooting(1, true);
                }
                break;
            }
        }

        // System.out.println("Presionado en: (" + e.getX() + ", " + e.getY() + ")");
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!running) return;

        for (Map.Entry<ControlConfig, String> entry : playerControlConfigs.entrySet()) {
            if (entry.getValue().equals("MOUSE1")) {
                switch (entry.getKey()) {
                    case MOVE_UP -> view.setPlayerMoving(1, false, CardinalDirection.NORTH);
                    case MOVE_DOWN -> view.setPlayerMoving(1, false, CardinalDirection.SOUTH);
                    case MOVE_LEFT -> view.setPlayerMoving(1, false, CardinalDirection.WEST);
                    case MOVE_RIGHT -> view.setPlayerMoving(1, false, CardinalDirection.EAST);
                    case SHOOT -> view.setPlayerShooting(1, false);
                }
                break;
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    // -------------------------------- VIEWER STATUS MODIFIERS --------------------------------

    public void startViewer() {
        running = true;
        thread = new Thread(this);
        thread.start();
    }

    public void pauseViewer(){
        running = false;
        thread.interrupt();

        try {
            thread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        thread = null;
    }

    public void restartViewer(){
        pauseViewer();
        startViewer();
    }

    // ------------------------------------- GETTERS & SETTERS -------------------------------------

    public Thread getThread() {
        return this.thread;
    }

    // ------------------------------------- LINKING METHODS -------------------------------------

    public boolean getRunning() {
        return running;
    }

    public ShipMovementDto getShipMovementDtoOfPlayer(long entityId) {
        return view.getShipMovementDtoOfPlayer(entityId);
    }

    // ======================= RENDERABLE

    public void addRenderable(Renderable renderable) {
        renderable.setViewer(this);
        boolean added = false;

        switch (renderable.getType()) {
            case ASTEROID:
                renderables.get(EntityType.ASTEROID).add(renderable);
                added = true;
                break;

            case PLAYER:
                renderables.get(EntityType.PLAYER).add(renderable);
                added = true;
                break;

            case PLANET:
                renderables.get(EntityType.PLANET).add(renderable);
                added = true;
                break;

            case SHOT:
                renderables.get(EntityType.SHOT).add(renderable);
                added = true;
                break;

        }
        if (!added) throw new NullPointerException("Could not add entity to the Viewer's entities list");
        if (renderable instanceof SelfUpdatingRenderableSS) ((SelfUpdatingRenderableSS) renderable).startThread();
    }

    public void deleteRenderable(EntityType type, long entityId) {
        CopyOnWriteArrayList<Renderable> iteratedList = renderables.get(type);

        for (Renderable renderable : iteratedList) {
            if (renderable.getEntityId() == entityId) iteratedList.remove(renderable);
        }
    }

    public void deleteAllRenderables() {
        for (Map.Entry<EntityType, CopyOnWriteArrayList<Renderable>> entry : renderables.entrySet()) {
            entry.getValue().clear();
        }
    }

    public void startAllRenderableSS() {
        for (Map.Entry<EntityType, CopyOnWriteArrayList<Renderable>> entry : renderables.entrySet()) {
            for (Renderable renderable : entry.getValue()) {
                if (renderable instanceof RenderableSS) ((RenderableSS) renderable).startThread();
            }
        }
    }

    public void stopAllRenderableSS() {
        for (Map.Entry<EntityType, CopyOnWriteArrayList<Renderable>> entry : renderables.entrySet()) {
            for (Renderable renderable : entry.getValue()) {
                if (renderable instanceof RenderableSS) ((RenderableSS) renderable).stopThread();
            }
        }
    }

}
