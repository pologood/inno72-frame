package com.inno72.log.center.action;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.json.JsonUtil;
import com.inno72.log.center.util.FastJsonUtils;
import com.inno72.log.center.vo.BizInfo;
import com.inno72.log.center.vo.SysLog;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping(value = "/log/")
@CrossOrigin
public class LogController {

	/**
	 *
	 * @param file 日志文件
	 * @param pushId 推送ID
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "upload", method = RequestMethod.POST)
	public @ResponseBody
	Result uploadImg(@RequestParam("file") MultipartFile file, String pushId) throws IOException {
		// 解压缩文件
		InputStream is = file.getInputStream();
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader reader = new BufferedReader(isr);
		String line;
		for (; ; ) {
			line = reader.readLine();
			if (null != line) {
				// 判断是何种类型日志
				String dimension = FastJsonUtils.getString(line, "dimension");
				if (dimension.equals("sys") ) {
					// todo 处理系统日志 指定kafka topic
					SysLog sysLog = JsonUtil.toObject(line, SysLog.class);
					System.out.println(sysLog);
				} else {
					// todo 处理业务日志 产品日志 指定kafka topic
					BizInfo bizInfo = JsonUtil.toObject(line, BizInfo.class);
					System.out.println(bizInfo);
				}
				// 解析
			} else {
				break;
			}
		}

		// todo 增加push表
		// 返回json
		return Results.success();
	}
}
