import java.util.ArrayList;

public class Arena {
    public static void main(String[] args) {
        ArrayList<Integer> ints = new ArrayList<>();
        ints.add(12);

        System.out.println(ints.size());
        for(int thing : ints){
            System.out.println(thing == 0);
        }
    }
}
