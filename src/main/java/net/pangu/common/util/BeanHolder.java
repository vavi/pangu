package net.pangu.common.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BeanHolder {
    private BeanHolder() {
    }

    private static Map<String, Object> beans = new ConcurrentHashMap<String, Object>();

    public static Object get(String beanName) {
	return beans.get(beanName);
    }

    public static void register(String beanName, Object obj) {
	beans.put(beanName, obj);
    }
}
