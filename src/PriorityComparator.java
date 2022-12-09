import java.util.Comparator;

public class PriorityComparator implements Comparator<ProcessData> {
    @Override
    public int compare(ProcessData o1, ProcessData o2) {
        return (o1.getPriority() < o2.getPriority() ? 1 : 0);
    }
}
