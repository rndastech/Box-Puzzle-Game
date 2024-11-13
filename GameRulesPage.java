import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameRulesPage is the page that displays the rules of the sliding puzzle game.
 */
public class GameRulesPage extends JFrame {

    private JButton backButton;

    /**
     * Constructor to set up the Game Rules page.
     */
    public GameRulesPage() {
        setTitle("Game Rules");
        setSize(800, 600); // Set the window size
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Set a layout for the content
        setLayout(new BorderLayout());

        // === Header Panel ===
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(40, 40, 40)); // Dark background for header
        headerPanel.setPreferredSize(new Dimension(getWidth(), 60)); // Set height of header
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // Title Label
        JLabel titleLabel = new JLabel("Game Rules");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36)); // Large, bold font for title
        titleLabel.setForeground(new Color(255, 204, 0)); // Dark yellow color
        headerPanel.add(titleLabel);

        // === Content Panel (Game Rules Text) ===
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(50, 50, 50)); // Dark gray background for content
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // Padding inside content panel

        // Text area to display game rules
        JTextArea textArea = new JTextArea(
                "The 9-box is a sliding puzzle consisting of a 3x3 grid of numbered boxes with one missing.\n" +
                "The boxes will be jumbled at the start.\n" +
                "You have to rearrange them in ascending order.\n" +
                "Let's see the minimum moves you need!"
        );
        textArea.setEditable(false);  // Disable editing
        textArea.setFont(new Font("Arial", Font.PLAIN, 18)); // Use a clean, readable font
        textArea.setForeground(new Color(255, 204, 0)); // Dark yellow color
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setOpaque(false); // Transparent background for text area
        textArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Padding around the text

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setOpaque(false); // Set the scroll pane to be transparent
        scrollPane.getViewport().setOpaque(false); // Remove scroll pane background
        contentPanel.add(scrollPane, BorderLayout.CENTER);

        // === Back Button ===
        backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.BOLD, 14));
        backButton.setBackground(new Color(80, 80, 80)); // Dark button color
        backButton.setForeground(Color.WHITE); // White text for the button
        backButton.setPreferredSize(new Dimension(100, 40));
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // When the back button is clicked, return to the GameHomepage
                dispose(); // Close this window
                new GameHomepage(); // Reopen the main game homepage window
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(50, 50, 50)); // Same dark background
        buttonPanel.add(backButton);

        // === Add Components ===
        add(headerPanel, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    /**
     * The main method to run the GameRulesPage application.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Ensure the GUI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new GameRulesPage());
    }
}
