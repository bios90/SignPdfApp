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
import com.dimfcompany.signpdfapp.models.Model_Product;
import com.dimfcompany.signpdfapp.sqlite.CrudHelper;
import com.dimfcompany.signpdfapp.sqlite.LocalDatabase;
import com.dimfcompany.signpdfapp.sqlite.SqliteHelper;
import com.dimfcompany.signpdfapp.sqlite.WintecProvider;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
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
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

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

        void onShowCallSuccess(String fileName);

        void onErrorPdfCreation();
    }

    private PdfCreationCallback callback;
    public final Context context;
    public final FileManager fileManager;
    public final LocalDatabase localDatabase;

    Document document;
    PdfWriter writer;

    BaseFont globalReg;
    BaseFont globalBold;
    BaseFont globalItalic;

    BaseColor yellow;
    BaseColor trans;

    Chunk lineChunk;

    Font reg10;
    Font reg10Underline;
    Font reg8;
    Font bold10;
    Font bold8;
    Font italic10;

    boolean borderMode = true;
    boolean showMode;

    Model_Document model_document;


    public PdfCreator(Context context, FileManager fileManager, LocalDatabase localDatabase)
    {
        this.context = context;
        this.fileManager = fileManager;
        this.localDatabase = localDatabase;
    }


    public void createPdfAsync(final Model_Document model_document, boolean showMode, final PdfCreationCallback callback)
    {
        this.callback = callback;
        this.model_document = model_document;
        this.showMode = showMode;

        new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                createPdfSync(model_document);
            }
        }).start();
    }

    public void createPdfSync(Model_Document model_document)
    {
        this.model_document = model_document;

        try
        {
            initAll();

            File file = fileManager.createRandomNameFile(Constants.EXTANSION_PDF, Constants.FOLDER_CONTRACTS);
            document = new Document(PageSize.A4, 0, 0, 0, 0);
            FileOutputStream fos = new FileOutputStream(file);
            writer = PdfWriter.getInstance(document, fos);
            writer.setCompressionLevel(9);
            document.open();

            writer.setPageEvent(new PageBgHelper());

            document.add(new Paragraph(50, "\u00a0"));
            document.add(getTopTable());

            document.add(new Paragraph(476, "\u00a0"));
            document.add(getAdressTable());

            document.newPage();

            document.add(getProductsTable());
            document.add(getBottomInfoText());
            document.add(new Paragraph(14, "\u00a0"));
            document.add(getBottomSignature());

            document.close();

            if (showMode)
            {
                String fileName = FileManager.getFileName(file);
                callback.onShowCallSuccess(fileName);
            } else
            {
                makeSqliteInsert(file);
                new Handler(Looper.getMainLooper()).post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        callback.onSuccessPdfCreation();
                    }
                });
            }

        } catch (Exception e)
        {
            Log.e(TAG, "createPdfSync: Exception on creating pdf!" + e.getMessage() + " ||| Line  is " + e.getStackTrace()[0].getLineNumber());
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

            String fio = "";
            if (model_document.getFio() != null)
            {
                fio = model_document.getFio();
            }


            PdfPCell cellFio = getParCell(fio, reg10Underline, false, null, null, true, null);
            cellFio.setFixedHeight(34);
            cellFio.setVerticalAlignment(Element.ALIGN_BOTTOM);
            cellFio.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLarge.addCell(cellFio);
            tableLarge.addCell(getEmptyCell(1, 1, false));


            tableLarge.addCell(getEmptyCell(1, 1, false));
            Paragraph parAddress = new Paragraph("  " + model_document.getAdress());
            parAddress.setLeading(25, 0);
            parAddress.setFont(reg10Underline);
            parAddress.add(lineChunk);

            PdfPCell cellAdress = new PdfPCell();
            cellAdress.addElement(parAddress);
            cellAdress.setBorder(Rectangle.NO_BORDER);
            cellAdress.setFixedHeight(60);
            cellAdress.setPaddingLeft(34);
            cellAdress.setVerticalAlignment(Element.ALIGN_TOP);
            cellAdress.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLarge.addCell(cellAdress);
            tableLarge.addCell(getEmptyCell(1, 1, false));


            tableLarge.addCell(getEmptyCell(1, 1, false));
            PdfPCell cellPhone = getParCell("  " + model_document.getPhone(), reg10Underline, false, null, null, true, 44f);
            cellPhone.setPaddingTop(23);
            cellPhone.setVerticalAlignment(Element.ALIGN_TOP);
            cellPhone.setHorizontalAlignment(Element.ALIGN_LEFT);
            tableLarge.addCell(cellPhone);
            tableLarge.addCell(getEmptyCell(1, 1, false));

            PdfPCell cellEmpty = getEmptyCell(1, 3, false);
            cellEmpty.setFixedHeight(17);
            tableLarge.addCell(cellEmpty);

            PdfPTable tableMiniSign = new PdfPTable(3);
            tableMiniSign.setWidthPercentage(100);
            tableMiniSign.setWidths(new int[]{35, 47, 11});

            PdfPCell miniSignature = getSignupMiniCell(true);
            miniSignature.setFixedHeight(40);
            tableMiniSign.addCell(miniSignature);


            String formatedFio = "/  " + StringManager.formatFioForOthcet(model_document.getFio());

            PdfPCell cellSignFio = getParCell(formatedFio, reg10, false, null, null, true, 20f);
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
            Log.e(TAG, "getAdressTable: Exception on creating Client page " + e.getMessage());
            return null;
        }
    }


    private PdfPTable getProductsTable()
    {
        try
        {
            PdfPTable tableProducts = new PdfPTable(10);
            tableProducts.setWidthPercentage(90);
            tableProducts.setWidths(new int[]{182, 478, 188, 180, 210, 240, 210, 178, 202, 204});

            fillTableHeader(tableProducts);

            for (Model_Product product : model_document.getListOfProducts())
            {
                fillProductWithElement(tableProducts, product);
            }

            return tableProducts;

        } catch (Exception e)
        {
            Log.e(TAG, "Exception on cratingProducts Table " + e.getMessage());
            return null;
        }
    }

    private void fillTableHeader(PdfPTable tableProducts)
    {
        float padding = 4;
        tableProducts.addCell(getEmptyCell(1, 1, true));

        tableProducts.addCell(getParCell("Наименование материала", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Ширина", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Высота", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Цвет профиля", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Крепление", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Тип упр.", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Кол-во", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Цена за изделие", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Сумма", bold8, padding, true, null, null));
    }

    private void fillProductWithElement(PdfPTable tableProducts, Model_Product product)
    {
        float padding = 6;

        String id = String.valueOf(model_document.getListOfProducts().indexOf(product) + 1);

        String material = "";
        if (GlobalHelper.validateProductMaterial(product))
        {
            material = product.getMaterial().getName();
        }


        String width;
        if (product.getWidth() == 0)
        {
            width = "-";
        }
        else
        {
            width = StringManager.formatNum(product.getWidth(), true);
        }

        String height;

        if (product.getHeight() == 0)
        {
            height = "-";
        }
        else
        {
            height = StringManager.formatNum(product.getHeight(), true);
        }

        String color = "";
        if (GlobalHelper.validateProductColor(product))
        {
            color = product.getColor().getName();
        }

        String krep = "";
        if (GlobalHelper.validateProductKrep(product))
        {
            krep = product.getKrep().getName();
        }

        String control = "";
        if (GlobalHelper.validateProductControl(product))
        {
            control = product.getControl().getName();
        }

        String count = StringManager.formatNum(product.getCount(), false);
        String price = StringManager.formatNum(product.getPrice(), true);
        String sum = StringManager.formatNum(product.getSum(), true);


        tableProducts.addCell(getParCell(id, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(material, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(width, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(height, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(color, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(krep, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(control, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(count, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(price, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(sum, reg10, padding, true, null, null));

    }

    private PdfPTable getBottomInfoText()
    {
        try
        {
            float padding = 4;
            double postPaySum = model_document.getItogo_sum() - model_document.getPrepay();

            String sum = StringManager.formatNum(model_document.getSum(), true);
            String montage = StringManager.formatNum(model_document.getMontage(), true);
            String delivery = StringManager.formatNum(model_document.getDelivery(), true);
            String sale = StringManager.formatNum(model_document.getSale(), true);
            String itogoSum = StringManager.formatNum(model_document.getItogo_sum(), true);
            String prepay = StringManager.formatNum(model_document.getPrepay(), true);
            String postpay = StringManager.formatNum(postPaySum, true);
            String dopInfo = model_document.getDop_info();

            PdfPTable bottomTable = new PdfPTable(3);
            bottomTable.setWidthPercentage(90);
            bottomTable.setWidths(new int[]{1420, 360, 468});

            PdfPCell cellDopInfoHeader = getParCell("Дополнительная информация:", bold10, padding, true, null, null, Element.ALIGN_LEFT, null);
            cellDopInfoHeader.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.RIGHT);
            bottomTable.addCell(cellDopInfoHeader);
            bottomTable.addCell(getParCell("Cумма", bold10, padding, true, null, null, Element.ALIGN_LEFT, null));
            bottomTable.addCell(getParCell(sum, bold10, padding, true, null, null, Element.ALIGN_LEFT, null));

            PdfPCell cellDopInfo = getParCell(dopInfo, italic10, true, null, 6, Element.ALIGN_LEFT, Element.ALIGN_TOP);
            cellDopInfo.setPadding(12);
            cellDopInfo.setBorder(Rectangle.LEFT | Rectangle.BOTTOM | Rectangle.RIGHT);
            bottomTable.addCell(cellDopInfo);

            bottomTable.addCell(getParCell("Монтаж:", bold10, padding, true, null, null, Element.ALIGN_LEFT, null));
            bottomTable.addCell(getParCell(montage, bold10, padding, true, null, null, Element.ALIGN_LEFT, null));

            bottomTable.addCell(getParCell("Доставка:", bold10, padding, true, null, null, Element.ALIGN_LEFT, null));
            bottomTable.addCell(getParCell(delivery, bold10, padding, true, null, null, Element.ALIGN_LEFT, null));

            bottomTable.addCell(getParCell("Сумма скидки:", bold10, padding, true, null, null, Element.ALIGN_LEFT, null));
            bottomTable.addCell(getParCell(sale, bold10, padding, true, null, null, Element.ALIGN_LEFT, null));

            bottomTable.addCell(getParCell("ИТОГО:", bold10, padding, true, null, null, Element.ALIGN_LEFT, null));
            bottomTable.addCell(getParCell(itogoSum, bold10, padding, true, null, null, Element.ALIGN_LEFT, null));

            bottomTable.addCell(getParCell("Предоплата:", bold10, padding, true, null, null, Element.ALIGN_LEFT, null));
            bottomTable.addCell(getParCell(prepay, bold10, padding, true, null, null, Element.ALIGN_LEFT, null));

            bottomTable.addCell(getParCell("Доплата:", bold10, padding, true, null, null, Element.ALIGN_LEFT, null));
            bottomTable.addCell(getParCell(postpay, bold10, padding, true, null, null, Element.ALIGN_LEFT, null));

            return bottomTable;
        } catch (Exception e)
        {
            Log.e(TAG, "getBottomInfoText: Exception on BottomTable Creating " + e.getMessage());
            return null;
        }
    }

    private Element getBottomSignature()
    {
        try
        {
            float padding = 4;
            PdfPTable tableSign = new PdfPTable(2);
            tableSign.setWidthPercentage(80);
            tableSign.setWidths(new int[]{1080, 980});
            tableSign.addCell(getEmptyCell(1, 1, false));

            PdfPTable tableMini = new PdfPTable(2);
            tableMini.setWidthPercentage(100);
            tableMini.setWidths(new int[]{480, 500});

            tableMini.addCell(getParCell("Заказ составлен верно", bold8, false, null, null, Element.ALIGN_LEFT, null));
            PdfPCell cellSign = getSignupMiniCell(false);
            cellSign.setRowspan(2);
            cellSign.setBorder(Rectangle.NO_BORDER);
            tableMini.addCell(cellSign);

            PdfPCell cellSignText2 = getParCell("(Подпись заказчика)", reg8, false, null, null, Element.ALIGN_LEFT, Element.ALIGN_TOP);
            cellSignText2.setPaddingTop(4);
            tableMini.addCell(cellSignText2);


            tableSign.addCell(tableMini);

            return tableSign;
        } catch (Exception e)
        {
            Log.e(TAG, "getBottomSignature: Error on creating creating getBottomSignature");
            return null;
        }
    }


    private void initAll()
    {
        try
        {
            globalReg = BaseFont.createFont("assets/segreg.ttf", "Cp1251", BaseFont.EMBEDDED);
            globalBold = BaseFont.createFont("assets/segbold.ttf", "Cp1251", BaseFont.EMBEDDED);
            globalItalic = BaseFont.createFont("assets/segitalic.ttf", "Cp1251", BaseFont.EMBEDDED);

            reg10 = new Font(globalReg, 10);
            reg10Underline = new Font(globalReg, 10, Font.UNDERLINE);
            reg8 = new Font(globalReg, 8);
            bold10 = new Font(globalBold, 10);
            bold8 = new Font(globalBold, 8);
            italic10 = new Font(globalItalic, 10);

            yellow = new BaseColor(255, 192, 0);
            trans = new BaseColor(0, 0, 0, 0);

            LineSeparator line = new LineSeparator(0, 100, trans, Element.ALIGN_CENTER, -2.2f);
            lineChunk = new Chunk(line);
        } catch (Exception e)
        {
            Log.e(TAG, "initAll: Exception on init ALL");
        }
    }

    private PdfPCell getParCell(String text, Font font, float padding, boolean border, @Nullable Integer colSpawn, @Nullable Integer rowSpawn)
    {
        PdfPCell cell = getParCell(text, font, border, colSpawn, rowSpawn);
        cell.setPadding(padding);
        return cell;
    }

    private PdfPCell getParCell(String text, Font font, boolean border, @Nullable Integer colSpawn, @Nullable Integer rowSpawn, @Nullable Integer alignHor, @Nullable Integer alignVert)
    {
        PdfPCell cell = getParCell(text, font, border, colSpawn, rowSpawn);
        if (alignHor != null)
        {
            cell.setHorizontalAlignment(alignHor);
        }

        if (alignVert != null)
        {
            cell.setVerticalAlignment(alignVert);
        }

        return cell;
    }

    private PdfPCell getParCell(String text, Font font, float padding, boolean border, @Nullable Integer colSpawn, @Nullable Integer rowSpawn, @Nullable Integer alignHor, @Nullable Integer alignVert)
    {
        PdfPCell cell = getParCell(text, font, border, colSpawn, rowSpawn, alignHor, alignVert);
        cell.setPadding(padding);
        return cell;
    }

    private PdfPCell getParCell(String text, Font font, boolean border, @Nullable Integer colSpawn, @Nullable Integer rowSpawn, boolean lineSeparator, @Nullable Float offset)
    {
        Paragraph par = new Paragraph(text, font);
        par.add(lineChunk);


        PdfPCell cell = new PdfPCell(par);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(2f);
        if (offset != null)
        {
            cell.setPaddingLeft(offset);
            cell.setPaddingTop(2f);
            cell.setPaddingRight(2f);
            cell.setPaddingBottom(2f);
        }
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

    private PdfPCell getParCell(String text, Font font, boolean border, @Nullable Integer colSpawn, @Nullable Integer rowSpawn)
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

    private PdfPCell getSignupMiniCell(boolean borderBottom)
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
        if (borderBottom)
        {
            pdfPCell.setBorder(Rectangle.BOTTOM);
        } else
        {
            pdfPCell.setBorder(Rectangle.NO_BORDER);
        }
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
                Log.e(TAG, "getImageFromFiles: file is null");
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
        localDatabase.insertDocument(model_document);
    }

    private PdfPTable getFormOrderTable()
    {
        try
        {
            PdfPTable tableFormOrder = new PdfPTable(2);
            tableFormOrder.setWidths(new int[]{20, 40});
            tableFormOrder.setWidthPercentage(90);

            tableFormOrder.addCell(getEmptyCell(1, 1, false));

            String strOrderForm = "";
            if (model_document.getOrder_form() != null)
            {
                strOrderForm = model_document.getOrder_form();
            }

            PdfPCell cellOrderForm = getParCell(strOrderForm, reg10, false, null, null);
            cellOrderForm.setHorizontalAlignment(Element.ALIGN_LEFT);
            cellOrderForm.setFixedHeight(14);
            tableFormOrder.addCell(cellOrderForm);

            return tableFormOrder;
        } catch (Exception e)
        {
            Log.e(TAG, "getFormOrderTable: Exception on creatingOrder Table");
            return null;
        }
    }

    class PageBgHelper extends PdfPageEventHelper
    {
        @Override
        public void onStartPage(PdfWriter writer, Document document)
        {
            Log.e(TAG, "onStartPage: OnPage Start Begin");
            super.onStartPage(writer, document);
            try
            {
                document.add(getFormOrderTable());
                document.add(new Paragraph(32, "\u00a0"));

            } catch (Exception e)
            {
                Log.e(TAG, "onStartPage: Exception on page start");
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document)
        {
            Log.e(TAG, "onEndPage: ");
            try
            {
                int pageNumber;

                if (document.getPageNumber() == 1)
                {
                    pageNumber = 1;
                } else
                {
                    pageNumber = 2;
                }

                PdfContentByte cb = writer.getDirectContent();

                PdfReader reader = new PdfReader(fileManager.getTemplateStream());
                PdfImportedPage page = writer.getImportedPage(reader, pageNumber);

                cb.addTemplate(page, 0, 0);

                document.setMargins(0, 0, 52, 200);

            } catch (Exception e)
            {
                Log.e(TAG, "onEndPage: Exception on template adding");
            }
        }
    }
}
