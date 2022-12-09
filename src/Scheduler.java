import java.util.Vector;

public abstract class Scheduler {

    ProcessData[] processes;
    int contextSwitching;
    int quantum;
    Vector<ProcessInterval> timeLine;

    String name;

    Scheduler(ProcessData[] processes, int contextSwitching, int quantum) {
        this.processes = processes;
        this.contextSwitching = contextSwitching;
        this.quantum = quantum;
        timeLine = new Vector<>();
        for (ProcessData processData : processes) {
            processData.setRemainingTime(processData.getBurstTime());
        }
    }

    protected void setName(String name) {
        this.name = name;
    }

    abstract void run();

    public double getAverageWaiting() {
        double sum = 0;
        for (ProcessData processData : processes) {
            sum += processData.getWaitingTime();
        }
        return sum / processes.length;
    }

    public double getAverageTurnAround() {
        double sum = 0;
        for (ProcessData processData : processes) {
            sum += processData.getTurnaroundTime();
        }
        return sum / processes.length;
    }


    public void printDetails() {
        System.out.println("Scheduler name : " + name);
        for (ProcessInterval processInterval : timeLine) {
            System.out.println(processInterval.getName() + " start : " + processInterval.getStart() + " end : " + processInterval.getEnd());
        }
        for (ProcessData processData : processes) {
            System.out.println(processData.getName() + " Waiting Time : " + processData.getWaitingTime() + " Turnaround Time : " + processData.getTurnaroundTime());
        }

        System.out.println("Average Waiting Time : " + getAverageWaiting());
        System.out.println("Average TurnAround Time : " + getAverageTurnAround());
        System.out.println();

    }
}
