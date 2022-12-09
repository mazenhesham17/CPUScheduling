import java.util.Comparator;

public class RemainingTimeComparator implements Comparator<ProcessData> {
    @Override
    public int compare(ProcessData o1, ProcessData o2) {
        return (o1.getRemainingTime() < o2.getRemainingTime() ? 1 : 0);
    }
}
