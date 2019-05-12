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

public class Adapter_Finished extends RecyclerView.Adapter<Adapter_Finished.CardFinished>
{

    public interface CardFinishedCallback
    {
        void clickedCard(Model_Document document);
        void clickedDelete(Model_Document document);
        void clickedSend(Model_Document document);
        void clickedPhone(Model_Document document);
    }

    List<Model_Document> listOfDocuments = new ArrayList<>();
    private CardFinishedCallback callback;

    @NonNull
    @Override
    public CardFinished onCreateViewHolder(@NonNull ViewGroup viewGroup, int i)
    {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View card = inflater.inflate(R.layout.item_finished_dogovor,viewGroup,false);
        return new CardFinished(card);
    }

    @Override
    public void onBindViewHolder(@NonNull CardFinished cardFinished, int i)
    {
        final Model_Document document = listOfDocuments.get(i);
        String date = GlobalHelper.getDateString(document.getDate(),GlobalHelper.FORMAT_FULL_MONTH);
        String city = GlobalHelper.getCityOfDocument(document);
        String header = document.getCode()+" | "+date+" | "+city;

        cardFinished.tv_header.setText(header);
        cardFinished.tv_fio.setText(document.getFio());
        cardFinished.tv_phone.setText(document.getPhone());
        cardFinished.tv_adress.setText(city+" "+document.getAdress());
        cardFinished.tv_date.setText(date);

        cardFinished.root_view.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.clickedCard(document);
            }
        });

        cardFinished.btn_send.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.clickedSend(document);
            }
        });

        cardFinished.btn_delete.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                callback.clickedDelete(document);
            }
        });

        if(!TextUtils.isEmpty(document.getPhone()))
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
        if(listOfDocuments == null)
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


    class CardFinished extends RecyclerView.ViewHolder
    {
        TextView tv_header;
        TextView tv_fio;
        TextView tv_phone;
        TextView tv_adress;
        TextView tv_date;
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
        }
    }
}
