import java.util.*;

public class ModifiedPlayfairCipher {
    private static final int SIZE = 8; // 5x5 matrix (since there are 25 letters)
    private static final String CHAR_POOL = "ABCDEFGHIKLMNOPQRSTUVWXYZabcdefghiklmnopqrstuvwxyz0123456789!@#$%&*?^+-*~`'.,/"; // Extended characters set
    private static final char PADDING_CHAR = 'X'; // Padding character for spaces

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Playfair Cipher Program");
        System.out.println("1. Encrypt as Decrypt");
        System.out.println("2. Decrypt as Encrypt");
        System.out.print("Select an option (1 or 2): ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter a keyword (SECURITY): ");
        String keyword = scanner.nextLine().toUpperCase();

        System.out.print("Enter the text message: ");
        String text = scanner.nextLine().toUpperCase();

        System.out.print("Enter permutation key (e.g., 4,1,3,0,2): ");
        String[] permKeyInput = scanner.nextLine().split(",");
        int[] permKey = Arrays.stream(permKeyInput).mapToInt(Integer::parseInt).toArray();

        // Ensure that the permutation key size matches the matrix size
        if (permKey.length != SIZE) {
            System.out.println("Error: Permutation key size must match the matrix size (" + SIZE + ").");
            return;
        }
        // Generate the key matrix and ensure it has enough characters
        char[][] keyMatrix = createKeyMatrix(SIZE, keyword);
        if (keyMatrix.length == 0) {
            System.out.println("Not enough characters to fill the matrix. Exiting.");
            return;
        }
        printMatrix("Key Matrix", keyMatrix);

        char[][] permutedMatrix = permuteRows(keyMatrix, permKey);
        printMatrix("Permuted Matrix", permutedMatrix);

        char[][] transposedMatrix = transposeMatrix(permutedMatrix);
        printMatrix("Transposed Matrix", transposedMatrix);

        List<String> digraphs = createDigraphs(text);
        System.out.println("Digraphs: " + digraphs);

        String resultText = "";
        if (choice == 1) {
            resultText = decrypt(digraphs, transposedMatrix); // Encrypt using decrypt logic
            System.out.println("Encrypted Text (using decrypt method X is space): " + resultText);
        } else if (choice == 2) {
            resultText = encrypt(digraphs, transposedMatrix); // Decrypt using encrypt logic
            System.out.println("Decrypted Text (using encrypt method X is Space ): " + resultText);
        } else {
            System.out.println("Invalid selection. Please restart the program.");
        }

        scanner.close();
    }

    private static char[][] createKeyMatrix(int size, String keyword) {
        Set<Character> usedChars = new LinkedHashSet<>();

        // First, add characters from the keyword to the set
        for (char c : keyword.toCharArray()) {
            usedChars.add(c);
        }

        // Then, add characters from CHAR_POOL until the matrix is filled
        for (char c : CHAR_POOL.toCharArray()) {
            usedChars.add(c);
            if (usedChars.size() == size * size) break; // Stop when we reach the required number of elements
        }

        // If we don't have enough characters, print an error and return an empty matrix
        if (usedChars.size() < size * size) {
            System.out.println("Error: Not enough unique characters to fill the key matrix.");
            return new char[0][0]; // Return an empty matrix as a fallback
        }

        // Fill the matrix with the characters
        char[][] keyMatrix = new char[size][size];
        Iterator<Character> iter = usedChars.iterator();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                keyMatrix[i][j] = iter.next();
            }
        }
        return keyMatrix;
    }
    private static char[][] permuteRows(char[][] matrix, int[] permKey) {
        char[][] permutedMatrix = new char[matrix.length][matrix[0].length];
        for (int i = 0; i < permKey.length; i++) {
            permutedMatrix[i] = matrix[permKey[i]];
        }
        return permutedMatrix;
    }
    private static char[][] transposeMatrix(char[][] matrix) {
        char[][] transposedMatrix = new char[matrix.length][matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                transposedMatrix[j][i] = matrix[i][j];
            }
        }
        return transposedMatrix;
    }

     private static List<String> createDigraphs(String text) {
        List<String> digraphs = new ArrayList<>();
        text = text.replace(" ", String.valueOf(PADDING_CHAR)); // Replace spaces with 'X'
        for (int i = 0; i < text.length(); i += 2) {
            char first = text.charAt(i);
            char second = (i + 1 < text.length() && text.charAt(i + 1) != first) ? text.charAt(i + 1) : PADDING_CHAR;
            digraphs.add("" + first + second);
        }
        return digraphs;
    }

private static String decrypt(List<String> digraphs, char[][] matrix) {
        StringBuilder ciphertext = new StringBuilder();
        for (String digraph : digraphs) {
            int[] pos1 = findPosition(matrix, digraph.charAt(0));
            int[] pos2 = findPosition(matrix, digraph.charAt(1));

            if (pos1[0] == pos2[0]) {
                ciphertext.append(matrix[pos1[0]][(pos1[1] + 1) % SIZE]);
                ciphertext.append(matrix[pos2[0]][(pos2[1] + 1) % SIZE]);
            } else if (pos1[1] == pos2[1]) {
                ciphertext.append(matrix[(pos1[0] + 1) % SIZE][pos1[1]]);
                ciphertext.append(matrix[(pos2[0] + 1) % SIZE][pos2[1]]);
            } else {
                ciphertext.append(matrix[pos1[0]][pos2[1]]);
                ciphertext.append(matrix[pos2[0]][pos1[1]]);
            }
        }
        return ciphertext.toString();
    }

     private static String encrypt(List<String> digraphs, char[][] matrix) {
        StringBuilder plaintext = new StringBuilder();
        for (String digraph : digraphs) {
            int[] pos1 = findPosition(matrix, digraph.charAt(0));
            int[] pos2 = findPosition(matrix, digraph.charAt(1));

            if (pos1[0] == pos2[0]) {
                plaintext.append(matrix[pos1[0]][(pos1[1] + SIZE - 1) % SIZE]);
                plaintext.append(matrix[pos2[0]][(pos2[1] + SIZE - 1) % SIZE]);
            } else if (pos1[1] == pos2[1]) {
                plaintext.append(matrix[(pos1[0] + SIZE - 1) % SIZE][pos1[1]]);
                plaintext.append(matrix[(pos2[0] + SIZE - 1) % SIZE][pos2[1]]);
            } else {
                plaintext.append(matrix[pos1[0]][pos2[1]]);
                plaintext.append(matrix[pos2[0]][pos1[1]]);
            }
        }
        return plaintext.toString();
    }

    private static int[] findPosition(char[][] matrix, char c) {
        // Handle padding character (X) in case it's used to replace spaces
        if (c == ' ') c = PADDING_CHAR; // Replace space with 'X' if needed

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == c) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalArgumentException("Character not found in matrix: " + c);
    }

    private static void printMatrix(String label, char[][] matrix) {
        System.out.println(label + ":");
        for (char[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }
}
