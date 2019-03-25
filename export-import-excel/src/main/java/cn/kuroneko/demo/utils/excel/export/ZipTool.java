package cn.kuroneko.demo.utils.excel.export;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@Service
public class ZipTool
{
	private static final Logger log = LoggerFactory.getLogger(ZipTool.class);
	//=============================================
	//  打包文件入口类
	//  parm:生成文件名,要打包的文件名,或目录
	// =============================================
	public boolean makeFile_Zip(String outfile, String infile)
	{
		try
		{
			// 文件输出流
			FileOutputStream fout = new FileOutputStream(outfile);
			// zip文件输出流
			ZipOutputStream out = new ZipOutputStream(fout);
			// 要打包的文件
			File file = new File(infile);
			// 进行打包zip操作,第一次打包不指定文件夹,因为程序接口中指定了一级文件夹
			makeFile_Zip_Do(out, file, "");
			// 关闭zip输出流
			out.close();
			//返回成功
			return true;
		}
		catch(FileNotFoundException e)
		{
			log.info("打包失败(指定的文件不存在)...");
			return false;
		}
		catch(Exception e)
		{
			log.info("打包失败(未知原因)...");
			return false;
		}
	}
	// =============================================
	//  打包文件操作类
	//  parm:zip输出流,打包文件,下一级的目录
	//=============================================
	public void makeFile_Zip_Do(ZipOutputStream out, File file, String dir) throws IOException
	{
		//如果当前打包的是目录
		if (file.isDirectory())
		{
			//输出进度信息
			log.info("当前正在打包文件夹:" + file + "...");
			//文件列表
			File[] files = file.listFiles();
			//添加下一个打包目录文件
			out.putNextEntry(new ZipEntry(dir + "/"));
			//
			dir = dir.length() == 0 ? "" : dir + "/";
			for (int i = 0; i < files.length; i++) {
				makeFile_Zip_Do(out, files[i], dir + files[i].getName());
			}
		}
		//如果当前打包文件
		else
		{
			//输出进度信息
			log.info("当前正在打包文件:" + file + "...");
			//
			out.putNextEntry(new ZipEntry(file.getName()));
			//文件输入流
			FileInputStream in = new FileInputStream(file);
			int i;
			//进行写入
			while ((i = in.read()) != -1) {
				out.write(i);
			}
			//关闭输入流
			in.close();
		}
	}
	//=============================================
	//  解压zip文件操作类
	//  parm:zip文件,输出文件夹
	//=============================================
	public boolean openFile_Zip(String zipfile,String savedir)
	{
		try
		{
			//文件输入流
			FileInputStream file = new FileInputStream(zipfile);
			//zip输入流
			ZipInputStream in = new ZipInputStream(file);
			ZipEntry z;
			while((z = in.getNextEntry()) != null)
			{
				//====如果是文件夹
				if(z.isDirectory())
				{
					log.info("正在解压文件夹:"+z.getName());
					File tempfile = new File(savedir + File.separator + z.getName().substring(0,z.getName().length()-1));
					//创建目录
					tempfile.mkdir();
					log.info("已经创建目录:"+savedir + File.separator + z.getName().substring(0,z.getName().length()-1));
				}
				//====如果是文件
				else
				{
					log.info("正在解压文件:"+z.getName());
					File tempfile = new File(savedir + File.separator + z.getName());
					//创建新文件
					tempfile.createNewFile();
					//文件输出流
					FileOutputStream out = new FileOutputStream(tempfile);
					int i;
					while((i = in.read()) != -1)
					{
						out.write(i);
					}
					//文件输出流关闭
					out.close();
				}
			}
			//文件输入流关闭
			in.close();
			return true;
		}
		catch(Exception e)
		{
			log.info("文件"+zipfile+"解压失败...");
			return false;
		}
	}
}
