import java.awt.*;

// sets up for the asteroidFrame and GamePanel.
public class asteroid {
    int x;
    int y;
    String quote;
    String answer;
    double rotation = 0;
    double rotationSpeed;
    int size; // int because pixels
    double speed;

    public asteroid(int x, int y, String quote, String answer) {
        this.x = x;
        this.y = y;
        this.quote = quote;
        this.answer = answer;
        // random size, speed, and rotation
        this.size = 60 + (int)(Math.random() * 40); // 60-100 pixels
        this.speed = 1.0 + Math.random() * 2.0; // 1 to 3 speed
        this.rotationSpeed = 0.01 + (Math.random() * 0.03); // 0.01 to 0.04
    }

    public void update() {
        y += speed;
        rotation += rotationSpeed;
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.translate(x + size/2, y + size/2);
        // this following rotate line is actually integral
        // without it, everything is spinning.
        g2d.rotate(rotation);

        // Draw asteroid (centered at origin now)
        g2d.setColor(new Color(100, 100, 100));
        g2d.fillOval(-size/2, -size/2, size, size);

        // draw craters on the rock, for texture.
        // just hard coded the color
        g2d.setColor(new Color(70, 70, 70));
        // three different craters
        int size1 = size / 4;
        int size2 = size / 5;
        int size3 = size / 4;
        g2d.fillOval(-size/4, -size/6, size1, size1);
        g2d.fillOval(size/6, 0, size2, size2);
        g2d.fillOval(-size/8, size/6, size3, size3);

        // Draw quote (rotate back to horizontal)
        g2d.rotate(-rotation);
        g2d.setColor(Color.YELLOW);
        int fontSize = Math.max(8, size / 8);
        g2d.setFont(new Font("Arial", Font.BOLD, fontSize));
        int maxChar = size / 6;
        String shortQuote;
        if (quote.length() > maxChar) {
            shortQuote = quote.substring(0, maxChar) + "...";
        } else {
            shortQuote = quote;
        }

        // center the text
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(shortQuote);
        g2d.drawString(shortQuote, -textWidth/2, fontSize/2);

        // Restore the original transform
        g2d.rotate(rotation);
        g2d.rotate(-rotation);
        g2d.translate(-(x + size/2), -(y + size/2));
    }

    public int getSize() {
        return size;
    }

    public double getSpeed() {
        return speed;
    }
}
