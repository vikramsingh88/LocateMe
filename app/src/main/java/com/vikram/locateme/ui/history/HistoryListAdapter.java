package com.vikram.locateme.ui.history;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vikram.locateme.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by M1032130 on 7/8/2017.
 */

public class HistoryListAdapter extends RecyclerView.Adapter<HistoryListAdapter.ViewHolder> {
    private List<LocationHistory> listLocationHistory;
    private final OnItemClickListener listener;
    private Context mContext;

    public interface OnItemClickListener {
        void onItemClick(LocationHistory item);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textName;
        TextView txtContact;
        TextView txtTimeStamp;
        TextView txtAddress;
        RelativeLayout relTeamContainer;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.txt_name);
            txtContact = (TextView) itemView.findViewById(R.id.txt_contact);
            txtTimeStamp = (TextView) itemView.findViewById(R.id.txt_time);
            txtAddress = (TextView) itemView.findViewById(R.id.txt_address);
            relTeamContainer = (RelativeLayout) itemView.findViewById(R.id.contain_team);

        }

        public void bind(final LocationHistory item, final OnItemClickListener listener) {
            relTeamContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    HistoryListAdapter(Context context, List<LocationHistory> listLocationHistory, OnItemClickListener listener) {
        this.listLocationHistory = listLocationHistory;
        this.listener = listener;
        this.mContext = context;
    }

    @Override
    public HistoryListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_location_row, parent, false);
        ViewHolder vhItem = new ViewHolder(v, viewType);
        return vhItem;
    }

    @Override
    public void onBindViewHolder(final HistoryListAdapter.ViewHolder holder, final int position) {
        holder.textName.setText(listLocationHistory.get(position).getUserName());
        holder.txtContact.setText(listLocationHistory.get(position).getContact());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date result1 = dateFormat.parse(listLocationHistory.get(position).getTimeStamp());
            holder.txtTimeStamp.setText(result1.toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String lat = listLocationHistory.get(position).getLatlon().split(":")[0];
                String lon = listLocationHistory.get(position).getLatlon().split(":")[1];
                final String address = getCompleteAddressString(Double.valueOf(lat), Double.valueOf(lon));
                holder.txtAddress.post(new Runnable() {
                    @Override
                    public void run() {
                        holder.txtAddress.setText(address);
                    }
                });
            }
        }).start();

        holder.bind(listLocationHistory.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return listLocationHistory.size();
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.d("HistoryListAdapter", "" + strReturnedAddress.toString());
            } else {
                Log.d("HistoryListAdapter", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("HistoryListAdapter", "Canont get Address!");
        }
        return strAdd;
    }
}