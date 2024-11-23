package com.lbrce;




import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class SnakaegameFinal extends JFrame {
    private static final int TILE_SIZE = 20;
    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;
    private static final int TOTAL_TILES = (WIDTH * HEIGHT) / (TILE_SIZE * TILE_SIZE);

    private ArrayList<Point> snake = new ArrayList<>();
    private Point food;
    private String direction = "RIGHT";
    private Timer timer;
    private boolean gameOver = false;

    public SnakaegameFinal() {
        setTitle("Snake Game");
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initializeGame();
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                switch (key) {
                    case KeyEvent.VK_UP:
                        if (!direction.equals("DOWN")) direction = "UP";
                        break;
                    case KeyEvent.VK_DOWN:
                        if (!direction.equals("UP")) direction = "DOWN";
                        break;
                    case KeyEvent.VK_LEFT:
                        if (!direction.equals("RIGHT")) direction = "LEFT";
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (!direction.equals("LEFT")) direction = "RIGHT";
                        break;
                }
            }
        });

        timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!gameOver) {
                    moveSnake();
                    checkCollision();
                    checkFood();
                    repaint();
                }
            }
        });
        timer.start();
    }

    private void initializeGame() {
        snake.clear();
        snake.add(new Point(5, 5));  // Starting position of the snake
        spawnFood();
    }

    private void spawnFood() {
        Random random = new Random();
        int x = random.nextInt(WIDTH / TILE_SIZE);
        int y = random.nextInt(HEIGHT / TILE_SIZE);
        food = new Point(x, y);
    }

    private void moveSnake() {
        Point head = new Point(snake.get(0));
        switch (direction) {
            case "UP":
                head.y--;
                break;
            case "DOWN":
                head.y++;
                break;
            case "LEFT":
                head.x--;
                break;
            case "RIGHT":
                head.x++;
                break;
        }
        snake.add(0, head);  // Move head in the direction
        snake.remove(snake.size() - 1);  // Remove tail
    }

    private void checkCollision() {
        Point head = snake.get(0);

        // Check wall collision
        if (head.x < 0 || head.x >= WIDTH / TILE_SIZE || head.y < 0 || head.y >= HEIGHT / TILE_SIZE) {
            gameOver = true;
        }

        // Check self collision
        for (int i = 1; i < snake.size(); i++) {
            if (head.equals(snake.get(i))) {
                gameOver = true;
                break;
            }
        }
    }

    private void checkFood() {
        if (snake.get(0).equals(food)) {
            snake.add(new Point(snake.get(snake.size() - 1)));  // Grow snake
            spawnFood();
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (!gameOver) {
            g.clearRect(0, 0, WIDTH, HEIGHT);

            // Draw food
            g.setColor(Color.RED);
            g.fillRect(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);

            // Draw snake
            g.setColor(Color.GREEN);
            for (Point point : snake) {
                g.fillRect(point.x * TILE_SIZE, point.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        } else {
            g.setColor(Color.RED);
            g.drawString("Game Over", WIDTH / 2 - 30, HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SnakaegameFinal game = new SnakaegameFinal();
            game.setVisible(true);
        });
    }
}


