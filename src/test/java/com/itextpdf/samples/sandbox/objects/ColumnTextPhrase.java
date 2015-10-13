/**
 * This example was written by Bruno Lowagie in answer to the following question:
 * http://stackoverflow.com/questions/27989754/itext-failure-with-adding-elements-with-5-5-4
 */
package com.itextpdf.samples.sandbox.objects;

import com.itextpdf.basics.font.FontConstants;
import com.itextpdf.core.font.PdfFont;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfReader;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.testutils.annotations.type.SampleTest;
import com.itextpdf.model.Document;
import com.itextpdf.model.element.Paragraph;
import com.itextpdf.samples.GenericTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class ColumnTextPhrase extends GenericTest {
    public static final String DEST = "./target/test/resources/sandbox/objects/column_text_phrase.pdf";
    public static final String SRC = "./src/test/resources/sandbox/objects/hello.pdf";

    public static void main(String[] args) throws Exception {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        new ColumnTextPhrase().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(new FileInputStream(SRC)),
                new PdfWriter(new FileOutputStream(dest)));
        Document doc = new Document(pdfDoc);
        PdfFont f = PdfFont.createStandardFont(pdfDoc, FontConstants.HELVETICA);
        Paragraph pz = new Paragraph("Hello World!").setFont(f).setFixedLeading(20);
        doc.add(pz.setWidth(200 - 120).setHeight(600 - 200).setMarginLeft(120));
        f = PdfFont.createFont(pdfDoc, FontConstants.HELVETICA_BOLD, "Cp1252", true);
        pz = new Paragraph("Hello World!").setFont(f).setFontSize(13);
        doc.add(pz.setWidth(200 - 120).setHeight(700 - 48).setMarginLeft(120));
        doc.close();
    }
}