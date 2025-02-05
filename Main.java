import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Integer> currentPages = new ArrayList<>();
    public static int[] refString = getRefString();
    public static int numPhysicalFrames = getN();
    public static boolean isNEWAlgo = getIsNew();

    public static void main(String[] args) {
        
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
                if(isNEWAlgo) {
                    runNEWAlgo(i);
                } else{
                    runOPTAlgo(i);
                }
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
        System.out.println("final hits / misses: " + hits + " / " + misses);
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

    public static boolean getIsNew(){
        System.out.println("Type 'n' to run the NEW algorithm, hit any other "
        + "key to run the OPT algorithm.");
        String newChoice = scanner.nextLine().toLowerCase();
        System.out.println(newChoice.equals("n"));
        if(newChoice.equals("n")){
            return true;
        } else {
            return false;
        }
    }

    public static void printArrListContents(ArrayList<Integer> arrlist){
        for(Integer item : arrlist){
            System.out.print(item + " ");
        }
        System.out.println();
    }

    public static void printArrayContents(int[] arr){
        for(Integer item : arr){
            System.out.print(item + " ");
        }
    }

    public static void runOPTAlgo(int index){
        ArrayList<Integer> evictList = new ArrayList<>(currentPages);
        int victimFrame = -1;
        for(int i = index; i < refString.length; i++){
            if(evictList.size() == 1){
                victimFrame = evictList.get(0);
                break;
            }
            if(evictList.contains(refString[i])){
                evictList.remove(evictList.indexOf(refString[i]));
            }

        }

        if(victimFrame != -1){
            System.out.println("Should evict " + victimFrame);
            currentPages.remove(currentPages.indexOf(victimFrame));
            currentPages.add(refString[index]);
        } else {
            System.out.println("Dealer's choice, options are");
            printArrListContents(evictList);
            System.out.println("we'll evict " + evictList.get(0));
            currentPages.remove(currentPages.indexOf(evictList.get(0)));
            currentPages.add(refString[index]);
        }
    }

    public static void runNEWAlgo(int index){
        ArrayList<Integer> evictList = new ArrayList<>(currentPages);
        int victimFrame = -1;
        for(int i = index; i >= 0; i--){
            if(evictList.contains(refString[i])
            && evictList.size() == 2){
                victimFrame = refString[i];
                break;
            } else if(evictList.contains(refString[i])){
                evictList.remove(evictList.indexOf(refString[i]));
            }
        }

        if(victimFrame != -1){
            System.out.println("Victim frame is " + victimFrame);
            currentPages.remove(currentPages.indexOf(victimFrame));
            currentPages.add(refString[index]);
        } else{
            System.out.println("doesn't really matter, let's evict "
            + evictList.get(0));
            currentPages.remove(currentPages.indexOf(evictList.get(0)));
            currentPages.add(refString[index]);
        }
    }
}