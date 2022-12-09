public class ShortestTImeFirst extends Scheduler {

    ShortestTImeFirst(ProcessData[] processes, int contextSwitching, int quantum) {
        super(processes, contextSwitching, quantum);
        super.setName("Preemptive Shortest Job First");
    }

    @Override
    void run() {
        int timer = 0, n = processes.length , finish = 0;
        ProcessInterval Interval = new ProcessInterval("start" , timer) ;
        while (finish < n) {
            int min = Integer.MAX_VALUE;
            boolean flag = false;
            ProcessData currentProcess = null;
            for (ProcessData p : processes) {
                if (p.remainingTime != 0 && p.getArrivalTime() <= timer && p.getBurstTime() < min) {
                    min = p.getBurstTime();
                    currentProcess = p;
                }
            }
            timer++;
            if (currentProcess != null) {
                currentProcess.decrement(1);
                flag = true ;
            }
            if (currentProcess.getRemainingTime() == 0) {
                finish++ ;
                flag = true;
            }
            if(flag==true){
                currentProcess.setEndTime(timer+contextSwitching);
                currentProcess.setTurnaroundTime(currentProcess.getEndTime()-currentProcess.getArrivalTime());
                currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                Interval = new ProcessInterval(currentProcess.getName(), timer) ;
            }
            timeLine.add(Interval) ;
        }
    }
}
