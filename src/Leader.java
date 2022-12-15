import java.util.*;

public class Leader {
    public static final Map<Integer, Integer> SIZE_TO_FREE = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        List<Thread> list = new ArrayList<>();

        Thread fillMap = new Thread(() -> {
            for (int i = 0; i < 100; i++) {
                String course = "RLRFR";
                Random random = new Random();
                StringBuilder route = new StringBuilder();
                int countR = 0;
                for (int j = 0; j < 100; j++) {
                    route.append(course.charAt(random.nextInt(course.length())));
                    if (route.charAt(j) == 'R') {
                        countR++;
                    }
                }

                synchronized (SIZE_TO_FREE) {
                    if (SIZE_TO_FREE.containsKey(countR)) {
                        SIZE_TO_FREE.put(countR, SIZE_TO_FREE.get(countR) + 1);
                    } else {
                        SIZE_TO_FREE.put(countR, 1);
                    }
                    System.out.println("Текущая частота " + SIZE_TO_FREE.get(countR));
                    SIZE_TO_FREE.notify();
                }
            }
        });
        fillMap.start();

        Thread printLeader = new Thread(new Print());
        list.add(printLeader);
        printLeader.start();

        printLeader.join();

        printLeader.interrupt();

    }

}

class Print implements Runnable {
    @Override
    public void run() {

        int biggestValue = 0;

        while (!Thread.interrupted()) {
            synchronized (Leader.SIZE_TO_FREE) {
                try {
                    Leader.SIZE_TO_FREE.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Map.Entry<Integer, Integer> max = Leader.SIZE_TO_FREE
                        .entrySet()
                        .stream()
                        .max(Map.Entry.comparingByValue())
                        .get();
                if (max.getValue() > biggestValue) {
                    biggestValue = max.getValue();
                    System.out.println("Лидер среди частот на данный момент " + max.getKey() + " встретился " + max.getValue() + " раз");
                }
            }
        }
    }
}
