package cn.kuroneko.demo.utils.excel.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * @author lip 创建于 2012-4-12 下午7:05:07
 */
@Service
public class FileHelper {

	private static final Logger log = LoggerFactory.getLogger(FileHelper.class);
	
	public String createBlankFile(String path,String fileName, String suffix)
			throws IOException {
		String filePath = path+ fileName + "." + suffix;
		log.info(filePath);
		
		File dirFile  = null ;
		
		try {
            dirFile  =   new  File(path);
             if ( ! (dirFile.exists())  &&   ! (dirFile.isDirectory())) {
                 boolean  creadok  =  dirFile.mkdirs();
                 if (creadok) {
                    log.info( " ok:创建文件夹成功！ " );
                } else {
                    log.info( " err:创建文件夹失败！ " );                    
                } 
            } 
         } catch (Exception e) {
            log.info(e.toString());
        } 
		
		
		
		
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		
		return file.toString();
	}
	
	public void createFile(String path,String fileName, String content, String suffix)
			throws IOException {
		String filePath = path+ fileName + "." + suffix;
		log.info(filePath);
		
		File dirFile  = null ;
		
		try {
            dirFile  =   new  File(path);
             if ( ! (dirFile.exists())  &&   ! (dirFile.isDirectory())) {
                 boolean  creadok  =  dirFile.mkdirs();
                 if (creadok) {
                    log.info( " ok:创建文件夹成功！ " );
                } else {
                    log.info( " err:创建文件夹失败！ " );                    
                } 
            } 
         } catch (Exception e) {
        	 log.info(e.toString());
        } 
		
		
		
		
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		try {

			OutputStream os = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	
	/**
	 * 	创建文件
	 * @param fileName	文件名
	 * @param content 	文件内容
	 * @param suffix	文件后缀
	 * @throws IOException
	 */
	public void createFile(String fileName, String content, String suffix)
			throws IOException {
		String path = "."+File.separator+"src"+File.separator + fileName + "." + suffix;
		log.info(path);
		File file = new File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		try {

			OutputStream os = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(os, "utf-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			log.info(e.toString());
		}

	}

	/** 以行为单位读取文件
	 * @param fileName
	 * @return
	 */
	public static String readFileByLines(String fileName) {
		File file = new File(fileName);
		BufferedReader reader = null;
		StringBuffer content = new StringBuffer();
		try {
			log.info("以行为单位读取文件内容，一次读一整行：");
			reader = new BufferedReader(new FileReader(file));

			String tempString = null;
			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null) {
				// 显示行号
				content.append(tempString);
			}

			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} 
				catch (IOException e1) {
					log.info(e1.toString());
				}
			}
		}
		return content.toString();
	}
	
	public void deleteFile(File file){ 
		   if(file.exists()){ 
		    if(file.isFile()){ 
		     file.delete(); 
		    }else if(file.isDirectory()){ 
		     File files[] = file.listFiles(); 
		     for(int i=0;i<files.length;i++){ 
		      this.deleteFile(files[i]); 
		     } 
		    } 
		    file.delete(); 
		   }else{ 
		    log.info("所删除的文件不存在！"+'\n'); 
		   } 
	} 
}
