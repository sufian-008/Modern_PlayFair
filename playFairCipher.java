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
    

    private static void printMatrix(String label, char[][] matrix) {
        System.out.println(label + ":");
        for (char[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }
}
