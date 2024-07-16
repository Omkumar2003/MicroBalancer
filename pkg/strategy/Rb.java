package pkg.strategy;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Config {
    String[] serverList = new String[]{"A", "B", "C", "D"};
}

class Idk extends Config {
    // bcoz of deadlock situation
    private final Lock lock = new ReentrantLock();

    Idk() {
        while (true) {
            for (int i = 0; i < serverList.length; i++) {

                final int serverIndex = i; 


                Thread T = new Thread(() -> {
                    boolean isSuccessful = false;
                    // imagine it is hitting th server
                    isSuccessful = true; // demo
                    if (!isSuccessful) {
                        assasinator(serverIndex);
                    }
                });
                T.start();


            }
            try {
                Thread.sleep(500);
            } catch (Exception e) {
                // inform admin if there is no admin then u r offically cooked
            }
        }
    }

    
    public void assasinator(int deadNode) {//
        lock.lock();
        try {
            String[] temp = new String[serverList.length - 1];
            int j = 0;
            for (int i = 0; i < serverList.length; i++) {
                if (i == deadNode) 
                {continue;
                }
                    temp[j++] = serverList[i];
            }
            serverList = temp;
        } finally {
            lock.unlock();
        }
    }
}

class RoundRobin extends Config {
    static int Bouncer = 0;
    private final Lock lock = new ReentrantLock();

    RoundRobin(String req) {
        forwarder(req);
    }

    public boolean forwarder(String req) {
        lock.lock();
        try {
            if (Bouncer < serverList.length - 1) {
                Bouncer++;
            } else {
                Bouncer = 0;
            }
            // Forwarding request to the server
            // ServerA s = new ServerA(req)
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            lock.unlock();
        }
    }
}

public class Rb {
    public static void main(String[] args) {
        new Idk();
        new RoundRobin("request");
    }
}
