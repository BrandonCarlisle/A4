import java.util.Arrays;


public class Banker {
    static int numCustomers = 5;
    static int numResources = 4;
    
    int[] Available = new int [numResources];
    int[][] Max = new int[numCustomers][numResources];
    int[][] Allocation = new int[numCustomers][numResources];
    int[][] Need = new int[numCustomers][numResources];
    
    private boolean[] Finish = new boolean[5];
    public int[] SafeStatus = new int[5];
    
    public Banker(int[] available, int[][] max) {
        this.Available = available;
        this.Max = max;
        AllocationInit();
        this.Need = CalcNeed();
        Arrays.fill(this.Finish, Boolean.FALSE);
        Arrays.fill(this.SafeStatus, 0);
    }
    
    private void ResetFinish() {
        Arrays.fill(this.Finish, Boolean.FALSE);
        Arrays.fill(this.SafeStatus, 0);
    }
    
    private void SafeNewCheck() {
        for (int x = 0; x < 5; x++) {
            if (SafeStatus[x] == -1) {
                SafeStatus[x] = 0;
            }
        }
    }
    
    private boolean CheckSafeDone() {
        boolean notDone = false;
        for (int x = 0; x < 5; x++) {
            if (this.SafeStatus[x] == 0) {
                notDone = true;
            }
        }
        return notDone;
    }
    
    private boolean IsSafeState() {
        boolean safe = true;
        for (int x = 0; x < 5; x++) {
            if (this.SafeStatus[x] == -1) {
                safe = false;
            }
        }
        return safe;
    }
    
    private boolean SafeCheck() {
        boolean runCheck = true;
        int[] work = this.Available;
        this.ResetFinish();
        
        while (runCheck) {
            for (int x = 0; x < 5; x++) {
                if (!this.Finish[x] && SafeStatus[x] == 0) {
                    boolean needCheck = true;
                    for (int ni = 0; ni < 4; ni++) {
                        int needi = this.Need[x][ni];
                        int worki = work[ni];
                        if (needi > worki) {
                            needCheck = false;
                            SafeStatus[x] = -1;
                            break;
                        }
                    }
                    if (needCheck) {
                        this.Finish[x] = true;
                        SafeStatus[x] = 1;
                        SafeNewCheck();
                        for (int y = 0; y < 4; y++) {
                            work[y] = work[y] + this.Allocation[x][y];
                        }
                    }                     
                }
            }
            runCheck = CheckSafeDone();
        }               
        return IsSafeState();
    }
    

    private boolean CheckResourceRequest(int cID, int[] request) {
        int[][] need = CalcNeed();
        
        for (int x = 0; x < 4; x++) {
            if (request[x] > need[cID][x]) {
                return false;
            }
            if (request[x] > this.Available[x]) {
                return false;
            }
        }
        return true;
    }
    
    
    
    public int RequestResources(int cID, int[] request) {
        boolean check = CheckResourceRequest(cID, request);
        
        if (check) {
            for (int x = 0; x < 4; x++) {
                this.Available[x] = this.Available[x] - request[x];
                this.Allocation[cID][x] = this.Allocation[cID][x] + request[x];
                this.Need[cID][x] = this.Need[cID][x] - request[x];
            }
        }
        else {
            return -1;
        }
        
        if (!SafeCheck()) {
            ReleaseResources(cID, request);
            return -1;
        }
        else {
            return 0;
        }
    }
    
    public void ReleaseResources(int cID , int[] request) {
        for (int x = 0; x < 4; x++) {
            if (this.Allocation[cID][x] >= request[x]) {
                this.Available[x] = this.Available[x] + request[x];
                this.Allocation[cID][x] = this.Allocation[cID][x] - request[x];
                this.Need[cID][x] = this.Need[cID][x] + request[x];
            }
            else {
                System.out.println("Error Release larger than allocated ");
            }
        }
    }
   
    
    private void AllocationInit() {
        int initArr[][] = {
          {0,0,0,0},
          {0,0,0,0},
          {0,0,0,0},
          {0,0,0,0},
          {0,0,0,0}
        };
        this.Allocation = initArr;
    }
    
    
    private int[][] CalcNeed() {
        int needTrack[][] = {
          {0,0,0,0},
          {0,0,0,0},
          {0,0,0,0},
          {0,0,0,0},
          {0,0,0,0}
        };
        
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 4; y++) {
                needTrack[x][y] = this.Max[x][y] - this.Allocation[x][y];
            }
        }
        return needTrack;
    }
    
    private void PrintAvailable() {
        System.out.println("Available:");
        for (int x = 0; x < 4; x++) {
            System.out.print(this.Available[x] + "\t");
        }
        System.out.print("\n"); 
    }    
    
    private void PrintMax() {
        System.out.println("Maximum:"); 
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 4; y++) {
               System.out.print(this.Max[x][y] + "\t"); 
            }
            System.out.print("\n"); 
        }
    }
    
    private void PrintAllocation() {
        System.out.println("Allocation:"); 
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 4; y++) {
               System.out.print(this.Allocation[x][y] + "\t"); 
            }
            System.out.print("\n"); 
        }
    }
    
    private void PrintNeed() {
        System.out.println("Need:"); 
        for (int x = 0; x < 5; x++) {
            for (int y = 0; y < 4; y++) {
               System.out.print(this.Need[x][y] + "\t"); 
            }
            System.out.print("\n"); 
        }
    }
    

    public void PrintDetails() {
        PrintAvailable();
        PrintMax();
        PrintAllocation();
        PrintNeed();
    }
}