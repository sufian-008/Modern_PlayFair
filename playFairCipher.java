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
    

    private static void printMatrix(String label, char[][] matrix) {
        System.out.println(label + ":");
        for (char[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }
}
