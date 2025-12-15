import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class multipleChoiceFrame extends JFrame {
    // initialize things
    private JLabel questionLabel;
    private JRadioButton[] answerButtons;
    private ButtonGroup buttonGroup;
    private JButton submitButton;
    private JLabel continueLabel;
    private String correctAnswer;
    private Map<String, String> map;
    private int numbChoices = 4;
    private static String previousQuestion;
    private static String previousAnswer;
    private String currentQuestion;

    public multipleChoiceFrame() {
        super("Multiple Choice Quiz");

        // get file for studying
        String fileName = null;
        boolean fileLoaded = false;

        while (!fileLoaded) {
            // we decided to make this a dropdown for only the three .txt we included in this program
            // but for future use, one could change this back to just an input
            String[] fileOptions = {"qf.txt", "asteroid.txt", "test.txt"};
            fileName = (String) JOptionPane.showInputDialog( //dropdown work
                    null,
                    "Select a file:",
                    "Load Quotes",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    fileOptions,
                    fileOptions[0]
            );
            // just a check
            if (fileName == null || fileName.isBlank()) {
                System.exit(0);
            }
            File file = new File(fileName); //old version keep it?
            if (!file.exists()) {
                JOptionPane.showMessageDialog(
                        null,
                        "File not found. Please try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
                continue;
            }
            try {
                // load quotes into Reader's map
                reader.load(file.getAbsolutePath());
                map = reader.getMap();
                if (map == null || map.isEmpty()) {
                    JOptionPane.showMessageDialog(
                            null,
                            "No quotes loaded. Please check your file format and try again.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    fileLoaded = true;
                }
            } catch (Exception e) { //we need
                JOptionPane.showMessageDialog(
                        null,
                        "Error loading file: " + e.getMessage() + "\nPlease try again.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        }
        // build the UI once
        buildUI();
        // load the first question
        loadNewQuestion();
    }


    // labels buttons layout
    private void buildUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // background
        BackgroundPanel background = new BackgroundPanel();
        background.setLayout(new GridBagLayout());
        GridBagConstraints grid = new GridBagConstraints();
        background.setBorder(new EmptyBorder(20, 20, 20, 20));

        // fonts
        Font questionFont = new Font("SansSerif", Font.PLAIN, 16);
        Font optionFont = new Font("Serif", Font.PLAIN, 14);

        // Create a panel for the question box at top center, should take up half-ish the page
        JPanel questionPanel = new JPanel(new BorderLayout());

        questionPanel.setBackground(Color.WHITE);
        questionLabel = new JLabel();
        questionLabel.setFont(questionFont);
        questionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        questionLabel.setVerticalAlignment(SwingConstants.CENTER);
        questionPanel.add(questionLabel, BorderLayout.CENTER);

        grid.gridx = 0;
        grid.gridy = 0;
        grid.weightx = 1.0;
        grid.weighty = 0.6; // 60 percent of vertical space good?
        grid.fill = GridBagConstraints.BOTH;
        grid.insets = new Insets(10, 50, 10, 50);
        background.add(questionPanel, grid);

        //answer choices in 2x2 grid and should take upp most of remaining space
        JPanel optionsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        optionsPanel.setOpaque(false);

        answerButtons = new JRadioButton[numbChoices];
        buttonGroup = new ButtonGroup();

        // for loop to put each answer button
        for (int i = 0; i < numbChoices; i++) {
            // we need panel for each option
            JPanel optionBox = new JPanel(new BorderLayout());
            
            optionBox.setBackground(Color.WHITE);
            answerButtons[i] = new JRadioButton();
            answerButtons[i].setFont(optionFont);
            answerButtons[i].setHorizontalAlignment(SwingConstants.CENTER);
            answerButtons[i].setBackground(Color.WHITE);
            buttonGroup.add(answerButtons[i]);
            optionBox.add(answerButtons[i], BorderLayout.CENTER);
            optionsPanel.add(optionBox);
        }

        grid.gridy = 1;
        grid.weighty = 0.3; // has to add up to 1, 0.6
        grid.insets = new Insets(10, 50, 10, 50);
        background.add(optionsPanel, grid);

        // bottom: submit / continue / exit
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setOpaque(false);

        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        exitButton.setBackground(Color.WHITE);
        exitButton.addActionListener((ActionEvent e) -> {
            dispose();
            new mainMenuFrame();
        });

        JButton previousButton = new JButton("Previous");
        previousButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        previousButton.setBackground(Color.WHITE);
        previousButton.addActionListener(this::handlePrevious);

        //buttons at bottom
        //submit
        submitButton = new JButton("Submit");
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitButton.setBackground(Color.WHITE);
        //continue
        continueLabel = new JLabel(" ", SwingConstants.CENTER);
        continueLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        continueLabel.setForeground(Color.WHITE);

        bottomPanel.add(exitButton, BorderLayout.WEST);
        bottomPanel.add(continueLabel, BorderLayout.CENTER);
        bottomPanel.add(submitButton, BorderLayout.EAST);
        bottomPanel.add(previousButton, BorderLayout.SOUTH);

        grid.gridy = 2;
        grid.weighty = 0.1; // 0.6, 0.3
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.insets = new Insets(10, 50, 10, 50);
        
        background.add(bottomPanel, grid);

        // button action
        submitButton.addActionListener(this::handleSubmit);
        add(background);
        pack();
        setSize(900, 700);
        setLocationRelativeTo(null); // center on screen
        setVisible(true);
    }

    // pick a random quote generate answers and update the components
    private void loadNewQuestion() {
        // we take advantage of the reader.java helper functions.
        String quote = reader.getQuestion(map);
        correctAnswer = reader.getCorrectAnswer(map, quote);
        currentQuestion = quote;

        ArrayList<String> randAnswers = multipleChoice.getRandAnswer(correctAnswer, map, numbChoices);
        Collections.shuffle(randAnswers);

        // Adjust font size based on character length
        // roughly hardcoded, can be adjusted with mathematical formula, but
        // we found this to be the simplest and most foolproof way of doing so.
        int quoteLength = quote.length();
        int fontSize;
        if (quoteLength < 20) {
            fontSize = 24;
        } else if (quoteLength < 50) {
            fontSize = 22;
        } else if (quoteLength < 100) {
            fontSize = 20;
        } else if (quoteLength < 150) {
            fontSize = 16;
        } else if (quoteLength < 200) {
            fontSize = 14;
        } else if (quoteLength < 300) {
            fontSize = 12;
        } else if (quoteLength < 400) {
            fontSize = 10;
        } else {
            fontSize = 9;
        }
        questionLabel.setFont(new Font("SansSerif", Font.PLAIN, fontSize));

        // update question text
        questionLabel.setText("<html><body style='width:400px; text-align:center; display:flex; align-items:center; justify-content:center;'>" + quote + "</body></html>");

        // adjust answer font size based on length
        for (int i = 0; i < numbChoices; i++) {
            String answer = randAnswers.get(i);
            int answerLength = answer.length();
            int answerFontSize;
            // hardcoded again
            if (answerLength < 30) {
                answerFontSize = 14;
            } else if (answerLength < 50) {
                answerFontSize = 12;
            } else {
                answerFontSize = 10;
            }
            answerButtons[i].setFont(new Font("Serif", Font.ITALIC, answerFontSize));
            answerButtons[i].setText("<html><body style='width:300px; text-align:center'>" + answer.substring(1, answer.length() - 1) + "</body></html>");
            answerButtons[i].setSelected(false);
        }
        buttonGroup.clearSelection();
        // reset continue
        continueLabel.setText(" ");
    }

    private void handlePrevious(ActionEvent e){
        if (previousQuestion == null || previousAnswer == null) {
            JOptionPane.showMessageDialog(
                this,
                "No previous question available.",
                "Previous",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String message = "<html><body style='width:1000px'>"
            + "<b>Previous Question:</b><br>" + previousQuestion
            + "<br><br><b>Previous Answer:</b><br>" + previousAnswer
            + "</body></html>";

        JOptionPane.showMessageDialog(
            this,
            message,
            "Previous",
            JOptionPane.INFORMATION_MESSAGE
        );
        }

    private void handleSubmit(ActionEvent e) {
        String selected = getSelectedAnswer();
        if (selected == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select an answer.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }


        //answers
        boolean isCorrect = reader.checkAnswer(selected, correctAnswer.substring(1, correctAnswer.length() - 1));
        if (!isCorrect) {
            continueLabel.setText("Incorrect. Try again.");
            return;
        }

        continueLabel.setText("Correct!");
        // we choose to use a dialog box to allow for exiting the system at anytime.
        // int choice = JOptionPane.showConfirmDialog(
        //         this,
        //         "Correct! Do you want the next question?",
        //         "Continue",
        //         JOptionPane.YES_NO_OPTION
        // );
        // store previous question and answer (remove surrounding quotes if present)
        if (currentQuestion != null && currentQuestion.length() >= 2) {
            previousQuestion = currentQuestion.substring(1, currentQuestion.length() - 1);
        } else {
            previousQuestion = currentQuestion;
        }
        if (correctAnswer != null && correctAnswer.length() >= 2) {
            previousAnswer = correctAnswer.substring(1, correctAnswer.length() - 1);
        } else {
            previousAnswer = correctAnswer;
        }

        //if (choice == JOptionPane.YES_OPTION) {
            loadNewQuestion();
        //} else {
            //dispose();
            //new mainMenuFrame();
        //}
    }

    private String getSelectedAnswer() {
        for (JRadioButton button : answerButtons) {
            if (button.isSelected()) {
                // Strip HTML tags to get plain text
                String text = button.getText();
                text = text.replaceAll("<html><body[^>]*>", "");
                text = text.replaceAll("</body></html>", "");
                return text;
            }
        }
        return null;
    }

    // Background panel class with stars
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int width = getWidth();
            int height = getHeight();

            // drawing the background and stars
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, width, height);
            g.setColor(Color.WHITE);

            // more stars with varying sizes and positions
            // same from asteroid background
            for (int i = 0; i < 300; i++) {
                int x = (i * 137) % width;
                int y = (i * 219) % height;
                int size = (i % 4) + 1;
                g.fillRect(x, y, size, size);
            }

            // some brighter/larger stars
            g.setColor(new Color(200, 200, 255));
            for (int i = 0; i < 50; i++) {
                int x = (i * 283) % width;
                int y = (i * 431) % height;
                g.fillRect(x, y, 2, 2);
            }
        }
    }
}