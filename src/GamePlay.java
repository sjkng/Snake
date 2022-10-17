import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

class Snake {
    protected static int[] xAxisSnakeLength = new int[750];
    protected static int[] yAxisSnakeLength = new int[750];
    protected static int snakeLength = 3;
    protected static ImageIcon snakeHead;
    protected static ImageIcon snakeBody;
}

class Food {
    protected static int[] foodXPosition = {25,50,75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,
            475,500,525,550,575,600,625,650,675,700,725,750,775,800,825,850};
    protected static int[] foodYPosition = {75,100,125,150,175,200,225,250,275,300,325,350,375,400,425,450,
            475,500,525,550,575,600,625};
    protected static ImageIcon foodImage;
    protected static Random randomPos = new Random();
    protected static int randomFoodXPos = randomPos.nextInt(34);
    protected static int randomFoodYPos = randomPos.nextInt(23);
}

class PassedTime {
    LocalDateTime now = LocalDateTime.now();
    DateTimeFormatter dateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy    HH:mm:ss");
    String currentTimeDate = now.format(dateTime);

    public String getTime() {
        return currentTimeDate;
    }
}

public class GamePlay extends JPanel implements KeyListener, ActionListener {
    // movement keys
    private int moves = 0;
    private boolean left = false;
    private boolean right = false;
    private boolean up = false;
    private boolean down = false;

    // Timer
    private final Timer timer;

    // Score
    private int score = 0;

    // Game over Flag
    private int gameOverFlag = 0;
    private  int newGame = 0;
    private int gameCounter = -1;


