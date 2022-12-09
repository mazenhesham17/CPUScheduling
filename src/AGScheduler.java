import java.util.Vector;

public class AGScheduler extends Scheduler {

    Vector<Vector<Pair<String, Integer>>> quantums;

    AGScheduler(ProcessData[] processes, int contextSwitching, int quantum) {
        super(processes, contextSwitching, quantum);
        super.setName("AG");
        quantums = new Vector<>();
    }

    private void updateQuantum() {
        Vector<Pair<String, Integer>> pairs = new Vector<>();
        for (ProcessData processData : processes) {
            Pair<String, Integer> pair = new Pair<>();
            pair.setFirst(processData.getName());
            pair.setSecond(processData.getQuantum());
            pairs.add(pair);
        }
        quantums.add(pairs);
    }

    @Override
    void run() {

    }

    @Override
    public void printDetails() {
        updateQuantum();
        super.printDetails();
        for (Vector<Pair<String, Integer>> pairs : quantums) {
            System.out.print("Quantum : ");
            for (Pair<String, Integer> pair : pairs) {
                System.out.print("(" + pair.getFirst() + "," + pair.getSecond() + ") ");
            }
            System.out.println();
        }
    }
}
