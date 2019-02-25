package crystal.scrumify.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PdfCreator {

    public static final String TAG = PdfCreator.class.getSimpleName();
    
    public static void createPdf(String destFile, Context mContext, float temperature, String location, String notulen) {

        if (new File(destFile).exists()) {
            new File(destFile).delete();
        }

        try {
            /**
             * Creating Document
             */
            Document document = new Document();

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(destFile));

            // Open to write
            document.open();

            // Document Settings
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Scrumify");
            document.addCreator("Scrumify");

            /***
             * Variables for further use....
             */
            BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            float mHeadingFontSize = 20.0f;
            float mValueFontSize = 16.0f;

            /**
             * How to USE FONT....
             */
            BaseFont urName = BaseFont.createFont("assets/fonts/brandon_medium.otf", "UTF-8", BaseFont.EMBEDDED);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));

            // Title Daily Meeting...
            // Adding Title....
            Font mOrderDetailsTitleFont = new Font(urName, 36.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDetailsTitleChunk = new Chunk("Daily Meeting", mOrderDetailsTitleFont);
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsTitleParagraph);

            // Fields of Order Details...
            // Adding Chunks for Title and value
            String dateNotulen = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            String temperatureNotulen = "(" + String.valueOf(temperature) + " C)";
            Font mOrderIdFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Chunk mOrderIdChunk = new Chunk("Date : " + dateNotulen + " " + temperatureNotulen, mOrderIdFont);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);

            Chunk mLocationChunk = new Chunk("Location : " + location, mOrderIdFont);
            Paragraph mLocationParagraph = new Paragraph(mLocationChunk);
            document.add(mLocationParagraph);

            // Adding Line Breakable Space....
            document.add(new Paragraph(""));
            // Adding Horizontal Line...
            document.add(new Chunk(lineSeparator));
            // Adding Line Breakable Space....
            document.add(new Paragraph(""));

            Font mNotulenIdFont = new Font(urName, mHeadingFontSize, Font.NORMAL, mColorAccent);
            Chunk mNotulenIdChunk = new Chunk("Minutes of Daily Meeting : ", mNotulenIdFont);
            Paragraph mNotulenIdParagraph = new Paragraph(mNotulenIdChunk);
            document.add(mNotulenIdParagraph);

            Font mNotulenTextIdFont = new Font(urName, mValueFontSize, Font.NORMAL, BaseColor.BLACK);
            Chunk mNotulenTextIdChunk = new Chunk(notulen, mNotulenTextIdFont);
            Paragraph mNotulenTextIdParagraph = new Paragraph(mNotulenTextIdChunk);
            document.add(mNotulenTextIdParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            document.close();

            Log.d(TAG, destFile);
            Toast.makeText(mContext, "Created... :)", Toast.LENGTH_SHORT).show();

            FileUtils.openFile(mContext, new File(destFile));

        } catch (IOException | DocumentException ie) {
            Log.e(TAG,"createPdf: Error " + ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(mContext, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }
}
