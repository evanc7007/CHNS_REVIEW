import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.*;

public class gamePanel extends JPanel {
    // initializing lots of stuff for the game that kind of just got added onto
    private ArrayList<asteroid> asteroids = new ArrayList<>();
    private int lives;
    private int score;
    private Timer gameTimer;
    private Timer spawnTimer;
    private Map<String, String> quotesMap;
    private String currentQuote;
    private String correctAnswer;
    private List<String> answerChoices;
    private JPanel answerPanel;

    public gamePanel(int lives, double timePerAsteroid, Map<String, String> quotesMap) {
        this.lives = lives;
        this.quotesMap = quotesMap;
        int width = 800;
        int height = 600;
        setPreferredSize(new Dimension(width, height));
        setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        // answer buttons panel at bottom bottom pls
        answerPanel = new JPanel();
        answerPanel.setBackground(Color.DARK_GRAY);
        answerPanel.setLayout(new GridLayout(1, 4, 10, 10));
        int answerPanelHeight = 100;
        answerPanel.setPreferredSize(new Dimension(width, answerPanelHeight));

        add(answerPanel, BorderLayout.SOUTH);

        //  create timers to keep track of spawning and updating the game
        spawnTimer = new Timer((int)(timePerAsteroid * 1000), e -> spawnAsteroid());
        int delayTime = 16; // roughly 60 FPS i think
        gameTimer = new Timer(delayTime, e -> {
            updateGame();
            repaint();
        });
    }

    public void startGame() {
        spawnTimer.start();
        gameTimer.start();
        spawnAsteroid(); // spawns first asteroid
    }

    private void spawnAsteroid() {
        int maxAsteroids = 8;
        if (asteroids.size() < maxAsteroids) {
            // limit the number of asteroid on the screen
            int x = (int)(Math.random() * (getWidth() - 100));
            String quote = reader.getQuestion(quotesMap);
            String answer = reader.getCorrectAnswer(quotesMap, quote);
            asteroids.add(new asteroid(x, 0, quote, answer));
            if (currentQuote == null) {
                setCurrentQuestion(quote, answer);
            }
        }
    }

    private void setCurrentQuestion(String quote, String answer) {
        currentQuote = quote;
        correctAnswer = answer;
        answerChoices = new ArrayList<>();
        answerChoices.add(answer);
        List<String> allAnswers = new ArrayList<>(quotesMap.values());
        while (answerChoices.size() < 4) {
            String randomAnswer = allAnswers.get((int)(Math.random() * allAnswers.size()));
            if (!answerChoices.contains(randomAnswer)) {
                answerChoices.add(randomAnswer);
            }
        }
        // randomly shuffles the answer choices.
        // found this function on the internet instead of logically coding it
        java.util.Collections.shuffle(answerChoices);
        updateAnswerButtons();
    }

    private void updateAnswerButtons() {
        answerPanel.removeAll();
        for (String choice : answerChoices) {
            JButton button = new JButton("<html><center>" + choice + "</center></html>");
            button.setFont(new Font("Arial", Font.PLAIN, 12));
            button.addActionListener(e -> checkAnswer(choice));
            answerPanel.add(button);
        }
        answerPanel.revalidate();
        answerPanel.repaint();
    }

    private void checkAnswer(String userAnswer) {
        if (reader.checkAnswer(userAnswer, correctAnswer)) {
            // if correct, increase score, remove asteroid
            if (!asteroids.isEmpty()) {
                // Find and remove the asteroid with the current quote
                for (int i = 0; i < asteroids.size(); i++) {
                    if (asteroids.get(i).quote.equals(currentQuote)) {
                        asteroids.remove(i);
                        break;
                    }
                }
                score++; // increase score
                // Set next question if there are still asteroids
                if (!asteroids.isEmpty()) {
                    asteroid next = asteroids.get(0);
                    setCurrentQuestion(next.quote, next.answer);
                } else {
                    currentQuote = null;
                }
            }
        } else {
            // if wrong, lose a life.
            lives--;
            if (lives <= 0) {
                gameOver();
            }
        }
    }


    private void updateGame() {
        for(int i = asteroids.size() - 1; i >= 0; i--) {
            asteroid asteroid1 = asteroids.get(i);
            asteroid1.update(); // use the asteroid's own update method with variable speed

            // check if hit bottom (use asteroid's size)
            if (asteroid1.y > getHeight() - 100) {
                asteroids.remove(i);
                lives--;
                // Update question to next asteroid
                if (!asteroids.isEmpty()) {
                    asteroid asteroid2 = asteroids.get(0);
                    setCurrentQuestion(asteroid2.quote, asteroid2.answer);
                } else {
                    currentQuote = null;
                }
                if (lives <= 0) {
                    gameOver();
                }
            }
        }
    }

    // what happens when game is over
    private void gameOver() {
        gameTimer.stop();
        spawnTimer.stop();
        // Show game over dialog with option to restart or return to main menu
        int choice = JOptionPane.showOptionDialog(this,
            // different formatting for quotes here, just what we need.
            "Game Over!\nFinal Score: " + score,
            "Game Over",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new Object[]{"Restart Game", "Return to Main Menu", "Exit"},
            "Restart Game");

        // Close this game window
        SwingUtilities.getWindowAncestor(this).dispose();

        // IDE gave this much cleaner statement
        // I've found that whenever there's a chain of else if's,
        // IDE really likes to change it to this switch statement.
        switch (choice) {
            case 0 -> // Restart the game
                SwingUtilities.invokeLater(() -> {
                    new asteroidFrame();
                });
            case 1 -> // return to main menu
                SwingUtilities.invokeLater(() -> {
                    new mainMenuFrame();
                });
            default -> // Exit the application
                System.exit(0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw stars background
        g.setColor(Color.WHITE);
        //to do: we need to fix numbers instead of using random
        for (int i = 0; i < 100; i++) {
            int x = (i * 137) % getWidth(); //these numbers make the best stars
            int y = (i * 219) % (getHeight() - 100);
            int size = (i % 3) + 1; // Varying star sizes
            g.fillRect(x, y, size, size);
        }
        // draw asteroids
        for(asteroid a : asteroids) {
            a.draw(g);
        }
        // Draw UI
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        // draw hearts(red dots) for lives
        g.drawString("Lives: ", 10, 25);
        for (int i = 0; i < lives; i++) {
            g.setColor(Color.RED);
            g.fillOval(80 + (i * 30), 10, 20, 20);
            // sorry about the hardcoding values, it's just trial and error.
        }
        // Draw score
        g.setColor(Color.WHITE);
        g.drawString("Score: " + score, 10, 55);
        // Show current quote at bottom for ease of reading.
        if (currentQuote != null) {
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.setColor(Color.YELLOW);
            String displayQuote = "Match this quote: \"" + currentQuote + "\"";
            // wrap text 
            int maxLength = 80;
            if (displayQuote.length() > maxLength) {
                displayQuote = displayQuote.substring(0, maxLength - 3) + "...\"";
            }
            g.drawString(displayQuote, 10, getHeight() - 110);
        }
    } // end of constructor
} // end of class