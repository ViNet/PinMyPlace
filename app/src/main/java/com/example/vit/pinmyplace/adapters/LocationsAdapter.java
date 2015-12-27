package com.example.vit.pinmyplace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.vit.pinmyplace.R;
import com.example.vit.pinmyplace.models.UserLocation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LocationsAdapter extends ArrayAdapter<UserLocation> {

    Context context;
    List<UserLocation> locations;

    public LocationsAdapter(Context context) {
        super(context, R.layout.locations_item);

        this.context = context;
        this.locations = new ArrayList<UserLocation>();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.locations_item, parent, false);

            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tvItemTitle);
            holder.location = (TextView) convertView.findViewById(R.id.tvItemLocation);
            holder.date = (TextView) convertView.findViewById(R.id.tvItemDate);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            UserLocation location = locations.get(position);
            //Log.d(MyApp.TAG, location.toString());

            holder.title.setText(location.getLocationTitle());
            holder.location.setText(location.getLocationDescription());

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy", context.getResources().getConfiguration().locale);
            String currentDate = sdf.format(new Date(location.getCreatedAt()));

            holder.date.setText(currentDate);

        return convertView;
    }

    public void remove(int position){
        if(position != ListView.INVALID_POSITION && position <= locations.size()){
            locations.remove(position);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public UserLocation getItem(int position) {
        return locations.get(position);
    }

    public void setLocations(List<UserLocation> locations){
        this.locations.clear();
        this.locations = locations;
        notifyDataSetChanged();

    }


    static class ViewHolder{
        TextView title;
        TextView location;
        TextView date;
    }
}
