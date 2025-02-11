import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Integer> currentPages = new ArrayList<>(); //pages currently saved
    private static ArrayList<ArrayList<String>> arrListOfArrLists = new ArrayList<>(); //represents all of memory
    private static ArrayList<String> pageFaults = new ArrayList<>();
    private static ArrayList<String> victimFrames = new ArrayList<>();
    private static int numPhysicalFrames = 0;
    private static int[] refString = null;
    private static boolean isNEWAlgo = false;
    private static boolean exit = false;

    public static void main(String[] args) {
        while(true){
            runMenu();
            if(exit){ //set in menu method
                break;
            }

            for(int i = 0; i < refString.length; i++){
                System.out.println("Press enter to continue, any other key + enter "
                + "to quit.");
                if(!scanner.nextLine().equals("")){
                    break;
                }

                if(currentPages.size() < numPhysicalFrames
                && !currentPages.contains(refString[i])){
                    currentPages.add(refString[i]); //vertical list of pages currently saved

                    //each physical frame (row in table) is represented by own ArrayList
                    ArrayList<String> arrList = new ArrayList<>();
                    if(i != 0){
                        for(int j = 0; j < i; j++){
                            arrList.add(" "); //pad other phys frame arrlists
                        }
                    }
                    arrList.add(Integer.toString(refString[i])); //add the access #
                    arrListOfArrLists.add(arrList); //add phys frame representation (row) to memory
                    victimFrames.add(" "); //align victimFrames because there is no item
                    pageFaults.add("F");

                } else if(currentPages.size() == numPhysicalFrames
                && !currentPages.contains(refString[i])){
                    //run algo to replace page
                    if(isNEWAlgo) {
                        runNEWAlgo(i);
                    } else{
                        runOPTAlgo(i);
                    }
                    pageFaults.add("F");

                } else {
                    //hit
                    victimFrames.add(" ");
                    pageFaults.add(" ");
                }
                normalizeArrayLists(i);
                displayInfo();
            }
            //clear lists used to run algo after a full run-through/loop exit
            currentPages.clear();
            arrListOfArrLists.clear();
            pageFaults.clear();
            victimFrames.clear();
        }
        scanner.close();
    }

    private static void runMenu(){
        while(true){
            printMenu();
            String userChoice = scanner.nextLine();
            if(userChoice.equals("0")){
                exit = true;
                return;
            } else if(userChoice.equals("1")){
                setN();
            } else if(userChoice.equals("2")){
                //we need n (phys frames number) so we can
                //make sure ref string is long enough
                verifyNumPhysicalFrames();
                setRefString();
            } else if(userChoice.equals("3")){
                //make sure we have num physical frames and a valid ref string
                verifyNumPhysicalFrames();
                verifyRefString();
                isNEWAlgo = false;
                return;
            } else if(userChoice.equals("4")){
                verifyNumPhysicalFrames();
                verifyRefString();
                isNEWAlgo = true;
                return;
            } else{
                System.out.println("Invalid input, please select items "
                + "from the list only.");
            }
        }
    }

    private static void printMenu(){
        System.out.println(
            "\n0 - Exit\n" +
            "1 - Input N\n" +
            "2 - Input Reference String\n" +
            "3 - Simulate OPT algorithm\n" +
            "4 - Simulate NEW algorithm\n" +
            "Select Option:"   
        );
    }

    private static void verifyNumPhysicalFrames(){
        if(numPhysicalFrames == 0){
            System.out.print("You need to enter a number of "
            + "physical frames first. ");
            setN();
        }
    }

    private static void verifyRefString(){
        if(refString == null){
            System.out.print("You need to enter a reference string first. ");
            setRefString();
        }
    }

    private static void setRefString(){
        System.out.println("Enter the reference string: ");
        while(true){
            try{
                String[] userRefString = scanner.nextLine().split("");
                if(userRefString.length < numPhysicalFrames || userRefString.length > 20){
                    System.out.println("Invalid length. "
                    + "Enter a string greater than or equal to\n" 
                    + "the number of physical frames and less than 20 characters.");
                    continue;
                }

                int[] intRefString = new int[userRefString.length];
                for(int i = 0; i < userRefString.length; i++){
                    intRefString[i] = Integer.parseInt(userRefString[i]);
                }
                refString = intRefString;
                return;
            } catch(NumberFormatException nfe){
                System.out.println("Invalid data type. Enter only numbers.");
            }
        }
    }

    private static void setN(){
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
        numPhysicalFrames = numFrames;
    }

    private static void runOPTAlgo(int index){
        //create an evictList where page numbers are removed if they
        //are "safe" according to the given algorithm. if evictList
        //length is 1 we have singled oout the victim frame.
        ArrayList<Integer> evictList = new ArrayList<>(currentPages);
        int victimFrame = -1;
        for(int i = index; i < refString.length; i++){
            //"look forward" and remove pages as we see them because they
            //are not candidates for eviction
            if(evictList.size() == 1){
                victimFrame = evictList.get(0);
                break;
            }
            if(evictList.contains(refString[i])){
                evictList.remove(evictList.indexOf(refString[i]));
            }
        }

        if(victimFrame != -1){ //found specific victim frame
            processPageFault(victimFrame, index);
        } else { //doesn't matter, pick first from candidates list
            processPageFault(evictList.get(0), index);
        }
    }

    private static void processPageFault(int victimFrame, int index){
            victimFrames.add(Integer.toString(victimFrame));
            int removalIndex = currentPages.indexOf(victimFrame); //index in current column
            currentPages.remove(removalIndex);
            currentPages.add(removalIndex, refString[index]); //replace w/new page
            //select the phys frame which corresponds to the column index
            //and add the new page to that phys frame ArrayList
            arrListOfArrLists.get(removalIndex).add(Integer.toString(refString[index]));
    }

    private static void runNEWAlgo(int index){
        ArrayList<Integer> evictList = new ArrayList<>(currentPages);
        int victimFrame = -1;
        for(int i = index; i >= 0; i--){ //start at current index and look backward
            if(evictList.contains(refString[i])
            && evictList.size() == 2){
                //second least recently used
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

    private static void displayInfo(){
        printDashedLine();
        printTableFormatRefString();
        printDashedLine();
        for(int i = 0; i < arrListOfArrLists.size(); i++){
            printTableArrList(i); //represents physical frame
        }
        printDashedLine();
        printPageFaults();
        printVictimFrames();
        printDashedLine();
    }

    private static void printDashedLine(){
        for(int i = 0; i < (18 + ((refString.length) * 4)); i++){
            System.out.print("-");
        }
        System.out.println();
    }

    private static void printTableFormatRefString(){
        System.out.print("|Reference String | ");
        for(int number : refString){
            System.out.print(number + " | ");
        }
        System.out.println();
    }

    private static void printTableArrList(int frameNumber){
        System.out.print("|Physical frame " + frameNumber + " | ");
        for(String page : arrListOfArrLists.get(frameNumber)){
            System.out.print(page + " | ");
        }
        System.out.println();
    }

    private static void normalizeArrayLists(int currentIndex){
        //each row should have index+1 items including blank spaces
        //if it does not append the the current last element to the end again
        for(int i = 0; i < arrListOfArrLists.size(); i++){
            if(arrListOfArrLists.get(i).size() < currentIndex+1){
                ArrayList<String> subArrList = arrListOfArrLists.get(i);
                String lastElement = subArrList.get(subArrList.size()-1);
                subArrList.add(lastElement);
            }
        }
    }

    private static void printPageFaults(){
        System.out.print("|    Page Faults  | ");
        for(String faultIndicator : pageFaults){
            System.out.print(faultIndicator + " | ");
        }
        System.out.println();
    }
    
    private static void printVictimFrames(){
        System.out.print("|   Victim Frame  | ");
        for(String frame : victimFrames){
            System.out.print(frame + " | ");
        }
        System.out.println();
    }
}