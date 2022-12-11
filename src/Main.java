import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.print("Enter the number of processes : ");
        int n = scanner.nextInt();
        ProcessData[] processes = new ProcessData[n];
        System.out.print("Enter the context Switching : ");
        int contextSwitching = scanner.nextInt();
        System.out.print("Enter the quantum for round robin : ");
        int quantum = scanner.nextInt();
        for (int i = 0; i < n; i++) {
            ProcessData processData = new ProcessData();
            System.out.print("Enter process name : ");
            String name = scanner.next();
            processData.setName(name);
            System.out.print("Enter process burst time : ");
            int burst = scanner.nextInt();
            processData.setBurstTime(burst);
            System.out.print("Enter process arrival time : ");
            int arrivalTime = scanner.nextInt();
            processData.setArrivalTime(arrivalTime);
            System.out.print("Enter process priority : ");
            int priority = scanner.nextInt();
            processData.setPriority(priority);
            System.out.print("Enter process quantum for AG : ");
            int quantumAG = scanner.nextInt();
            processData.setQuantum(quantumAG);
            processes[i] = processData;
        }
        Arrays.sort(processes, new ArrivalTimeComparator());
        FileWriter fileWriter ;
        try {
            fileWriter = new FileWriter("output.txt") ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scheduler scheduler = new ShortestRemainingTimeFirstScheduler(processes, contextSwitching, quantum);
        scheduler.run();
        scheduler.printDetails(fileWriter);

        scheduler = new RoundRobinScheduler(processes, contextSwitching, quantum);
        scheduler.run();
        scheduler.printDetails(fileWriter);

        scheduler = new PreemptivePriorityScheduler(processes, contextSwitching, quantum);
        scheduler.run();
        scheduler.printDetails(fileWriter);

        scheduler = new AGScheduler(processes, contextSwitching, quantum);
        scheduler.run();
        scheduler.printDetails(fileWriter);


        try {
            fileWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}

