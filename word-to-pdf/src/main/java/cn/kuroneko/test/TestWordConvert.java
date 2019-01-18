package cn.kuroneko.test;

import cn.kuroneko.utils.WordUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Data
public class TestWordConvert {

    @Test
    public void exportPdf() throws Exception{
        WordUtils wordUtils = new WordUtils();
        String modelPath="doc/template.docx";
        long timeStamp = LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli();
        String tempFilePath="out/";
        String docxSuffix=".docx";
        String pdfSuffix=".pdf";
        String tempFileName= String.format("temp%s",timeStamp);   //生成word文件的文件名
        String outputFilePath= "out/";   //生成word文件的文件路径
        String docFileName=tempFilePath+tempFileName+docxSuffix;
        String pdfFileName=outputFilePath+"Poi"+tempFileName+pdfSuffix;
        Map<String, String> params = new HashMap<String, String>();
        params.put("$year", "18");
        params.put("$month", "11");
        params.put("$day", "12");
        params.put("$no", "11111");
        params.put("$reportSource","china");
        params.put("$name", "厉害的人");
        params.put("$danwei", "厉害的公司");
        params.put("$sex", "男人");
        params.put("${remark}", "jjjj");
//        使用poi进行特定字符串的替换并输出docx文件
        wordUtils.replaceWithPath(modelPath,docFileName,params);
//        使用poi将docx文件转换为pdf
        wordUtils.convertWordToPdfByPoi(docFileName,pdfFileName);
//        使用LibreOffice将docx文件转换为pdf
        wordUtils.converWordToPdfByLibreOffice(docFileName,outputFilePath);
    }

}
