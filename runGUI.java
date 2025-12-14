import javax.swing.SwingUtilities;

public class runGUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new mainMenuFrame().setVisible(true);
        });
    }
}
