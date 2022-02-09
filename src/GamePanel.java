import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Marketa Halvova
 */
public class GamePanel extends JPanel implements ActionListener {

    private static final int SCREEN_WIDTH = 600;
    private static final int SCREEN_HEIGHT = 600;
    private static final int UNIT_SIZE = 25; // 25x25
    private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    private static final int DELAY = 75; // for timer
    private final int[] x = new int[GAME_UNITS+1];
    private final int[] y = new int[GAME_UNITS+1];
    private int bodyParts;
    private int foodEaten;
    private int foodX;
    private int foodY;
    private char direction;
    private boolean running = false;
    private boolean pause = false;
    private boolean newGame = true;
    private Timer timer = null;
    private final Random random = new Random();

    /**
     *
     * @return true  - if game is running
     *         false - if game is not running
     */
    public boolean isRunning() {
        return running;
    }

    /**
     *
     * @return true  - if game is paused
     *         false - if game is not paused
     */
    public boolean isPause() {
        return pause;
    }

    /**
     *
     * @return timer
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * sets running
     *
     * @param running - true if game is running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * sets pause
     *
     * @param pause - true if game is paused
     */
    public void setPause(boolean pause) {
        this.pause = pause;
    }

    /**
     * starts timer
     */
    public void startTimer() {
        this.timer.start();
    }

    /**
     * stops timer
     */
    public void stopTimer() {
        this.timer.stop();
    }

    /**
     * sets size and color of panel, adds key listener, sets panel focusable and sets new game
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT+23)); // set panel size (+23 for menu bar)
        this.setBackground(Color.black); // set panel color
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        setNewGame();
    }

    /**
     * sets new game (zeros arrays x,y; sets default snake setting)
     */
    public void setNewGame() {
        Arrays.fill(this.x, 0);
        Arrays.fill(this.y, 0);
        this.bodyParts = 6;
        this.foodEaten = 0;
        this.direction = 'R';
    }

    /**
     * starts first game
     */
    public void startGame() {
        newFood();
        running = true;
        newGame = false;
        this.timer = new Timer(DELAY, this); // delay (initial delay,  delay between events), whose timer it is
        this.timer.start();
    }

