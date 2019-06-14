package com.dimfcompany.signpdfapp.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.dimfcompany.signpdfapp.base.Constants;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_Price_Element;
import com.dimfcompany.signpdfapp.models.Model_Product;
import com.dimfcompany.signpdfapp.local_db.raw.LocalDatabase;
import com.dimfcompany.signpdfapp.utils.custom_classes.CustomDottedLineSeparator;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
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
import java.io.IOException;
import java.io.InputStream;

public class PdfCreator
{
    private static final String TAG = "PdfCreator";

    public interface PdfCreationCallback
    {
        void onSuccessPdfCreation(Model_Document model_document);

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
    BaseFont globalPt;
    BaseFont globalTimesNewRoman;

    BaseColor yellow;
    BaseColor trans;

    Chunk lineChunk;

    Font reg10;
    Font reg10Underline;
    Font reg8;
    Font bold10;
    Font bold8;
    Font italic10;
    Font timesNewRoman10;

    Font pt46;
    Font pt36;
    Font pt30;
    Font pt28;
    Font pt26;
    Font pt24;

    boolean borderMode = false;
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

    public void createPdfSync(final Model_Document model_document)
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
            document.add(new Paragraph(14, "\u00a0"));
            document.add(getBottomStamp());

            document.close();

            if (showMode)
            {
                String fileName = FileManager.getFileName(file);
                callback.onShowCallSuccess(fileName);
                return;
            }

            makeCheck(model_document);

            if (model_document.getVaucher() != null)
            {
                makeVaucher();
            }


            String newMainFileName = getRenamedFile(false,false);
            newMainFileName += ".pdf";
            if (FileManager.rename(file, newMainFileName, null)) ;
            {
                file = fileManager.getFileFromTemp(newMainFileName, Constants.FOLDER_CONTRACTS, null);
            }


            String fileName = FileManager.getFileName(file);
            Log.e(TAG, "createPdfSync: Main file name is " + fileName);
            model_document.setPdf_file_name(fileName);

