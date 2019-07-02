import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;


public class SnakePanel extends JPanel implements KeyListener, ActionListener {
    private static final int NUMROWS = 40;
    private static final int NUMCOLS = 40;
    //Snake:
    private ArrayList<Point> snake;
    private enum Direct{NORTH, SOUTH, EAST, WEST, NONE};
    private Direct curDirect;
    private Point apple;
    private int growBody;
    private boolean gameOver;

    public SnakePanel(){
        snake = new ArrayList<Point>();
        resetGame();
    }

    public void resetGame(){
        snake.clear();
        int x = (int)(Math.random()*NUMCOLS);
        int y = (int)(Math.random()*NUMROWS);
        Point p = new Point(x,y);
        snake.add(p);
        curDirect = Direct.NONE;
        createApple();
        growBody = 0;
        gameOver = false;
        repaint();
    }

    private void createApple(){
        while(true){
            int x = (int)(Math.random() * NUMCOLS);
            int y = (int)(Math.random() * NUMROWS);
            boolean isInSnake = false;
            for(Point p : snake){
                if(p.x == x && p.y == y){
                    isInSnake = true;
                }
            }
            if(!isInSnake){
                apple = new Point(x, y);
                break;
            }
        }
    }


    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Font gOver = new Font("Comic Sans MS", Font.BOLD, 20);
        Font scoreF = new Font("Century Gothic Bold", Font.BOLD, 20);
        int tileWidth = this.getWidth()/NUMCOLS;
        int tileHeight = this.getHeight()/NUMCOLS;
        g.setColor(Color.black);
        g.fillRect(0,0,this.getWidth(),this.getHeight());
        //Snake
        for(int i = 0; i < snake.size();i++){
            Point p = snake.get(i);
            //int red = (int) (Math.random()*257);
            //int green = (int) (Math.random()*256);
            //int blue = (int) (Math.random()*257);
            g.setColor(Color.blue);
            g.fillRect(tileWidth * p.x, tileHeight * p.y, tileWidth-1, tileHeight - 1);
        }
        g.setColor(Color.YELLOW);
        g.fillOval(tileWidth*apple.x, tileHeight*apple.y, tileWidth, tileHeight);
        if(gameOver){
            g.setColor(Color.WHITE);
            g.setFont(gOver);
            g.drawString("Game Over", this.getWidth()/2 - 100,this.getHeight()/2);
            g.drawString("Press the space bar to play again", 10, 150);
        }
        drawEyes(g);
        //Draw string for score
        g.setColor(Color.WHITE);
        g.setFont(scoreF);
        g.drawString("Score: " + snake.size(), 20, this.getHeight() - 40);
    }
    public void drawEyes(Graphics g){
        Point eye1 = null, eye2 = null;
        int tileWidth = this.getWidth()/NUMCOLS;
        int tileHeight = this.getHeight()/NUMCOLS;
        int offSetx = tileWidth/6;
        int offSety = tileHeight/6;
        switch(curDirect){
            case NONE:
            case NORTH:
                eye1 = new Point(tileWidth/2 - offSetx, offSety*2);
                eye2 = new Point(tileWidth/2 + offSetx, offSety*2);
                break;
            case SOUTH:
                eye1 = new Point(tileWidth/2 - offSetx, tileHeight - offSety*2);
                eye2 = new Point(tileWidth/2 + offSetx, tileHeight - offSety*2);
                break;
            case WEST:
                eye1 = new Point(offSetx*2, tileHeight/2 + offSety*2);
                eye2 = new Point(offSetx*2, tileHeight/2 - offSety*2);
                break;
            case EAST:
                eye1 = new Point(tileWidth - offSetx*2, tileHeight/2 + offSety*2);
                eye2 = new Point(tileWidth - offSetx*2, tileHeight/2 - offSety*2);
                break;
        }
        Point head = snake.get(0);
        g.drawRect(eye1.x + head.x*tileWidth, eye1.y + head.y*tileHeight,  1, 1);
        g.drawRect(eye2.x + head.x*tileWidth, eye2.y + head.y*tileHeight,  1, 1);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if(gameOver){
            return;
        }
        //every timer event call will update the position of the snake
        Point newHead = null;
        Point curHead = snake.get(0);
        switch(curDirect){
            case NORTH:
                newHead = new Point(curHead.x, curHead.y - 1);
                break;
            case SOUTH:
                newHead = new Point(curHead.x, curHead.y + 1);
                break;
            case WEST:
                newHead = new Point(curHead.x-1, curHead.y);
                break;
            case EAST:
                newHead = new Point(curHead.x + 1, curHead.y);
                break;
            case NONE:
                return;
        }
        if(newHead.x <= 0 || newHead.y <= 0 || newHead.x > NUMCOLS || newHead.y > NUMROWS){
            this.gameOver = true;
        }
        snake.add(0, newHead);
        if(growBody > 0){
            growBody--;
        }else{
            snake.remove(snake.size()-1);
        }
        this.repaint();
        if(newHead.x == apple.x && newHead.y == apple.y){
            createApple();
            growBody = 4;
        }
        //if new head i on body of snake, game over
        for(int i = 1; i < snake.size(); i++){
            Point p = snake.get(i);
            if(newHead.x == p.x && newHead.y == p.y){
                this.gameOver = true;
                return;
            }
        }
    }
    @Override
    public void keyPressed(KeyEvent e) {
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP:
                curDirect = Direct.NORTH;
                break;
            case KeyEvent.VK_DOWN:
                curDirect = Direct.SOUTH;
                break;
            case KeyEvent.VK_RIGHT:
                curDirect = Direct.EAST;
                break;
            case KeyEvent.VK_LEFT:
                curDirect = Direct.WEST;
                break;
            case KeyEvent.VK_SPACE:
                if(this.gameOver){
                    resetGame();
                }
                break;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
