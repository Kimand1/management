package com.gyeongbukSystem.management.mainSystem.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class PropResource {
	public static enum PropType { GLOBALS, CMS };
	public static final PropType DEFAULT_BUNDLE_NAME = PropType.CMS;
    //public static Properties neoProp;
    //public static Properties globalProp;
    public static Map<PropType, Properties> RESOURCE_BUNDLE_MAP = new HashMap<PropType, Properties>();

    /*
    @Resource(name="neoProp")
    public void setNeoProp(Properties prop) {
    	neoProp = prop;
    	RESOURCE_BUNDLE_MAP.put(PropType.CMS, neoProp);
    }

    @Resource(name="globalProp")
    public void setGlobalProp(Properties prop) {
    	globalProp = prop;
    	RESOURCE_BUNDLE_MAP.put(PropType.GLOBALS, globalProp);
    }
	*/

	private PropResource() {}

	public static void clearCache() {

	}

	public static String getString(String key) {
		return getString(DEFAULT_BUNDLE_NAME, key, "");
	}

	public static String getString(String key, String def) {
		return getString(DEFAULT_BUNDLE_NAME, key, def);
	}

	public static String getString(PropType bundleKey, String key) {
		return getString(bundleKey, key, "");
	}

	public static String getString(PropType bundleKey, String key, String def) {
		key = StringUtils.defaultIfEmpty(key, "X_X");
		String ret = def;
		Properties rb = RESOURCE_BUNDLE_MAP.get(bundleKey);
		if(rb != null) {
			ret = StringUtils.defaultIfEmpty(rb.getProperty(key), def);
		}
		return ret;
	}


	public static Integer getInteger(String key) {
		return getInteger(DEFAULT_BUNDLE_NAME, key, 0);
	}

	public static Integer getInteger(String key, Integer def) {
		return getInteger(DEFAULT_BUNDLE_NAME, key, def);
	}

	public static Integer getInteger(PropType bundleKey, String key) {
		return getInteger(bundleKey, key, 0);
	}

	public static Integer getInteger(PropType bundleKey, String key, Integer def) {
		key = StringUtils.defaultIfEmpty(key, "X_X");
		Integer ret = def;

		try {
			Properties rb = RESOURCE_BUNDLE_MAP.get(bundleKey);
			if(rb != null) {
				ret = Integer.parseInt(rb.getProperty(key));
			}
			if(ret == null && def != null) {
				ret = def;
			}
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static Double getDouble(String key) {
		return getDouble(DEFAULT_BUNDLE_NAME, key, 0.0);
	}

	public static Double getDouble(String key, Double def) {
		return getDouble(DEFAULT_BUNDLE_NAME, key, def);
	}

	public static Double getDouble(PropType bundleKey, String key) {
		return getDouble(bundleKey, key, 0.0);
	}

	public static Double getDouble(PropType bundleKey, String key, Double def) {
		key = StringUtils.defaultIfEmpty(key, "X_X");
		Double ret = def;

		try {
			Properties rb = RESOURCE_BUNDLE_MAP.get(bundleKey);
			if(rb != null) {
				ret = Double.parseDouble(rb.getProperty(key));
			}
			else {
				ret = def;
			}
		}
		catch(IllegalArgumentException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
