import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class StartPage {

    public StartPage() {
        JFrame frame = new JFrame("Employee Management System");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Load background image using ImageLoader
        Image bgImg = ImageLoader.load("start.jpg", 800, 500);
        JLabel bg;
        if (bgImg != null) {
            bg = new JLabel(new ImageIcon(bgImg));
        } else {
            bg = new JLabel();
            bg.setBackground(new Color(30, 41, 59));
            bg.setOpaque(true);
        }
        bg.setLayout(null);

        // Dark overlay panel
        JPanel overlay = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(0, 0, 0, 120));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        overlay.setOpaque(false);
        overlay.setLayout(null);
        overlay.setBounds(0, 0, 800, 500);

        // Title
        JLabel title = new JLabel("Employee Management System", JLabel.CENTER);
        title.setBounds(100, 120, 600, 60);
        title.setFont(new Font("Georgia", Font.BOLD, 34));
        title.setForeground(Color.WHITE);

        // Subtitle
        JLabel subtitle = new JLabel("Manage your team with ease", JLabel.CENTER);
        subtitle.setBounds(100, 185, 600, 30);
        subtitle.setFont(new Font("Georgia", Font.ITALIC, 16));
        subtitle.setForeground(new Color(200, 220, 255));

        // Decorative line
        JSeparator sep = new JSeparator();
        sep.setBounds(280, 225, 240, 2);
        sep.setForeground(new Color(100, 160, 255));

        // Enter Button
        JButton btn = new JButton("Enter System") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(30, 90, 180));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(60, 130, 230));
                } else {
                    g2d.setColor(new Color(40, 110, 200));
                }
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2d.setColor(new Color(100, 180, 255, 80));
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBounds(320, 340, 160, 45);
        btn.setFont(new Font("Georgia", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Version label
        JLabel version = new JLabel("v1.0  |  Capstone Project", JLabel.CENTER);
        version.setBounds(100, 440, 600, 20);
        version.setFont(new Font("Arial", Font.PLAIN, 11));
        version.setForeground(new Color(180, 180, 180));

        overlay.add(title);
        overlay.add(subtitle);
        overlay.add(sep);
        overlay.add(btn);
        overlay.add(version);
        bg.add(overlay);

        frame.setContentPane(bg);

        btn.addActionListener(e -> {
            frame.dispose();
            new LoginPage();
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}
        SwingUtilities.invokeLater(StartPage::new);
    }
}