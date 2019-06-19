package com.dimfcompany.signpdfapp.base.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dimfcompany.signpdfapp.R;
import com.dimfcompany.signpdfapp.models.Model_User;
import com.dimfcompany.signpdfapp.utils.GlobalHelper;

import java.util.List;

public class AdapterRvUsers extends RecyclerView.Adapter<AdapterRvUsers.CardUser>
{
    public interface UsersListener
    {
        void clickedCard(Model_User user);
        void clickedRole(Model_User user);
    }

    List<Model_User> users;
    int colorBlue, colorGreen, colorRed;
    UsersListener listener;

    public AdapterRvUsers(Context context)
    {
        colorBlue = ResourcesCompat.getColor(context.getResources(), R.color.blue, null);
        colorGreen = ResourcesCompat.getColor(context.getResources(), R.color.green, null);
        colorRed = ResourcesCompat.getColor(context.getResources(), R.color.redBase, null);
    }

    @NonNull
    @Override
    public CardUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new CardUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardUser holder, int position)
    {
        Model_User user = users.get(position);

        holder.tv_name.setText(GlobalHelper.getFullName(user));
        holder.tv_email.setText(user.getEmail());
        holder.tv_docs_count.setText(user.getDocuments().size() + " документов");
        holder.tv_role.setText(user.getRole().getName());

        if (user.getRole_id() == 7)
        {
            holder.tv_role.setTextColor(colorGreen);
        }
        else if (user.getRole_id() == 999)
        {
            holder.tv_role.setTextColor(colorRed);
        }
        else
        {
            holder.tv_role.setTextColor(colorBlue);
        }

        holder.tv_role.setOnClickListener(view ->
        {
            listener.clickedRole(user);
        });

        holder.itemView.setOnClickListener((view) ->
        {
            listener.clickedCard(user);
        });
    }

    @Override
    public int getItemCount()
    {
        if (users == null)
        {
            return 0;
        }
        return users.size();
    }

    public void setUsers(List<Model_User> users)
    {
        this.users = users;
        notifyDataSetChanged();
    }

    public void setListener(UsersListener listener)
    {
        this.listener = listener;
    }

    static class CardUser extends RecyclerView.ViewHolder
    {
        private TextView tv_name;
        private TextView tv_email;
        private TextView tv_role;
        private TextView tv_docs_count;

        public CardUser(@NonNull View itemView)
        {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_email = itemView.findViewById(R.id.tv_email);
            tv_role = itemView.findViewById(R.id.tv_role);
            tv_docs_count = itemView.findViewById(R.id.tv_docs_count);
        }
    }
}
