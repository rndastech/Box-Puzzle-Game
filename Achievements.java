import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;

public class Achievements extends JFrame {
    private static final int BUTTON_SIZE = 150;
    JPanel achievementsPanel;
    int scores[];
    public Achievements() {
        setTitle("Achievements");
        setSize(1024, 768);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        scores = new int[3];
        // Background panel similar to home screen
        BackgroundPanel backgroundPanel = new BackgroundPanel("images/background.jpg");
        backgroundPanel.setLayout(new BorderLayout());
        setContentPane(backgroundPanel);
        readBestScores();
        // === Back Button in Top-Left Panel ===
        JPanel topPanel = new JPanel(new BorderLayout());  // Main top panel
        topPanel.setOpaque(false);  // Transparent panel to display background

        JPanel backButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));  // Left-aligned back button
        backButtonPanel.setOpaque(false);

        JButton backButton = createImageButton("images/back.png", 120, 60);  // Increased dimensions for back button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();  // Close this window
                new GameHomepage();  // Reopen the main game homepage window
            }
        });

        backButtonPanel.add(backButton);  // Add back button to the panel
        topPanel.add(backButtonPanel, BorderLayout.NORTH);  // Place in the top-left corner

        // === Achievements Grid Panel ===
        achievementsPanel = new JPanel(new GridLayout(3, 3, 15, 15));
        achievementsPanel.setOpaque(false);
//        for (int i = 1; i <= 9; i++) {
//            JButton achievementButton = createImageButton("Assets/Images/Achievements/" + i + ".png", BUTTON_SIZE, BUTTON_SIZE);
//            int achievementNumber = i;
//            achievementButton.addActionListener(e -> showAchievementDialog(achievementNumber));
//            achievementsPanel.add(achievementButton);
//        }
        String lockedImagePath = "Assets/Images/Achievements/locked.png";

        // Iterate over the achievements (buttons) and apply the limits
        for (int i = 1; i <= 9; i++) {
            // Get the achievement button for the ith achievement (1-based index)// Get the button for the ith achievement
            JButton achievementButton;
            // Easy Level Achievements (1-3)
            if (i == 1) {
                if (scores[0] <= 100) {
               achievementButton = createImageButton("Assets/Images/Achievements/" + i + ".png", BUTTON_SIZE, BUTTON_SIZE);
                } else {
                    achievementButton = createImageButton("Assets/Images/Achievements/locked.png", BUTTON_SIZE, BUTTON_SIZE);
                }
            } else if (i == 2) {
                if (scores[0] <= 50) {
                    achievementButton = createImageButton("Assets/Images/Achievements/" + i + ".png", BUTTON_SIZE, BUTTON_SIZE);
                } else {
                   achievementButton = createImageButton("Assets/Images/Achievements/locked.png", BUTTON_SIZE, BUTTON_SIZE);
                }
            } else if (i == 3) {
                if (scores[0] <= 31) {
                    achievementButton = createImageButton("Assets/Images/Achievements/" + i + ".png", BUTTON_SIZE, BUTTON_SIZE);
                } else {
                   achievementButton = createImageButton("Assets/Images/Achievements/locked.png", BUTTON_SIZE, BUTTON_SIZE);
                }
            }

            // Medium Level Achievements (4-6)
            else if (i == 4) {
                if (scores[1] <= 100) {
                    achievementButton = createImageButton("Assets/Images/Achievements/" + i + ".png", BUTTON_SIZE, BUTTON_SIZE);
                } else {
                    achievementButton = createImageButton("Assets/Images/Achievements/locked.png", BUTTON_SIZE, BUTTON_SIZE);
                }
            } else if (i == 5) {
                if (scores[1] <= 50) {
                 achievementButton = createImageButton("Assets/Images/Achievements/" + i + ".png", BUTTON_SIZE, BUTTON_SIZE);
                } else {
                  achievementButton = createImageButton("Assets/Images/Achievements/locked.png", BUTTON_SIZE, BUTTON_SIZE);
                }
            } else if (i == 6) {
                if (scores[1] <= 31) {
                    achievementButton = createImageButton("Assets/Images/Achievements/" + i + ".png", BUTTON_SIZE, BUTTON_SIZE);
                } else {
                   achievementButton = createImageButton("Assets/Images/Achievements/locked.png", BUTTON_SIZE, BUTTON_SIZE);
                }
            }

            // Hard Level Achievements (7-9)
            else if (i == 7) {
                if (scores[2] <= 100) {
                    achievementButton = createImageButton("Assets/Images/Achievements/" + i + ".png", BUTTON_SIZE, BUTTON_SIZE);
                } else {
                  achievementButton = createImageButton("Assets/Images/Achievements/locked.png", BUTTON_SIZE, BUTTON_SIZE);
                }
            } else if (i == 8) {
                if (scores[2] <= 50) {
                   achievementButton = createImageButton("Assets/Images/Achievements/" + i + ".png", BUTTON_SIZE, BUTTON_SIZE);
                } else {
                 achievementButton = createImageButton("Assets/Images/Achievements/locked.png", BUTTON_SIZE, BUTTON_SIZE);
                }
            } else{
                if (scores[2] <= 31) {
                    achievementButton = createImageButton("Assets/Images/Achievements/" + i + ".png", BUTTON_SIZE, BUTTON_SIZE);
                } else {
                    achievementButton = createImageButton("Assets/Images/Achievements/locked.png", BUTTON_SIZE, BUTTON_SIZE);
                }
            }
            int achievementNumber = i;
            achievementButton.addActionListener(e -> showAchievementDialog(achievementNumber));
            achievementsPanel.add(achievementButton);
        }
        System.out.println(scores[0]+" "+scores[1]+" "+scores[2]);
        // Center Layout for Top Panel and Achievements Grid
        backgroundPanel.add(topPanel, BorderLayout.NORTH);  // Add top panel with back button
        backgroundPanel.add(achievementsPanel, BorderLayout.CENTER);  // Add grid in center
