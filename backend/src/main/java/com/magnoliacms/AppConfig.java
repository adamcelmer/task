package com.magnoliacms;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "app")
public interface AppConfig {

    String baseUrl();

    CleanupScheduler cleanupScheduler();

    interface CleanupScheduler {
        String cron();
        int expirationDays();
    }
}
