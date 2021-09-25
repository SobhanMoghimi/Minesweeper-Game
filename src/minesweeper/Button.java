package minesweeper;

import javax.swing.*;
import java.awt.*;


public class Button extends JButton
{
    private ImageIcon flagImage = new ImageIcon("flag.png");
    private ImageIcon bombImage= new ImageIcon("bomb.png");
    private boolean isBomb;
    int weight;
    private int condition;
    Button()
    {
        this.setSize(10,10);
        Color buttonColor = new Color(0, 0, 0, 255);
        this.setFont(new Font("Serif",Font.PLAIN,10));
        this.setBackground(buttonColor);
        this.condition=0;
        this.weight=0;
    }

    public void setDefault()
    {
        Color buttonColor = new Color(0, 0, 0);
        this.setBackground(buttonColor);
        this.setIcon(null);
        this.condition=0;
    }

    public void rightClicked()
    {
        this.setIcon(flagImage);
        this.setBackground(new Color(201,201,201));
        this.condition=2;
    }

    public void leftClicked()
    {
        if(weight>0)
        {
            this.setText(Integer.toString(weight));
        }
        this.setBackground(new Color(201, 201,201));
        this.condition=1;
    }

    public void showBomb()
    {
        this.setBackground(new Color(201, 201, 201));
        this.setIcon(bombImage);
    }

    public boolean isBomb() {
        return isBomb;
    }

    public int getCondition() {
        return condition;
    }

    public void setBomb() {
        isBomb = true;
        weight=-1;
    }
}