    public GamePlay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        int delayStart = 140;
        timer = new Timer(delayStart, this);
        timer.start();
    }

    public void paint (Graphics graphics) {
        // Snake movement
        if (moves == 0 && gameOverFlag == 0 && !up && !down && !left && !right) {
            Snake.xAxisSnakeLength[2] = 50;
            Snake.xAxisSnakeLength[1] = 75;
            Snake.xAxisSnakeLength[0] = 100;

            Snake.yAxisSnakeLength[2] = 100;
            Snake.yAxisSnakeLength[1] = 100;
            Snake.yAxisSnakeLength[0] = 100;
            ++gameCounter;
        }

        // Draw title Image border:
        graphics.setColor(Color.BLACK);
        graphics.drawRect(24,11,851,55);
        graphics.setColor(Color.YELLOW);
        graphics.fillRect(25,12,850,54);

        // Draw title Image:
        ImageIcon titleImage = new ImageIcon("Assets/snaketitle.png");
        titleImage.paintIcon(this, graphics, 33, 19);

        // Draw gameplay border:
        graphics.setColor(Color.white);
        graphics.drawRect(24,74,851,576);

        // Draw gameplay background:
        graphics.setColor(Color.BLACK);
        graphics.fillRect(25,75,850,575);

        // Draw the Score
        graphics.setColor(Color.BLUE);
        graphics.setFont(new Font("Showcard Gothic",Font.PLAIN,12));
        graphics.drawString("SCORE: " + score, 740, 28);

        // Draw the length of Snake
        graphics.drawString("Snake's LENGTH: " + Snake.snakeLength, 740, 58);

        // Draw game counter
        graphics.drawString("Tries: " + gameCounter, 740, 43);

        // Draw Date and Time
        PassedTime newTime = new PassedTime();
        graphics.drawString("" + newTime.getTime(), 50,28);

        // Draw the Snake:
        Snake.snakeHead = new ImageIcon("Assets/head.png");
        Snake.snakeHead.paintIcon(this, graphics, Snake.xAxisSnakeLength[0], Snake.yAxisSnakeLength[0]);
        for (int i = 0; i < Snake.snakeLength; ++i) {
            if (i == 0 && (right || left || up || down)) {
                Snake.snakeHead = new ImageIcon("Assets/head.png");
                Snake.snakeHead.paintIcon(this, graphics, Snake.xAxisSnakeLength[i], Snake.yAxisSnakeLength[i]);
            }
            if (i != 0) {
                Snake.snakeBody = new ImageIcon("Assets/body.png");
                Snake.snakeBody.paintIcon(this, graphics,  Snake.xAxisSnakeLength[i], Snake.yAxisSnakeLength[i]);
            }
        }
        // Check if Head of Snake is Colliding with Food
        Food.foodImage = new ImageIcon("Assets/apple.png");
        if ((Food.foodXPosition[Food.randomFoodXPos] ==  Snake.xAxisSnakeLength[0]) &&
                (Food.foodYPosition[Food.randomFoodYPos] == Snake.yAxisSnakeLength[0])) {
            ++Snake.snakeLength;
            score += 10;
            // Regenerate and randomize food position
            Food.randomFoodXPos = Food.randomPos.nextInt(34);
            Food.randomFoodYPos = Food.randomPos.nextInt(23);
        }
        Food.foodImage.paintIcon(this, graphics, Food.foodXPosition[Food.randomFoodXPos],
                Food.foodYPosition[Food.randomFoodYPos]);

        // Collision of SnakeHead with SnakeBody  GAME OVER
        for (int i = 1; i < Snake.snakeLength; ++i) {
            if ( Snake.xAxisSnakeLength[i] ==  Snake.xAxisSnakeLength[0] &&
                    Snake.yAxisSnakeLength[i] == Snake.yAxisSnakeLength[0]) {
                right = false;
                left = false;
                up = false;
                down = false;
                gameOverFlag = 1;

                // Showing GAME OVER
                graphics.setColor(Color.RED);
                graphics.setFont(new Font("Showcard Gothic",Font.BOLD,50));
                graphics.drawString("Game  OVER", 300, 350);
                graphics.setFont(new Font("Arial",Font.PLAIN,10));
                graphics.drawString("Press SPACE to PLAY", 400, 380);
            }
        }

        // Showing new game
        if(!up && !down && !right && !left && gameOverFlag == 0 && newGame == 0) {
            newGame = 1;
            graphics.setColor(Color.GREEN);
            graphics.setFont(new Font("Showcard Gothic",Font.BOLD,50));
            graphics.drawString("Start New GAME", 250, 350);
            graphics.setFont(new Font("Arial",Font.PLAIN,10));
            graphics.drawString("Press SPACE to PLAY", 430, 380);
        }
        graphics.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (right) {
            for (int dimensY = Snake.snakeLength - 1; dimensY >= 0; --dimensY) {
                Snake.yAxisSnakeLength[dimensY + 1] = Snake.yAxisSnakeLength[dimensY];
            }
            for (int dimensX = Snake.snakeLength - 1; dimensX >= 0; --dimensX) {
                if (dimensX == 0) {
                    Snake.xAxisSnakeLength[dimensX] =  Snake.xAxisSnakeLength[dimensX] + 25;
                } else {
                    Snake.xAxisSnakeLength[dimensX] =  Snake.xAxisSnakeLength[dimensX - 1];
                }
                if ( Snake.xAxisSnakeLength[dimensX] > 850) {
                    Snake.xAxisSnakeLength[dimensX] = 25;
                }
            }
            repaint();
        }
        if (left) {
            for (int dimensY = Snake.snakeLength - 1; dimensY >= 0; --dimensY) {
                Snake.yAxisSnakeLength[dimensY + 1] = Snake.yAxisSnakeLength[dimensY];
            }
            for (int dimensX = Snake.snakeLength - 1; dimensX >= 0; --dimensX) {
                if (dimensX == 0) {
                    Snake.xAxisSnakeLength[dimensX] =  Snake.xAxisSnakeLength[dimensX] - 25;
                } else {
                    Snake.xAxisSnakeLength[dimensX] =  Snake.xAxisSnakeLength[dimensX - 1];
                }
                if ( Snake.xAxisSnakeLength[dimensX] < 25) {
                    Snake.xAxisSnakeLength[dimensX] = 850;
                }
            }
            repaint();
        }
        if (up) {
            for (int dimensX = Snake.snakeLength - 1; dimensX >= 0; --dimensX) {
                Snake.xAxisSnakeLength[dimensX + 1] = Snake.xAxisSnakeLength[dimensX];
            }
            for (int dimensY = Snake.snakeLength - 1; dimensY >= 0; --dimensY) {
                if (dimensY == 0) {
                    Snake.yAxisSnakeLength[dimensY] = Snake.yAxisSnakeLength[dimensY] - 25;
                } else {
                    Snake.yAxisSnakeLength[dimensY] = Snake.yAxisSnakeLength[dimensY - 1];
                }
                if (Snake.yAxisSnakeLength[dimensY] < 75) {
                    Snake.yAxisSnakeLength[dimensY] = 625;
                }
            }
            repaint();
        }
        if (down) {
            for (int dimensX = Snake.snakeLength - 1; dimensX >= 0; --dimensX) {
                Snake.xAxisSnakeLength[dimensX + 1] =  Snake.xAxisSnakeLength[dimensX];
            }
            for (int dimensY = Snake.snakeLength - 1; dimensY >= 0; --dimensY) {
                if (dimensY == 0) {
                    Snake.yAxisSnakeLength[dimensY] = Snake.yAxisSnakeLength[dimensY] + 25;
                } else {
                    Snake.yAxisSnakeLength[dimensY] = Snake.yAxisSnakeLength[dimensY - 1];
                }
                if (Snake.yAxisSnakeLength[dimensY] > 625) {
                    Snake.yAxisSnakeLength[dimensY] = 75;
                }
            }
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // Reset the Game with Space key or Enter
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            moves = 0;
            score = 0;
            up = false;
            down = false;
            left = false;
            right = false;
            Snake.snakeLength = 3;
            gameOverFlag = 0;
            repaint();
        }
        if (gameOverFlag == 0 && gameCounter > 0) {
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                moves++;
                if (!left) {
                    right = true;
                } else {
                    right = false;
                }
                down = false;
                up = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT && moves > 0) {
                moves++;
                if (!right) {
                    left = true;
                } else {
                    left = false;
                }
                down = false;
                up = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                moves++;
                if (!down) {
                    up = true;
                } else {
                    up = false;
                }
                left = false;
                right = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                moves++;
                if (!up) {
                    down = true;
                } else {
                    down = false;
                }
                left = false;
                right = false;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}