package com.dimfcompany.signpdfapp.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants
{
    //TODO make base url Here
//    public static final String URL_BASE = "http://192.168.1.66/wintec/";
    public static final String URL_BASE = "https://wintec-smart.ru/";
    public static final String URL_REGISTER = "register";
    public static final String URL_LOGIN = "login";
    public static final String URL_RESETPASS = "forgotpass";
    public static final String URL_INSERT_DOCUMENT = "insert_document";
    public static final String URL_USER_ROLE_NAME = "role_name";
    public static final String URL_USER_DOCS_COUNT= "docs_count";
    public static final String URL_GET_ALL_DOCS = "get_all_docs";
    public static final String URL_GET_ALL_USERS_ALL_DOCS = "get_all_users_all_documents";
    public static final String URL_GET_DOCUMETS_WITH_FULL_INFO = "get_document_with_full_info";
    public static final String URL_DELETE_DOCUMENT_ON_SERVER = "delete_document";


    public static final int RQ_TAKE_SIGNATURE = 9000;
    public static final int RQ_PRODUCTS_SCREEN = 9001;
    public static final int RQ_PRODUCTS_DIALOG = 9002;
    public static final int RQ_PRODUCTS_DIALOG_EDIT = 9003;
    public static final int RQ_VAUCHER_SCREEN = 9004;
    public static final int RQ_ADD_PRICE_ELEMENT = 9005;
    public static final int RQ_EDIT_PRICE_ELEMENT = 9007;

    public static final int JOB_ID_SYNC = 7000;

    public static final String BROADCAST_UPDATE_FINISHED_UI = "com.dimfcompany.signpdfapp.base.update_finished_ui";


    public static final String ERROR_NO_FIRSTNAME = "Заполните поле \"Имя\"";
    public static final String ERROR_NO_SECONDNAME = "Заполните поле \"Фамилия\"";
    public static final String ERROR_NO_ROLE = "Выберите \"Должность\"";
    public static final String ERROR_NO_EMAIL = "Заполните поле \"Email\"";
    public static final String ERROR_EMAIL_NOT_VALID = "Введите корректный Email";
    public static final String ERROR_NO_PASSWORD = "Заполните поле \"Пароль\"";
    public static final String ERROR_PASSWORD_NOT_MATCH = "Пароли не совпадают";
    public static final String ERROR_PASSWORD_TOO_SHORT = "Пароль должен содержать минимум 8 символов";
    public static final String ERROR_NO_FB_TOKEN = "Отсутсвует fb_token";


    public static final String EXTRA_MODEL_DOCUMENT = "Extra_Model_Document";
    public static final String EXTRA_PRODUCT = "Extra_Product";
    public static final String EXTRA_SIGNATURE_FILE_NAME = "Signature_File_Name";
    public static final String EXTRA_MODEL_VAUCHER = "Extra_Model_Vaucher";
    public static final String EXTRA_PRICE_ELEMENT = "Extra_Price_Element";

    public static final String EXTANSION_PNG = "png";
    public static final String EXTANSION_PDF = "pdf";

    public static final String FOLDER_TEMP_FILES = "temp_files";
    public static final String FOLDER_CONTRACTS = "contracts";
    public static final String FOLDER_CHECKS = "checks";
    public static final String FOLDER_VAUCHERS = "vauchers";

    public static final String WINTEC_SITES = "WINTEC.RU | WINTEC-DESIGNO.RU | WINTECHO.RU | WINKARNIZ.RU | WINPATIO.RU | PERGOTENTA.RU | WINLIFT.RU | WINSHADE.RU | WINPLISSE.RU | WINSAILS.RU | RAFSHTORY.RU | WINSTREET.RU | SAMARSKY-DOM.RU";
    public static final String WINTEC_DESC = "Всю дополнительную информация по продукции вы можете найти по данной QR ссылке , а так же на наших сайтах и по телефонам";


    public static final String MY_SHARED_PREFS = "My_Shared_Prefs";
    public static final String CURRENT_USER = "Current_User";
    public static final String FB_TOKEN = "Fb_Token";


    public static final List<String> headerStrings =  Arrays.asList("Заглавие 1", "Заглавие 2", "Заглавие 3");


    public static final String NOTIFICATION_CHANEL1 = "Напомнинания о добоавлении нового документа";
}

