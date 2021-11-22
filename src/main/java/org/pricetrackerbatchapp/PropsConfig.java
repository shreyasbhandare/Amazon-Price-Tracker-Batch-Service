package org.pricetrackerbatchapp;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public final class PropsConfig {
    private static final String appPropsPath = Thread.currentThread().getContextClassLoader().getResource("application.properties").getPath();
    private static Properties appProps;

    public static Properties getAppProps() {
        if(appProps == null) {
            appProps = new Properties();
            try {
                appProps.load(new FileInputStream(appPropsPath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return appProps;
    }
}