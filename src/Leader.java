import java.util.*;

public class Leader {
    public static final Map<Integer, Integer> SIZE_TO_FREE = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {

        List<Thread> list = new ArrayList<>();

        Thread fillMap = new FillMap();
        fillMap.start();
        list.add(fillMap);


        Thread printLeader = new Print();
        printLeader.start();

        for (Thread f : list) {
            f.join();
        }

        printLeader.interrupt();

    }

}

class Print extends Thread {
    @Override
    public void run() {
        int biggestValue = 0;
        while (!Thread.interrupted()) {
            synchronized (Leader.SIZE_TO_FREE) {
                try {
                    Leader.SIZE_TO_FREE.wait();
                } catch (InterruptedException e) {
                    return;
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

class FillMap extends Thread {
    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
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

            synchronized (Leader.SIZE_TO_FREE) {
                if (Leader.SIZE_TO_FREE.containsKey(countR)) {
                    Leader.SIZE_TO_FREE.put(countR, Leader.SIZE_TO_FREE.get(countR) + 1);
                } else {
                    Leader.SIZE_TO_FREE.put(countR, 1);
                }
                Leader.SIZE_TO_FREE.notify();
            }
        }
    }
}
