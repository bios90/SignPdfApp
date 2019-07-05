package com.dimfcompany.signpdfapp.utils;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.github.angads25.toggle.widget.LabeledSwitch;

public class ViewBinder
{
    private static final String TAG = "ViewBinder";

    public static void bindUserPageInfo(Model_User user, String last_app_version, View parent)
    {
        int colorGreen = ResourcesCompat.getColor(parent.getContext().getResources(), R.color.green, null);
        int colorRed = ResourcesCompat.getColor(parent.getContext().getResources(), R.color.redBase, null);
        int colorBlue = ResourcesCompat.getColor(parent.getContext().getResources(), R.color.blue, null);
        int colorGray4 = ResourcesCompat.getColor(parent.getContext().getResources(), R.color.gray4, null);

        Drawable bg_btn_red = ContextCompat.getDrawable(parent.getContext(), R.drawable.btn_grad_red_rounded);
        Drawable bg_btn_green = ContextCompat.getDrawable(parent.getContext(), R.drawable.btn_grad_green_rounded);

        TextView tv_user_name_header = parent.findViewById(R.id.tv_user_name_header);
        TextView tv_first_name = parent.findViewById(R.id.tv_first_name);
        TextView tv_last_name = parent.findViewById(R.id.tv_last_name);
        TextView tv_email = parent.findViewById(R.id.tv_email);
        TextView tv_email_verified = parent.findViewById(R.id.tv_email_verified);
        TextView tv_admin_approved = parent.findViewById(R.id.tv_admin_approved);
        TextView tv_created_at = parent.findViewById(R.id.tv_created_at);
        TextView tv_role = parent.findViewById(R.id.tv_role);
        TextView tv_docs_count = parent.findViewById(R.id.tv_docs_count);
        TextView tv_app_version = parent.findViewById(R.id.tv_app_version);

        Button btn_toggle_approved = parent.findViewById(R.id.btn_toggle_approved);

        tv_user_name_header.setText(StringManager.getFullName(user));
        tv_first_name.setText(user.getFirst_name());
        tv_last_name.setText(user.getLast_name());
        tv_email.setText(user.getEmail());

        if (user.getVerified() == 0)
        {
            tv_email_verified.setText("Не подтвержден");
            tv_email_verified.setTextColor(colorRed);
        }
        else if (user.getVerified() == 1)
        {
            tv_email_verified.setText("Подтвержден");
            tv_email_verified.setTextColor(colorGreen);
        }

        if (user.getAdmin_approved() == 0)
        {
            btn_toggle_approved.setBackground(bg_btn_green);
            btn_toggle_approved.setText("Одобрить");
            tv_admin_approved.setText("Ожидает одобрения");
            tv_admin_approved.setTextColor(colorRed);
        }
        else if (user.getAdmin_approved() == 1)
        {
            btn_toggle_approved.setBackground(bg_btn_red);
            btn_toggle_approved.setText("Отозвать");
            tv_admin_approved.setText("Одобрен");
            tv_admin_approved.setTextColor(colorGreen);
        }

        String role = "-";
        if (user.getRole() != null && user.getRole().getName() != null)
        {
            role = user.getRole().getName();
        }

        String app_version = "-";
        int version_color = colorGray4;
        if (user.getApp_version() != null)
        {
            app_version = user.getApp_version();
            if (last_app_version != null)
            {
                if (last_app_version.equals(user.getApp_version()))
                {
                    version_color = colorGreen;
                }
                else
                {
                    version_color = colorRed;
                }
            }
        }
        tv_app_version.setText(app_version);
        tv_app_version.setTextColor(version_color);

        tv_created_at.setText(GlobalHelper.getDateString(user.getCreated_at(), GlobalHelper.FORMAT_FULL_MONTH));
        tv_role.setText(role);

        if (user.getRole_id() == 1)
        {
            tv_role.setTextColor(colorBlue);
        }
        if (user.getRole_id() == 7)
        {
            tv_role.setTextColor(colorGreen);
        }
        if (user.getRole_id() == 999)
        {
            tv_role.setTextColor(colorRed);
        }

        String docs_count = "0";
        if (user.getDocuments() != null && user.getDocuments().size() > 0)
        {
            docs_count = String.valueOf(user.getDocuments().size());
        }

        tv_docs_count.setText(docs_count);
    }

    public static void bindUserAuthPage(Model_User user, View parent)
    {
        if (user == null)
        {
            Log.e(TAG, "bindUserAuthPage: USer is null cant bind");
            return;
        }

        TextView tv_header = parent.findViewById(R.id.tv_header);
        TextView tv_user_name_header = parent.findViewById(R.id.tv_user_name_header);
        EditText et_email = parent.findViewById(R.id.et_email);
        EditText et_first_name = parent.findViewById(R.id.et_first_name);
        EditText et_last_name = parent.findViewById(R.id.et_last_name);
        Spinner spinner_access = parent.findViewById(R.id.spinner_access);
        RadioGroup rg_approved = parent.findViewById(R.id.rg_approved);
        RadioGroup rg_email_verified = parent.findViewById(R.id.rg_email_verified);

        tv_header.setText("Редактирование пользователя");
        tv_user_name_header.setText(StringManager.getFullName(user));
        et_email.setText(user.getEmail());

        et_email.setEnabled(false);
        et_email.setBackground(ContextCompat.getDrawable(parent.getContext(), R.drawable.rounded_gray_gray_stroke));

        et_first_name.setText(user.getFirst_name());
        et_last_name.setText(user.getLast_name());

        switch (user.getRole_id())
        {
            case 1:
                spinner_access.setSelection(0);
                break;
            case 7:
                spinner_access.setSelection(1);
                break;
            case 999:
                spinner_access.setSelection(2);
                break;
        }

        GlobalHelper.setRadioSelectedAt(rg_approved, user.getAdmin_approved());
        GlobalHelper.setRadioSelectedAt(rg_email_verified, user.getVerified());
    }

    public static void bindDocumentToMarkerView(Model_Document document, View view)
    {
        TextView tv_date = view.findViewById(R.id.tv_date);
        TextView tv_fio = view.findViewById(R.id.tv_fio);
        TextView tv_adress = view.findViewById(R.id.tv_adress);
        TextView tv_sum = view.findViewById(R.id.tv_sum);

        tv_date.setText(GlobalHelper.getDateString(document.getDate()));
        tv_sum.setText(StringManager.formatNum(document.getItogo_sum(), false) + " р");

        if (document.getFio() != null)
        {
            tv_fio.setText(document.getFio());
            tv_fio.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_fio.setVisibility(View.GONE);
        }

        if (document.getAdress() != null)
        {
            tv_adress.setText(document.getAdress());
            tv_adress.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_adress.setVisibility(View.GONE);
        }
    }
}
