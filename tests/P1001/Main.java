import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        Integer a[] = new Integer[3];
        a[0] = cin.nextInt();
        a[1] = cin.nextInt();
        a[2] = a[0] + a[1];
        System.out.println(a[2]);
    }
}