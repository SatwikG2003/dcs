import java.util.*;

class Process {
    int id;
    boolean active;
    Process(int id) {
        this.id = id;
        this.active = true;
    }
}

public class BullyAlgorithm {
    static Process[] processes;
    static int coordinator;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        processes = new Process[n];
        for (int i = 0; i < n; i++) {
            processes[i] = new Process(i + 1);
        }

        coordinator = n;
        System.out.println("Process " + coordinator + " is the coordinator.");

        while (true) {
            System.out.println("\n1. Crash process");
            System.out.println("2. Recover process");
            System.out.println("3. Start election");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter process to crash: ");
                    int crash = sc.nextInt();
                    processes[crash - 1].active = false;
                    if (crash == coordinator) System.out.println("Coordinator crashed!");
                    break;

                case 2:
                    System.out.print("Enter process to recover: ");
                    int recover = sc.nextInt();
                    processes[recover - 1].active = true;
                    System.out.println("Process " + recover + " recovered.");
                    startElection(recover);
                    break;

                case 3:
                    System.out.print("Enter process initiating election: ");
                    int initiator = sc.nextInt();
                    startElection(initiator);
                    break;

                case 4:
                    System.out.println("Exiting...");
                    System.exit(0);
            }
        }
    }

    static void startElection(int initiator) {
        System.out.println("Election initiated by Process " + initiator);
        int newCoordinator = -1;

        for (int i = initiator; i < processes.length; i++) {
            if (processes[i].active) {
                System.out.println("Process " + (i + 1) + " responded OK");
                newCoordinator = i + 1;
            }
        }

        if (newCoordinator == -1)
            newCoordinator = initiator;

        coordinator = newCoordinator;
        System.out.println("Process " + coordinator + " becomes the new coordinator.");
    }
}
