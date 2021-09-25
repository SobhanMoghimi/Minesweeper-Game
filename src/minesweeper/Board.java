package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


public class Board extends JFrame
{
    private JPanel header;
    private JPanel body;
    private JLabel timeLabel;
    private JLabel flagLabel;
    private final int boardSize;
    private Button[][] buttons;
    private int flagCount;
    private int bombsRemain;
    private boolean gameStarted;
    private int currentSecond=0;
    private boolean gameOver;
    private int bombCount;
    private int buttonsRemained=0;
    private boolean won;
    final Timer timer = new Timer(1000,new ActionListener()
    {
        public void actionPerformed(ActionEvent e)
        {
            currentSecond+=1;
            int hour=currentSecond/3600;
            int minute=(currentSecond/60) %60;
            timeLabel.setText(String.format("%02d",hour)+":"+String.format("%02d",minute)+":"+String.format("%02d",currentSecond%60));
        }
    });


    Board(int size)
    {
        gameStarted=false;
        gameOver=false;
        this.setTitle("MineSweeper");
        this.setBackground(new Color(100,41,148));
        boardSize=size;
        buttons=new Button[size][size];
        if(size!=20)
            this.setBounds(300,200,43*size,43*size);
        else
            this.setBounds(300,0,43*size,43*size);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        header=new JPanel();
        header.setPreferredSize(new Dimension(43*size,50));
        header.setLayout(new BorderLayout());
        header.setBackground(new Color(100,41,148));
        this.add(header,BorderLayout.NORTH);
        if(size==8)
        {
            flagCount=12;
            bombsRemain=12;
            bombCount=12;
        }
        if(size==12)
        {
            flagCount=23;
            bombsRemain=23;
            bombCount=23;
        }
        if(size==19)
        {
            flagCount=55;
            bombsRemain=55;
            bombCount=55;
        }
        buttonsRemained=size*size;

        timeLabel=new JLabel();
        timeLabel.setText("00:00:00");
        timeLabel.setFont(new Font("Sans-Serif",Font.PLAIN,18));
        timeLabel.setForeground(new Color(255, 255, 255));

        flagLabel=new JLabel();
        flagLabel.setText("Flags:"+flagCount);
        flagLabel.setSize(18,18);
        flagLabel.setForeground(new Color(255, 255, 255));
        flagLabel.setFont(new Font("Sans-Serif",Font.PLAIN,18));

//        endGame=new JLabel();
//        endGame.setText("Playing...");
//        endGame.setForeground(Color.BLACK);
//        endGame.setSize(18,18);
//        header.add(endGame,BorderLayout.CENTER);


        header.add(flagLabel,BorderLayout.WEST);
        header.add(timeLabel,BorderLayout.EAST);

        body = new JPanel();
        body.setLayout(new GridLayout(size, size));
        body.setBackground(new Color(0, 0, 0));
        this.add(body);

        addButtons();
        this.setVisible(true);
    }

    private MouseListener mouseListener = new MouseAdapter()
    {
        public void mousePressed(MouseEvent mouseEvent)
        {
            if(!gameStarted)
            {
                timer.start();
                gameStarted=true;
            }
            if(!gameOver)
            {
                if(SwingUtilities.isRightMouseButton(mouseEvent))
                {
                    rightClick(mouseEvent.getSource());
                }
                else if(SwingUtilities.isLeftMouseButton(mouseEvent))
                {
                    leftClick(mouseEvent.getSource());
                }
            }

        }
    };

    private boolean rightClick(Object source)
    {
        int[] index=findButton(source);
        int row=index[0]; int column=index[1];

        if(buttons[row][column].getCondition()==0)
        {
            buttons[row][column].rightClicked();
            if(buttons[row][column].isBomb())
            {
                bombsRemain--;
            }
            flagCount--;
        }
        else if(buttons[row][column].getCondition()==2)
        {
            buttons[row][column].setDefault();
            if(buttons[row][column].isBomb())
            {
                bombsRemain++;
            }
            flagCount++;
        }
        flagLabel.setText("Flags: " + flagCount);
        endGame();
        return false;
    }

