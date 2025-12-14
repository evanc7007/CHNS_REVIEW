import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainMenuFrame extends JFrame {

    // these values are hardcoded to match the spacing of the buttons later
    int width = 400;
    int height = 250;

    public mainMenuFrame() {
        // initialize things
        setTitle("Main Menu");
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the frame

        // background panel class below
        BackgroundPanel panel = new BackgroundPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        //title 
        JLabel titleLabel = new JLabel("Let's STUDY", SwingConstants.CENTER);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);

        // Multiple Choice Section
        JButton multipleChoiceButton = new JButton("Start Multiple Choice Game");
        multipleChoiceButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        // IDE gave me the lambda expression here.
        multipleChoiceButton.addActionListener((ActionEvent ae) -> {
            // Dispose the main menu and start the multiple choice game
            dispose();
            new multipleChoiceFrame(); // leads to Multiple Choice
        });

        // asteroids Section
        JButton asteroidButton = new JButton("Start Asteroid Game");
        asteroidButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        asteroidButton.addActionListener((ActionEvent ae) -> {
            // Dispose the main menu and start the asteroid game
            dispose();
            new asteroidFrame(); // leads to Asteroid Game
        });


        // Exit button 
        JButton exitButton = new JButton("Exit");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener((ActionEvent ae) -> {
            System.exit(0);
        });



        // adding everything to the panel with spacings.
        // Note: Spacings were set by trial and error, thus values are hardcoded.
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(multipleChoiceButton);
        //add button
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        // add space
        panel.add(asteroidButton);
        // add button
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        // add space
        panel.add(exitButton);
        // add button

        // Add panel to the frame
        add(panel);
        setVisible(true); // Make sure the frame is visible!!
    }

}

// the following is the same background from the asteroid game
// we just use it here to save some time on beautification.
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // matches above
            int width = 400;  // Fixed width and height
            int height = 300; 

            // drawing the background and stars
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.WHITE);
            for (int i = 0; i < 100; i++) {
                int x = (i * 137) % width;
                int y = (i * 219) % (height - 100); 
                int size = (i % 3) + 1; 
                g.fillRect(x, y, size, size);
            }
        }
    }