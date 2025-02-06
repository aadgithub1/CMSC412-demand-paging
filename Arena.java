import java.util.ArrayList;
import java.util.Scanner;

public class Arena {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        sc.close();

        ArrayList<Integer> arrL = new ArrayList<>(5);
        System.out.println(arrL.size());
        for(Integer thing : arrL){
            System.out.print(thing + " ");
        }
    }
}
