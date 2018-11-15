package cn.kuroneko.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class WordUtils {
    /**
     * 直接使用poi进行导出，速度快，但是不靠谱，会丢失内容
     *
     * @param inputPath
     * @param outputPath
     * @throws Exception
     */
    public void convertWordToPdfByPoi(String inputPath, String outputPath) throws Exception {
        XWPFDocument document;
        try (InputStream doc = Files.newInputStream(Paths.get(inputPath))) {
            document = new XWPFDocument(doc);
        }
        PdfOptions options = PdfOptions.create();
        try (OutputStream out = Files.newOutputStream(Paths.get(outputPath))) {
            PdfConverter.getInstance().convert(document, out, options);
        }

    }

    /**
     * 使用LibreOffice来进行pdf的完美导出，速度比较慢，但是靠谱
     *
     * @param docxPath
     * @param outputPath
     * @return
     * @throws IOException
     */
    public boolean converWordToPdfByLibreOffice(String docxPath, String outputPath) throws IOException {
        File file = new File(docxPath);
        String path = file.getParent();
        try {
            String osName = System.getProperty("os.name");
            String command = "";
            //经过实际测试windows和linux使用的指令是一样的，但是网上有说不一样的，因此不删除注释仅供参考
            if (osName.contains("Windows")) {
//                需要配置一下环境变量
                command = "soffice --convert-to pdf:writer_pdf_Export  --outdir " + outputPath + " " + docxPath;
            } else {
//                command = "doc2pdf --output=" + path + File.separator + file.getName().replaceAll(".(?i)docx", ".pdf") + " " + docxPath;
                command = "soffice --convert-to pdf:writer_pdf_Export  --outdir " + outputPath + " " + docxPath;

            }
            log.info(command);
            String result = CommandExecute.executeCommand(command);
            if (result.equals("") || result.contains("writer_pdf_Export")) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return false;
    }

    /**
     * 通过文件路径来读取文件
     *
     * @param inputPath
     * @param outPath
     * @param params
     * @throws Exception
     */
    public void replaceWithPath(String inputPath, String outPath, Map<String, String> params) throws Exception {
        XWPFDocument doc = new XWPFDocument(POIXMLDocument.openPackage(inputPath));
//       检查输出文件的路径是否存在
        String dir = outPath.substring(0, outPath.indexOf("/") + 1);
        File outDir = new File(dir);
        if (!outDir.exists()) {
            outDir.mkdir();
        }
        wordReplace(doc, params);
        FileOutputStream fileOutputStream = new FileOutputStream(outPath);
//        这里生成的document无法直接使用PdfConverter转化成pdf，很奇怪
//        PdfOptions options = PdfOptions.create();
//        PdfConverter.getInstance().convert(doc, fileOutputStream, options);
        doc.write(fileOutputStream);
        close(fileOutputStream);
    }

    /**
     * 替换段落和表格中的特定字符串
     *
     * @param doc
     * @param params
     * @return
     * @throws Exception
     */
    private XWPFDocument wordReplace(XWPFDocument doc, Map<String, String> params) throws Exception {
        //替换段落中的指定文字
        Iterator<XWPFParagraph> paragraphsIterator = doc.getParagraphsIterator();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        while (paragraphsIterator.hasNext()) {
            XWPFParagraph next = paragraphsIterator.next();
            for (Map.Entry<String, String> entry :
                    entries) {
                List<XWPFRun> runs = next.getRuns();
                for (int i = 0; i < runs.size(); i++) {
                    String text = runs.get(i).getText(0);
                    System.out.println(text);
                    if (text != null && text.contains(entry.getKey())) {
                        String replace = text.replace(entry.getKey(), entry.getValue());
                        runs.get(i).setText(replace, 0);
                    }
                }
            }
        }
        Iterator<XWPFTable> tablesIterator = doc.getTablesIterator();
        while (tablesIterator.hasNext()) {
            XWPFTable next = tablesIterator.next();
            int numberOfRows = next.getNumberOfRows();
            for (int i = 0; i < numberOfRows; i++) {
                XWPFTableRow row = next.getRow(i);
                List<XWPFTableCell> tableCells = row.getTableCells();
                for (XWPFTableCell cell :
                        tableCells) {
//                    针对每个单元格内的内容进行分词，可能会导致替换不精确比如${test}，很有可能会被分词为'${','test','}'导致替换失败
//                    for (XWPFParagraph p:
//                         cell.getParagraphs()) {
//                        for (XWPFRun runs:
//                             p.getRuns()) {
//                            String text = runs.getText(0);
//                            System.out.println(text);
//                            for (Map.Entry<String, String> entry:
//                                    entries) {
//                                if(text != null && text.contains(entry.getKey())) {
//                                    String replace = text.replace(entry.getKey(), entry.getValue());
//                                    runs.setText(replace,0);
//                                }
//                                }
//                        }
//                    }

//                    不进行单元格内的分词，直接将单元格内所有字符串替换，但是缺点是单元格内只能有一个字符串
                    String cellText = cell.getText();
                    System.out.println(cellText);
                    for (Map.Entry<String, String> entry :
                            entries) {
                        if (cellText != null && cellText.contains(entry.getKey())) {
                            cellText = cellText.replace(entry.getKey(), entry.getValue());

                        }
                    }
//                    要先删除单元格内原有的内容，否则新的内容会追加到现有内容后面而不是替换
                    cell.removeParagraph(0);
                    cell.setText(cellText);

                }
            }
        }
        return doc;
    }

    /**
     * 关闭输出流
     *
     * @param os
     */
    private void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 关闭输入流
     *
     * @param os
     */
    private void close(InputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
