package com.vikram.locateme.ui.main.approve;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.vikram.locateme.R;

import java.util.List;

/**
 * Created by M1032130 on 7/1/2017.
 */

public class ApproveListAdapter extends RecyclerView.Adapter<ApproveListAdapter.ViewHolder> {
    private List<ApproveContact> listPending;
    private final OnItemClickListener listener;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(ApproveContact item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        ImageView imageView;
        TextView textNumber;
        RelativeLayout relTeamContainer;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.txt_name);
            textNumber = (TextView) itemView.findViewById(R.id.txt_number);
            imageView = (ImageView) itemView.findViewById(R.id.row_icon);
            relTeamContainer = (RelativeLayout) itemView.findViewById(R.id.contain_team);

        }

        public void bind(final ApproveContact item, final OnItemClickListener listener) {
            relTeamContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    ApproveListAdapter(Context context, List<ApproveContact> listPending, OnItemClickListener listener) {
        this.listPending = listPending;
        this.listener = listener;
        this.mContext = context;
    }

    @Override
    public ApproveListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_contact, parent, false);
        ViewHolder vhItem = new ViewHolder(v, viewType);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(ApproveListAdapter.ViewHolder holder, int position) {
        holder.textName.setText(getContactName(mContext, listPending.get(position).getApprove()));
        holder.textNumber.setText(listPending.get(position).getApprove());
        //get first letter of each String item
        String firstLetter = String.valueOf(getContactName(mContext, listPending.get(position).getApprove()).charAt(0));
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        // generate random color
        int color = generator.getColor(listPending.get(position));
        TextDrawable drawable = TextDrawable.builder().buildRound(firstLetter, color); // radius in px
        holder.imageView.setImageDrawable(drawable);

        holder.bind(listPending.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return listPending.size();
    }

    public static String getContactName(Context context, String phoneNumber) {
        String contactName = "Unknown";
        if (phoneNumber != null && !phoneNumber.equals("")) {
            ContentResolver cr = context.getContentResolver();
            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
            if (cursor == null) {
                return contactName;
            }

            if(cursor.moveToFirst()) {
                contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
            }

            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return contactName;
    }

}