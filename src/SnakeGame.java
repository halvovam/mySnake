import java.awt.*;

/**
 * @author Marketa Halvova
 */
public class SnakeGame {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            GameFrame s = new GameFrame();
            s.setVisible(true);
        });
    }

}