package cn.kuroneko.test;

import cn.kuroneko.utils.FreemarkerUtils;
import cn.kuroneko.utils.PdfUtils;
import org.junit.Test;

import freemarker.template.Configuration;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class TestConvert {
    @Test
    public void exportPdf() throws Exception{
        String filePath="templates/";
        String fileName="test.ftl";
        String fontPath="templates/font/msyh.ttf";
        String outputFile="out/test.pdf";
        //       检查输出文件的路径是否存在
        String dir = outputFile.substring(0, outputFile.indexOf("/") + 1);
        File outDir = new File(dir);
        if (!outDir.exists()) {
            outDir.mkdir();
        }

        Map<String,Object> map= new HashMap<>();
        Map<String,Object> productInfo= new HashMap<>();
        productInfo.put("name","kuroneko");
        map.put("productInfo",productInfo);

        String s = FreemarkerUtils.loadFtlHtml(new File(filePath), fileName, map);
        System.out.println(s);
        PdfUtils.savePdf(new FileOutputStream(outputFile),s,fontPath);

    }
}
