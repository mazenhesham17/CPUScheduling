public class ProcessInterval {
    String name;
    int start;
    int end;

    ProcessInterval(String name, int start) {
        this.name = name;
        this.start = start;

    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public int getStart() {
        return start;
    }
}
