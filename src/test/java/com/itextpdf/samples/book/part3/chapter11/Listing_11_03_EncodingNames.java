package com.itextpdf.samples.book.part3.chapter11;

import com.itextpdf.basics.font.PdfEncodings;
import com.itextpdf.core.font.PdfFont;
import com.itextpdf.core.font.PdfFontFactory;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.test.annotations.type.SampleTest;
import com.itextpdf.model.Document;
import com.itextpdf.model.element.AreaBreak;
import com.itextpdf.model.element.Paragraph;
import com.itextpdf.samples.GenericTest;

import java.io.IOException;

import org.junit.Ignore;
import org.junit.experimental.categories.Category;

@Ignore
@Category(SampleTest.class)
public class Listing_11_03_EncodingNames extends GenericTest {
    public static final String DEST
            = "./target/test/resources/book/part3/chapter11/Listing_11_03_EncodingNames.pdf";
    public static final String[] FONT = {
            //"c:/windows/fonts/ARBLI__.TTF",
            "./src/test/resources/book/part3/chapter11/Puritan2.otf",
            /*"c:/windows/fonts/arialbd.ttf"*/"./src/test/resources/font/FreeSans.ttf"
    };

    public static void main(String[] agrs) throws Exception {
        new Listing_11_03_EncodingNames().manipulatePdf(DEST);
    }

    @Override
    protected void manipulatePdf(String dest) throws Exception {
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(dest));
        Document doc = new Document(pdfDoc);
        showEncodings(doc, FONT[0]);
        showEncodings(doc, FONT[1]);
        doc.add(new AreaBreak());
        showEncodings(doc, FONT[2]);
        doc.close();
    }

    public void showEncodings(Document doc, String fontConstant) throws IOException {
        PdfFont font = PdfFontFactory.createFont(fontConstant, PdfEncodings.WINANSI, true);

        // TODO No getPostscriptFontName
        doc.add(new Paragraph("PostScript name: " + font.getFontProgram().getFontNames().getFontName()));
        doc.add(new Paragraph("Available code pages:"));
        // TODO No getCodePagesSupported
        // String[] encoding = font.getCodePagesSupported();
//        for (int i = 0; i < encoding.length; i++) {
//            doc.add(new Paragraph("encoding[" + i + "] = " + encoding[i]));
//        }
        doc.add(new Paragraph("\n"));
    }
}
