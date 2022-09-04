package course.concurrency.exams.auction;


import static java.lang.Thread.currentThread;

public  class Task implements Runnable {
    private static final ThreadLocal<Integer> value =
            ThreadLocal.withInitial(() -> 0);


    @Override
    public void run() {

        System.out.println(value.get());
        Integer currentValue = value.get();
        value.set(currentValue + 1);
    }
}

