package com.example.vit.pinmyplace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.vit.pinmyplace.R;
import com.example.vit.pinmyplace.models.UserLocation;

import java.util.ArrayList;
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

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

            UserLocation location = locations.get(position);
            holder.title.setText(location.getLocationTitle());
            holder.location.setText(location.getLocationDescription());
        }
        return convertView;
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    public void setLocations(List<UserLocation> locations){
        this.locations = locations;
        notifyDataSetChanged();
    }


    static class ViewHolder{
        TextView title;
        TextView location;
    }
}
