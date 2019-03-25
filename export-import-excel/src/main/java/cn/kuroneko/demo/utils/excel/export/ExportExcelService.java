package cn.kuroneko.demo.utils.excel.export;


import cn.kuroneko.demo.utils.excel.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author lip 创建于 2012-11-3
 * 
 */
@Service
public class ExportExcelService<T extends Object> {
	
	public static final String OUTPUT_ROOTPATH="excel"+File.separator+"tmp"+File.separator;
	
	@Autowired
	private FileHelper fileHelper;

	public String generateExcel(String dir,String fileName, List<T> list,String[] title,String[] value,String sheet,boolean isMap)
			throws IOException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			IntrospectionException {
		
		
		Workbook wb = new HSSFWorkbook();
		// 创建一个EXCEL
		DataFormat format = wb.createDataFormat();
		// 创建一个SHEET
		Sheet sheet1 = wb.createSheet(sheet);
/*		String[] title = { "卡号","密码","金额(元)","有效期" };
		String[] value = { "number","'plainPwd","amount","deadline" };*/

		// 创建一行
		Row headTitle = sheet1.createRow((short) 0);
		CellStyle style = wb.createCellStyle();

		// 填充标题
		int i = 0;
		for (String s : title) {
			Cell cell = headTitle.createCell(i);
			cell.setCellValue(s);
			i++;
		}
		int j = 1;
		for (T obj : list) {
			int m = 0;
			Row row = sheet1.createRow((short) j);
			for (String pro : value) {
				boolean strFormat = false;
				boolean numFormat = false;
				if(pro.contains("'")) //如果带',则输出也带'
				{
					pro = pro.replace("'", "");
					strFormat = true;
				}
				
				Cell cell = row.createCell(m);
				if(pro.contains("￥")) //如果带',则输出也带'
				{
					numFormat = true;
					pro=pro.replaceAll("￥", "");
				}
				String v = "";
				if(isMap)
				{
					v = this.getValue(obj, pro);
				}
				else
				{
					v = this.getProperty(obj, pro);
				}
				if(strFormat)
				{
					cell.setCellValue("'"+v);
				}
				else
				{
					if(numFormat&& StringUtils.isNotBlank(v))
					{
//						CellStyle style = wb.createCellStyle();
				        style.setDataFormat(HSSFDataFormat.getBuiltinFormat("0.00")); // 两位小数，只加了一个格式的自定义，反应到Excle上面为自定义的金额，其他格式类似
				        cell.setCellStyle(style);  
				        cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
						cell.setCellValue(Double.parseDouble(v));
					}
					else
					{
						if(HSSFDateUtil.isCellDateFormatted(cell)){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Date date = HSSFDateUtil.getJavaDate(cell.getNumericCellValue());
							return sdf.format(date).toString();
						}
						else
						{
							cell.setCellValue(v);
						}
					}
				}
				m++;
			}
			j++;
		}

		for(int m=0;m<title.length;m++)
		{
			sheet1.autoSizeColumn((short)m); //调整第一列宽度
		}
		
		//如果fileName为空，则以时间作为文件名
		if (StringUtils.isBlank(fileName)) {
			fileName = String.valueOf(DateUtils.now().getTime());
		}
		else
		{
			fileName = fileName+"_"+String.valueOf(DateUtils.now().getTime());
		}
		
		String file = fileHelper.createBlankFile(OUTPUT_ROOTPATH+dir+File.separator, fileName, "xls");
/*		FileOutputStream fileOut = new FileOutputStream("d:\\cardpark/"
				+ fileName + ".xls");*/
		FileOutputStream fileOut = new FileOutputStream(file);
		wb.write(fileOut);
		fileOut.close();
		return file;
	}
	
	/**
	 * 获取对象属性,以字符串形式返回
	 * 
	 * @param obj
	 * @param property
	 * @return
	 * @throws IntrospectionException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public String getProperty(Object obj, String property)
			throws IntrospectionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Class clazz = obj.getClass();
		PropertyDescriptor pd = new PropertyDescriptor(property, clazz);
		Method getMethod = pd.getReadMethod();// 获得get方法
		Object o = getMethod.invoke(obj);
		if(o==null)
		{
			return"";
		}
		else
		{
			return o.toString();
		}
	}
	
	public String getValue(Object obj, String key)
			throws IntrospectionException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		Map map = (Map)obj;
		Object o = map.get(key);
		if(o==null)
		{
			return"";
		}
		else
		{
			return o.toString();
		}
	}

}
