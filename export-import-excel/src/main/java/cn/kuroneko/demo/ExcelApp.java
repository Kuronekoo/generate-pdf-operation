package cn.kuroneko.demo;

import cn.kuroneko.demo.domain.Person;
import cn.kuroneko.demo.utils.excel.DateUtils;
import cn.kuroneko.demo.utils.excel.FieldNameAndType;
import cn.kuroneko.demo.utils.excel.ParseExcelSheetToList;
import cn.kuroneko.demo.utils.excel.export.ExportExcelService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@RestController("/")
public class ExcelApp {
    public static void main(String[] args) {
        SpringApplication.run(ExcelApp.class, args);
    }
    @Autowired
    private ExportExcelService exportExcelService;

    @RequestMapping("/import")
    public void importExcel(@RequestParam("file") MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        FieldNameAndType[] nameAndTypes = new FieldNameAndType[6];
        nameAndTypes[0] = new FieldNameAndType("id", Long.class);
        nameAndTypes[1] = new FieldNameAndType("name", String.class);
        nameAndTypes[2] = new FieldNameAndType("phone", String.class);
        nameAndTypes[3] = new FieldNameAndType("address", String.class);
        nameAndTypes[4] = new FieldNameAndType("longitude", String.class);
        nameAndTypes[5] = new FieldNameAndType("latitude", String.class);
        ParseExcelSheetToList<Person> parseExcelSheetToList = new ParseExcelSheetToList<Person>(inputStream,
                Person.class, nameAndTypes);
        List<Person> list = parseExcelSheetToList.getList();
        for (Person p :
                list) {
            System.out.println(p);
        }
        return;
    }

    @RequestMapping("/export")
    public void exportExcel(HttpServletResponse response) throws Exception {
        Person person = new Person();
        person.setId(1l);
        person.setName("Alice");
        person.setPhone("12345");
        person.setAddress("CN");
        person.setLatitude("12.33");
        person.setLongitude("50.44");

        Person person1 = new Person();
        person1.setId(2l);
        person1.setName("Catalina");
        person1.setPhone("666666");
        person1.setAddress("Uk");
        person1.setLatitude("2.33");
        person1.setLongitude("7.77");

        List<Person> people = new ArrayList<>();
        people.add(person);
        people.add(person1);
        String[] title = { "id","name","phone","address" ,"longitude","latitude"};
        String[] value = { "id","name","phone","address" ,"longitude","latitude"};
        String sheet = "person";
        String dir = DateUtils.currentDate();
        String fileName = "person";
        String f = exportExcelService.generateExcel(dir,fileName,people,title,value,sheet,false);
        File file = new File(f);
        InputStream input = FileUtils.openInputStream(file);
        byte[] data = IOUtils.toByteArray(input);

        String fn = URLEncoder.encode(file.getName(), "UTF-8");

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fn + "\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IOUtils.write(data, response.getOutputStream());
        IOUtils.closeQuietly(input);
    }

}
