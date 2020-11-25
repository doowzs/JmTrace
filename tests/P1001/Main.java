import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        int[] arr = new int[3];
        arr[0] = cin.nextInt();
        arr[1] = cin.nextInt();
        arr[2] = arr[0] + arr[1];
        System.out.println(arr[2]);
    }
}