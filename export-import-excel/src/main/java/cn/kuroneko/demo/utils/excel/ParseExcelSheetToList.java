package cn.kuroneko.demo.utils.excel;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * author:      lp
 * create date: 2015/9/23.
 */
public class ParseExcelSheetToList<T> {
    private final Class<T> type;
    private final FieldNameAndType[] fieldNameAndTypes;
    private final InputStream inputStream;
    private final Sheet sheet;
    private List<T> list;


    public ParseExcelSheetToList(InputStream inputStream, Class<T> type, FieldNameAndType[] fieldNameAndTypes) throws Exception{
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream can not been null");
        }
        this.inputStream = inputStream;

        if (type == null) {
            throw new IllegalArgumentException("type can not been null");
        }
        this.type = type;

        if (fieldNameAndTypes == null) {
            throw new IllegalArgumentException("fieldNameAndTypes can not been null");
        }
        this.fieldNameAndTypes = fieldNameAndTypes;

        try {
            Workbook workbook = new HSSFWorkbook(inputStream);
            sheet = workbook.getSheetAt(0);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("construct HSSFWorkbook failed");
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public List<T> getList() {
        if (list == null) {
            list = traverseSheet();
        }
        return list;
    }
    private List<T> traverseSheet() {
        List<T> list = new ArrayList<T>();
        int lastRowNum = sheet.getLastRowNum();
        // 忽略掉第一行表头记录
        for (int     i = 1; i <= lastRowNum; i++) {
            Row row = sheet.getRow(i);
            list.add(parseRecord(row));
        }
        return list;
    }
    private T parseRecord(Row row) {
        T targetObject = null;
        try {
            targetObject = type.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < fieldNameAndTypes.length; i++) {
            Cell cell = row.getCell(i);

            if(cell!=null)
            {
                Object value = null;
                try {
                    Method method = targetObject.getClass().getMethod("set" + ParseExcelSheetToList.capitalize(fieldNameAndTypes[i].getFieldName()),
                            fieldNameAndTypes[i].getFieldType());
                    Class parameterType = fieldNameAndTypes[i].getFieldType();
                    if (int.class == parameterType || Integer.class == parameterType) {
                        // getNumericCellValue 返回的数值型为 double 型，如果目标类型为整形的话要做强制转换。
                        value = (int) cell.getNumericCellValue();
                    }
                    if (double.class == parameterType || Double.class == parameterType) {
                        value = cell.getNumericCellValue();
                    }
                    if (String.class == parameterType) {
                        //value = cell.getStringCellValue();

                        value = String.valueOf(this.getCellVal(cell));
                    }
//                    if(EnumGroupType.class == parameterType)
//                    {
//                        value = EnumGroupType.valueOf(cell.getStringCellValue());
//                    }
//                    if(EnumTeam.class == parameterType)
//                    {
//                        value = EnumTeam.valueOf(cell.getStringCellValue());
//                    }
                    method.invoke(targetObject, value);

                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }

        }

        return targetObject;
    }


    private String getCellVal(Cell cell)
    {
        String value = "";
        if(cell!=null)
        {

            switch (cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC: // 数字
                    DecimalFormat df = new DecimalFormat("0");

                    value = df.format(cell.getNumericCellValue());
                    break;
                case HSSFCell.CELL_TYPE_STRING: // 字符串
                    value = cell.getStringCellValue();
                    break;
                case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
                    value = cell.getBooleanCellValue() + "";
                    break;
                case HSSFCell.CELL_TYPE_FORMULA: // 公式
                    value = cell.getCellFormula() + "";
                    break;
                case HSSFCell.CELL_TYPE_BLANK: // 空值
                    value = "";
                    break;
                case HSSFCell.CELL_TYPE_ERROR: // 故障
                    value = "非法字符";
                    break;
                default:
                    value = "未知类型";
                    break;
            }
        }
        else
        {
            value =  "";
        }

        return value;
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return s;
        }
        return Character.toTitleCase(s.charAt(0)) + s.substring(1, s.length());
    }
}