            new Handler(Looper.getMainLooper()).post(new Runnable()
            {
                @Override
                public void run()
                {
                    callback.onSuccessPdfCreation(model_document);
                }
            });


        } catch (Exception e)
        {
            e.printStackTrace();
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

    private void makeVaucher() throws IOException, DocumentException
    {
        float totalHeight = 0;
        totalHeight += 40;

        PdfPTable tableVaucherTop = getCheckAndVaucherTopTable();
        PdfPTable tableVaucherMiddle = getVaucherTable();
        PdfPTable tableVaycherBottom = getCheckBottomTable();

        totalHeight += tableVaucherTop.getTotalHeight();
        totalHeight += tableVaucherMiddle.getTotalHeight();
        totalHeight += tableVaycherBottom.getTotalHeight();

        File fileVaucher = fileManager.createRandomNameFile(Constants.EXTANSION_PDF, Constants.FOLDER_VAUCHERS);
        document = new Document(new Rectangle(298, totalHeight), 20, 20, 20, 20);
        FileOutputStream fos = new FileOutputStream(fileVaucher);
        writer = PdfWriter.getInstance(document, fos);
        writer.setCompressionLevel(9);
        document.open();

        document.add(tableVaucherTop);
        document.add(tableVaucherMiddle);
        document.add(tableVaycherBottom);

        document.close();

        String newVaucherFileName = getRenamedFile(false,true);
        newVaucherFileName += ".pdf";
        if (FileManager.rename(fileVaucher, newVaucherFileName, null)) ;
        {
            Log.e(TAG, "makeCheck: renamed ok");
            fileVaucher = fileManager.getFileFromTemp(newVaucherFileName, Constants.FOLDER_VAUCHERS, null);
        }

        String vaucher_file_name = FileManager.getFileName(fileVaucher);
        model_document.setVaucher_file_name(vaucher_file_name);
    }

    private void makeCheck(Model_Document model_document) throws DocumentException, IOException
    {
        float totalHeight = 0;
        totalHeight += 40;

        PdfPTable tableCheckTop = getCheckAndVaucherTopTable();
        PdfPTable tableProducts = getCheckProductsTable();
        PdfPTable tableBottomInfo = getTableBottomInfoTable();
        PdfPTable tableCheckBottom = getCheckBottomTable();

        totalHeight += tableCheckTop.getTotalHeight();
        totalHeight += tableProducts.getTotalHeight();
        totalHeight += tableBottomInfo.getTotalHeight();
        totalHeight += tableCheckBottom.getTotalHeight();

        File fileCheck = fileManager.createRandomNameFile(Constants.EXTANSION_PDF, Constants.FOLDER_CHECKS);
        document = new Document(new Rectangle(298, totalHeight), 20, 20, 20, 20);
        FileOutputStream fos = new FileOutputStream(fileCheck);
        writer = PdfWriter.getInstance(document, fos);
        writer.setCompressionLevel(9);
        document.open();

        document.add(tableCheckTop);
        document.add(tableProducts);
        document.add(tableBottomInfo);
        document.add(tableCheckBottom);

        document.close();

        String newChekFileName = getRenamedFile(true,false);
        newChekFileName += ".pdf";
        if (FileManager.rename(fileCheck, newChekFileName, null)) ;
        {
            Log.e(TAG, "makeCheck: renamed ok");
            fileCheck = fileManager.getFileFromTemp(newChekFileName, Constants.FOLDER_CHECKS, null);
        }
        ;


        String check_file_name = FileManager.getFileName(fileCheck);
        Log.e(TAG, "makeCheck: CheckFile NAme is " + check_file_name);
        model_document.setCheck_file_name(check_file_name);
    }

    private PdfPTable getVaucherTable() throws DocumentException
    {
        float padding = 4f;
        PdfPTable tableVaucher = new PdfPTable(4);
        tableVaucher.setTotalWidth(272);
        tableVaucher.setLockedWidth(true);
        tableVaucher.setWidths(new int[]{30, 25, 20, 25});

        PdfPCell cellLine = getCellLine();
        cellLine.setColspan(4);

        if (model_document.getVaucher().getHeader() != null)
        {
            PdfPCell cellFirstLine = getParCell(model_document.getVaucher().getHeader(), pt30, padding, borderMode, 4, null, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE);
            tableVaucher.addCell(cellFirstLine);
            tableVaucher.addCell(cellLine);
        }

        for (Model_Price_Element price_element : model_document.getVaucher().getPrice_elements())
        {
            String text = price_element.getText();
            String price = "= " + StringManager.formatNum(price_element.getPrice(),false);

            PdfPCell cellLeft = getParCell(text, pt28, padding, borderMode, 2, null, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE);
            tableVaucher.addCell(cellLeft);

            PdfPCell cellRight = getParCell(price, pt26, padding, borderMode, 2, null, Element.ALIGN_RIGHT, Element.ALIGN_MIDDLE);
            tableVaucher.addCell(cellRight);
        }

        if (model_document.getVaucher().getPrice_elements().size() > 0)
        {
            tableVaucher.addCell(cellLine);

            String itogoSum = StringManager.formatNum(GlobalHelper.countVaucherElementsSum(model_document), false);
            PdfPCell emptyCell18 = getEmptyCell(1, 4, borderMode);
            emptyCell18.setFixedHeight(18);

            tableVaucher.addCell(emptyCell18);
            tableVaucher.addCell(getParCell("ИТОГ", pt46, borderMode, 2, null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
            tableVaucher.addCell(getParCell(itogoSum, pt36, borderMode, 2, null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
            tableVaucher.addCell(emptyCell18);

            tableVaucher.addCell(cellLine);
        }

        return tableVaucher;
    }


    private PdfPTable getCheckProductsTable() throws DocumentException
    {
        float padding = 4f;
        PdfPTable tableProducts = new PdfPTable(4);
        tableProducts.setTotalWidth(272);
        tableProducts.setLockedWidth(true);
        tableProducts.setWidths(new int[]{30, 25, 20, 25});

        for (Model_Product product : model_document.getListOfProducts())
        {
            double sum = product.getPrice() * product.getCount();
            String strPrice = StringManager.formatNum(product.getPrice(), false);
            String strCount = "*" + product.getCount();
            String strSum = "= " + StringManager.formatNum(sum, false);

            PdfPCell cellFirstLine = getParCell(product.getMaterial().getName(), pt28, padding, borderMode, 4, null, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE);
            tableProducts.addCell(cellFirstLine);


            String secondLine = "";
            if (product.getColor() != null)
            {
                secondLine += product.getColor().getName();
            }
            if (!TextUtils.isEmpty(product.getWidth()))
            {
                secondLine += " Ш-" + product.getWidth();
            }
            if (!TextUtils.isEmpty(product.getHeight()))
            {
                secondLine += " В-" + product.getHeight();
            }

            PdfPCell cellSecondLine = getParCell(secondLine, pt24, padding, borderMode, 4, null, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE);
            tableProducts.addCell(cellSecondLine);

            tableProducts.addCell(getEmptyCell(1, 1, borderMode));
            tableProducts.addCell(getParCell(strPrice, pt26, padding, borderMode, null, null, Element.ALIGN_LEFT, null));
            tableProducts.addCell(getParCell(strCount, pt26, padding, borderMode, null, null));
            tableProducts.addCell(getParCell(strSum, pt26, padding, borderMode, null, null, Element.ALIGN_RIGHT, null));

            PdfPCell cellLine = getCellLine();
            cellLine.setColspan(4);
            tableProducts.addCell(cellLine);
        }


//            String bottomLine = StringManager.repeatingString(null,"_",40);
//            PdfPCell cellLine = getParCell(bottomLine, pt26,borderMode,4,null,null,null);
//            tableProducts.addCell(cellLine);


        String strMontage = StringManager.formatNum(model_document.getMontage(), false);
        String strDelivery = StringManager.formatNum(model_document.getDelivery(), false);
        String strSale = StringManager.formatNum(model_document.getSale(), false);

        tableProducts.addCell(getParCell("Монтаж", pt28, padding, borderMode, 4, null, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE));
        tableProducts.addCell(getEmptyCell(1, 1, borderMode));
        tableProducts.addCell(getParCell(strMontage, pt26, borderMode, null, null, Element.ALIGN_LEFT, null));
        tableProducts.addCell(getParCell("*1", pt26, borderMode, null, null));
        tableProducts.addCell(getParCell("= " + strMontage, pt26, borderMode, null, null, Element.ALIGN_RIGHT, null));


        tableProducts.addCell(getParCell("Доставка", pt28, padding, borderMode, 4, null, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE));
        tableProducts.addCell(getEmptyCell(1, 1, borderMode));
        tableProducts.addCell(getParCell(strDelivery, pt26, borderMode, null, null, Element.ALIGN_LEFT, null));
        tableProducts.addCell(getParCell("*1", pt26, borderMode, null, null));
        tableProducts.addCell(getParCell("= " + strDelivery, pt26, borderMode, null, null, Element.ALIGN_RIGHT, null));


        tableProducts.addCell(getParCell("Скидка", pt28, padding, borderMode, 4, null, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE));
        tableProducts.addCell(getEmptyCell(1, 1, borderMode));
        tableProducts.addCell(getParCell(strSale, pt26, borderMode, null, null, Element.ALIGN_LEFT, null));
        tableProducts.addCell(getParCell("*1", pt26, borderMode, null, null));
        tableProducts.addCell(getParCell("= " + strSale, pt26, borderMode, null, null, Element.ALIGN_RIGHT, null));


        PdfPCell cellLine2 = getCellLine();
        cellLine2.setColspan(4);
        tableProducts.addCell(cellLine2);

        String itogoSum = StringManager.formatNum(GlobalHelper.countItogoSum(model_document), false);
        PdfPCell emptyCell18 = getEmptyCell(1, 4, borderMode);
        emptyCell18.setFixedHeight(18);

        tableProducts.addCell(emptyCell18);
        tableProducts.addCell(getParCell("ИТОГ", pt46, borderMode, 2, null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
        tableProducts.addCell(getParCell(itogoSum, pt36, borderMode, 2, null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE));
        tableProducts.addCell(emptyCell18);


        tableProducts.addCell(cellLine2);

        PdfPCell emptyCell6 = getEmptyCell(1, 4, borderMode);
        emptyCell6.setFixedHeight(6);

        double postPay = GlobalHelper.countItogoSum(model_document) - model_document.getPrepay();
        String prePayStr = StringManager.formatNum(model_document.getPrepay(), false);
        String postPayStr = StringManager.formatNum(postPay, false);

        tableProducts.addCell(emptyCell6);

        tableProducts.addCell(getParCell("Предоплата", pt28, borderMode, 2, null, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE));
        tableProducts.addCell(getParCell("= " + prePayStr, pt26, borderMode, 2, null, Element.ALIGN_RIGHT, null));

        tableProducts.addCell(emptyCell6);

        tableProducts.addCell(getParCell("Доплата", pt28, borderMode, 2, null, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE));
        tableProducts.addCell(getParCell("= " + postPayStr, pt26, borderMode, 2, null, Element.ALIGN_RIGHT, null));


        return tableProducts;
    }

    private PdfPTable getCheckBottomTable() throws DocumentException, IOException
    {
        PdfPTable tableCheckBottom = new PdfPTable(1);
        tableCheckBottom.setWidths(new int[]{100});
        tableCheckBottom.setTotalWidth(272);
        tableCheckBottom.setLockedWidth(true);

        Image qr_wintec = getImageFromAsset("qr_wintec.png");
        PdfPCell cellQr = new PdfPCell(qr_wintec, true);
        cellQr.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellQr.setBorder(Rectangle.NO_BORDER);
        cellQr.setFixedHeight(100);
        tableCheckBottom.addCell(cellQr);

        tableCheckBottom.addCell(getParCell(Constants.WINTEC_DESC, pt24, borderMode, null, null, Element.ALIGN_CENTER, null));

        PdfPCell cellEmptySpace = getEmptyCell(1, 1, borderMode);
        cellEmptySpace.setFixedHeight(14);
        tableCheckBottom.addCell(cellEmptySpace);

        PdfPCell cellSites = getParCell(Constants.WINTEC_SITES, pt24, borderMode, null, null, Element.ALIGN_CENTER, null);
        tableCheckBottom.addCell(cellSites);

        String starLine = StringManager.repeatingString(null, "x", 36);
        tableCheckBottom.addCell(getParCell(starLine, pt24, borderMode, null, null));

        tableCheckBottom.addCell(getParCell("E-MAIL: INFO@WINTEC.RU", pt24, borderMode, null, null));
        tableCheckBottom.addCell(getParCell("МОСКВА +7.495.369.11.29", pt24, borderMode, null, null));
        tableCheckBottom.addCell(getParCell("САМАРА +7.846.244.00.10", pt24, borderMode, null, null));
        tableCheckBottom.addCell(getParCell("РОССИЯ +7.903.250.22.09", pt24, borderMode, null, null));

        return tableCheckBottom;
    }


    private PdfPTable getTableBottomInfoTable() throws DocumentException
    {

        PdfPCell cellEmptySpace = getEmptyCell(1, 1, borderMode);
        cellEmptySpace.setFixedHeight(14);


        PdfPTable tableCheckBottomInfo = new PdfPTable(1);
        tableCheckBottomInfo.setWidths(new int[]{100});
        tableCheckBottomInfo.setTotalWidth(272);
        tableCheckBottomInfo.setLockedWidth(true);

        tableCheckBottomInfo.addCell(getCellLine());
        tableCheckBottomInfo.addCell(cellEmptySpace);

        tableCheckBottomInfo.addCell(getParCell("Адрес", pt24, borderMode, null, null, Element.ALIGN_LEFT, null));
        String adress = "Не указан";
        if (model_document.getAdress() != null && model_document.getAdress().trim().length() > 0)
        {
            adress = model_document.getAdress();
        }
        PdfPCell cellAdress = getParCell(adress, pt28, borderMode, null, null, Element.ALIGN_RIGHT, null);
        tableCheckBottomInfo.addCell(cellAdress);
        tableCheckBottomInfo.addCell(cellEmptySpace);

        tableCheckBottomInfo.addCell(getParCell("Телефон", pt24, borderMode, null, null, Element.ALIGN_LEFT, null));
        String phone = "Не указан";
        if (model_document.getPhone() != null && model_document.getPhone().trim().length() > 0)
        {
            phone = model_document.getPhone();
        }
        tableCheckBottomInfo.addCell(getParCell(phone, pt28, borderMode, null, null, Element.ALIGN_RIGHT, null));
        tableCheckBottomInfo.addCell(cellEmptySpace);


        tableCheckBottomInfo.addCell(getParCell("Заказчик", pt24, borderMode, null, null, Element.ALIGN_LEFT, null));
        String fio = "Не указан";
        if (model_document.getFio() != null && model_document.getFio().trim().length() > 0)
        {
            fio = model_document.getFio();
        }
        tableCheckBottomInfo.addCell(getParCell(fio, pt28, borderMode, null, null, Element.ALIGN_RIGHT, null));

        PdfPCell cellEmpty8 = getEmptyCell(1, 1, false);
        cellEmpty8.setFixedHeight(8);
        tableCheckBottomInfo.addCell(cellEmpty8);

        PdfPCell cellEmpty = getEmptyCell(1, 1, borderMode);
        cellEmpty.setBorder(Rectangle.BOX);
        cellEmpty.setFixedHeight(100);
        cellEmpty.setBorderWidth(2);
        tableCheckBottomInfo.addCell(cellEmpty);
        tableCheckBottomInfo.addCell(getParCell("(Подпись)", pt24, borderMode, null, null, Element.ALIGN_LEFT, null));

        tableCheckBottomInfo.addCell(cellEmptySpace);
        tableCheckBottomInfo.addCell(getCellLine());
        tableCheckBottomInfo.addCell(cellEmptySpace);

        return tableCheckBottomInfo;
    }


    private PdfPTable getTopTable() throws DocumentException
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
    }


    private PdfPTable getAdressTable() throws DocumentException, IOException
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


        PdfPCell cellEmptySpace = getEmptyCell(1, 3, false, 6);
        tableLarge.addCell(cellEmptySpace);

        tableLarge.addCell(getEmptyCell(1, 1, false));
        String address = "  ";
        if(model_document.getAdress() != null)
        {
            address+=model_document.getAdress();
        }
        Paragraph parAddress = new Paragraph(address);
        parAddress.setLeading(18, 0);
        parAddress.setFont(reg10);
//            parAddress.setFont(reg10Underline);
//            parAddress.add(lineChunk);

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
        String phone = "  ";
        if(model_document.getPhone() != null)
        {
            phone+=model_document.getPhone();
        }
        PdfPCell cellPhone = getParCell(phone, reg10Underline, false, null, null, true, 44f);
        cellPhone.setPaddingTop(17);
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
    }


    private PdfPTable getProductsTable() throws DocumentException
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
    }

    private void fillTableHeader(PdfPTable tableProducts)
    {
        float padding = 4;
        tableProducts.addCell(getEmptyCell(1, 1, true));

        tableProducts.addCell(getParCell("Наименование товара", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Ширина", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Высота/вынос", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Цвет комплектации", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Крепление", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Тип упр.", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Кол-во", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Цена за изделие", bold8, padding, true, null, null));
        tableProducts.addCell(getParCell("Сумма", bold8, padding, true, null, null));
    }

    private void fillProductWithElement(PdfPTable tableProducts, Model_Product product)
    {
        float padding = 6;
        float paddingSmall = 2;

        String id = String.valueOf(model_document.getListOfProducts().indexOf(product) + 1);

        String material = "";
        if (GlobalHelper.validateProductMaterial(product))
        {
            material = product.getMaterial().getName();
        }


        String width;
        if (TextUtils.isEmpty(product.getWidth()))
        {
            width = "-";
        } else
        {
            width = product.getWidth();
        }

        String height;

        if (TextUtils.isEmpty(product.getHeight()))
        {
            height = "-";
        } else
        {
            height = product.getHeight();
        }

        String color = "-";
        if (GlobalHelper.validateProductColor(product))
        {
            color = product.getColor().getName();
        }

        String krep = "-";
        if (GlobalHelper.validateProductKrep(product))
        {
            krep = product.getKrep().getName();
        }

        String control = "-";
        if (GlobalHelper.validateProductControl(product))
        {
            control = product.getControl().getName();
        }

        String count = StringManager.formatNum(product.getCount(), false);
        String price = StringManager.formatNum(product.getPrice(), true);
        String sum = StringManager.formatNum(product.getSum(), true);


        tableProducts.addCell(getParCell(id, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(material, reg10, paddingSmall, true, null, null));
        tableProducts.addCell(getParCell(width, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(height, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(color, reg10, paddingSmall, true, null, null));
        tableProducts.addCell(getParCell(krep, reg10, paddingSmall, true, null, null));
        tableProducts.addCell(getParCell(control, reg10, paddingSmall, true, null, null));
        tableProducts.addCell(getParCell(count, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(price, reg10, padding, true, null, null));
        tableProducts.addCell(getParCell(sum, reg10, padding, true, null, null));

    }

    private PdfPTable getBottomInfoText() throws DocumentException
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
        bottomTable.setKeepTogether(true);
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
    }

    private Element getBottomSignature() throws DocumentException, IOException
    {

        float padding = 4;
        PdfPTable tableSign = new PdfPTable(2);
        tableSign.setKeepTogether(true);
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
    }

    private PdfPTable getBottomStamp() throws DocumentException, IOException
    {

        PdfPTable tableWide = new PdfPTable(3);
        tableWide.setKeepTogether(true);
        tableWide.setWidthPercentage(100);
        tableWide.setWidths(new int[]{890, 1064, 540});
        tableWide.addCell(getEmptyCell(1, 1, borderMode));


        PdfPTable tableStamp = new PdfPTable(2);
        tableStamp.setWidthPercentage(100);
        tableStamp.setWidths(new int[]{475, 590});

        PdfPCell cellGenDir = getParCell("Генеральный директор", timesNewRoman10, borderMode, null, null, Element.ALIGN_LEFT, Element.ALIGN_MIDDLE);
        cellGenDir.setPaddingTop(11);
        tableStamp.addCell(cellGenDir);

        PdfPCell cellStamp = new PdfPCell(getImageFromAsset("sign_stamp.png"));
        cellStamp.setRowspan(2);
        cellStamp.setBorder(Rectangle.NO_BORDER);
        cellStamp.setFixedHeight(112);
        tableStamp.addCell(cellStamp);

        PdfPCell cellFio = getParCell("/Рыбакина Л.В./", timesNewRoman10, borderMode, null, null, Element.ALIGN_RIGHT, Element.ALIGN_TOP);
        cellFio.setPaddingTop(12);
        tableStamp.addCell(cellFio);


        PdfPCell cellMiddle = new PdfPCell(tableStamp);
        cellMiddle.setBorder(Rectangle.NO_BORDER);

        tableWide.addCell(cellMiddle);
        tableWide.addCell(getEmptyCell(1, 1, borderMode));


        return tableWide;
    }


    private void initAll() throws IOException, DocumentException
    {
        globalReg = BaseFont.createFont("assets/segreg.ttf", "Cp1251", BaseFont.EMBEDDED);
        globalBold = BaseFont.createFont("assets/segbold.ttf", "Cp1251", BaseFont.EMBEDDED);
        globalItalic = BaseFont.createFont("assets/segitalic.ttf", "Cp1251", BaseFont.EMBEDDED);
        globalPt = BaseFont.createFont("assets/pt_mono.ttf", "Cp1251", BaseFont.EMBEDDED);
        globalTimesNewRoman = BaseFont.createFont("assets/tnr.ttf", "Cp1251", BaseFont.EMBEDDED);

        reg10 = new Font(globalReg, 10);
        reg10Underline = new Font(globalReg, 10, Font.UNDERLINE);
        reg8 = new Font(globalReg, 8);
        bold10 = new Font(globalBold, 10);
        bold8 = new Font(globalBold, 8);
        italic10 = new Font(globalItalic, 10);
        timesNewRoman10 = new Font(globalTimesNewRoman, 10);

        pt46 = new Font(globalPt, 26);
        pt36 = new Font(globalPt, 18);
        pt30 = new Font(globalPt, 16);
        pt28 = new Font(globalPt, 14);
        pt26 = new Font(globalPt, 13);
        pt24 = new Font(globalPt, 11);

        yellow = new BaseColor(255, 192, 0);
        trans = new BaseColor(0, 0, 0, 0);

        LineSeparator line = new LineSeparator(0, 100, trans, Element.ALIGN_CENTER, -2.2f);
        lineChunk = new Chunk(line);
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
        return getEmptyCell(rowSpawn, colSpawn, border, null);
    }

    private PdfPCell getEmptyCell(int rowSpawn, int colSpawn, boolean border, @Nullable Integer fixedHight)
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

        if (fixedHight != null)
        {
            emptyCell.setFixedHeight(fixedHight);
        }

        return emptyCell;
    }

    private PdfPCell getSignupMiniCell(boolean borderBottom) throws IOException, BadElementException
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


    private PdfPCell getCellLine()
    {
        Paragraph paragraph = new Paragraph();
        CustomDottedLineSeparator separator = new CustomDottedLineSeparator();
        paragraph.add(new Chunk(separator));
        //Todo decide what  to do with Line
//        LineSeparator line = new LineSeparator(2, 100, BaseColor.BLACK, Element.ALIGN_CENTER, -2.2f);
//        paragraph.add(new Chunk(line));

        PdfPCell cell = new PdfPCell(paragraph);
        cell.setBorder(Rectangle.NO_BORDER);

        return cell;
    }

    private Image getImageFromFiles(File img) throws IOException, BadElementException
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
    }


    private Image getImageFromFiles(String fileName) throws IOException, BadElementException
    {
        File file = fileManager.getFileFromTemp(fileName, null);
        return getImageFromFiles(file);
    }


    private Image getImageFromAsset(String fileName) throws IOException, BadElementException
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
    }

    private PdfPTable getFormOrderTable() throws DocumentException
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
    }

    public String getRenamedFile(boolean isCheck, boolean isVaucher)
    {
        String name = model_document.getCode();
        if (model_document.getFio() != null && model_document.getFio().trim().length() > 0)
        {
            name = StringManager.transliterate(model_document.getFio()) + "_" + name;
        }

        if (isCheck)
        {
            name += "_check";
        }

        if (isVaucher)
        {
            name += "_vaucher";
        }

        name = name.replaceAll("\\s+", "_");

        return name;
    }

    private PdfPTable getCheckAndVaucherTopTable() throws DocumentException, IOException
    {
        PdfPTable tableCheckTop = new PdfPTable(1);
        tableCheckTop.setWidths(new int[]{100});
        tableCheckTop.setTotalWidth(272);
        tableCheckTop.setLockedWidth(true);

        Image wintecLogo = getImageFromAsset("logo_border.png");
        PdfPCell cellLogo = new PdfPCell(wintecLogo, true);
        cellLogo.setBorder(Rectangle.NO_BORDER);
        cellLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
        cellLogo.setFixedHeight(100);
        tableCheckTop.addCell(cellLogo);

        PdfPCell cellSite = getParCell("www.wintec.ru", pt36, borderMode, null, null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE);
        tableCheckTop.addCell(cellSite);

        PdfPCell cellCities = getParCell("Москва|Санкт-Петербург|Сочи|Самара", pt26, borderMode, null, null, Element.ALIGN_CENTER, Element.ALIGN_MIDDLE);
        tableCheckTop.addCell(cellCities);

        PdfPCell cellOoo = getParCell("ООО \"Лаборатория Уюта\"", pt36, borderMode, null, null, Element.ALIGN_CENTER, null);
        cellOoo.setPaddingTop(16);
        tableCheckTop.addCell(cellOoo);
        tableCheckTop.addCell(getEmptyCell(1, 1, false, 8));


        String codeText = "Заказ " + model_document.getCode();
        String fromDate = "от " + GlobalHelper.getDateString(model_document.getDate(), GlobalHelper.FORMAT_FULL_MONTH);
        tableCheckTop.addCell(getParCell(codeText, pt30, borderMode, null, null, Element.ALIGN_CENTER, null));
        tableCheckTop.addCell(getParCell(fromDate, pt30, borderMode, null, null, Element.ALIGN_CENTER, null));


        String starLine = StringManager.repeatingString(null, "x", 34);
        PdfPCell cellLine = getParCell(starLine, pt26, borderMode, null, null, null, null);
        cellLine.setFixedHeight(32);
        tableCheckTop.addCell(cellLine);

        return tableCheckTop;
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

                document.setMargins(0, 0, 52, 88);

            } catch (Exception e)
            {
                Log.e(TAG, "onEndPage: Exception on template adding");
            }
        }
    }
}
