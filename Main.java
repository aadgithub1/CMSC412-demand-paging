import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        int[] refString = getRefString();
        int numPhysicalFrames = getN();
        ArrayList<Integer> currentPages = new ArrayList<>();

        int hits = 0;
        int misses = 0;

        for(int i = 0; i < refString.length; i++){
            if(currentPages.size() < numPhysicalFrames
            && !currentPages.contains(refString[i])){
                currentPages.add(refString[i]);
                misses++;
                System.out.print("add block ");
                printArrListContents(currentPages);
                System.out.println();
            } else if(currentPages.size() == numPhysicalFrames
            && !currentPages.contains(refString[i])){
                // runOPTAlgo();
                misses++;
                System.out.print("algo block ");
                printArrListContents(currentPages);
                System.out.println();
            } else {
                hits++;
                System.out.print("hits block ");
                System.out.println();
            }
        }
        System.out.println("final current pages");
        printArrListContents(currentPages);
        System.out.println("final misses / hits" + misses + "/" + hits);
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

    public static int getN(){
        System.out.println("Enter the number of physical frames.");
        try{
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException nfe){
            System.out.println("Invalid date type.");
        }
        return -1;
    }

    public static void printArrListContents(ArrayList<Integer> arrlist){
        for(Integer item : arrlist){
            System.out.print(item + " ");
        }
    }

    public static void printArrayContents(int[] arr){
        for(Integer item : arr){
            System.out.print(item + " ");
        }
    }
}