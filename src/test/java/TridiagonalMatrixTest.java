import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import ru.vsu.cs.cg24.g82.dementiev_a.cubic_splines.cubicsplines.TridiagonalMatrix;

public class TridiagonalMatrixTest {
    @Test
    public void test() {
        double[][] matrix = new double[][] {
                new double[] {1, 2, 0, 0, 7},
                new double[] {3, 4, 6, 0, 27},
                new double[] {0, 8, 7, 5, 58},
                new double[] {0, 0, 4, 4, 24}
        };
        TridiagonalMatrix.MatrixRow[] rows = new TridiagonalMatrix.MatrixRow[4];
        for (int i = 0; i < 4; i++) {
            double a = matrix[i][i];
            double b = matrix[i][(i + 1) % 4];
            double c = matrix[i][(i + 3) % 4];
            double d = matrix[i][4];
            rows[i] = new TridiagonalMatrix.MatrixRow(a, b, c, d);
        }
        double[] r = TridiagonalMatrix.solve(rows);
        double s;
        for (int i = 0; i < 4; i++) {
            s = 0;
            for (int j = 0; j < 4; j++) {
                s += matrix[i][j] * r[j];
            }
            Assertions.assertEquals(matrix[i][4], s);
        }
    }

    @Test
    public void testEmptyMatrix() {
        Assertions.assertEquals(0, TridiagonalMatrix.solve(new TridiagonalMatrix.MatrixRow[0]).length);
    }
}
