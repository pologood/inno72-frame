package com.inno72.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

	public Properties load(String path) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(new File(path));
		Properties properties = new Properties();
		properties.load(fileInputStream);
		return properties;
	}
}
