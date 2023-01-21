package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class Game extends JPanel implements ActionListener
{
    int WIDTH = 500;
    int HEIGHT = 500;
    int DOT = 10;
    int DOT_AMOUNT = 2500;
    int DELAY = 500;

    int head_x = WIDTH/(DOT * 2);
    int head_y = WIDTH/(DOT * 2);

    ArrayList<Integer> x = new ArrayList<>();
    ArrayList<Integer> y = new ArrayList<>();

    int x_limit = WIDTH/DOT;
    int y_limit = WIDTH/DOT;

    boolean IS_ALIVE = true;
    boolean left = false;
    boolean right = true;
    boolean up = false;
    boolean down = false;

    Timer timer;
    Random r;
    Image snake_food;
    Image snake_head;
    Image snake_tail;

    int food_x;
    int food_y;
    int food_eaten = 0;

    public Game()
    {
        r = new Random();
        addKeyListener(new SnakeAdapter());
        setBackground(Color.BLACK);
        setFocusable(true);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));

        ImageIcon im_food = new ImageIcon("src/images/snake_food.png");
        ImageIcon im_head = new ImageIcon("src/images/snake_head.png");
        ImageIcon im_tail = new ImageIcon("src/images/snake_tail.png");

        snake_food = im_food.getImage();
        snake_head = im_head.getImage();
        snake_tail = im_tail.getImage();

    }

    public void tick()
    {
        if(IS_ALIVE)
        {
            updateSnakeHead();
            if(checkBounds() && !checkBodyCollision())
            {
                if(checkFood())
                {
                    x.add(x.get(x.size() - 1));
                    y.add(y.get(y.size() - 1));
                    food_eaten++;
                    updateSnakeBody();
                    updateFood();
                }
                else
                {
                    updateSnakeBody();
                }
            }
            else
            {
                x.clear();
                y.clear();
                food_eaten = 0;

                head_x = WIDTH/(DOT * 2);
                head_y = WIDTH/(DOT * 2);
                left = false;
                right = true;
                up = false;
                down = false;
            }
        }
    }

    public boolean checkBounds()
    {
        if((head_x > -1) && (head_x < (x_limit-1)) && (head_y > -1) && (head_y < (y_limit - 1)))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean checkFood()
    {
        if((head_x == food_x) && (head_y == food_y))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void updateFood()
    {
        int tries = 5;
        boolean ok = true;
        int xF = 0;
        int yF = 0;
        for(int i = 0; i < tries; i++)
        {
            xF = r.nextInt(x_limit);
            yF = r.nextInt(y_limit);
            if((food_x == xF) && (food_y == yF))
            {
                ok = false;
            }
            else
            {
                for(int j = 0; j < x.size(); j++)
                {
                    if((food_x == x.get(j)) && (food_y == y.get(j)))
                    {
                        ok = false;
                    }
                }
            }
            if(ok)
            {
                break;
            }
        }
        food_x = xF;
        food_y = yF;
    }

    public boolean checkBodyCollision()
    {
        boolean isCollision = false;
        for(int i = 0; i < x.size(); i++)
        {
            if(((head_x == x.get(i)) && (head_y == y.get(i))))
            {
                isCollision = true;
                break;
            }
        }

        return isCollision;
    }

    public void updateSnakeBody()
    {
        x.set(0, head_x);
        y.set(0, head_y);

        for(int i = 1; i < x.size(); i++)
        {
            x.set(i, x.get(i - 1));
            y.set(i, y.get(i - 1));
        }
    }

    public void updateSnakeHead()
    {
        if(checkBounds())
        {
            if(left)
            {
                head_x--;
            }
            if(right)
            {
                head_x++;
            }
            if(up)
            {
                head_y--;
            }
            if(down)
            {
                head_y++;
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {

    }

    private class SnakeAdapter extends KeyAdapter
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            int key_code = e.getKeyCode();
            if((key_code == KeyEvent.VK_LEFT) && !right)
            {
                left = true;
                up = false;
                down = false;
            }
            if((key_code == KeyEvent.VK_RIGHT) && !left)
            {
                right = true;
                up = false;
                down = false;
            }
            if((key_code == KeyEvent.VK_UP) && !down)
            {
                up = true;
                right = false;
                left = false;
            }
            if((key_code == KeyEvent.VK_DOWN) && !up)
            {
                down = true;
                right = false;
                left = false;
            }
        }
    }
}
