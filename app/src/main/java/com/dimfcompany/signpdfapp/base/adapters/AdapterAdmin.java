package com.dimfcompany.signpdfapp.base.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.models.Model_Document;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;
import com.dimfcompany.signpdfapp.utils.StringManager;

import java.util.List;

public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.Card>
{
    private AdapterFinished.CardFinishedCallback callback;
    private List<Model_Document> documents;

    @NonNull
    @Override
    public Card onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View card = inflater.inflate(R.layout.item_finished_dogovor_global, viewGroup, false);
        return new Card(card);
    }

    @Override
    public void onBindViewHolder(@NonNull Card holder, int position)
    {
        final Model_Document document = documents.get(position);
        String date = GlobalHelper.getDateString(document.getDate(), GlobalHelper.FORMAT_FULL_MONTH);
        String city = GlobalHelper.getCityOfDocument(document);
        String header = document.getCode() + " | " + date;

        String address = city;
        if (document.getAdress() != null)
        {
            address += " " + document.getAdress();
        }

        holder.tv_header.setText(header);
        holder.tv_fio.setText(document.getFio());
        holder.tv_phone.setText(document.getPhone());
        holder.tv_adress.setText(address);
        holder.tv_date.setText(date);
        holder.tv_user.setText(StringManager.getFullName(document.getUser()));

        if (document.getVaucher_file_name() != null || document.getVaucher() != null)
        {
            holder.tv_has_vaucher.setVisibility(View.VISIBLE);
        }else
        {
            holder.tv_has_vaucher.setVisibility(View.GONE);
        }



        holder.root_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.clickedCard(document);
            }
        });

        if (!TextUtils.isEmpty(document.getPhone()))
        {
            holder.tv_phone.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    callback.clickedPhone(document);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        if(documents == null)
        {
            return 0;
        }
        return documents.size();
    }


    public void setCallback(AdapterFinished.CardFinishedCallback callback)
    {
        this.callback = callback;
    }

    public void setItems(List<Model_Document> documents)
    {
        this.documents = documents;
        notifyDataSetChanged();
    }


    static class Card extends RecyclerView.ViewHolder
    {
        TextView tv_header;
        TextView tv_fio;
        TextView tv_phone;
        TextView tv_adress;
        TextView tv_date;
        TextView tv_user;
        TextView tv_has_vaucher;
        Button btn_delete;
        Button btn_send;
        View root_view;

        private Card(@NonNull View itemView)
        {
            super(itemView);

            tv_header = itemView.findViewById(R.id.tv_header);
            tv_fio = itemView.findViewById(R.id.tv_fio);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_adress = itemView.findViewById(R.id.tv_adress);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_user = itemView.findViewById(R.id.tv_user);
            tv_has_vaucher = itemView.findViewById(R.id.tv_has_vaucher);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_send = itemView.findViewById(R.id.btn_send);
            root_view = itemView.findViewById(R.id.root_view);
        }
    }
}
