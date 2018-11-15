package cn.kuroneko.utils;

import com.google.common.base.Strings;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Objects;

/**
 * Created by fuxiao
 * on 2017/6/1.
 * email: fuxiao9@crv.com.cn
 */
public class FreemarkerUtils {

    private static final String DEFAULT_ENCODE = "UTF-8";

    public static String loadFtlHtml(File dirPath, String fileName, Map globalMap) throws Exception {

        if (Objects.isNull(globalMap) || Strings.isNullOrEmpty(fileName)) {
            throw new IllegalArgumentException("Directory file");
        }

        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);
        try {
            cfg.setDirectoryForTemplateLoading(dirPath);
            cfg.setDefaultEncoding(DEFAULT_ENCODE);
            cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);//.RETHROW
            cfg.setClassicCompatible(true);
            Template temp = cfg.getTemplate(fileName);

            StringWriter stringWriter = new StringWriter();
            temp.process(globalMap, stringWriter);
            return stringWriter.toString();
        } catch (IOException | TemplateException e) {
            throw new Exception(e.getCause());
        }
    }
}
