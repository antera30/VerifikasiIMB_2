package com.example.fajar.verifikasiimb.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fajar.verifikasiimb.config.AppConfig;
import com.example.fajar.verifikasiimb.fragment.ItemFragment.OnListFragmentInteractionListener;
import com.example.fajar.verifikasiimb.R;
import com.example.fajar.verifikasiimb.function.GPSTracker;
import com.example.fajar.verifikasiimb.model.Bangunan;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<Bangunan> mValues;
    private final OnListFragmentInteractionListener mListener;
    private Context mContext;
    private Bitmap image;
    private String encodedImage;
    private static final int MAX_WIDTH = 500;
    private static final int MAX_HEIGHT = 400;
    private GPSTracker gps;

    public MyItemRecyclerViewAdapter(List<Bangunan> items, OnListFragmentInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
        holder.mContentView.setText(mValues.get(position).getNamaJalan());
        gps = new GPSTracker(mContext);
        double latitude1, longitude1, latitude2, longitude2;
        if(gps.canGetLocation()){
            latitude1 = gps.getLatitude();
            longitude1 = gps.getLongitude();
            latitude2 = mValues.get(position).getLatitude();
            longitude2 = mValues.get(position).getLongitude();
            int distance = (int) distFrom(latitude1, longitude1, latitude2, longitude2);
            holder.mDistanceView.setText(decimalFormatter(distance)+" meter");
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        encodedImage = ""+AppConfig.BASE_URL+mValues.get(position).getGambarBangunan();
        if (encodedImage != null) {
            Picasso.with(mContext)
                    .load(encodedImage)
                    .resize(MAX_WIDTH, MAX_HEIGHT)
                    .centerCrop()
                    .into(holder.mGambarBangunan);
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    public String decimalFormatter(double number){
        DecimalFormat decimalFormat = new DecimalFormat("#,###,###");
        String formattedNumber = decimalFormat.format(number);
        return formattedNumber;
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mDistanceView;
        public final ImageView mGambarBangunan;
        public Bangunan mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.pemilik);
            mDistanceView = (TextView) view.findViewById(R.id.jarak);
            mContentView = (TextView) view.findViewById(R.id.alamat);
            mGambarBangunan = (ImageView) view.findViewById(R.id.gambar_bangunan);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public static double distFrom(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = (double) (earthRadius * c);
        int intdis = (int) dist;

        return intdis;
    }

}