    /**
     * if pause   - draws pause screen
     *    running - draws game screen
     *    newGame - draws start screen
     *
     * @param graphics - the Graphics object
     */
    public void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (pause) {
            drawPause(graphics);
        } else if (running) {
            drawGame(graphics);
        } else if (newGame){
            drawStart(graphics);
        } else {
            gameOver(graphics);
        }
    }

    private void drawStart(Graphics graphics) {
        graphics.setColor(Color.green);
        graphics.setFont(new Font("Showcard Gothic", Font.BOLD, 45));
        FontMetrics fontMetrics = getFontMetrics(graphics.getFont());

        graphics.drawString("Welcome",
                (SCREEN_WIDTH - fontMetrics.stringWidth("Welcome"))/2,
                SCREEN_HEIGHT / 2 - 3 * graphics.getFont().getSize());
        graphics.drawString("For new game go to",
                (SCREEN_WIDTH - fontMetrics.stringWidth("For new game go to"))/2,
                SCREEN_HEIGHT / 2 - graphics.getFont().getSize());
        graphics.drawString("Game -> New game",
                (SCREEN_WIDTH - fontMetrics.stringWidth("Game -> New game"))/2,
                SCREEN_HEIGHT / 2);
        graphics.drawString("You may press \"P\"",
                (SCREEN_WIDTH - fontMetrics.stringWidth("You may press \"P\""))/2,
                SCREEN_HEIGHT / 2 + 2 * graphics.getFont().getSize());
        graphics.drawString("for pause game",
                (SCREEN_WIDTH - fontMetrics.stringWidth("for pause game"))/2,
                SCREEN_HEIGHT / 2 + 3 * graphics.getFont().getSize());
    }

    private void drawPause(Graphics graphics) {
        graphics.setColor(Color.green);
        graphics.setFont(new Font("Showcard Gothic", Font.BOLD, 40));
        FontMetrics fontMetrics = getFontMetrics(graphics.getFont());

        graphics.drawString("Game paused",
                (SCREEN_WIDTH - fontMetrics.stringWidth("Game paused"))/2,
                SCREEN_HEIGHT / 2);
    }

    private void drawGame (Graphics graphics) {
        if (running) {
            //snake
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    graphics.setColor(new Color(0,255,51));
                } else {
                    graphics.setColor(new Color(0,153,0));
                }
                graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // food
            graphics.setColor(Color.red);
            graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            // score
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Rage Italic", Font.BOLD, 40));
            FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Score: " + foodEaten,
                    (SCREEN_WIDTH - fontMetrics.stringWidth("Score: " + foodEaten))/2,
                    graphics.getFont().getSize());
        }
    }

    private void newFood() {
        foodX = random.nextInt( (SCREEN_WIDTH/UNIT_SIZE)) * UNIT_SIZE; // food on "grid" - x axis
        foodY = random.nextInt( (SCREEN_HEIGHT/UNIT_SIZE)) * UNIT_SIZE; // food on "grid" - y axis
    }

    private void checkBodyLength() {
        if (bodyParts == GAME_UNITS) {
            running = false;
        }
        if (!running) {
            this.timer.stop();
        }
    }

    private void move() {
        //body
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        //head
        switch (direction) {
            case 'U':
                if (y[0] == 0) {
                    y[0] = SCREEN_HEIGHT - UNIT_SIZE;
                } else {
                    y[0] = y[0] - UNIT_SIZE;
                }
                break;
            case 'D':
                if (y[0] == SCREEN_HEIGHT - UNIT_SIZE) {
                    y[0] = 0;
                } else {
                    y[0] = y[0] + UNIT_SIZE;
                }
                break;
            case 'L':
                if (x[0] == 0) {
                    x[0] = SCREEN_WIDTH - UNIT_SIZE;
                } else {
                    x[0] = x[0] - UNIT_SIZE;
                }
                break;
            case 'R':
                if (x[0] == SCREEN_WIDTH - UNIT_SIZE) {
                    x[0] = 0;
                } else {
                    x[0] = x[0] + UNIT_SIZE;
                }
                break;
        }
    }

    private void checkFood() {
        if ((x[0] == foodX) && y[0] == foodY) {
            bodyParts++;
            foodEaten++;
            newFood();
        }
    }

    private void checkCollisions() {
        for (int i = bodyParts - 1; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) { // snake ate his body
                running = false;
                break;
            }
        }

        if (!running) {
            this.timer.stop();
        }
    }

    private void gameOver(Graphics graphics) {
        // score
        graphics.setColor(Color.red);
        graphics.setFont(new Font("Rage Italic", Font.BOLD, 40));
        FontMetrics fontMetrics = getFontMetrics(graphics.getFont());
        graphics.drawString("Score: " + foodEaten,
                (SCREEN_WIDTH - fontMetrics.stringWidth("Score: " + foodEaten))/2,
                graphics.getFont().getSize());

        if (bodyParts == GAME_UNITS) { // you won
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Showcard Gothic", Font.BOLD, 75));
            fontMetrics = getFontMetrics(graphics.getFont());
            graphics.drawString("YOU WON!", (SCREEN_WIDTH - fontMetrics.stringWidth("YOU WON!"))/2,
                    SCREEN_WIDTH/2);
        } else { // game over
            graphics.setColor(Color.red);
            graphics.setFont(new Font("Showcard Gothic", Font.BOLD, 75));
            fontMetrics = getFontMetrics(graphics.getFont());
            graphics.drawString("Game Over :(", (SCREEN_WIDTH - fontMetrics.stringWidth("Game Over :("))/2,
                    SCREEN_WIDTH/2);
        }
        setNewGame();
    }

    private class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed (KeyEvent keyEvent) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
                case KeyEvent.VK_P:
                    if (pause) {
                        pause = false;
                        running = true;
                        timer.start();
                    } else {
                        timer.stop();
                        running = false;
                        pause = true;
                        repaint();
                    }
                    break;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        if (running) {
            checkBodyLength();
            move();
            checkFood();
            checkCollisions();
        }
        repaint();
    }
}
