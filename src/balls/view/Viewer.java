package balls.view;

import balls.dto.Position;
import balls.model.Ball;
import balls.model.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Viewer extends Canvas implements Runnable {
    private Thread thread;
    private final View view;
    private long lastSecondMillisecond;
    private int timesIteratedInLastSecond;
    private volatile boolean running=false;
    private BufferStrategy bufferStrategy;
    private Dimension cursorPosition;
    private Image asteoridImage;

    public Viewer(View view) {
        this.view = view;
        setBackground(Color.WHITE);
        setIgnoreRepaint(true);

        addShipMovementListener(this);
        addMouseMovementListener(this);
        addMouseClickListener(this);
        this.cursorPosition = new Dimension(0, 0);
        asteoridImage=new ImageIcon("src/img/asteorid.png").getImage();
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
                    g2.setColor(getBackground());
                    g2.fillRect(0, 0, getWidth(), getHeight());

                    // Draw room
                    //paintRectangle(g2);

                    // Draw all balls
                    synchronized (view.getAllBalls()) {
                        for (Ball ball : view.getAllBalls()) {
                            paintBall(ball, g2);
                        }
                    }

                    // Draw all players
                    paintPlayer(g2);
                }
            } catch (Exception e) {
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

    private void paintBall(Ball ball, Graphics2D g) {
        int diameter = ball.getDIAMETER();
        int radius = Math.round((float) diameter /2);
        Dimension topLeftCornerOfBall = new Dimension((int)ball.getPosition().getWidth() - radius, (int)ball.getPosition().getHeight() - radius);

        g.setColor(ball.getCOLOR());


        //g.fillOval(topLeftCornerOfBall.width, topLeftCornerOfBall.height, diameter, diameter);
        g.drawImage(asteoridImage,
                topLeftCornerOfBall.width,
                topLeftCornerOfBall.height,
                diameter, diameter,null);
    }

    private void paintPlayer(Graphics2D g) {
        // Ship position
        int px = view.getPlayerPosition().width;
        int py = view.getPlayerPosition().height;

        // Ship size
        int w = view.getPlayerSize().width;
        int h = view.getPlayerSize().height;

        //Calculate rotation angle for the player
        double rotation = view.getPlayerRotationAngle() + Math.toRadians(90);

        // Keep g status for painting
        Graphics2D g2 = (Graphics2D) g.create();

        // Rotar arround the center of the ship
        g2.rotate(rotation, px, py);

        // Draw the ship centered in its position
        int[] xPoints = {px, px + w / 2, px, px - w / 2};
        int[] yPoints = {py - h / 2, py + h / 2, py + h/3, py + h / 2};

        g2.setColor(Color.GREEN);
        g2.fillPolygon(xPoints, yPoints, 4);

        g2.dispose();
    }

    //private void paintRectangle(Graphics2D g) {
        //ArrayList<Room> rooms = view.getAllRooms();

        //for (Room room : rooms) {
            //Graphics2D g2 = (Graphics2D) g;
            //if (room.getIsOccupied()) {
                //g2.setColor(Color.RED);
                //g2.fillRect(room.getPosition().width, room.getPosition().height, room.getSize().width, room.getSize().height);
            //}
            //g2.setColor(Color.BLUE);
            //g2.setStroke(new BasicStroke(3));
            //g2.drawRect(room.getPosition().width, room.getPosition().height, room.getSize().width, room.getSize().height);

        //}

    //}

    // ----------- DATA PANEL UPDATE

    private void updateDataPanelIfShould() {
        if (this.lastSecondMillisecond +1000 < System.currentTimeMillis()) {
            //If the second has changed
            view.updateFPS(timesIteratedInLastSecond);
            view.updateRenderTime((double) Math.round(((double) 1000 /timesIteratedInLastSecond) * 1000.0) / 1000.0);
            view.updateBallCount(view.getAllBalls().size());

            timesIteratedInLastSecond = 0;
            this.lastSecondMillisecond = System.currentTimeMillis();

        }
    }

    // ----------------------------------- MOUSE & KEYBOARD LISTENERS -----------------------------------

    private void addShipMovementListener(Viewer viewer) {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!viewer.running) return;

                switch (e.getKeyCode()) {
                    case 87: // W
                        view.setPlayerMovingUp(true);
                        break;
                    case 65: // A
                        view.setPlayerMovingLeft(true);
                        break;
                    case 83: // S
                        view.setPlayerMovingDown(true);
                        break;
                    case 68: // D
                        view.setPlayerMovingRight(true);
                        break;
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
                if (!viewer.running) return;

                switch (e.getKeyCode()) {
                    case 87: // W
                        view.setPlayerMovingUp(false);
                        break;
                    case 65: // A
                        view.setPlayerMovingLeft(false);
                        break;
                    case 83: // S
                        view.setPlayerMovingDown(false);
                        break;
                    case 68: // D
                        view.setPlayerMovingRight(false);
                        break;
                }
            }
            @Override
            public void keyTyped(KeyEvent e) {}
        });
    }

    private void addMouseMovementListener(Viewer viewer) {
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
            if (!viewer.running) return;

            int x = e.getX();
            int y = e.getY();

            Dimension mouseP = new Dimension(x, y);
            viewer.setCursorPosition(mouseP);
            //view.calcPlayerRotation(mouseP);

            }
        });
    }

    private void addMouseClickListener(Viewer viewer) {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!viewer.running) return;
                System.out.println("Click en: (" + e.getX() + ", " + e.getY() + ")");
                System.out.println("Botón: " + e.getButton());
            }

            @Override
            public void mousePressed(MouseEvent e) {
                if (!viewer.running) return;
                System.out.println("Presionado en: (" + e.getX() + ", " + e.getY() + ")");
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (!viewer.running) return;
                System.out.println("Soltado en: (" + e.getX() + ", " + e.getY() + ")");
            }
        });

    }

    // -------------------------------- VIEWER STATUS MODIFIERS --------------------------------

    public void startViewer() {
        //if(view.getAllRooms().isEmpty()){
            //view.addRoom(new Position(50, 50), new Dimension(150, 120));
            //view.addRoom(new Position(100, 300), new Dimension(150, 120));
        //}
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

    public Dimension getCursorPosition() {
        return this.cursorPosition;
    }

    public void setCursorPosition(Dimension postion) {
        this.cursorPosition = postion;
    }

    // ------------------------------------- LINKING METHODS -------------------------------------

    public boolean getRunning() {
        return running;
    }


}
