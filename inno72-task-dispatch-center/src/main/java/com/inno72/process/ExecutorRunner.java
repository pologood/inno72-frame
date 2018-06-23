package com.inno72.process;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ExecutorRunner {

	private ConcurrentHashMap<String, Process> holder = new ConcurrentHashMap<>();

	public void start(String id, String path) {
		File file = new File(path);
		ProcessBuilder builder = new ProcessBuilder("java", "-jar", file.getName());
		builder.directory(file.getParentFile());
		try {
			Process process = builder.start();
			this.holder.put(id, process);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stop(String id) {
		if (this.holder.get(id) == null) {

		} else {
			this.holder.get(id).destroy();
		}
	}

	public void ss() {
		ProcessBuilder builder = new ProcessBuilder();
	}

}
