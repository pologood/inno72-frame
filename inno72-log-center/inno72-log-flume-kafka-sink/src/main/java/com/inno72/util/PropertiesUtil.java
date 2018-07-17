package com.inno72.util;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

	public Properties load(String path) throws IOException {
		return PropertiesLoaderUtils.loadProperties(new ClassPathResource(path));
	}

}
