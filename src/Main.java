
import java.util.Scanner;
import java.io.*; 


public class Main{
    
	public static void PrintDetails(Banker bank) {
		bank.PrintDetails();
	}
	
	public static void AlocResources(Banker bank, int cID, int[] reqArr) {
        int check = bank.RequestResources(0, reqArr);
        if (check == -1) {
            System.out.println("Resource Allocation Failed");
        }
        else {
            System.out.println("Resources Allocated");
        }
	}
	
	public static void RelResources(Banker bank, int cID, int[] reqArr) {
		bank.ReleaseResources(0, reqArr);
		System.out.println("Resources Released");
	}
	
	public static boolean UserCommand(Banker bank) {
		Boolean keeprunning = true;
        Scanner reader = new Scanner(System.in);
              
        while (keeprunning) {
        	System.out.println("Enter Command: ");
            String com = reader.nextLine(); 
            
            switch (com.split(" ")[0]) {
            case "*":
            	PrintDetails(bank);
            	break;
            case "exit":
            	System.out.println("Exited");
            	keeprunning = false;
            	break;
            case "RQ":
            	String[] req = com.split(" ");
            	if (req.length != 6) {
            		System.out.println("Error: Invalid Argument Size");
            	}
            	else {
            		int cID = Integer.parseInt(req[1]);
            		int reqArr[] = {Integer.parseInt(req[2]),Integer.parseInt(req[3]),Integer.parseInt(req[4]),Integer.parseInt(req[5])};
            		AlocResources(bank, cID, reqArr);
            	}
            	break;
            case "RL":
            	String[] rel = com.split(" ");
            	if (rel.length != 6) {
            		System.out.println("Error: Invalid Argument Size");
            	}
            	else {
            		int cID = Integer.parseInt(rel[1]);
            		int relArr[] = {Integer.parseInt(rel[2]),Integer.parseInt(rel[3]),Integer.parseInt(rel[4]),Integer.parseInt(rel[5])};
            		RelResources(bank, cID, relArr);
            	}
            	break;
            default:
            	System.out.println("Invalid Command");
            	break;
            }
        }
                
        reader.close();   
        return keeprunning;
	}
	
    public static void main(String []args){
        int availableInit[] = {0,0,0,0};
        if (args.length == 4) {
            for(int i = 0; i < args.length; i++) {
                availableInit[i] = Integer.parseInt(args[i]);
            }
        }
        else {
            availableInit[0] = 10;
            availableInit[1] = 7;
            availableInit[2] = 7;
            availableInit[3] = 8;
        }
        
        String fileName = "src/maxInit.txt";
        String line = null;
        int maxInit[][] = null;
        try {
            int maxRead[][] = {
                    {0,0,0,0},
                    {0,0,0,0},
                    {0,0,0,0},
                    {0,0,0,0},
                    {0,0,0,0}
           };
            FileReader fileReader = 
                new FileReader(fileName);

            BufferedReader bufferedReader = 
                new BufferedReader(fileReader);
            
            int lineCount = 0;
            while((line = bufferedReader.readLine()) != null) {
            	if (lineCount == 5)
            		break;
            	
            	String[] maxResources = line.split(",");
            	if (maxResources.length != 4) {
            		System.out.println("maxInit.txt Improper Formatting");
            		break;
            	}
            	
            	int maxLine[] = {Integer.parseInt(maxResources[0]),Integer.parseInt(maxResources[1]),Integer.parseInt(maxResources[2]),Integer.parseInt(maxResources[3])};
            	maxRead[lineCount] = maxLine;
            	lineCount++;
            }   
            bufferedReader.close();    
            maxInit = maxRead;
        }
        catch(FileNotFoundException ex) {
            System.out.println(
                "Unable to open file '" + 
                fileName + "'");
            
            int maxDefault[][] = {
                    {6,4,7,3},
                    {4,2,3,2},
                    {2,5,3,3},
                    {6,3,3,2},
                    {5,6,7,5}
           };
           maxInit = maxDefault; 
        }
        catch(IOException ex) {
            System.out.println(
                "Error reading file '" 
                + fileName + "'");                  
        }
        
                 
        Banker bank = new Banker(availableInit, maxInit);      
        UserCommand(bank);
     }
}

