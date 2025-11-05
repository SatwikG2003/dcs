import java.util.*;
class Node {
    final int id;
    long clockOffsetMs;
    boolean alive = true;
    Node(int id, long offsetMs) {
        this.id = id;
        this.clockOffsetMs = offsetMs;
    }
    long currentTime() {
        return System.currentTimeMillis() + clockOffsetMs;
    }
}
public class Berkeley {
    public static void main(String[] args) {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(0, 0)); // master
        nodes.add(new Node(1, 500));
        nodes.add(new Node(2, -800));
        nodes.add(new Node(3, 1200));
        nodes.add(new Node(4, -300));

        Node master = nodes.get(0);
        List<Long> samples = new ArrayList<>();
        for (Node n : nodes) {
            if (!n.alive) continue;
            samples.add(n.currentTime());
        }

        Collections.sort(samples);
        int trim = Math.max(0, samples.size()/10);
        List<Long> trimmed = samples.subList(trim, samples.size()-trim);
        long sum = 0;
        for (long t: trimmed) sum += t;
        long avg = sum / trimmed.size();

        System.out.println("Before sync (offset ms):");
        for (Node n : nodes) System.out.printf("Node %d: %d\n", n.id, n.clockOffsetMs);

        for (Node n : nodes) if (n.alive) {
            long adjust = avg - n.currentTime();
            n.clockOffsetMs += adjust;
        }

        System.out.println("\nAfter sync (offset ms):");
        for (Node n : nodes) System.out.printf("Node %d: %d\n", n.id, n.clockOffsetMs);
    }
}
