package zolikon.downloadservice.retry;


import com.google.common.util.concurrent.AbstractScheduledService;

import java.util.concurrent.TimeUnit;

public class Retry extends AbstractScheduledService{

    private final long delay;
    private final int maxRetryCount;
    private int actualRetry;

    public Retry(long delay, int maxRetryCount) {
        this.delay = delay;
        this.maxRetryCount = maxRetryCount;
    }

    @Override
    protected void runOneIteration() throws Exception {
        actualRetry++;
        if(actualRetry>=maxRetryCount){
            return;
        }
        System.out.println(actualRetry);
    }

    @Override
    protected Scheduler scheduler() {
        return Scheduler.newFixedDelaySchedule(0,delay, TimeUnit.SECONDS);
    }


    public static void main(String[] args) throws Exception{
        new Retry(2,3);
    }
}
