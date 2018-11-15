package cn.kuroneko.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Created by fuxiao
 * on 2017/6/1.
 * email: fuxiao9@crv.com.cn
 */
public class PdfUtils {

    public static void savePdf(OutputStream out, String html, String fontPath) {
        Document document = new Document(PageSize.A4, 5, 5, 5, 5);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, out);
            document.open();
            BaseFont baseCnFont = BaseFont.createFont(fontPath, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new ByteArrayInputStream(html.getBytes()), null,
                    Charset.forName("UTF-8"), new FontProvider() {
                        @Override
                        public boolean isRegistered(String s) {
                            return false;
                        }

                        @Override
                        public Font getFont(String s, String s1, boolean b, float v, int i, BaseColor baseColor) {
                            return new Font(baseCnFont, 10);
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }
}
