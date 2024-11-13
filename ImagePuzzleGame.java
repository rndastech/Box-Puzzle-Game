import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.border.EmptyBorder;
import java.util.List;

public class ImagePuzzleGame extends JFrame implements ActionListener {
    JPanel p1;
    JPanel p2;
    JButton re;
    JButton resignButton;
    JButton backButton;
    JButton[][] b = new JButton[3][3];
    int[][] ar = new int[3][3];
    JLabel l1;
    JLabel l2;
    JLabel l3;
    JLabel statusLabel;
    JLabel bestScoreLabel;
    int moves;
    BufferedImage[] imageParts = new BufferedImage[9];
    BufferedImage[] emptyImg = new BufferedImage[1];
    final Color EMPTY_COLOR = new Color(200, 200, 200);
    boolean gameActive = false;
    String level;
    String bestScoresFile;
    public ImagePuzzleGame(String l) {
        super("9 Box Image Puzzle");
        moves = 0;
        level = l;
        bestScoresFile = "Files/"+level+"_lead.txt";
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String name = JOptionPane.showInputDialog(this, "RULES\nTo move: If there is an empty adjacent square next to a tile, a tile may be slid into the empty location.\nTo win: The image parts must be moved back into their original positions.\n\nEnter your name:", "9 Box Image Puzzle", JOptionPane.QUESTION_MESSAGE);

        setSize(1024, 768);
        setLayout(new BorderLayout());

        // Set the background image to the same as the home screen
        BackgroundPanel backgroundPanel = new BackgroundPanel("images/background.jpg");
        setContentPane(backgroundPanel);

        p1 = new JPanel(new FlowLayout());
        p2 = new JPanel(new GridLayout(3, 3, 2, 2)); // Remove grid gaps
        p2.setBackground(Color.BLACK);

        l1 = new JLabel("    ");
        l2 = new JLabel("Moves: 0");
        l3 = new JLabel("Username: " + name);
        bestScoreLabel = new JLabel("Best: ");
        statusLabel = new JLabel("Status: Not Started");

        re = new JButton("New Game");
        re.addActionListener(this);

        resignButton = new JButton("Resign");
        resignButton.addActionListener(this);
        resignButton.setEnabled(false);

        backButton = new JButton("Back");
        backButton.addActionListener(this);

        p1.add(backButton);
        p1.add(re);
        p1.add(resignButton);
        p1.add(l2);
        p1.add(l3);
        p1.add(bestScoreLabel);
        p1.add(statusLabel);

        add(p1, BorderLayout.NORTH);
        add(p2, BorderLayout.CENTER);
        add(l1, BorderLayout.SOUTH);

        loadImages();
        initializeButtons();

        setVisible(true);
    }

    private void loadImages() {
        try {
            for (int i = 0; i < 9; i++) {
                imageParts[i] = ImageIO.read(getClass().getResource("/Assets/Images/"+level+"/imgrnd" + (i + 1) + ".jpg"));
            }
            emptyImg[0] = ImageIO.read(getClass().getResource("/Assets/Images/question.jpg"));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading images. Please check the file paths.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    private void updateBestScoreLabel() {
        int bestScore = getBestScore();
        if (bestScore != Integer.MAX_VALUE) {
            bestScoreLabel.setText("Best: " + bestScore); // Display the best score if available
        } else {
            bestScoreLabel.setText("Best: "); // Display nothing if no score is found
        }
    }

    private int getBestScore() {
        List<Integer> scores = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(bestScoresFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    scores.add(Integer.parseInt(line.trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid score entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading best scores file: " + e.getMessage());
        }

        return scores.isEmpty() ? Integer.MAX_VALUE : Collections.min(scores);
    }

    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                b[i][j] = new JButton();
                b[i][j].setBorderPainted(true);
                b[i][j].setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                b[i][j].setContentAreaFilled(false);
                b[i][j].addActionListener(this);
                p2.add(b[i][j]);
            }
        }
    }

    private void shufflePuzzle() {
        int[] arr = {0, 1, 2, 3, 4, 5, 6, 7, 8};
        // Generate a solvable permutation
        do {
            for (int i = arr.length - 1; i > 0; i--) {
                int index = (int) (Math.random() * (i + 1));
                int temp = arr[index];
                arr[index] = arr[i];
                arr[i] = temp;
            }
        } while (!isSolvable(arr));

        // Fill the button array and set images
        int k = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                ar[i][j] = arr[k];
                if (arr[k] != 8) {
                    b[i][j].setIcon(new ImageIcon(imageParts[arr[k]]));
                } else {
                    b[i][j].setIcon(new ImageIcon(emptyImg[0]));
                    b[i][j].setBackground(EMPTY_COLOR);
                }
                k++;
            }
        }

