package cn.kuroneko.demo;

import cn.kuroneko.demo.utils.PdfUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@Controller("/")
public class PdfApp {
    public static void main(String[] args) {
        SpringApplication.run(PdfApp.class,args);
    }

    @Autowired
    private FreeMarkerConfigurer configurer;

    @GetMapping("/pdf")
    public void exportPdf2(HttpServletRequest request, HttpServletResponse response){
        List<Map<String,Object>> listVars = new ArrayList<>();
        Map<String,Object> variables = new HashMap<>();
        variables.put("year","2018");
        variables.put("month","12");
        variables.put("day","17");
        variables.put("idea","木大木大木大木大");
        listVars.add(variables);
        PdfUtils.preview(configurer,"template.ftl",listVars,response);
    }

}
