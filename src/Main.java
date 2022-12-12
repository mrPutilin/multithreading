import java.util.*;
import java.util.concurrent.*;


public class Main {

    public static final Map<Integer, Integer> SIZE_TO_FREQ = new HashMap<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Callable<String> myCall = new Generate();

        ExecutorService threadPool = Executors.newFixedThreadPool(100);

        for (int i = 0; i < 100; i++) {
            threadPool.submit(myCall);
        }

        threadPool.shutdown();


        ExecutorService threadPool2 = Executors.newFixedThreadPool(100);

        int maxValue = 0;
        int maxKey = 0;

        synchronized (SIZE_TO_FREQ) {
            for (Map.Entry<Integer, Integer> entry : SIZE_TO_FREQ.entrySet()) {
                if (entry.getValue() > maxValue) {
                    maxValue = entry.getValue();
                    maxKey = entry.getKey();
                }
                threadPool2.submit(() ->
                {
                    SIZE_TO_FREQ.put(entry.getKey(), entry.getValue() + 1);
                });
            }

        }
        System.out.println("Самое частое кол-во повторений " + maxKey + " (встретилось " + SIZE_TO_FREQ.get(maxKey) + " раз)");

        threadPool2.shutdown();


        System.out.println("Другие размеры");

        for (Map.Entry<Integer, Integer> entry : SIZE_TO_FREQ.entrySet()) {
            System.out.println(entry.getKey() + " (" + entry.getValue() + " раз)");
        }

    }

}


class Generate implements Callable<String> {
    @Override
    public String call() throws Exception {

        String course = "RLRFR";

        Random random = new Random();
        StringBuilder route = new StringBuilder();
        int countR = 0;
        for (int i = 0; i < 100; i++) {
            route.append(course.charAt(random.nextInt(course.length())));
            if (route.charAt(i) == 'R') {
                countR++;
            }
        }

        if (Main.SIZE_TO_FREQ.containsKey(countR)) {
            Main.SIZE_TO_FREQ.put(countR, Main.SIZE_TO_FREQ.get(countR) + 1);
        } else {
            Main.SIZE_TO_FREQ.put(countR, 1);
        }

        return route + " -> " + countR;
    }
}

