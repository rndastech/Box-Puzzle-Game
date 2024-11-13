import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class GameRulesPage extends JFrame {
    private JButton backButton;

    public GameRulesPage() {
        // Frame setup
        this.setTitle("Game Rules");
        this.setSize(1024, 756);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Make sure the window closes properly
        this.setLocationRelativeTo(null); // Center window
        this.setLayout(new BorderLayout());

        // Title bar at the top
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(40, 40, 40));
        headerPanel.setPreferredSize(new Dimension(this.getWidth(), 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align to the left
        JLabel titleLabel = new JLabel("Game Rules");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(255, 204, 0));
        headerPanel.add(titleLabel);

        // Text area for game description
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(50, 50, 50));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JTextArea gameRulesText = new JTextArea(
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
                        "Good luck, and have fun solving the puzzle!"
        );
        gameRulesText.setEditable(false);
        gameRulesText.setFont(new Font("Arial", Font.PLAIN, 18));
        gameRulesText.setForeground(new Color(255, 204, 0));
        gameRulesText.setLineWrap(true);
        gameRulesText.setWrapStyleWord(true);
        gameRulesText.setOpaque(false);
        gameRulesText.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JScrollPane scrollPane = new JScrollPane(gameRulesText);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // Back button with an image
        backButton = new JButton();
        backButton.setIcon(new ImageIcon("./images/back.png")); // Set the image on the button
        backButton.setPreferredSize(new Dimension(120, 40)); // Set button size, adjust as necessary
        backButton.setBackground(new Color(80, 80, 80));
        backButton.setBorder(BorderFactory.createEmptyBorder()); // Remove border
        backButton.addActionListener(e -> {
            // Define action on button click (e.g., close current window and open previous)
            dispose(); // Close the current frame
            new GameHomepage().setVisible(true); // Uncomment this to navigate to GameHomepage (if exists)
        });

        // Panel to hold the back button
        JPanel topLeftPanel = new JPanel();
        topLeftPanel.setBackground(new Color(50, 50, 50));
        topLeftPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Align the button to the left
        topLeftPanel.add(backButton);

        // Add components to the frame
        this.add(headerPanel, BorderLayout.NORTH); // Add title to the top
        this.add(contentPanel, BorderLayout.CENTER); // Add the content panel with rules
        this.add(topLeftPanel, BorderLayout.NORTH); // Add the back button panel to the top-left

        // Set frame visibility
        this.setVisible(true);
    }

    public static void main(String[] var0) {
        SwingUtilities.invokeLater(() -> {
            new GameRulesPage();
        });
    }
}
