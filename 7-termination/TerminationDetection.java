import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class TerminationDetection {
    private static ConcurrentLinkedQueue<Message> messageQueue = new ConcurrentLinkedQueue<>();
    private static AtomicBoolean terminated = new AtomicBoolean(false);
    private static Random random = new Random();

    static class Message {
        int senderId;
        int receiverId;
        String type;
        Message(int senderId, int receiverId, String type) {
            this.senderId = senderId;
            this.receiverId = receiverId;
            this.type = type;
        }
    }

    static class Process extends Thread {
        private int id;
        private boolean isActive;
        private AtomicInteger deficit;
        private List<Integer> children;
        private List<Integer> parents;
        private boolean isInitiator;

        public Process(int id, boolean isInitiator) {
            this.id = id;
            this.isActive = true;
            this.deficit = new AtomicInteger(0);
            this.children = new ArrayList<>();
            this.parents = new ArrayList<>();
            this.isInitiator = isInitiator;
        }

        @Override
        public void run() {
            if (isInitiator)
                sendMessages();
            while (!terminated.get()) {
                if (isActive) {
                    try { Thread.sleep(100); } catch (InterruptedException e) {}
                    if (random.nextDouble() < 0.7) {
                        becomePassive();
                    } else {
                        sendMessages();
                    }
                } else {
                    processMessages();
                }
                if (isInitiator && !isActive && children.isEmpty() && deficit.get() == 0) {
                    terminated.set(true);
                    System.out.println("Process " + id + " detected termination.");
                    return;
                }
            }
        }

        private void sendMessages() {
            int numMessages = random.nextInt(2);
            for (int i = 0; i < numMessages; i++) {
                int receiverId = random.nextInt(5);
                if (receiverId != id) {
                    deficit.incrementAndGet();
                    synchronized (children) {
                        children.add(receiverId);
                    }
                    messageQueue.add(new Message(id, receiverId, "COMPUTATION"));
                    System.out.println("Process " + id + " sent COMPUTATION to Process " + receiverId);
                }
            }
        }

        private void processMessages() {
            Message msg = messageQueue.poll();
            if (msg != null && msg.receiverId == id) {
                if (msg.type.equals("COMPUTATION")) {
                    isActive = true;
                    synchronized (parents) { parents.add(msg.senderId); }
                    System.out.println("Process " + id + " received COMPUTATION from Process " + msg.senderId);
                } else if (msg.type.equals("SIGNAL")) {
                    deficit.decrementAndGet();
                    synchronized (children) { children.remove(Integer.valueOf(msg.senderId)); }
                    System.out.println("Process " + id + " received SIGNAL from Process " + msg.senderId);
                }
            } else if (msg != null) {
                messageQueue.add(msg);
            }
        }

        private void becomePassive() {
            isActive = false;
            System.out.println("Process " + id + " became passive.");
            synchronized (parents) {
                for (Integer parentId : parents) {
                    messageQueue.add(new Message(id, parentId, "SIGNAL"));
                    System.out.println("Process " + id + " sent SIGNAL to Process " + parentId);
                }
            }
        }
    }

    private static final int processCount = 5;

    public static void main(String[] args) {
        Process[] processes = new Process[processCount];
        for (int i = 0; i < processCount; i++) {
            processes[i] = new Process(i, i == 0);
            processes[i].start();
        }
        for (Process p : processes) {
            try { p.join(); } catch (InterruptedException e) {}
        }
        System.out.println("All processes have terminated.");
    }
}
