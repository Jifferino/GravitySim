import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class main {
    public static void main(String[] args) throws Exception {

        JFrame frame = new JFrame();

        frame.setResizable(false);

        frame.setTitle("Physics Sim");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        gamePanel gamePanel = new gamePanel();
        frame.add(gamePanel);
        
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);


        gamePanel.startGameThread();
    }
}
