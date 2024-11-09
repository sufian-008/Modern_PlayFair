import java.util.*;

public class ModifiedPlayfairCipher {
    

    private static void printMatrix(String label, char[][] matrix) {
        System.out.println(label + ":");
        for (char[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
    }
}