    private boolean leftClick(Object source)
    {
        int[] index=findButton(source);
        int row=index[0]; int column=index[1];
        if(buttons[row][column].getCondition()==1) return false;
        if(buttons[row][column].getCondition()==2)
        {
            buttons[row][column].setDefault();
            flagCount++;
            bombsRemain++;
            flagLabel.setText("Flags: " + flagCount);
        }
        else if(buttons[row][column].getCondition()==0)
        {
            if(buttons[row][column].isBomb())
            {
                revealBombs();
            }
            else
            {
                    reveal(row,column);
            }
            endGame();
        }
        return true;
    }
    private boolean reveal(int row,int col)
    {
        if(row<0 || col<0 || row>=boardSize || col>=boardSize)
        {
            return false;
        }
        if(buttons[row][col].isBomb())
        {
            return false;
        }
        if(buttons[row][col].getCondition()==0)
        {
            buttons[row][col].leftClicked();
            buttonsRemained--;
        }
        else
        {
            return false;
        }
        if(buttons[row][col].weight==0)
        {
            reveal(row-1,col-1);
            reveal(row-1,col);
            reveal(row-1,col+1);

            reveal(row,col-1);
            reveal(row,col+1);

            reveal(row+1,col-1);
            reveal(row+1,col);
            reveal(row+1,col+1);
        }
        return true;
    }
    private void revealBombs()
    {
        for(int i=0;i<boardSize;i++)
            for(int j=0;j<boardSize;j++)
            {
                if(buttons[i][j].isBomb())
                {
                    buttons[i][j].showBomb();
                }
            }

        timer.stop();
            gameOver=true;
            won=false;
    }

    private int[] findButton(Object source)
    {
        int[] index=new int[2];
        for(int i=0;i<boardSize;i++)
        {
            for(int j=0;j<boardSize;j++)
            {
                if(buttons[i][j] == source)
                {
                    index[0]=i;
                    index[1]=j;
                }
            }
        }
        return index;
    }

    private void addButtons()
    {
        for(int i=0;i<boardSize;i++)
            for(int j=0;j<boardSize;j++)
            {
                buttons[i][j]=new Button();
                buttons[i][j].addMouseListener(mouseListener);
                buttons[i][j].setFocusable(false);
                body.add(buttons[i][j]);
            }
        deployBombs();
        countWeights();
    }

    private void endGame()
    {
        if((bombsRemain==0 && flagCount==0)|| (buttonsRemained==bombCount))
        {
            timer.stop();
            won=true;
            gameOver=true;
        }
    }

    private void countWeights()
    {
        ArrayList<Integer> row=new ArrayList<>();
        ArrayList<Integer> col=new ArrayList<>();
        for (int i = 0; i < boardSize; i++)
        {
            for (int j = 0; j < boardSize; j++)
            {
                if (buttons[i][j].isBomb())
                {

                    for(int k=i-1;k<=i+1;k++)
                        for(int z=j-1;z<=j+1;z++)
                        {
                            if(k==i && z==j) continue;
                            if(k<0 || k>=boardSize || z<0 || z>=boardSize)
                            {
                                continue;
                            }
                            row.add(k);
                            col.add(z);
                        }
                    for(int k=0;k<row.size();k++)
                    {
                        buttons[row.get(k)][col.get(k)].weight++;
                    }
                    row.clear();
                    col.clear();
                }
            }
        }
    }
    private void deployBombs()
    {
        int bombCount=0;
        while(bombCount<bombsRemain)
        {
            int row=(int) (Math.random()*(boardSize));
            int column=(int)(Math.random()*(boardSize));
            if(!buttons[row][column].isBomb())
            {
                buttons[row][column].setBomb();
                bombCount++;
            }
        }
    }
    public boolean isWon()
    {
        return this.won;
    }
    public boolean isGameOver()
    {
        return this.gameOver;
    }
}

