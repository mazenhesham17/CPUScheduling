public class ProcessData {
    String name;
    int arrivalTime;
    int burstTime;
    int priority;
    int quantum;
    int remainingTime;
    int endTime;


    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public int calculateTurnaroundTime() {
        return endTime - arrivalTime;
    }

    public int calculateWaitingTime() {
        return calculateTurnaroundTime() - burstTime;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(int arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public void increment(int value) {
        quantum += value;
    }

    public void clear() {
        quantum = 0;
    }

    public void decrement(int value) {
        remainingTime -= value;
    }

}
