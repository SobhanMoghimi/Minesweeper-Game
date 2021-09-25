package minesweeper;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args)
    {
    startGame();
    }

    public static void startGame()
    {
        String[] options = {"Easy", "Normal", "Hard"};
        int diff = JOptionPane.showOptionDialog(null, "Choose Difficulty", "Hello!", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
        Board game = null;
        if (diff == JOptionPane.YES_OPTION) {
            game = new Board(8);
        }
        if (diff == JOptionPane.NO_OPTION) {
            game = new Board(12);
        }
        if (diff == JOptionPane.CANCEL_OPTION) {
            game = new Board(19);
        }

        if (game != null)
        {
            boolean isOver=false;
            String time;
            while (!game.isGameOver())
            {
                time=game.timer.toString();
            }
            if(game.isWon())
            {
                JOptionPane.showMessageDialog(null,"You won!!!!","Congratulations!",JOptionPane.PLAIN_MESSAGE);
            }
            else
            {
                JOptionPane.showMessageDialog(null,"You Lost...","Sorry :(",JOptionPane.PLAIN_MESSAGE,null);
            }
            int condition = JOptionPane.showOptionDialog(null, "Do you want to play again?!", "Play Again?!", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
            if (condition == JOptionPane.YES_OPTION)
            {
                game.dispose();
                startGame();
            }
            if(condition==JOptionPane.NO_OPTION)
            {
            }
        }
    }
}

