package course.concurrency.m3_shared.intro;

import java.io.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class T {

    private final Lock lock = new ReentrantLock();

    public void updateRegistry(File file) {
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            lock.lock();
            // ...

        } catch (FileNotFoundException fnf) { /*...*/ } finally {
            lock.unlock();
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) { /*...*/ }
            }
        }
    }
}
