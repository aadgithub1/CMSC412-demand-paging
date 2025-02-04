import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        int[] refString = getRefString();

        for(int item : refString){
            System.out.println(item);
        }
    }

    public static int[] getRefString(){
        System.out.println("Enter the reference string: ");
        String[] userRefString = scanner.nextLine().split("");
        int[] intUserRefString = new int[userRefString.length];
        try{
            for(int i = 0; i < intUserRefString.length; i++){
                intUserRefString[i] = Integer.parseInt(userRefString[i]);
            }
        } catch(NumberFormatException nfe){
            System.err.println("Invalid data type.");
        }
        return intUserRefString;
    }
}