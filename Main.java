import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Integer> currentPages = new ArrayList<>();
    public static ArrayList<ArrayList<Integer>> arrListOfArrLists = new ArrayList<>();
    public static int[] refString = getRefString();
    public static int numPhysicalFrames = getN();
    public static boolean isNEWAlgo = getIsNew();
    public static int longestList = 0;

    public static void main(String[] args) {
        
        int hits = 0;
        int misses = 0;

        for(int i = 0; i < refString.length; i++){
            System.out.println("Press enter to continue, any other key + enter "
            + "to quit.");
            if(!scanner.nextLine().equals("")){
                break;
            }

            if(currentPages.size() < numPhysicalFrames
            && !currentPages.contains(refString[i])){
                currentPages.add(refString[i]);
                ArrayList<Integer> newArrList = new ArrayList<>();
                newArrList.add(refString[i]);
                arrListOfArrLists.add(newArrList);
                misses++;
                // System.out.print("add block ");
                // printArrListContents(currentPages);
                // System.out.println();
            } else if(currentPages.size() == numPhysicalFrames
            && !currentPages.contains(refString[i])){
                if(isNEWAlgo) {
                    runNEWAlgo(i);
                } else{
                    runOPTAlgo(i);
                }
                misses++;
                // System.out.print("algo block ");
                // printArrListContents(currentPages);
                // System.out.println();
            } else {
                hits++;
                // System.out.print("hits block ");
                // System.out.println();
            }
            updateLongestList();
            normalizeLists();
            displayInfo();
        }
        System.out.println("final current pages");
        printArrListContents(currentPages);
        // System.out.println("final hits / misses: " + hits + " / " + misses);
        scanner.close();
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
            int removalIndex = currentPages.indexOf(victimFrame);
            currentPages.remove(removalIndex);
            currentPages.add(removalIndex, refString[index]);
            arrListOfArrLists.get(removalIndex).add(refString[index]);
        } else {
            System.out.println("Dealer's choice, options are");
            printArrListContents(evictList);
            System.out.println("we'll evict " + evictList.get(0));
            int removalIndex = currentPages.indexOf(evictList.get(0));
            currentPages.remove(removalIndex);
            currentPages.add(removalIndex, refString[index]);
            arrListOfArrLists.get(removalIndex).add(refString[index]);
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
            int removalIndex = currentPages.indexOf(victimFrame);
            currentPages.remove(removalIndex);
            currentPages.add(removalIndex, refString[index]);
            arrListOfArrLists.get(removalIndex).add(refString[index]);
        } else{
            System.out.println("doesn't really matter, let's evict "
            + evictList.get(0));
            int removalIndex = currentPages.indexOf(evictList.get(0));
            currentPages.remove(removalIndex);
            currentPages.add(removalIndex, refString[index]);
            arrListOfArrLists.get(removalIndex).add(refString[index]);
        }
    }

    public static void displayInfo(){
        printDashedLine();
        printTableFormatRefString();
        printDashedLine();
        for(int i = 0; i < arrListOfArrLists.size(); i++){
            printTableArrList(i);
        }
    }

    public static void printDashedLine(){
        System.out.println("-----------------------------------------------"
        + "-------------------");
    }

    public static void printTableFormatRefString(){
        System.out.print("Reference String | ");
        for(int number : refString){
            System.out.print(number + " | ");
        }
        System.out.println();
    }

    public static void printTableArrList(int frameNumber){
        System.out.print("Physical frame " + frameNumber + " | ");
        for(Integer page : arrListOfArrLists.get(frameNumber)){
            System.out.print(page + " | ");
        }
        System.out.println();
    }

    public static void updateLongestList(){
        for(ArrayList<Integer> list : arrListOfArrLists){
            if(list.size() > longestList){
                longestList = list.size();
            }
        }
    }
    public static void normalizeLists(){
        for(ArrayList<Integer> list : arrListOfArrLists){
            if(list.size() < longestList){
                list.add(list.get(list.size()-1));
            }
        }
    }
}