package com.itextpdf.samples;

import com.itextpdf.canvas.PdfCanvas;
import com.itextpdf.canvas.color.DeviceGray;
import com.itextpdf.basics.PdfException;
import com.itextpdf.core.geom.Rectangle;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.xobject.PdfFormXObject;
import com.itextpdf.core.pdf.PdfPage;
import com.itextpdf.core.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class Listing_03_26_MoviePosters {

    static private final String RESULT = "./result.pdf";

    public static void main(String args[]) throws IOException, PdfException {

        //Initialize writer
        FileOutputStream fos = new FileOutputStream(RESULT);
        PdfWriter writer = new PdfWriter(fos);

        //Initialize document and add page
        PdfDocument pdfDoc = new PdfDocument(writer);
        PdfPage page = pdfDoc.addNewPage();

        //Initialize form XObject and write to it
        PdfFormXObject xObj = new PdfFormXObject(pdfDoc, new Rectangle(8, 8, 579, 68));
        PdfCanvas celluloid = new PdfCanvas(xObj);
        celluloid.rectangle(8, 8, 579, 68);
        for (float f = 8.25f; f < 581; f += 6.5f) {
            celluloid.roundRectangle(f, 8.5f, 6, 3, 1.5f).roundRectangle(f, 72.5f, 6, 3, 1.5f);
        }
        celluloid.setFillColor(new DeviceGray(0.1f)).eoFill();
        celluloid.release();

        //Flush xObj to output file
        xObj.flush();

        //Add XObjects to page canvas
        PdfCanvas canvas = new PdfCanvas(page);
        for (int i = 0; i < 10; i++) {
            canvas.addXObject(xObj, 0, i * 84.2f);
        }
        canvas.release();

        //Close document
        pdfDoc.close();

    }


}
