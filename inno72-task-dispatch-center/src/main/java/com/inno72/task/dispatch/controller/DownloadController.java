package com.inno72.task.dispatch.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.inno72.task.dispatch.service.TaskModelService;

@Controller
@RequestMapping("/download")
public class DownloadController {

	@Autowired
	private TaskModelService taskModelService;

	@GetMapping("/log")
	public ResponseEntity<byte[]> downloadLogFile(@RequestParam("taskName") String taskName, String date) {
		return this.taskModelService.downloadLog(LocalDate.parse(date), taskName);
	}
}
