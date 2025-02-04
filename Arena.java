import java.util.ArrayList;

public class Arena {
    public static void main(String[] args) {
        ArrayList<Integer> ints = new ArrayList<>();
        ints.add(12);
        ints.add(14);
        ints.add(18);

        ArrayList<Integer> copy = new ArrayList<>(ints);

        copy.remove(copy.indexOf(12));
        copy.remove(copy.indexOf(18));
        System.out.println(copy.get(0));
    }
}