        // Set the panel spacing
        p2.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void updateBestScores(int currentMoves) {
        List<Integer> scores = new ArrayList<>();

        // Read existing scores from file
        try (BufferedReader br = new BufferedReader(new FileReader(bestScoresFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    scores.add(Integer.parseInt(line.trim()));
                } catch (NumberFormatException e) {
                    System.err.println("Skipping invalid score entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading best scores file: " + e.getMessage());
        }

        // Add current score and sort scores
        scores.add(currentMoves);
        Collections.sort(scores);

        // Keep only the top 5 scores
        if (scores.size() > 5) {
            scores = scores.subList(0, 5);
        }

        // Write updated scores back to file
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(bestScoresFile))) {
            for (int score : scores) {
                bw.write(String.valueOf(score));
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing to best scores file: " + e.getMessage());
        }
        updateBestScoreLabel();
    }



    private boolean isSolvable(int[] arr) {
        int inversions = 0;
        for (int i = 0; i < arr.length; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[i] > arr[j] && arr[i] != 8 && arr[j] != 8) { // Ignore the empty tile
                    inversions++;
                }
            }
        }
        // The configuration is solvable if the number of inversions is even
        return inversions % 2 == 0;
    }

    public void actionPerformed(ActionEvent ae) {
        if (ae.getSource() == re) {
            startNewGame();
            return;
        }

        if (ae.getSource() == resignButton) {
            resignGame();
            return;
        }

        if (ae.getSource() == backButton) {
            // Close the game window and go back to the home screen
            this.dispose();
            SwingUtilities.invokeLater(() -> new GameHomepage());
            return;
        }

        if (!gameActive) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (ae.getSource() == b[i][j]) {
                    if (isValidMove(i, j)) {
                        movePiece(i, j);
                        l2.setText("Moves: " + moves);
                        moves++;
                        if (checkWin()) {
                            endGame("Won");
                        }
                    }
                    return;
                }
            }
        }
    }

    private void startNewGame() {
        moves = 0;
        l2.setText("Moves: 0");
        l1.setText(" ");
        shufflePuzzle();
        updateBestScoreLabel();
        gameActive = true;
        statusLabel.setText("Status: Active");
        resignButton.setEnabled(true);
    }

    private void resignGame() {
        endGame("Resigned");
        showSolvedPuzzle();
    }

    private void endGame(String status) {
        gameActive = false;
        statusLabel.setText("Status: " + status);
        resignButton.setEnabled(false);

        if (status.equals("Won")) {
            l1.setText("You Win! In " + moves + " Moves");
            updateBestScores(moves); // Update best scores
        }
    }


    private void showSolvedPuzzle() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                b[i][j].setIcon(new ImageIcon(imageParts[i * 3 + j]));
            }
        }
    }

    private boolean isValidMove(int i, int j) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newI = i + dir[0];
            int newJ = j + dir[1];
            if (newI >= 0 && newI < 3 && newJ >= 0 && newJ < 3 && ar[newI][newJ] == 8) {
                return true;
            }
        }
        return false;
    }

    private void movePiece(int i, int j) {
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            int newI = i + dir[0];
            int newJ = j + dir[1];
            if (newI >= 0 && newI < 3 && newJ >= 0 && newJ < 3 && ar[newI][newJ] == 8) {
                b[newI][newJ].setIcon(b[i][j].getIcon());
                b[i][j].setIcon(new ImageIcon(emptyImg[0]));
                b[i][j].setBackground(EMPTY_COLOR);

                int temp = ar[i][j];
                ar[i][j] = ar[newI][newJ];
                ar[newI][newJ] = temp;

                return;
            }
        }
    }

    private boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (ar[i][j] != i * 3 + j) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImagePuzzleGame(args[0]));
    }
}