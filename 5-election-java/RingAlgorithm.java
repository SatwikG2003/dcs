import java.util.*;

class RingProcess {
    int id;
    boolean active;
    RingProcess(int id) {
        this.id = id;
        this.active = true;
    }
}

public class RingAlgorithm {
    static RingProcess[] processes;
    static int coordinator;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter number of processes: ");
        int n = sc.nextInt();

        processes = new RingProcess[n];
        for (int i = 0; i < n; i++) {
            processes[i] = new RingProcess(i + 1);
        }

        coordinator = n;
        System.out.println("Process " + coordinator + " is the coordinator.");

        while (true) {
            System.out.println("\n1. Crash process");
            System.out.println("2. Start election");
            System.out.println("3. Exit");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1:
                    System.out.print("Enter process to crash: ");
                    int crash = sc.nextInt();
                    processes[crash - 1].active = false;
                    if (crash == coordinator)
                        System.out.println("Coordinator crashed!");
                    break;

                case 2:
                    System.out.print("Enter process initiating election: ");
                    int initiator = sc.nextInt();
                    startElection(initiator, n);
                    break;

                case 3:
                    System.out.println("Exiting...");
                    System.exit(0);
            }
        }
    }

    static void startElection(int initiator, int n) {
        List<Integer> activeIds = new ArrayList<>();
        int i = initiator - 1;
        System.out.println("Election initiated by Process " + initiator);

        do {
            if (processes[i].active)
                activeIds.add(processes[i].id);
            i = (i + 1) % n;
        } while (i != initiator - 1);

        int newCoordinator = Collections.max(activeIds);
        coordinator = newCoordinator;
        System.out.println("Process " + coordinator + " is elected as new coordinator.");
    }
}
