import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import javax.imageio.ImageIO;
import java.io.*;
import java.util.LinkedList;

/**
 * Created by irek on 25.03.2017.
 */
public class DocCreator {
    Document document = new Document(PageSize.A7);
    File f;
    OutputStream outputStream;
    public DocCreator(LinkedList<Badge> badges, String region) {


            f = new File(".\\"+region+".zip");
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            outputStream = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            PdfWriter.getInstance(document, outputStream);
        } catch (DocumentException e) {
            e.printStackTrace();
        }


        document.open();
        //document.add(new Paragraph("Hello world, " + "this is a test pdf file."));
        for (Badge badge: badges) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            try {
                ImageIO.write(badge.getBadge(), "png", baos);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Image iTextImage = null;
            try {
                iTextImage = Image.getInstance(baos.toByteArray());
            } catch (BadElementException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            iTextImage.scaleAbsolute(PageSize.A7);
            iTextImage.setAbsolutePosition(0,0);
            try {
                document.add(iTextImage);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
            document.newPage();
        }
        document.close();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Pdf created successfully.");
    }

}
