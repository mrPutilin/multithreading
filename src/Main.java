import java.util.*;
import java.util.concurrent.*;

public class Main {

    public static final Map<Integer, Integer> SIZE_TO_FREQ = new HashMap<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        Runnable myRun = new Generate();

        ExecutorService threadPool = Executors.newFixedThreadPool(1000);

        for (int i = 0; i < 1000; i++) {
            threadPool.submit(myRun);
        }

        int maxValue = 0;
        int maxKey = 0;

        for (Map.Entry<Integer, Integer> entry : SIZE_TO_FREQ.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                maxKey = entry.getKey();
            }
        }

        System.out.println("Самое частое кол-во повторений " + maxKey + " (встретилось " + SIZE_TO_FREQ.get(maxKey) + " раз)");


        System.out.println("Другие размеры");

        SIZE_TO_FREQ
                .entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(a -> System.out.println("- " + a.getKey() + " (" + a.getValue() + " раз)"));


        threadPool.shutdown();


        /*
        Чужой способо написания кода, по нахождению максимума в Map.
        Не обращать внимания, пусть будет тут.

        Map.Entry<Integer, Integer> max = SIZE_TO_FREQ
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .get();
        System.out.println(max.getKey() + " " + max.getValue());
         */

    }

}


class Generate implements Runnable {
    @Override
    public void run() {

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

        synchronized (Main.SIZE_TO_FREQ) {
            if (Main.SIZE_TO_FREQ.containsKey(countR)) {
                Main.SIZE_TO_FREQ.put(countR, Main.SIZE_TO_FREQ.get(countR) + 1);
            } else {
                Main.SIZE_TO_FREQ.put(countR, 1);
            }
        }
    }
}

