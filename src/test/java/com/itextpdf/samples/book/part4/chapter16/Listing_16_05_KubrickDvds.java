package com.itextpdf.samples.book.part4.chapter16;

import com.itextpdf.basics.geom.Rectangle;
import com.itextpdf.canvas.PdfCanvas;
import com.itextpdf.core.pdf.PdfArray;
import com.itextpdf.core.pdf.PdfDictionary;
import com.itextpdf.core.pdf.PdfDocument;
import com.itextpdf.core.pdf.PdfName;
import com.itextpdf.core.pdf.PdfReader;
import com.itextpdf.core.pdf.PdfString;
import com.itextpdf.core.pdf.PdfWriter;
import com.itextpdf.core.pdf.annot.PdfFileAttachmentAnnotation;
import com.itextpdf.core.pdf.filespec.PdfFileSpec;
import com.itextpdf.core.testutils.annotations.type.SampleTest;
import com.itextpdf.model.Document;
import com.itextpdf.model.element.List;
import com.itextpdf.model.element.ListItem;
import com.itextpdf.model.element.Paragraph;
import com.itextpdf.model.element.Text;
import com.itextpdf.model.renderer.ListItemRenderer;
import com.itextpdf.samples.GenericTest;
import com.lowagie.database.DatabaseConnection;
import com.lowagie.database.HsqldbConnection;
import com.lowagie.filmfestival.Movie;
import com.lowagie.filmfestival.PojoFactory;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;

import org.junit.experimental.categories.Category;

@Category(SampleTest.class)
public class Listing_16_05_KubrickDvds extends GenericTest {
    public static final String DEST = "./target/test/resources/book/part4/chapter16/Listing_16_05_KubrickDvds.pdf";
    public static final String RESOURCE
            = "./src/test/resources/book/part4/chapter16/posters/%s.jpg";
    public static final String PATH = "./target/test/resources/book/part4/chapter16/%s";

    public static void main(String args[]) throws Exception {
        new Listing_16_05_KubrickDvds().manipulatePdf(DEST);
    }

    public void manipulatePdf(String dest) throws Exception {
        FileOutputStream os = new FileOutputStream(dest);
        os.write(createPdf());
        os.flush();
        os.close();
        extractAttachments(dest);
    }

    /**
     * Extracts attachments from an existing PDF.
     *
     * @param src the path to the existing PDF
     * @throws IOException
     */
    public void extractAttachments(String src) throws IOException {
        PdfDocument pdfDoc = new PdfDocument(new PdfReader(src));
        PdfReader reader = new PdfReader(src);
        PdfArray array;
        PdfDictionary annot;
        PdfDictionary fs;
        PdfDictionary refs;
        for (int i = 1; i <= pdfDoc.getNumOfPages(); i++) {
            array = pdfDoc.getPage(i).getPdfObject().getAsArray(PdfName.Annots);
            if (array == null) continue;
            for (int j = 0; j < array.size(); j++) {
                annot = array.getAsDictionary(j);
                if (PdfName.FileAttachment.equals(annot.getAsName(PdfName.Subtype))) {
                    fs = annot.getAsDictionary(PdfName.FS);
                    refs = fs.getAsDictionary(PdfName.EF);
                    for (PdfName name : refs.keySet()) {
                        FileOutputStream fos
                                = new FileOutputStream(String.format(PATH, fs.getAsString(name).toString()));
                        fos.write(refs.getAsStream(name).getBytes());
                        fos.flush();
                        fos.close();
                    }
                }
            }
        }
        reader.close();
    }

    /**
     * Creates the PDF.
     *
     * @return the bytes of a PDF file.
     * @throws IOException
     * @throws SQLException
     */
    public byte[] createPdf() throws IOException, SQLException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfDocument pdfDoc = new PdfDocument(new PdfWriter(baos));
        Document doc = new Document(pdfDoc);
        doc.add(new Paragraph("This is a list of Kubrick movies available in DVD stores."));
        DatabaseConnection connection = new HsqldbConnection("filmfestival");
        Set<Movie> movies = new TreeSet<Movie>();
        movies.addAll(PojoFactory.getMovies(connection, 1));
        movies.addAll(PojoFactory.getMovies(connection, 4));
        ListItem item;
        Text text;
        List list = new List();
        for (Movie movie : movies) {
            item = new ListItem(movie.getMovieTitle(false));
            item.setNextRenderer(new AnnotatedListItemRenderer(item,
                    String.format(RESOURCE, movie.getImdb()),
                    String.format("%s.jpg", movie.getImdb()),
                    movie.getMovieTitle(false)));
            list.add(item);
        }
        doc.add(list);

        doc.close();
        connection.close();

        return baos.toByteArray();
    }


    private class AnnotatedListItemRenderer extends ListItemRenderer {
        private String fileDisplay;
        private String filePath;
        private String fileTitle;

        public AnnotatedListItemRenderer(ListItem modelElement, String path, String display, String title) {
            super(modelElement);
            fileDisplay = display;
            filePath = path;
            fileTitle = title;
        }

        @Override
        public void draw(PdfDocument document, PdfCanvas canvas) {
            super.draw(document, canvas);
            PdfFileSpec fs = null;
            try {
                fs = PdfFileSpec.createEmbeddedFileSpec(document, filePath, null, fileDisplay, null, null, false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Rectangle rect = new Rectangle(getOccupiedAreaBBox().getRight(),
                    getOccupiedAreaBBox().getBottom(), 10, 10);
            PdfFileAttachmentAnnotation annotation =
                    new PdfFileAttachmentAnnotation(document, rect, fs);
            annotation.setIconName(PdfName.Paperclip);
            annotation.setContents(fileTitle);
            annotation.put(PdfName.Name, new PdfString("Paperclip"));
            document.getLastPage().addAnnotation(annotation);
        }
    }
}