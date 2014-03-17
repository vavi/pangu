package net.pangu.registry.env;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import net.pangu.common.util.IOUtil;

public class Evnoriment {

    private static Properties properties;

    public static void loadEnv(String envFileName) {
	InputStream is = null;

	try {
	    is = Thread.currentThread().getContextClassLoader()
		    .getResourceAsStream(envFileName);
	    properties.load(is);
	} catch (IOException e) {
	    e.printStackTrace();
	} finally {
	    IOUtil.close(is);
	}
    }

    public static Object getValue(String key) {
	return properties.get(key);
    }
}
