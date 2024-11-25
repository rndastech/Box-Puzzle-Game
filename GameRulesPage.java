import java.awt.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class GameRulesPage extends JFrame {
    private JButton backButton;
    private BufferedImage backgroundImage;
    private static final int BACK_BUTTON_WIDTH = 120;
    private static final int BACK_BUTTON_HEIGHT = 60;
    private static final int SECTION_SPACING = 8;

    public GameRulesPage() {
        setTitle("Game Rules");
        setSize(1024, 768);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            backgroundImage = ImageIO.read(new File("Assets/Images/GameLoader/background.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel mainPanel = new JPanel() {
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
        };
        mainPanel.setLayout(new BorderLayout());

        // Top Panel with Back Button and Logo
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setOpaque(false);

        // Back button
        backButton = createImageButton("Assets/Images/GameLoader/back.png", BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
        backButton.addActionListener(e -> {
            dispose();
            new GameHomepage().setVisible(true);
        });
        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backButtonPanel.setOpaque(false);
        backButtonPanel.add(backButton);
        topPanel.add(backButtonPanel, BorderLayout.WEST);

        // Logo
        JLabel logoLabel = new JLabel(new ImageIcon("Assets/Images/GameLoader/logo.png"));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        topPanel.add(logoLabel, BorderLayout.CENTER);

        // Center Panel containing group name and content
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);

        // Group Name Panel
        JPanel groupNamePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        groupNamePanel.setOpaque(false);
        JLabel groupNameLabel = new JLabel(new ImageIcon("Assets/Images/GameLoader/groupName.png"));
        groupNamePanel.add(groupNameLabel);
        centerPanel.add(groupNamePanel);
        centerPanel.add(Box.createVerticalStrut(10));

        // Content Panel
        JPanel contentPanel = createStyledPanel(600);
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));

        addSection(contentPanel, "Game Rules",
                "9-Box Sliding Puzzle Game Rules:\n\n" +
                        "1. Objective: The goal of the game is to rearrange the numbered tiles in ascending order from 1 to 8, with the blank space in the last (bottom-right) position.\n\n" +
                        "2. Game Setup: The puzzle consists of a 3x3 grid containing eight numbered tiles (1 to 8) and one empty space. At the beginning of the game, the tiles are shuffled randomly within the grid, leaving the empty space in a random position.\n\n" +
                        "3. How to Play: You can move a tile into the empty space by clicking on it or using the arrow keys (up, down, left, right). Only tiles adjacent to the empty space can be moved into it.\n\n" +
                        "4. Movement Rules: You can only move one tile at a time. Moving a tile into the empty space changes the position of the empty space as well. The goal is to move the tiles into the correct positions by sliding them around.\n\n" +
                        "5. Winning the Game: The game is won when the tiles are arranged in ascending order from 1 to 8, with the empty space in the bottom-right corner.\n\n" +
                        "6. Strategy Tips:\n" +
                        "- Try to work from top-left to bottom-right to systematically arrange the tiles.\n" +
                        "- Focus on solving smaller sections of the puzzle first.\n" +
                        "- Always keep an eye on the empty space and use it to your advantage.\n\n" +
                        "Good luck, and have fun solving the puzzle!");

        // Wrap contentPanel in a panel with FlowLayout for centering
        JPanel contentWrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        contentWrapperPanel.setOpaque(false);
        contentWrapperPanel.add(contentPanel);

        centerPanel.add(contentWrapperPanel);

        // Add panels to main panel
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        // Add padding around the main panel
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        setContentPane(mainPanel);
    }

    private JPanel createStyledPanel(int width) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(true);
        panel.setBackground(new Color(245, 245, 220, 255));  // Semi-transparent beige
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(0, 34, 68), 2),
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        panel.setPreferredSize(new Dimension(width, 500));
        panel.setMaximumSize(new Dimension(width, 500));
        return panel;
    }

    private void addSection(JPanel container, String title, String content) {
        if (container.getComponentCount() > 0) {
            container.add(Box.createVerticalStrut(SECTION_SPACING));
            container.add(new JSeparator());
            container.add(Box.createVerticalStrut(SECTION_SPACING));
        }

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(0, 34, 68));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(titleLabel);
        container.add(Box.createVerticalStrut(5));

        JTextArea textArea = new JTextArea(content);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setForeground(new Color(0, 34, 68));
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);
        textArea.setEditable(false);
        textArea.setFocusable(false);
        textArea.setMargin(new Insets(2, 5, 2, 5));
        textArea.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(textArea);
    }

    private JButton createImageButton(String imagePath, int targetWidth, int targetHeight) {
        JButton button = new JButton();
        try {
            BufferedImage originalImage = ImageIO.read(new File(imagePath));
            BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight,
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                    RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.drawImage(originalImage, 0, 0, targetWidth, targetHeight, null);
            g2d.dispose();

            button.setIcon(new ImageIcon(scaledImage));
            button.setPreferredSize(new Dimension(targetWidth, targetHeight));

        } catch (IOException e) {
            e.printStackTrace();
        }

        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new GameRulesPage().setVisible(true);
        });
    }
}