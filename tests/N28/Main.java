import java.math.BigInteger;
import java.util.Scanner;

class Matrix {
    static void dump(BigInteger [][]arr, int n) {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < n; ++j) {
                System.out.print(arr[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
    static void swap(BigInteger [][]arr, int i1, int j1, int i2, int j2) {
        BigInteger temp = arr[i1][j1];
        arr[i1][j1] = arr[i2][j2];
        arr[i2][j2] = temp;
    }
    static BigInteger determinant(BigInteger [][]mat, int n) {
        BigInteger zero = new BigInteger("0");
        BigInteger det = new BigInteger("1");
        BigInteger mul = new BigInteger("1");
        for (int cur = 0; cur < n; ++cur) {
            //System.out.println("ROW " + cur);
            //dump(mat, n);
            int idx = cur;
            while (mat[idx][cur].equals(zero) && idx < n) ++idx;
            if (idx == n) {
                System.out.println("Error");
                return new BigInteger("0");
            }
            if (idx != cur) {
                for (int col = 0; col < n; ++col) {
                    swap(mat, idx, col, cur, col);
                }
                if ((idx - cur) % 2 == 1) det = det.negate();
            }
            for (int row = cur + 1; row < n; ++row) {
                BigInteger ccur = mat[cur][cur];
                BigInteger crow = mat[row][cur];
                BigInteger gcd = ccur.gcd(crow);
                ccur = ccur.divide(gcd);
                crow = crow.divide(gcd);
                for (int col = cur; col < n; ++col) {
                    mat[row][col] = (ccur.multiply(mat[row][col])).subtract(crow.multiply(mat[cur][col]));
                }
                mul = mul.multiply(ccur);
            }
        }
        for (int i = 0; i < n; ++i) {
            det = det.multiply(new BigInteger(String.valueOf(mat[i][i])));
        }
        return det.divide(mul);
    }
}

public class Main {
    public static void main(String []args) {
        Scanner cin = new Scanner(System.in);
        int n = cin.nextInt();
        if (n <= 2) {
            System.out.println(n == 2 ? 5 : 1);
        } else {
            BigInteger one = new BigInteger("1");
            BigInteger three = new BigInteger("3");
            BigInteger [][]arr = new BigInteger[n+1][n+1];
            for (int i = 0; i < n; ++i) {
                for (int j = 0; j < n; ++j) {
                    arr[i][j] = new BigInteger("0");
                    if (i == j) {
                        arr[i][j] = arr[i][j].add(three);
                    } else if ((i + 1) % n == j) {
                        arr[i][j] = arr[i][j].subtract(one);
                    } else if ((j + 1) % n == i) {
                        arr[i][j] = arr[i][j].subtract(one);
                    }
                }
            }
            BigInteger ans = Matrix.determinant(arr, n);
            System.out.println(ans);
        }
    }
}