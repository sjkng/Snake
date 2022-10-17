import javax.swing.*;
import java.awt.*;

public class MainProgram {
    public static void main(String[] args) {
        JFrame gameWindow = new JFrame();
        GamePlay gamePlay = new GamePlay();
        try {
            gameWindow.setBounds(10,10,915,700);
            gameWindow.setBackground(Color.DARK_GRAY);
            gameWindow.setResizable(true);
            gameWindow.setVisible(true);
            gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gameWindow.add(gamePlay);
        } catch (Exception e) {
            System.out.println("Something went wrong.");
            System.out.println("Please Restart the GAME.");
        }
    }
}