package de.allcom.jobs;

import org.jobrunr.jobs.context.JobRunrDashboardLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MyBackgroundJob {
    private static final Logger LOGGER = new JobRunrDashboardLogger(LoggerFactory.getLogger(MyBackgroundJob.class));

    public void sayHello(String name) {
        LOGGER.warn("Starting long running job...");
        LOGGER.info(String.format("name: %s", name));


        System.out.println("Привет, " + name);
        LOGGER.warn("Finished long running job...");

    }
}