//        setAchievementLimitsAndLock();
        setVisible(true);
    }

    private JButton createImageButton(String iconPath, int width, int height) {
        JButton button = new JButton();
        try {
            BufferedImage originalImage = ImageIO.read(new File(iconPath));
            BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.drawImage(originalImage, 0, 0, width, height, null);
            g2d.dispose();

            button.setIcon(new ImageIcon(scaledImage));
            button.setPreferredSize(new Dimension(width, height));
        } catch (Exception e) {
            System.out.println("Button icon not found at: " + iconPath);
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

    private void showAchievementDialog(int achievementNumber) {
        JDialog achievementDialog = new JDialog(this, "Achievement " + achievementNumber, true);
        achievementDialog.setSize(300, 200);
        achievementDialog.setLocationRelativeTo(this);

        JLabel detailsLabel = new JLabel("Details of Achievement " + achievementNumber, SwingConstants.CENTER);
        detailsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        achievementDialog.add(detailsLabel);

        achievementDialog.setVisible(true);
    }

    private void setAchievementLimitsAndLock() {
        // Define the path to the locked image
        String lockedImagePath = "Assets/Images/Achievements/locked.png";

        // Iterate over the achievements (buttons) and apply the limits
        for (int i = 1; i <= 9; i++) {
            // Get the achievement button for the ith achievement (1-based index)
            JButton achievementButton = (JButton) achievementsPanel.getComponent(i - 1); // Get the button for the ith achievement

            // Easy Level Achievements (1-3)
            if (i == 1) {
                if (scores[0] <= 100) {
//                    achievementButton.setIcon(new ImageIcon("Assets/Images/Achievements/1.png"));
                } else {
                    achievementButton.setIcon(new ImageIcon(lockedImagePath));
                }
            } else if (i == 2) {
                if (scores[0] <= 50) {
//                    achievementButton.setIcon(new ImageIcon("Assets/Images/Achievements/2.png"));
                } else {
                    achievementButton.setIcon(new ImageIcon(lockedImagePath));
                }
            } else if (i == 3) {
                if (scores[0] <= 31) {
//                    achievementButton.setIcon(new ImageIcon("Assets/Images/Achievements/3.png"));
                } else {
                    achievementButton.setIcon(new ImageIcon(lockedImagePath));
                }
            }

            // Medium Level Achievements (4-6)
            else if (i == 4) {
                if (scores[1] <= 100) {
//                    achievementButton.setIcon(new ImageIcon("Assets/Images/Achievements/4.png"));
                } else {
                    achievementButton.setIcon(new ImageIcon(lockedImagePath));
                }
            } else if (i == 5) {
                if (scores[1] <= 50) {
//                    achievementButton.setIcon(new ImageIcon("Assets/Images/Achievements/5.png"));
                } else {
                    achievementButton.setIcon(new ImageIcon(lockedImagePath));
                }
            } else if (i == 6) {
                if (scores[1] <= 31) {
//                    achievementButton.setIcon(new ImageIcon("Assets/Images/Achievements/6.png"));
                } else {
                    achievementButton.setIcon(new ImageIcon(lockedImagePath));
                }
            }

            // Hard Level Achievements (7-9)
            else if (i == 7) {
                if (scores[2] <= 100) {
//                    achievementButton.setIcon(new ImageIcon("Assets/Images/Achievements/7.png"));
                } else {
                    achievementButton.setIcon(new ImageIcon(lockedImagePath));
                }
            } else if (i == 8) {
                if (scores[2] <= 50) {
//                    achievementButton.setIcon(new ImageIcon("Assets/Images/Achievements/8.png"));
                } else {
                    achievementButton.setIcon(new ImageIcon(lockedImagePath));
                }
            } else if (i == 9) {
                if (scores[2] <= 31) {
//                    achievementButton.setIcon(new ImageIcon("Assets/Images/Achievements/9.png"));
                } else {
                    achievementButton.setIcon(new ImageIcon(lockedImagePath));
                }
            }
        }
        System.out.println(scores[0]+" "+scores[1]+" "+scores[2]);
    }

    private void readBestScores() {
        try {
            // Initialize variables to track the lowest scores
            int easyMin = Integer.MAX_VALUE;
            int mediumMin = Integer.MAX_VALUE;
            int hardMin = Integer.MAX_VALUE;

            // Read Easy scores from file
            BufferedReader easyReader = new BufferedReader(new FileReader("Files/Easy_lead.txt"));
            String line;
            while ((line = easyReader.readLine()) != null) {
                try {
                    int score = Integer.parseInt(line.trim());
                    easyMin = Math.min(easyMin, score); // Track the lowest score
                } catch (NumberFormatException e) {
                    // Handle invalid numbers in the file
                    System.out.println("Invalid score in Easy file: " + line);
                }
            }
            easyReader.close();

            // Read Medium scores from file
            BufferedReader mediumReader = new BufferedReader(new FileReader("Files/Medium_lead.txt"));
            while ((line = mediumReader.readLine()) != null) {
                try {
                    int score = Integer.parseInt(line.trim());
                    mediumMin = Math.min(mediumMin, score); // Track the lowest score
                } catch (NumberFormatException e) {
                    // Handle invalid numbers in the file
                    System.out.println("Invalid score in Medium file: " + line);
                }
            }
            mediumReader.close();

            // Read Hard scores from file
            BufferedReader hardReader = new BufferedReader(new FileReader("Files/Hard_lead.txt"));
            while ((line = hardReader.readLine()) != null) {
                try {
                    int score = Integer.parseInt(line.trim());
                    hardMin = Math.min(hardMin, score); // Track the lowest score
                } catch (NumberFormatException e) {
                    // Handle invalid numbers in the file
                    System.out.println("Invalid score in Hard file: " + line);
                }
            }
            hardReader.close();
            scores[0] = easyMin;
            scores[1] = mediumMin;
            scores[2] = hardMin;
            // Assign the lowest values found for each difficulty

        } catch (IOException e) {
            e.printStackTrace();
            // In case of an error, default to 0 for all scores
            scores[0] = scores[1] = scores[2] = Integer.MAX_VALUE;
        }

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(Achievements::new);
    }
}
