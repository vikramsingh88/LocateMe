package com.vikram.locateme.ui.main.contact;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.vikram.locateme.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by M1032130 on 6/29/2017.
 */

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {
    private List<Contact> listContact, mFilterList;
    private final OnItemClickListener listener;
    Context mContext;

    public interface OnItemClickListener {
        void onItemClick(Contact item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        ImageView imageView;
        TextView textNumber;
        RelativeLayout relTeamContainer;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.txt_name);
            textNumber= (TextView) itemView.findViewById(R.id.txt_number);
            imageView = (ImageView) itemView.findViewById(R.id.row_icon);
            relTeamContainer = (RelativeLayout) itemView.findViewById(R.id.contain_team);

        }

        public void bind(final Contact item, final OnItemClickListener listener) {
            relTeamContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    ContactListAdapter(List<Contact> listContact, Context context, OnItemClickListener listener) {
        this.listContact = listContact;
        this.mFilterList = new ArrayList<>();
        this.mFilterList.addAll(this.listContact);
        this.listener = listener;
        this.mContext = context;
    }

    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_contact, parent, false);
        ViewHolder vhItem = new ViewHolder(v, viewType);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ContactListAdapter.ViewHolder holder, int position) {
        holder.textName.setText(mFilterList.get(position).getName());
        holder.textNumber.setText(mFilterList.get(position).getNumber());
        //get first letter of each String item
        String firstLetter = String.valueOf(mFilterList.get(position).getName().charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(mFilterList.get(position));
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color); // radius in px
        holder.imageView.setImageDrawable(drawable);

        holder.bind(mFilterList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return (null != mFilterList ? mFilterList.size() : 0);
    }

    //Searching item
    public void filter(final String text) {
        // Searching could be complex..so we will dispatch it to a different thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Clear the filter list
                mFilterList.clear();
                // If there is no search value, then add all original list items to filter list
                if (TextUtils.isEmpty(text)) {

                    mFilterList.addAll(listContact);

                } else {
                    // Iterate in the original List and add it to filter list
                    for (Contact item : listContact) {
                        if (item.getName().toLowerCase().contains(text.toLowerCase()) ) {
                            // Adding Matched items
                            mFilterList.add(item);
                        }
                    }
                }
                // Set on UI Thread
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Notify the List that the DataSet has changed
                        notifyDataSetChanged();
                    }
                });

            }
        }).start();
    }

}