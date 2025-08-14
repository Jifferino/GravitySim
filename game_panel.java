import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;

public class gamePanel extends JPanel implements Runnable{

    public static ArrayList<object> Objects = new ArrayList<>();

    //SCREEN SETTINGS
    final static int originalTileSize = 16; //16x16 tile
    final static int scale = 3;

    final static int tileSize = originalTileSize * scale; //48x48 tile
    final static int maxScreenCol = 50;// 2400 x pixels
    final static int maxScreenRow = 40;// 1920 y pixels
    final static int screenWidth = tileSize * maxScreenCol;
    final static int screenHeight = tileSize * maxScreenRow;
    final int numObjects = 50; //TOTAL NUMBE OF OBJECTS
    final int normalRadius = 10;
    final int bigRadius = 50; //RADIUS OF BIGGER PARTICLE

    //FPS
    int FPS = 60;

    Thread gameThread;

    public gamePanel() {
        
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
    }

    public void startGameThread(){

        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run(){
        
        float randx;
        float randy;

        for(int i = 0; i < numObjects; i++ ){
            randx = (float) Math.random() * 800 + 800;
            randy = (float) Math.random() * 800 + 400;

            vector pos = new vector(randx, randy);
            Objects.add(new object((float)(1e4), pos, normalRadius));
            }

            vector center = new vector(screenWidth/2, screenHeight/2);
            Objects.add(new object((float)(1e12), center, bigRadius)); //BIG PARTICLE, ACTS LIKE A SUN OF SORTS- CAN CHANGE THE BIGRADIUS ABOVE

            double drawInterval = 1000000000/FPS;
            double delta = 0;
            long lastTime = System.nanoTime();
            long currentTime;

        while(gameThread != null){

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;

            lastTime = currentTime;

            if(delta >= 1){
                update(delta);
                repaint();
                delta--;
            }

        }

        // double drawInterval = 1000000000/FPS; //0.01666 seconds
        // double nextDrawTime = System.nanoTime() + drawInterval;

        // while(gameThread != null){

        //     // 1 UPDATE: update information such as character positions
        //     update(drawInterval);

        //     //2 DRAW: draw the screen with update information.
        //     repaint();

        //     try {
        //         double remainingTime = nextDrawTime - System.nanoTime();
        //         remainingTime = remainingTime/1000000;

        //         if(remainingTime < 0){
        //             remainingTime = 0;
        //         }

        //         Thread.sleep((long) remainingTime);

        //         nextDrawTime += drawInterval;

        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }
    }
    public void update(double drawInt){
        for(object obj : Objects){
            obj.updatePos(drawInt);
            obj.updateConstraint();
            obj.collisions(); //COLLISIONS - VERY BUGGY
        }
    }
    //@Override
    public void paintComponent(Graphics g){

        super.paintComponent(g);

        Graphics2D graphics = (Graphics2D)g;

        graphics.setColor(Color.white);

        graphics.draw(new Ellipse2D.Float(screenWidth/2 - 1000, screenHeight/2 - 1000, 2000, 2000));

        for(object obj : Objects){
            if (obj.self != null) {
                graphics.fill(obj.self);
            }
        }

    }

}
