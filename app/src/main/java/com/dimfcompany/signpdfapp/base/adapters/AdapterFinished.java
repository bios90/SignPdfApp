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

import java.util.ArrayList;
import java.util.List;

public class AdapterFinished extends RecyclerView.Adapter<AdapterFinished.CardFinished>
{

    public interface CardFinishedCallback
    {
        void clickedCard(Model_Document document);

        void clickedPhone(Model_Document document);
    }

    List<Model_Document> listOfDocuments = new ArrayList<>();
    private CardFinishedCallback callback;

    private boolean showSync = true;

    @NonNull
    @Override
    public CardFinished onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View card = inflater.inflate(R.layout.item_finished_dogovor, viewGroup, false);
        return new CardFinished(card);
    }

    @Override
    public void onBindViewHolder(@NonNull CardFinished cardFinished, int i)
    {
        final Model_Document document = listOfDocuments.get(i);
        String date = GlobalHelper.getDateString(document.getDate(), GlobalHelper.FORMAT_FULL_MONTH);
        String city = GlobalHelper.getCityOfDocument(document);
        String header = document.getCode() + " | " + date;

        String address = city;
        if (document.getAdress() != null)
        {
            address += " " + document.getAdress();
        }

        cardFinished.tv_header.setText(header);
        cardFinished.tv_fio.setText(document.getFio());
        cardFinished.tv_phone.setText(document.getPhone());
        cardFinished.tv_adress.setText(address);
        cardFinished.tv_date.setText(date);

        if (document.getSync_status() == 0)
        {
            cardFinished.tv_status_0.setVisibility(View.VISIBLE);
            cardFinished.tv_status_1.setVisibility(View.GONE);
        }
        else if (document.getSync_status() == 1)
        {
            cardFinished.tv_status_1.setVisibility(View.VISIBLE);
            cardFinished.tv_status_0.setVisibility(View.GONE);
        }

        if (!showSync)
        {
            cardFinished.tv_status_0.setVisibility(View.GONE);
            cardFinished.tv_status_1.setVisibility(View.GONE);
        }

        if (document.getVaucher_file_name() != null || document.getVaucher() != null)
        {
            cardFinished.tv_has_vaucher.setVisibility(View.VISIBLE);
        }else
            {
                cardFinished.tv_has_vaucher.setVisibility(View.GONE);
            }

        cardFinished.root_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.clickedCard(document);
            }
        });

        if (!TextUtils.isEmpty(document.getPhone()))
        {
            cardFinished.tv_phone.setOnClickListener(new View.OnClickListener()
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
        if (listOfDocuments == null)
        {
            return 0;
        }
        return listOfDocuments.size();
    }


    public void setCallback(CardFinishedCallback callback)
    {
        this.callback = callback;
    }

    public void setItems(List<Model_Document> documents)
    {
        this.listOfDocuments = documents;
        notifyDataSetChanged();
    }

    public void setShowSync(boolean showSync)
    {
        this.showSync = showSync;
    }

    class CardFinished extends RecyclerView.ViewHolder
    {
        TextView tv_header;
        TextView tv_fio;
        TextView tv_phone;
        TextView tv_adress;
        TextView tv_date;
        TextView tv_status_0;
        TextView tv_status_1;
        TextView tv_has_vaucher;
        Button btn_delete;
        Button btn_send;
        View root_view;

        public CardFinished(@NonNull View itemView)
        {
            super(itemView);

            tv_header = itemView.findViewById(R.id.tv_header);
            tv_fio = itemView.findViewById(R.id.tv_fio);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_adress = itemView.findViewById(R.id.tv_adress);
            tv_date = itemView.findViewById(R.id.tv_date);
            btn_delete = itemView.findViewById(R.id.btn_delete);
            btn_send = itemView.findViewById(R.id.btn_send);
            root_view = itemView.findViewById(R.id.root_view);
            tv_status_0 = itemView.findViewById(R.id.tv_status_0);
            tv_status_1 = itemView.findViewById(R.id.tv_status_1);
            tv_has_vaucher = itemView.findViewById(R.id.tv_has_vaucher);
        }
    }
}
