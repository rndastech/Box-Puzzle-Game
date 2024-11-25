import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class LeaderboardPage extends JFrame {
    // Constants
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 700;
    private static final int BUTTON_TARGET_WIDTH = 200;
    private static final Color PRIMARY_COLOR = new Color(133, 94, 66);
    private static final Color SECONDARY_COLOR = new Color(173, 216, 230);
    private ImageIcon originalIcon;
    private ImageIcon hoverIcon;
    // Components
    private static final int BACK_BUTTON_WIDTH = 120;
    private static final int BACK_BUTTON_HEIGHT = 60;
    private static final float HOVER_SCALE_FACTOR = 1.1f;
    private Map<String, Integer> easyData = new HashMap<>();
    private Map<String, Integer> mediumData = new HashMap<>();
    private Map<String, Integer> hardData = new HashMap<>();
    private JPanel leaderboardPanel;
    private DefaultTableModel tableModel;
    private JLabel currentDifficultyLabel;
    private final double SCALE_FACTOR;

    public LeaderboardPage() {
        SCALE_FACTOR = determineScaleFactor();
        setupWindow();
        setupUI();
        readDataFromFiles();
        displayLeaderboard(easyData, "Easy Mode");
        setVisible(true);
    }

    private double determineScaleFactor() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        return gc.getDefaultTransform().getScaleX();
    }

    private void setupWindow() {
        setTitle("9-Box Puzzle Game Leaderboard");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setContentPane(new BackgroundPanel());
        getContentPane().setLayout(new BorderLayout(20, 20));
    }

    private void setupUI() {
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout(20, 0));
        headerPanel.setOpaque(false);

        // Title Label
        currentDifficultyLabel = new JLabel("Leaderboard", SwingConstants.CENTER);
        currentDifficultyLabel.setFont(new Font("Arial", Font.BOLD, 32));
        currentDifficultyLabel.setForeground(Color.WHITE);
        headerPanel.add(currentDifficultyLabel, BorderLayout.CENTER);

        // Back Button with custom size
        JButton backButton = createImageButton("Assets/Images/GameLoader/back.png", BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT);
        backButton.addActionListener(e -> {
            dispose();
            new GameHomepage();
        });
        headerPanel.add(backButton, BorderLayout.WEST);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setOpaque(false);

        // Mode Buttons with default size
        JButton easyButton = createImageButton("Assets/Images/GameLoader/easy.png", null, null);
        JButton mediumButton = createImageButton("Assets/Images/GameLoader/medium.png", null, null);
        JButton hardButton = createImageButton("Assets/Images/GameLoader/hard.png", null, null);

        // Add action listeners
        easyButton.addActionListener(e -> displayLeaderboard(easyData, "Easy Mode"));
        mediumButton.addActionListener(e -> displayLeaderboard(mediumData, "Medium Mode"));
        hardButton.addActionListener(e -> displayLeaderboard(hardData, "Difficult Mode"));

        buttonPanel.add(easyButton);
        buttonPanel.add(mediumButton);
        buttonPanel.add(hardButton);

        // Layout
        add(headerPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);

        // Leaderboard Table
        setupLeaderboardTable();
    }

    private JButton createImageButton(String iconPath, Integer customWidth, Integer customHeight) {
        JButton button = new JButton();
        try {
            // Load original image
            BufferedImage originalImage = ImageIO.read(new File(iconPath));

            // Determine target dimensions
            int targetWidth = customWidth != null ? customWidth : BUTTON_TARGET_WIDTH;
            int targetHeight;

            if (customHeight != null) {
                targetHeight = customHeight;
            } else {
                float aspectRatio = (float) originalImage.getHeight() / originalImage.getWidth();
                targetHeight = Math.round(targetWidth * aspectRatio);
            }

            // Create the normal-sized version with improved quality
            ImageIcon normalIcon = new ImageIcon(createHighQualityScaledImage(originalImage, targetWidth, targetHeight));

            // Create the hover version (slightly larger)
            int hoverWidth = Math.round(targetWidth * HOVER_SCALE_FACTOR);
            int hoverHeight = Math.round(targetHeight * HOVER_SCALE_FACTOR);
            ImageIcon hoverIcon = new ImageIcon(createHighQualityScaledImage(originalImage, hoverWidth, hoverHeight));

            // Set the initial icon and size
            button.setIcon(normalIcon);
            button.setPreferredSize(new Dimension(targetWidth, targetHeight));

            // Store icons as client properties for this specific button
            button.putClientProperty("normalIcon", normalIcon);
            button.putClientProperty("hoverIcon", hoverIcon);

        } catch (Exception e) {
            System.err.println("Error loading button icon from: " + iconPath);
            e.printStackTrace();
        }

        // Button styling
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);

        // Use button-specific icons in hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
                ImageIcon hoverIcon = (ImageIcon) button.getClientProperty("hoverIcon");
                if (hoverIcon != null) {
                    button.setIcon(hoverIcon);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                ImageIcon normalIcon = (ImageIcon) button.getClientProperty("normalIcon");
                if (normalIcon != null) {
                    button.setIcon(normalIcon);
                }
            }
        });

        return button;
    }

    private BufferedImage createHighQualityScaledImage(BufferedImage original, int targetWidth, int targetHeight) {
        // Create a new buffered image with transparency support
        BufferedImage scaledImage = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = scaledImage.createGraphics();

        // Configure for highest quality rendering
        configureHighQualityGraphics(g2d);

        // Additional high-quality scaling configurations
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

        // Draw the image with high-quality scaling
        g2d.drawImage(original, 0, 0, targetWidth, targetHeight, null);
        g2d.dispose();

        return scaledImage;
    }


    private void setupLeaderboardTable() {
        leaderboardPanel = new JPanel(new BorderLayout());
        leaderboardPanel.setOpaque(false);

        // Create table model with non-editable cells
        tableModel = new DefaultTableModel(new Object[]{"Rank", "Player", "Moves"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Create and customize table
        JTable table = new JTable(tableModel);
        customizeTable(table);

        // Scrollpane setup
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add to panel
        leaderboardPanel.add(scrollPane, BorderLayout.CENTER);
        add(leaderboardPanel, BorderLayout.SOUTH);
    }

    private void customizeTable(JTable table) {
        // Custom renderer for modern look
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);

                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? new Color(250, 250, 250) : Color.WHITE);
                } else {
                    c.setBackground(SECONDARY_COLOR);
                }

                setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
                setHorizontalAlignment(column == 0 || column == 2 ? JLabel.CENTER : JLabel.LEFT);

                return c;
            }
        });

        // Header styling
        JTableHeader header = table.getTableHeader();
        header.setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value,
                        isSelected, hasFocus, row, column);
                setBackground(PRIMARY_COLOR);
                setForeground(Color.WHITE);
                setFont(new Font("Arial", Font.BOLD, 16));
                setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
                setHorizontalAlignment(JLabel.CENTER);
                return c;
            }
        });

        // Table properties
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setFocusable(false);
        table.setSelectionBackground(SECONDARY_COLOR);
        table.setSelectionForeground(Color.BLACK);

        // Column widths
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100);  // Rank
        columnModel.getColumn(1).setPreferredWidth(300);  // Player
        columnModel.getColumn(2).setPreferredWidth(100);  // Moves
    }

    private JButton createStyledButton(String text, String symbol, Color color) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                configureHighQualityGraphics(g2d);

                // Draw background
                if (getModel().isPressed()) {
                    g2d.setColor(color.darker());
                } else if (getModel().isRollover()) {
                    g2d.setColor(color.brighter());
                } else {
                    g2d.setColor(color);
                }

                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

                // Draw text
                g2d.setColor(Color.WHITE);
                FontMetrics metrics = g2d.getFontMetrics();
                String buttonText = symbol + " " + text;
                int x = (getWidth() - metrics.stringWidth(buttonText)) / 2;
                int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
                g2d.drawString(buttonText, x, y);

                g2d.dispose();
            }
        };

        // Button setup
        button.setPreferredSize(new Dimension(150, 40));
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);

        // Hover effect
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

    private void configureHighQualityGraphics(Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
    }

    private void addStyledButton(JPanel panel, String text, String symbol, Color color, Runnable action) {
        JButton button = createStyledButton(text, symbol, color);
        button.addActionListener(e -> action.run());
        panel.add(button);
    }

    private class BackgroundPanel extends JPanel {
        private BufferedImage backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = ImageIO.read(new File("Assets/Images/GameLoader/background.jpg"));
            } catch (IOException e) {
                // Use gradient background if image fails to load
                backgroundImage = null;
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            configureHighQualityGraphics(g2d);

            if (backgroundImage != null) {
                // Draw scaled background image
                g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);
            } else {
                // Gradient background fallback
                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(240, 248, 255),
                        0, getHeight(), new Color(230, 240, 250)
                );
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
            g2d.dispose();
        }
    }

    private void displayLeaderboard(Map<String, Integer> data, String title) {
        currentDifficultyLabel.setText(title);
        tableModel.setRowCount(0);

        ArrayList<Map.Entry<String, Integer>> sortedData = new ArrayList<>(data.entrySet());
        sortedData.sort(Map.Entry.comparingByValue());

        int rank = 1;
        for (Map.Entry<String, Integer> entry : sortedData) {
            tableModel.addRow(new Object[]{
                    rank++,
                    entry.getKey(),
                    entry.getValue()
            });
        }
    }

    private void readDataFromFiles() {
        readDataFromFile("Files/Easy.txt", easyData);
        readDataFromFile("Files/Medium.txt", mediumData);
        readDataFromFile("Files/Hard.txt", hardData);
    }

    private void readDataFromFile(String fileName, Map<String, Integer> dataMap) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                if (parts.length == 2) {
                    dataMap.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
        } catch (IOException e) {
            createFile(fileName, dataMap);
        }
    }

    private void createFile(String fileName, Map<String, Integer> dataMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // Create empty file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Enable HiDPI scaling
        System.setProperty("sun.java2d.uiScale", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new LeaderboardPage();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}