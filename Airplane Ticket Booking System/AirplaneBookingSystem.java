import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import com.toedter.calendar.JDateChooser;

public class AirplaneBookingSystem {
    private static JFrame mainFrame;
    private static Map<String, String> userData = new HashMap<>();
    private static Image backgroundImage, logoImage;
    private static String loggedInUser;
    private static java.util.List<Booking> bookings = new ArrayList<>();
    private static int nextBookingId = 1;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            loadImages();
            setupUserData();
            createLoginWindow();
        });
    }

    private static void loadImages() {
        try {
            backgroundImage = new ImageIcon("C:/Users/HARSHIT JAISWAL/Downloads/Aeroplane.jpg").getImage();
            logoImage = new ImageIcon("C:/Users/HARSHIT JAISWAL/Downloads/logo.png").getImage();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Could not load images.", "Image Error", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void setupUserData() {
        userData.put("admin", "password");
        userData.put("harshit", "jaiswal");
    }

    private static class BackgroundPanel extends JPanel {
        private Image bgImage;

        public BackgroundPanel(LayoutManager layout, Image bgImage) {
            super(layout);
            this.bgImage = bgImage;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null) {
                int width = getWidth(), height = getHeight();
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g2d.drawImage(bgImage, 0, 0, width, height, this);
                g2d.dispose();
            } else {
                g.setColor(new Color(135, 206, 235));
                g.fillRect(0, 0, getWidth(), getHeight());
            }
        }
    }

    private static class OutlinedLabel extends JLabel {
        public OutlinedLabel(String text) {
            super(text);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - textHeight) / 2 + fm.getAscent();

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2.5f));
            g2d.drawString(getText(), x, y);

            g2d.setColor(getForeground());
            g2d.setStroke(new BasicStroke(0));
            g2d.drawString(getText(), x, y);
            g2d.dispose();
        }
    }

    private static class OutlinedButton extends JButton {
        public OutlinedButton(String text) {
            super(text);
            setOpaque(false);
            setFocusPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (getModel().isPressed()) {
                g.setColor(getBackground().darker());
            } else {
                g.setColor(getBackground());
            }
            g.fillRect(0, 0, getWidth(), getHeight());

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(getText());
            int textHeight = fm.getHeight();
            int x = (getWidth() - textWidth) / 2;
            int y = (getHeight() - textHeight) / 2 + fm.getAscent();

            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2.5f));
            g2d.drawString(getText(), x, y);

            g2d.setColor(getForeground());
            g2d.setStroke(new BasicStroke(0));
            g2d.drawString(getText(), x, y);
            g2d.dispose();
        }
    }

    private static void createLoginWindow() {
        mainFrame = new JFrame("Ichigo Airlines - Login");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        BackgroundPanel panel = new BackgroundPanel(new BorderLayout(), backgroundImage);
        
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        
        JLabel topRightLogo = new JLabel();
        if (logoImage != null) {
            Image scaledLogo = logoImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
            topRightLogo.setIcon(new ImageIcon(scaledLogo));
        }
        topRightLogo.setHorizontalAlignment(SwingConstants.RIGHT);
        topPanel.add(topRightLogo, BorderLayout.EAST);
        
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(-10, 0, 0, 0));
        
        JPanel innerPanel = new JPanel();
        innerPanel.setOpaque(false);
        innerPanel.setLayout(new BoxLayout(innerPanel, BoxLayout.Y_AXIS));
        
        JLabel centerLogo = createLogo(350, 350);
        
        JPanel loginPanel = createLoginForm();
        
        innerPanel.add(createLogoPanel(centerLogo));
        innerPanel.add(Box.createVerticalStrut(2));
        innerPanel.add(loginPanel);
        
        centerPanel.add(innerPanel, BorderLayout.NORTH);
        
        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(createCopyrightPanel(), BorderLayout.SOUTH);
        
        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }

    private static JPanel createLoginForm() {
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setOpaque(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        JTextField usernameField = createTextField();
        JPasswordField passwordField = createPasswordField();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        loginPanel.add(createLabel("Username:", 18), gbc);
        
        gbc.gridx = 1;
        loginPanel.add(usernameField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        loginPanel.add(createLabel("Password:", 18), gbc);
        
        gbc.gridx = 1;
        loginPanel.add(passwordField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        OutlinedButton loginButton = createLoginButton(usernameField, passwordField);
        loginPanel.add(loginButton, gbc);
        
        gbc.gridy = 3;
        loginPanel.add(createSignupLink(), gbc);
        
        return loginPanel;
    }

    private static JLabel createLogo(int width, int height) {
        JLabel centerLogo = new JLabel();
        if (logoImage != null) {
            Image scaledCenterLogo = logoImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            centerLogo.setIcon(new ImageIcon(scaledCenterLogo));
            centerLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        return centerLogo;
    }

    private static JPanel createLogoPanel(JLabel logo) {
        JPanel logoPanel = new JPanel();
        logoPanel.setOpaque(false);
        logoPanel.add(logo);
        return logoPanel;
    }

    private static JPanel createCopyrightPanel() {
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setOpaque(false);
        OutlinedLabel copyrightLabel = createLabel("© Copyright reserved to Harshit Jaiswal", 14);
        copyrightLabel.setForeground(Color.WHITE);
        bottomPanel.add(copyrightLabel);
        return bottomPanel;
    }

    private static OutlinedLabel createLabel(String text, int fontSize) {
        OutlinedLabel label = new OutlinedLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        label.setForeground(Color.WHITE);
        return label;
    }

    private static OutlinedButton createLoginButton(JTextField usernameField, JPasswordField passwordField) {
        OutlinedButton loginButton = new OutlinedButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        loginButton.setBackground(new Color(30, 144, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.addActionListener(e -> login(usernameField.getText(), new String(passwordField.getPassword())));
        return loginButton;
    }

    private static JLabel createSignupLink() {
        JLabel signupLink = new JLabel("Don't have an account? Sign Up");
        signupLink.setFont(new Font("Arial", Font.PLAIN, 16));
        signupLink.setForeground(new Color(255, 215, 0));
        signupLink.setCursor(new Cursor(Cursor.HAND_CURSOR));
        signupLink.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showSignupScreen();
            }
        });
        return signupLink;
    }

    private static JTextField createTextField() {
        JTextField field = new JTextField(15);
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        return field;
    }

    private static JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField(15);
        field.setFont(new Font("Arial", Font.PLAIN, 18));
        return field;
    }

    private static void login(String username, String password) {
        if (userData.containsKey(username) && userData.get(username).equals(password)) {
            loggedInUser = username;
            JOptionPane.showMessageDialog(mainFrame, "Welcome back " + loggedInUser + "!", 
                "Login", JOptionPane.INFORMATION_MESSAGE);
            mainFrame.setVisible(false);
            showBookingScreen();
        } else {
            JOptionPane.showMessageDialog(mainFrame, "Invalid Username or Password", 
                "Login", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private static class Booking {
        private int id;
        private String name, flight, date, source, destination, gender;
        
        public Booking(int id, String name, String flight, String date, String source, String destination, String gender) {
            this.id = id;
            this.name = name;
            this.flight = flight;
            this.date = date;
            this.source = source;
            this.destination = destination;
            this.gender = gender;
        }
        
        public int getId() { return id; }
        public String getName() { return name; }
        public String getFlight() { return flight; }
        public String getDate() { return date; }
        public String getSource() { return source; }
        public String getDestination() { return destination; }
        public String getGender() { return gender; }
    }
}