package com.inno72.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Expand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.inno72.oss.OSSUtil;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.exception.RarException;
import de.innosystec.unrar.rarfile.FileHeader;

@Component
public class FileUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtil.class);

//	@Autowired
//	private Inno72LogTaskServiceProperties inno72LogTaskServiceProperties;

	public String remotePath;

	public String local0Path;

	public boolean download(String filePath) {

		try {

			String local0Path = "/tmp/";
			String localFile = local0Path + filePath;
			if (isExitsPath(localFile)){
				LOGGER.info("文件路径不存在 {}", filePath);
				return false;
			}
			OSSUtil.downloadFile( filePath, localFile);
			deCompress(localFile, localFile.substring(0, localFile.indexOf(".")));
		} catch (Exception e) {
			LOGGER.info("下载文件异常 {}, {}", filePath, e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * 解压缩
	 */
	public static void deCompress(String sourceFile,String destDir) throws Exception{
		//保证文件夹路径最后是"/"或者"\"
		
		if (destDir.indexOf(".zip") != -1) {
			destDir = destDir.substring(0, destDir.lastIndexOf("/"));
		}
		char lastChar = destDir.charAt(destDir.length()-1);
		if(lastChar!='/'&&lastChar!='\\'){
			destDir += File.separator;
		}
		//根据类型，进行相应的解压缩
		String type = sourceFile.substring(sourceFile.lastIndexOf(".")+1);
		if(type.equals("zip")){
			FileUtil.unzip(sourceFile, destDir);
		}else if(type.equals("rar")){
			FileUtil.unrar(sourceFile, destDir);
		}else{
			throw new Exception("只支持zip和rar格式的压缩包！");
		}
	}

	/**
	 * 解压zip格式压缩包
	 * 对应的是ant.jar
	 */
	private static void unzip(String sourceZip,String destDir) throws Exception{
		try{
			Project p = new Project();
			Expand e = new Expand();
			e.setProject(p);
			e.setSrc(new File(sourceZip));
			e.setOverwrite(false);
			e.setDest(new File(destDir));
           /*
           ant下的zip工具默认压缩编码为UTF-8编码，
           而winRAR软件压缩是用的windows默认的GBK或者GB2312编码
           所以解压缩时要制定编码格式
           */
			e.setEncoding("gbk");
			e.execute();
		}catch(Exception e){
			throw e;
		}
	}
	/**
	 * 解压rar格式压缩包。
	 * 对应的是java-unrar-0.3.jar，但是java-unrar-0.3.jar又会用到commons-logging-1.1.1.jar
	 */
	public static void unrar(String srcPath,String unrarPath) throws RarException, IOException, Exception{
		File srcFile = new File(srcPath);
		if(null == unrarPath || "".equals(unrarPath)){
			unrarPath = srcFile.getParentFile().getPath();
		}
		// 保证文件夹路径最后是"/"或者"\"
		char lastChar = unrarPath.charAt(unrarPath.length() - 1);
		if (lastChar != '/' && lastChar != '\\') {
			unrarPath += File.separator;
		}
		LOGGER.info(srcPath,"unrar file to :"+unrarPath);

		unrar(srcFile, unrarPath);
	}

	private static void unrar(File srcFile,String unrarPath) throws RarException, IOException, Exception{
		FileOutputStream fileOut = null;
		Archive rarfile = null;

		try{
			rarfile = new Archive(srcFile);
			FileHeader fh = rarfile.nextFileHeader();
			while(fh!=null){

				String entrypath = "";
				if(fh.isUnicode()){//解決中文乱码
					entrypath = fh.getFileNameW().trim();
				}else{
					entrypath = fh.getFileNameString().trim();
				}
				entrypath = entrypath.replaceAll("\\\\", "/");

				File file = new File(unrarPath + entrypath);
				LOGGER.info(unrarPath,"unrar entry file :{}", file.getPath());

				if(fh.isDirectory()){
					file.mkdirs();
				}else{
					File parent = file.getParentFile();
					if(parent!=null && !parent.exists()){
						parent.mkdirs();
					}
					fileOut = new FileOutputStream(file);
					rarfile.extractFile(fh, fileOut);
					fileOut.close();
				}
				fh = rarfile.nextFileHeader();
			}
			rarfile.close();

		} catch (Exception e) {
			throw e;
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
					fileOut = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (rarfile != null) {
				try {
					rarfile.close();
					rarfile = null;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static boolean isExitsPath(String path) throws InterruptedException{
		String [] paths=path.split("/");
		StringBuffer fullPath=new StringBuffer();
		for (int i = 0; i < paths.length; i++) {
			String name = paths[i];
			if (name.contains(".zip")) {
				name = name.substring(0, name.indexOf("."));
			}
			fullPath.append(name).append("/");
			File file=new File(fullPath.toString());
			if(!file.exists()){
				file.mkdir();
				System.out.println("创建目录为："+fullPath.toString());
			}
		}
		File file=new File(fullPath.toString());//目录全路径
		if (!file.exists()) {
			return true;
		}else{
			return false;
		}
	}


}
