import java.awt.*;
import javax.swing.*;

public class asteroidFrame extends JFrame {
    // for game setup initializing
    private JSlider difficultySlider;
    private JSpinner livesSpinner;
    private String selectedFileName;

    public asteroidFrame() {
        setTitle("Asteroid Game - Setup");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // file selection first right
        String[] fileOptions = {"asteroid.txt", "test.txt"};

        selectedFileName = (String) JOptionPane.showInputDialog(
            null,
            "Select a file:",
            "Load Quotes",
            JOptionPane.QUESTION_MESSAGE,
            null,
            fileOptions,
            fileOptions[0]
        );

        if (selectedFileName == null) {
            dispose();
            return;
        }

        //background setup
        JPanel panel = new JPanel();
        panel.setOpaque(false);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(new Color(255, 255, 255, 200)); // semi-transparent white

        // title
        JLabel titleLabel = new JLabel("ASTEROID GAME SETUP");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 25));
        titleLabel.setForeground(Color.BLACK);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(30));

        // Display selected file
        JPanel filePanel = new JPanel();
        JLabel fileLabel = new JLabel("Selected File: " + selectedFileName);
        fileLabel.setFont(new Font("Arial", Font.BOLD, 14));
        filePanel.add(fileLabel);
        panel.add(filePanel);
        panel.add(Box.createVerticalStrut(20));

        // difficulty slider (is 10 too much?)
        JLabel diffLabel = new JLabel("Difficulty: 1 (Easy) - 10 (IMPOSSIBLE)");
        diffLabel.setForeground(Color.BLACK);
        diffLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultySlider = new JSlider(1, 10, 5);
        difficultySlider.setMajorTickSpacing(1);
        difficultySlider.setPaintTicks(true);
        difficultySlider.setPaintLabels(true);
        panel.add(diffLabel);
        panel.add(difficultySlider);
        panel.add(Box.createVerticalStrut(20));
        // slider much easier.

        // lives
        JPanel livesPanel = new JPanel();
        JLabel livesLabel = new JLabel("Lives: ");
        livesLabel.setFont(new Font("Arial", Font.BOLD, 16));
        // Spinner is just a ticker in my mind. As in it goes up and down.
        livesSpinner = new JSpinner(new SpinnerNumberModel(3, 1, 10, 1));
        livesSpinner.setFont(new Font("Arial", Font.PLAIN, 16));
        livesPanel.add(livesLabel);
        livesPanel.add(livesSpinner);
        panel.add(livesPanel);
        panel.add(Box.createVerticalStrut(30));

        // Start button
        JButton startButton = new JButton("START GAME");
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // IDE gave the lambda way of writing this.
        startButton.addActionListener(e -> startGame());
        panel.add(startButton);

        add(panel);
        setVisible(true);
    }

    private void startGame() {
        String fileName = selectedFileName;

        // Load the quotes
        // Once again, IDE really likes the try catch for SCanner
        try {
            reader.load(fileName);
            if (reader.getMap().isEmpty()) {
                // error message looks a little different from normal.
                JOptionPane.showMessageDialog(this,
                    "No quotes found in file!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (HeadlessException e) {
            // IDE suggested Headless Exception
            // error message looks a little different from normal.
            JOptionPane.showMessageDialog(this,
                "Error loading file: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int difficulty = difficultySlider.getValue();
        int timePerAsteroid = 11 - difficulty;
        int lives = (Integer) livesSpinner.getValue();

        // Close setup window
        dispose();

        // send the game
        JFrame gameFrame = new JFrame("Asteroid Quote Defense");
        gamePanel gamePanel = new gamePanel(lives, timePerAsteroid, reader.getMap());
        gameFrame.add(gamePanel);
        gameFrame.setSize(800, 700);
        gameFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);
        gamePanel.startGame();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new asteroidFrame();
        });
    }
}