package cn.kuroneko;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Controller("/")
public class FtltoWordApp {
    public static void main(String[] args) {
        SpringApplication.run(FtltoWordApp.class, args);
    }

    @Autowired
    private FreeMarkerConfigurer configurer;

    @GetMapping("/export")
    public void exportPdf2(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Map<String, String> researcherMap = new HashMap<>();
        researcherMap.put("name", "张三三");
        researcherMap.put("dept", "超级研发部");
        researcherMap.put("phone", "13777778888");
        List<Object> researcherList = new ArrayList<>();
        researcherList.add(researcherMap);

        Map<String, String> marketMap = new HashMap<>();
        marketMap.put("name", "李思思");
        marketMap.put("sex", "女");
        marketMap.put("hobbi", "acg");
        marketMap.put("dept", "市场部");
        marketMap.put("job", "市场经理");
        marketMap.put("level", "s4");

        Map<String, String> marketMap2 = new HashMap<>();
        marketMap2.put("name", "王琪琪");
        marketMap2.put("sex", "女");
        marketMap2.put("hobbi", "overwatch");
        marketMap2.put("dept", "超级市场部");
        marketMap2.put("job", "超级市场经理");
        marketMap2.put("level", "s100");
        List<Object> marketList = new ArrayList<>();
        marketList.add(marketMap);
        marketList.add(marketMap2);

        Map<String, Object> variables = new HashMap<>();
        variables.put("productSource", "中国超级研发中心");
        variables.put("date", "2019年03月13日");
        variables.put("researcherList", researcherList);
        variables.put("marketList", marketList);
        variables.put("productDesc", "宇宙无敌光速发动机");


        Map<String, Object> data = new HashMap<>();
        data.put("data",variables);
        Template template = configurer.getConfiguration().getTemplate("template.ftl");
        String fileName="测试文件.doc";
        fileName = java.net.URLEncoder.encode(fileName, "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
        try {
            ServletOutputStream outputStream = response.getOutputStream();
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            template.process(data, outputStreamWriter);
            outputStreamWriter.flush();
            outputStreamWriter.close();
            outputStream.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        }
    }

}
