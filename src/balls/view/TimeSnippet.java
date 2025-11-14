package balls.view;

import static java.lang.Thread.sleep;

public class TimeSnippet {
    public static void main(String[] args) throws InterruptedException {
        for (int i=0; i<10; i++) {
            long lastSecondMillisecond = System.currentTimeMillis();
            System.out.println("Millisecond: " + lastSecondMillisecond + ", Millisecond +1000: " +(lastSecondMillisecond + 1000));
            sleep(1000);
        }
    }
}
