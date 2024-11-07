import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImagePuzzleGame extends JFrame implements ActionListener {
    JPanel p1;
    JPanel p2;
    JButton re;
    JButton resignButton;
    JButton[][] b = new JButton[3][3];
    int[][] ar = new int[3][3];
    JLabel l1;
    JLabel l2;
    JLabel l3;
    JLabel statusLabel;
    int moves;
    BufferedImage[] imageParts = new BufferedImage[9];
    BufferedImage[] emptyImg = new BufferedImage[1];
    final Color EMPTY_COLOR = new Color(200, 200, 200);
    boolean gameActive = false;

    ImagePuzzleGame() {
        super("9 Box Image Puzzle");
        moves = 0;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JOptionPane.showMessageDialog(this, "ABOUT\n\nThe 9-box image puzzle consists of a 3x3 grid of image parts with one part missing. The parts are jumbled when the puzzle starts and the goal is to reconstruct the original image by sliding the parts.", "9 Box Image Puzzle", JOptionPane.INFORMATION_MESSAGE);

        String name = JOptionPane.showInputDialog(this, "RULES\nTo move: If there is an empty adjacent square next to a tile, a tile may be slid into the empty location.\nTo win: The image parts must be moved back into their original positions.\n\nEnter your name:", "9 Box Image Puzzle", JOptionPane.QUESTION_MESSAGE);

        setSize(600, 700);
        setLayout(new BorderLayout());

        p1 = new JPanel(new FlowLayout());
        p2 = new JPanel(new GridLayout(3, 3, 2, 2)); // Remove grid gaps
        p2.setBackground(Color.BLACK);

        l1 = new JLabel("    ");
        l2 = new JLabel("Moves: 0");
        l3 = new JLabel("Username: " + name);
        statusLabel = new JLabel("Status: Not Started");

        re = new JButton("New Game");
        re.addActionListener(this);

        resignButton = new JButton("Resign");
        resignButton.addActionListener(this);
        resignButton.setEnabled(false);

        p1.add(re);
        p1.add(resignButton);
        p1.add(l2);
        p1.add(l3);
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
                imageParts[i] = ImageIO.read(getClass().getResource("/Assets/Images/Hard/imgrnd" + (i + 1) + ".jpg"));
            }
            emptyImg[0] = ImageIO.read(getClass().getResource("/Assets/Images/question.jpg"));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading images. Please check the file paths.", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }
    

    private void initializeButtons() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                b[i][j] = new JButton();
                b[i][j].setBorderPainted(false);
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
        SwingUtilities.invokeLater(() -> new ImagePuzzleGame());
    }
}