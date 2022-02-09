import javax.swing.*;
import java.awt.event.*;

/**
 * @author Marketa Halvova
 */
public class GameFrame extends JFrame {

    protected Action pauseGame, continueGame, newGame;
    private final GamePanel gamePanel = new GamePanel();

    /**
     * sets frame settings
     */
    public GameFrame() {
        this.add(gamePanel); // add game panel to contentPanel
        this.setTitle("Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack(); // gives size to frame


        this.setLocationRelativeTo(null); // center frame on screen
        this.setComponent();
    }

    /**
     * set bar settings
     */
    private void setComponent() {
        JMenuBar bar = new JMenuBar();
        JMenu menu = new JMenu("Game");
        this.setJMenuBar(bar);
        menu.setMnemonic(KeyEvent.VK_G);

        pauseGame = new PauseGame("Pause", KeyEvent.VK_P);
        continueGame = new ContinueGame("Continue", KeyEvent.VK_C);
        newGame = new NewGame("New game", KeyEvent.VK_N);


        JMenuItem menuItem;
        Action[] actions = {pauseGame, continueGame, newGame};
        for (Action action : actions) {
            menuItem = new JMenuItem(action);
            menu.add(menuItem);
        }
        bar.add(menu);
    }

    private class PauseGame extends AbstractAction {
        private PauseGame(String text, Integer mnemonic) {
            super(text);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gamePanel.isRunning()) {
                gamePanel.stopTimer();
                gamePanel.setRunning(false);
                gamePanel.setPause(true);
                repaint();
            }
        }
    }

    private class ContinueGame extends AbstractAction {
        private ContinueGame(String text, Integer mnemonic) {
            super(text);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gamePanel.isPause()){
                gamePanel.setPause(false);
                if (gamePanel.getTimer() == null) {
                    gamePanel.startGame();
                } else {
                    gamePanel.setRunning(true);
                    gamePanel.startTimer();
                }
            }
        }
    }

    private class NewGame extends AbstractAction {
        private NewGame(String text, Integer mnemonic) {
            super(text);
            putValue(MNEMONIC_KEY, mnemonic);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (gamePanel.getTimer() != null) {
                gamePanel.stopTimer();
            }
            gamePanel.setNewGame();
            gamePanel.setPause(false);
            gamePanel.startGame();
        }
    }

}
