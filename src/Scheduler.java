import java.io.FileWriter;
import java.io.IOException;
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
            sum += processData.calculateWaitingTime();
        }
        return sum / processes.length;
    }

    public double getAverageTurnAround() {
        double sum = 0;
        for (ProcessData processData : processes) {
            sum += processData.calculateTurnaroundTime();
        }
        return sum / processes.length;
    }

    protected void fixTimeLine() {
        Vector<ProcessInterval> temp = new Vector<>();
        for (ProcessInterval processInterval : timeLine) {
            if (temp.isEmpty() || !temp.lastElement().getName().equals(processInterval.getName())) {
                temp.add(processInterval);
            } else {
                temp.lastElement().setEnd(processInterval.getEnd());
            }
        }
        timeLine = temp;
    }

    public void printDetails(FileWriter fileWriter) {
        try {
            fileWriter.write("Scheduler name : " + name+"\n");
            fileWriter.write("\n");
            for (ProcessInterval processInterval : timeLine) {
                fileWriter.write(processInterval.getName() + " start : " + processInterval.getStart() + " end : " + processInterval.getEnd()+"\n");
            }
            fileWriter.write("\n");

            for (ProcessData processData : processes) {
                fileWriter.write(processData.getName() + " Waiting Time : " + processData.calculateWaitingTime() + " Turnaround Time : " + processData.calculateTurnaroundTime()+"\n");
            }
            fileWriter.write("\n");

            fileWriter.write("Average Waiting Time : " + getAverageWaiting()+"\n");
            fileWriter.write("Average TurnAround Time : " + getAverageTurnAround()+"\n");
            fileWriter.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
