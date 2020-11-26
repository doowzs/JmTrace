import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner cin = new Scanner(System.in);
        short a[] = new short[3];
        a[0] = cin.nextShort();
        a[1] = cin.nextShort();
        a[2] = (short)(a[0] + a[1]);
        System.out.println(a[2]);
    }
}