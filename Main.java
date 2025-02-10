import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Integer> currentPages = new ArrayList<>();
    public static ArrayList<ArrayList<String>> arrListOfArrLists = new ArrayList<>();
    public static ArrayList<String> pageFaults = new ArrayList<>();
    public static ArrayList<String> victimFrames = new ArrayList<>();
    public static int numPhysicalFrames = getN();
    public static int[] refString = getRefString();
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
                ArrayList<String> arrList = new ArrayList<>();
                if(i != 0){
                    for(int j = 0; j < i; j++){
                        arrList.add(" "); //pad other phys frame arrlists
                    }
                }
                arrList.add(Integer.toString(refString[i])); //add the access #
                arrListOfArrLists.add(arrList);
                victimFrames.add(" ");
                pageFaults.add("F");

                misses++;
            } else if(currentPages.size() == numPhysicalFrames
            && !currentPages.contains(refString[i])){
                if(isNEWAlgo) {
                    runNEWAlgo(i);
                } else{
                    runOPTAlgo(i);
                }
                misses++;
                pageFaults.add("F");

            } else {
                hits++;
                victimFrames.add(" ");
                pageFaults.add(" ");
            }
            normalizeArrayLists(i);
            displayInfo();
        }
        scanner.close();
    }

    public static int[] getRefString(){
        System.out.println("Enter the reference string: ");
        while(true){
            try{
                String[] refString = scanner.nextLine().split("");
                if(refString.length < numPhysicalFrames || refString.length > 20){
                    System.out.println("Invalid length. "
                    + "Enter a string greater than or equal to\n" 
                    + "the number of physical frames and less than 20 characters.");
                    continue;
                }

                int[] intRefString = new int[refString.length];
                for(int i = 0; i < refString.length; i++){
                    intRefString[i] = Integer.parseInt(refString[i]);
                }
                return intRefString;
            } catch(NumberFormatException nfe){
                System.out.println("Invalid data type. Enter only numbers.");
            }
        }
    }

    public static int getN(){
        int numFrames = 0;
        System.out.println("Enter the number of physical frames.");
        while(numFrames < 2 || numFrames > 8) {
            try{
                numFrames = Integer.parseInt(scanner.nextLine());
                if(numFrames < 2 || numFrames > 8){
                    System.out.println("Error: Out of range, enter numbers 2-8.");
                }
            } catch(NumberFormatException nfe){
                System.out.println("Invalid data type; enter numbers 2-8.");
            }
        }
        return numFrames;
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
            processPageFault(victimFrame, index);
        } else {
            processPageFault(evictList.get(0), index);
        }
    }

    public static void processPageFault(int victimFrame, int index){
            victimFrames.add(Integer.toString(victimFrame));
            int removalIndex = currentPages.indexOf(victimFrame);
            currentPages.remove(removalIndex);
            currentPages.add(removalIndex, refString[index]);
            arrListOfArrLists.get(removalIndex).add(Integer.toString(refString[index]));
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
            processPageFault(victimFrame, index);
        } else{
            processPageFault(evictList.get(0), index);
        }
    }

    public static void displayInfo(){
        printDashedLine();
        printTableFormatRefString();
        printDashedLine();
        for(int i = 0; i < arrListOfArrLists.size(); i++){
            printTableArrList(i);
        }
        printDashedLine();
        printPageFaults();
        printVictimFrames();
        printDashedLine();
    }

    public static void printDashedLine(){
        for(int i = 0; i < (18 + ((refString.length) * 4)); i++){
            System.out.print("-");
        }
        System.out.println();
    }

    public static void printTableFormatRefString(){
        System.out.print("|Reference String | ");
        for(int number : refString){
            System.out.print(number + " | ");
        }
        System.out.println();
    }

    public static void printTableArrList(int frameNumber){
        System.out.print("|Physical frame " + frameNumber + " | ");
        for(String page : arrListOfArrLists.get(frameNumber)){
            System.out.print(page + " | ");
        }
        System.out.println();
    }

    public static void normalizeArrayLists(int currentIndex){
        //Including padding (" ") all arrayLists which represent
        //physical frames should have i + 1 items at all times in the
        //context of the main loop, which iterates over the reference string.
        //If that is not the case, copy the number at the end of the arraylist
        //and add it again to give the effect of the table columns growing.

        for(int i = 0; i < arrListOfArrLists.size(); i++){
            if(arrListOfArrLists.get(i).size() < currentIndex+1){
                ArrayList<String> subArrList = arrListOfArrLists.get(i);
                String lastElement = subArrList.get(subArrList.size()-1);
                subArrList.add(lastElement);
            }
        }
    }

    public static void printPageFaults(){
        System.out.print("|    Page Faults  | ");
        for(String faultIndicator : pageFaults){
            System.out.print(faultIndicator + " | ");
        }
        System.out.println();
    }
    
    public static void printVictimFrames(){
        System.out.print("|   Victim Frame  | ");
        for(String frame : victimFrames){
            System.out.print(frame + " | ");
        }
        System.out.println();
    }
}