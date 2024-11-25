import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

class BackgroundPanel extends JPanel {
    private Image backgroundImage;

    public BackgroundPanel(String imagePath) {
        try {
            backgroundImage = ImageIO.read(new File(imagePath));
        } catch (Exception e) {
            System.out.println("Background image not found at: " + imagePath);
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}

public class GameHomepage extends JFrame {
    private static final int BUTTON_TARGET_WIDTH = 200;

    public GameHomepage() {
        setTitle("Game Homepage");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel("Assets/Images/GameLoader/background.jpg");
        backgroundPanel.setLayout(new BorderLayout(20, 20));
        setContentPane(backgroundPanel);

        // === Top Panel ===
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(1, 4, 20, 0));
        topPanel.setOpaque(false);

        // Initialize and configure buttons with their respective action listeners
        JButton leaderboardButton = createImageButton("Assets/Images/GameLoader/leaderboard.png");
        leaderboardButton.addActionListener(e -> showleaderboard());

        JButton aboutUsButton = createImageButton("Assets/Images/GameLoader/aboutus.png");
        aboutUsButton.addActionListener(e -> showaboutus());

        JButton gameRulesButton = createImageButton("Assets/Images/GameLoader/rules.png");
        gameRulesButton.addActionListener(e -> showRules());

        JButton achievementsButton = createImageButton("Assets/Images/GameLoader/achievements.png");
        achievementsButton.addActionListener(e -> showAchievements());

        // Add buttons in order
        topPanel.add(leaderboardButton);
        topPanel.add(aboutUsButton);
        topPanel.add(gameRulesButton);
        topPanel.add(achievementsButton);

        // Create a wrapper panel to center the top panel
        JPanel topWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 10));
        topWrapper.setOpaque(false);
        topWrapper.add(topPanel);

        // === Title Label ===
        JLabel titleLabel = new JLabel(new ImageIcon("Assets/Images/GameLoader/logo.png"));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER); // Center the image

        // === Center Panel ===
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setOpaque(false);

        JPanel levelsPanel = new JPanel();
        levelsPanel.setLayout(new GridLayout(4, 1, 0, 20));
        levelsPanel.setOpaque(false);

        // Create and configure level buttons with their respective action listeners
        JButton easyButton = createImageButton("Assets/Images/GameLoader/easy.png");
        easyButton.addActionListener(e -> startGame("Easy"));

        JButton mediumButton = createImageButton("Assets/Images/GameLoader/medium.png");
        mediumButton.addActionListener(e -> startGame("Medium"));

        JButton hardButton = createImageButton("Assets/Images/GameLoader/hard.png");
        hardButton.addActionListener(e -> startGame("Hard"));

        JLabel groupNameLabel = new JLabel(new ImageIcon("Assets/Images/GameLoader/groupName.png"));
        groupNameLabel.setHorizontalAlignment(SwingConstants.CENTER);  // Center the image
        levelsPanel.add(groupNameLabel);

        levelsPanel.add(easyButton);
        levelsPanel.add(mediumButton);
        levelsPanel.add(hardButton);

        centerPanel.add(levelsPanel);

        // Create a panel for vertical layout of title and center content
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setOpaque(false);
        contentPanel.add(titleLabel, BorderLayout.NORTH);
        contentPanel.add(centerPanel, BorderLayout.CENTER);

        // Add panels to frame
        add(topWrapper, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);

        // Add padding around the frame
        ((JPanel)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        setVisible(true);
    }

    private void startGame(String difficulty) {
        // Close the current window
        this.dispose();

        // Start the game window
        SwingUtilities.invokeLater(() -> {
            ImagePuzzleGame game = new ImagePuzzleGame(difficulty);
            game.setVisible(true);
        });
    }

    private void showRules() {
        // Close the current window
        this.dispose();

        // Start the game window
        SwingUtilities.invokeLater(() -> {
            GameRulesPage game = new GameRulesPage();
            game.setVisible(true);
        });
    }

    private void showleaderboard() {
        // Close the current window
        this.dispose();

        // Start the game window
        SwingUtilities.invokeLater(() -> {
            LeaderboardPage game = new LeaderboardPage();
            game.setVisible(true);
        });
    }

    private void showaboutus() {
        // Close the current window
        this.dispose();

        // Start the game window
        SwingUtilities.invokeLater(() -> {
            GameAboutUs game = new GameAboutUs();
            game.setVisible(true);
        });
    }

    private void showAchievements() {
        // Close the current window
        this.dispose();

        // Start the game window
        SwingUtilities.invokeLater(() -> {
            Achievements game = new Achievements();
            game.setVisible(true);
        });
    }

    private JButton createImageButton(String iconPath) {
        JButton button = new JButton();
        try {
            BufferedImage originalImage = ImageIO.read(new File(iconPath));
            float aspectRatio = (float) originalImage.getHeight() / originalImage.getWidth();
            int targetHeight = Math.round(BUTTON_TARGET_WIDTH * aspectRatio);

            BufferedImage scaledImage = new BufferedImage(BUTTON_TARGET_WIDTH, targetHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawImage(originalImage, 0, 0, BUTTON_TARGET_WIDTH, targetHeight, null);
            g2d.dispose();

            button.setIcon(new ImageIcon(scaledImage));
            button.setPreferredSize(new Dimension(BUTTON_TARGET_WIDTH, targetHeight));

        } catch (Exception e) {
            System.out.println("Button icon not found at: " + iconPath);
            e.printStackTrace();
        }

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        // Add hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });

        return button;
    }

    private void openNewWindow(String title) {
        JFrame newFrame = new JFrame(title);
        newFrame.setSize(400, 300);
        newFrame.setLocationRelativeTo(this);
        newFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel label = new JLabel(title, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        newFrame.add(label);

        newFrame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> new GameHomepage());
    }
}