package net.pangu.common.util;

import java.io.IOException;
import java.io.InputStream;

public class IOUtil {
    private IOUtil() {
    }

    public static void close(InputStream is) {
	if (null != is) {
	    try {
		is.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	}
    }
}
