package com.dimfcompany.signpdfapp.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.sqlite.CrudHelper;
import com.dimfcompany.signpdfapp.sqlite.SqliteHelper;
import com.dimfcompany.signpdfapp.sqlite.WintecProvider;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class PdfCreator
{
    private static final String TAG = "PdfCreator";

    public interface PdfCreationCallback
    {
        void onSuccessPdfCreation();
        void onErrorPdfCreation();
    }

    private PdfCreationCallback callback;
    public final Context context;
    public final FileManager fileManager;
    public final CrudHelper crudHelper;

    Document document;
    PdfWriter writer;

    BaseFont globalReg;
    BaseFont globalBold;

    BaseColor yellow;

    Font reg10;


    Font bold10;

    Model_Document model_document;


    public PdfCreator(Context context, FileManager fileManager, CrudHelper crudHelper)
    {
        this.context = context;
        this.fileManager = fileManager;
        this.crudHelper = crudHelper;
    }


    public void createPdfAsync(final Model_Document model_document, final PdfCreationCallback callback)
    {
        this.callback = callback;
        this.model_document = model_document;

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                createPdfSync(model_document);
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        callback.onSuccessPdfCreation();
                    }
                });
            }
        }).start();
    }

    public void createPdfSync(Model_Document model_document)
    {
        this.model_document = model_document;

        try
        {
            initAll();

            File file = fileManager.createRandomNameFile(Constants.EXTANSION_PDF);
            document = new Document(PageSize.A4, 0, 0, 0, 0);
            FileOutputStream fos = new FileOutputStream(file);
            writer = PdfWriter.getInstance(document, fos);
            writer.setCompressionLevel(9);
            document.open();
            PdfContentByte cb = writer.getDirectContent();

            PdfReader reader = new PdfReader(fileManager.getTemplateStream());
            PdfImportedPage page = writer.getImportedPage(reader, 1);

            document.newPage();
            cb.addTemplate(page, 0, 0);

            document.add(new Paragraph(50, "\u00a0"));
            document.add(getTopTable());

            document.add(new Paragraph(476, "\u00a0"));
            document.add(getAdressTable());

            document.close();

            makeSqliteInsert(file);

        } catch (Exception e)
        {
            Log.e(TAG, "createPdfSync: Exception on creating pdf!");
            if (callback != null)
            {
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        callback.onErrorPdfCreation();
                    }
                });
            }
        }
    }


    private PdfPTable getTopTable()
    {
        try
        {
            PdfPTable tableLarge = new PdfPTable(3);
            tableLarge.setWidthPercentage(100);
            tableLarge.setWidths(new int[]{38, 21, 4});
            tableLarge.addCell(getEmptyCell(1, 1, false));


            PdfPTable tableInfo = new PdfPTable(4);
            tableInfo.setWidthPercentage(100);
            tableInfo.setWidths(new int[]{81, 41, 36, 50});

            PdfPCell topText = getParCell("Договор оказания услуг №77-777", bold10, true, 4, null);
            tableInfo.addCell(topText);

            PdfPCell cellMoscow = getParCell("Москва", reg10, true, null, null);
            PdfPCell cellSpb = getParCell("СПБ", reg10, true, null, null);
            PdfPCell cellSochi = getParCell("Сочи", reg10, true, null, null);
            PdfPCell cellSamara = getParCell("Самара", reg10, true, null, null);

            switch (model_document.getCity())
            {
                case 0:
                    cellMoscow.setBackgroundColor(yellow);
                    break;
                case 1:
                    cellSpb.setBackgroundColor(yellow);
                    break;
                case 2:
                    cellSochi.setBackgroundColor(yellow);
                    break;
                case 3:
                    cellSamara.setBackgroundColor(yellow);
                    break;
            }

            tableInfo.addCell(cellMoscow);
            tableInfo.addCell(cellSpb);
            tableInfo.addCell(cellSochi);
            tableInfo.addCell(cellSamara);

            PdfPCell cellDate = getParCell("ДАТА:" + GlobalHelper.getCurrentTimeFullString(), bold10, true, 4, null);
            tableInfo.addCell(cellDate);

            tableLarge.addCell(new PdfPCell(tableInfo));
            tableLarge.addCell(getEmptyCell(1, 1, false));

            return tableLarge;

        } catch (Exception e)
        {
            Log.e(TAG, "getTopTable: Exception on creating Top Table");
            return null;
        }
    }


    private PdfPTable getAdressTable()
    {
        try
        {
            PdfPTable tableLarge = new PdfPTable(3);
            tableLarge.setWidthPercentage(100);
            tableLarge.setWidths(new int[]{205, 189, 19});
            tableLarge.addCell(getEmptyCell(1, 1, false));


            PdfPCell cellFio = getParCell(model_document.getFio(), reg10, false, null, null);
            cellFio.setPaddingTop(21);
            cellFio.setPaddingLeft(6);
            cellFio.setVerticalAlignment(Element.ALIGN_TOP);
            cellFio.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLarge.addCell(cellFio);
            tableLarge.addCell(getEmptyCell(1, 1, false));


            tableLarge.addCell(getEmptyCell(1, 1, false));
            Paragraph parAddress = new Paragraph("             " + model_document.getAdress());
            parAddress.setLeading(25.4f, 0);
            parAddress.setFont(reg10);
            PdfPCell cellAdress = new PdfPCell();
            cellAdress.addElement(parAddress);
            cellAdress.setBorder(Rectangle.NO_BORDER);
            cellAdress.setFixedHeight(60);
            cellAdress.setPaddingLeft(6);
            cellAdress.setVerticalAlignment(Element.ALIGN_TOP);
            cellAdress.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLarge.addCell(cellAdress);
            tableLarge.addCell(getEmptyCell(1, 1, false));


            tableLarge.addCell(getEmptyCell(1, 1, false));
            PdfPCell cellPhone = getParCell("                  " + model_document.getPhone(), reg10, false, null, null);
            cellPhone.setPaddingTop(23);
            cellPhone.setVerticalAlignment(Element.ALIGN_TOP);
            cellPhone.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLarge.addCell(cellPhone);
            tableLarge.addCell(getEmptyCell(1, 1, false));

            PdfPCell cellEmpty = getEmptyCell(1, 3, false);
            cellEmpty.setFixedHeight(18);
            tableLarge.addCell(cellEmpty);

            PdfPTable tableMiniSign = new PdfPTable(3);
            tableMiniSign.setWidthPercentage(100);
            tableMiniSign.setWidths(new int[]{35, 47, 11});

            PdfPCell miniSignature = getSignupMiniCell();
            miniSignature.setFixedHeight(40);
            tableMiniSign.addCell(miniSignature);


            String formatedFio = "/  " + StringManager.formatFioForOthcet(model_document.getFio());

            Log.e(TAG, "getAdressTable: Formated Fio is "+formatedFio );

            PdfPCell cellSignFio = getParCell(formatedFio, reg10, false, null, null);
            cellSignFio.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellSignFio.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cellFio.setPaddingBottom(2);
            tableMiniSign.addCell(cellSignFio);
            tableMiniSign.addCell(getEmptyCell(1, 1, false));


            PdfPCell cellForMiniTable = new PdfPCell(tableMiniSign);
            cellForMiniTable.setPadding(0);
            cellForMiniTable.setBorder(Rectangle.NO_BORDER);
            tableLarge.addCell(getEmptyCell(1, 1, false));
            tableLarge.addCell(cellForMiniTable);
            tableLarge.addCell(getEmptyCell(1, 1, false));


            return tableLarge;
        } catch (Exception e)
        {
            Log.e(TAG, "getAdressTable: Exception on creating Client page");
            return null;
        }
    }

    private void initAll()
    {
        try
        {
            globalReg = BaseFont.createFont("assets/segreg.ttf", "Cp1251", BaseFont.EMBEDDED);
            globalBold = BaseFont.createFont("assets/segbold.ttf", "Cp1251", BaseFont.EMBEDDED);

            reg10 = new Font(globalReg, 10);
            bold10 = new Font(globalBold, 10);

            yellow = new BaseColor(255, 192, 0);
        } catch (Exception e)
        {
            Log.e(TAG, "initAll: Exception on init ALL");
        }
    }

    PdfPCell getParCell(String text, Font font, boolean border, @Nullable Integer colSpawn, @Nullable Integer rowSpawn)
    {
        PdfPCell cell = new PdfPCell(new Paragraph(text, font));
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        if (border)
        {
            cell.setBorder(Rectangle.BOX);
        } else
        {
            cell.setBorder(Rectangle.NO_BORDER);
        }

        if (colSpawn != null)
        {
            cell.setColspan(colSpawn);
        }

        if (rowSpawn != null)
        {
            cell.setRowspan(rowSpawn);
        }

        return cell;
    }

    private PdfPCell getEmptyCell(int rowSpawn, int colSpawn, boolean border)
    {
        PdfPCell emptyCell = new PdfPCell();
        emptyCell.setRowspan(rowSpawn);
        emptyCell.setColspan(colSpawn);
        if (border)
        {
            emptyCell.setBorder(Rectangle.BOX);
        } else
        {
            emptyCell.setBorder(Rectangle.NO_BORDER);
        }

        return emptyCell;
    }

    private PdfPCell getSignupMiniCell()
    {
        Image image = null;

        if (model_document.getSignature_file_name() != null)
        {
            image = getImageFromFiles(model_document.getSignature_file_name());
        }
        if (image == null)
        {
            image = getImageFromAsset("defaultsign.png");
        }


        PdfPCell pdfPCell = new PdfPCell(image, true);
        pdfPCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfPCell.setBorder(Rectangle.BOTTOM);
        pdfPCell.setPaddingBottom(2);
        pdfPCell.setPaddingLeft(0);

        return pdfPCell;
    }

    private Image getImageFromFiles(File img)
    {
        try
        {
            if (img == null || !img.exists())
            {
                return null;
            }

            Bitmap bitmap = ImageManager.newResizedBitmap(img, 400);
            ByteArrayOutputStream mediaStream = new ByteArrayOutputStream();

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, mediaStream);

            Image image = Image.getInstance(mediaStream.toByteArray());

            mediaStream.flush();
            mediaStream.close();
            mediaStream = null;
            bitmap = null;
            return image;
        } catch (Exception e)
        {
            Log.e(TAG, "getImageFromFiles: Exception on getting image file");
            return null;
        }
    }


    private Image getImageFromFiles(String fileName)
    {
        File file = fileManager.getFileFromTemp(fileName, null);
        return getImageFromFiles(file);
    }


    private Image getImageFromAsset(String fileName)
    {
        try
        {
            InputStream inputStream = context.getAssets().open(fileName);
            Bitmap bmp = BitmapFactory.decodeStream(inputStream);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();

            bmp.compress(Bitmap.CompressFormat.JPEG, 90, stream);

            Image image = Image.getInstance(stream.toByteArray());

            inputStream.close();
            stream.flush();
            stream.close();

            bmp = null;
            inputStream = null;
            stream = null;
            return image;
        } catch (Exception e)
        {
            Log.e(TAG, "initImageFromAsset: Exception on getting image from asset");
            return null;
        }
    }

    private void makeSqliteInsert(File file)
    {
        String fileName = FileManager.getFileName(file);
        model_document.setPdf_file_name(fileName);

        Integer insertedId = crudHelper.insertDocument(model_document);

        Cursor testCursor = context.getContentResolver().query(WintecProvider.CONTENT_URI_TABLE_DOCUMENTS, null, null, null, null);

        Log.e(TAG, "makeSqliteInsert: Date To insert is "+model_document.getDate() );
        while (testCursor.moveToNext())
        {
            Log.e(TAG, "test Adress is: " + testCursor.getString(testCursor.getColumnIndex(SqliteHelper.ColumnsDocuments.ADRESS)));
        }

        Log.e(TAG, "makeSqliteInsert: Finished Inserting all to Sqlite");
    }

    private void testOpenFile(File file)
    {
        Intent target = new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(file), "application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        Intent intent = Intent.createChooser(target, "Open File");
        try
        {
            context.startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            Log.e(TAG, "testOpenFile: Activity not found Exception" );
        }
    }
}
