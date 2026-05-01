import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class LoginPage {

    public LoginPage() {
        JFrame frame = new JFrame("Login");
        frame.setSize(800, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        // Load background image using ImageLoader
        Image bgImg = ImageLoader.load("login.jpg", 800, 500);
        JLabel bg;
        if (bgImg != null) {
            bg = new JLabel(new ImageIcon(bgImg));
        } else {
            bg = new JLabel();
            bg.setBackground(new Color(20, 30, 55));
            bg.setOpaque(true);
        }
        bg.setLayout(null);

        // Dark overlay
        JPanel overlay = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(new Color(0, 0, 0, 130));
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        overlay.setOpaque(false);
        overlay.setLayout(null);
        overlay.setBounds(0, 0, 800, 500);

        // Login card
        JPanel card = new JPanel() {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(new Color(255, 255, 255, 30));
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(new Color(255, 255, 255, 60));
                g2d.setStroke(new BasicStroke(1f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);
                g2d.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(null);
        card.setBounds(250, 80, 300, 340);

        JLabel loginTitle = new JLabel("Welcome Back", JLabel.CENTER);
        loginTitle.setBounds(0, 20, 300, 40);
        loginTitle.setFont(new Font("Georgia", Font.BOLD, 24));
        loginTitle.setForeground(Color.WHITE);

        JLabel loginSub = new JLabel("Sign in to continue", JLabel.CENTER);
        loginSub.setBounds(0, 58, 300, 20);
        loginSub.setFont(new Font("Georgia", Font.ITALIC, 13));
        loginSub.setForeground(new Color(200, 210, 255));

        JLabel userLabel = new JLabel("Username");
        userLabel.setBounds(30, 100, 240, 20);
        userLabel.setFont(new Font("Arial", Font.BOLD, 12));
        userLabel.setForeground(new Color(200, 220, 255));

        JTextField user = new JTextField();
        user.setBounds(30, 122, 240, 36);
        user.setFont(new Font("Arial", Font.PLAIN, 14));
        user.setBackground(new Color(255, 255, 255, 200));
        user.setForeground(new Color(20, 20, 60));
        user.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 255), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        user.setCaretColor(new Color(20, 20, 60));

        JLabel passLabel = new JLabel("Password");
        passLabel.setBounds(30, 172, 240, 20);
        passLabel.setFont(new Font("Arial", Font.BOLD, 12));
        passLabel.setForeground(new Color(200, 220, 255));

        JPasswordField pass = new JPasswordField();
        pass.setBounds(30, 194, 240, 36);
        pass.setFont(new Font("Arial", Font.PLAIN, 14));
        pass.setBackground(new Color(255, 255, 255, 200));
        pass.setForeground(new Color(20, 20, 60));
        pass.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(100, 150, 255), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        pass.setCaretColor(new Color(20, 20, 60));

        JLabel errorLabel = new JLabel("", JLabel.CENTER);
        errorLabel.setBounds(30, 240, 240, 20);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 12));
        errorLabel.setForeground(new Color(255, 100, 100));

        JButton btn = new JButton("Login") {
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
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.dispose();
                super.paintComponent(g);
            }
        };
        btn.setBounds(80, 272, 140, 40);
        btn.setFont(new Font("Georgia", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        card.add(loginTitle);
        card.add(loginSub);
        card.add(userLabel);
        card.add(user);
        card.add(passLabel);
        card.add(pass);
        card.add(errorLabel);
        card.add(btn);

        overlay.add(card);
        bg.add(overlay);
        frame.setContentPane(bg);

        ActionListener loginAction = e -> {
            if (user.getText().equals("admin") &&
                new String(pass.getPassword()).equals("1234")) {
                frame.dispose();
                new EmployeeGUI();
            } else {
                errorLabel.setText("Invalid username or password!");
                pass.setText("");
            }
        };

        btn.addActionListener(loginAction);
        pass.addActionListener(loginAction);

        frame.setVisible(true);
    }
}