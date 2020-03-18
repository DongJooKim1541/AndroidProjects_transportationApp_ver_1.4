package com.example.busappver14.ui.subway;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.busappver14.R;

import java.util.ArrayList;

public class SubwayStationListSearchViewModelAdapter extends ArrayAdapter<SubwayItems> implements AdapterView.OnItemClickListener {

    private Context context;
    private ArrayList<SubwayItems> list;
    private SubwayItems si;
    private int resource;
    private ListView myListView;

    public SubwayStationListSearchViewModelAdapter(Context context, int resource, ArrayList<SubwayItems> objects, ListView myListView) {
        super(context,resource,objects);
        list=objects;
        this.context=context;
        this.resource=resource;
        this.myListView=myListView;
        this.myListView.setOnItemClickListener(this);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        LayoutInflater linf=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView=linf.inflate(resource,null);

        si=list.get(position);

        if(si!=null){
            TextView subwayRouteName=(TextView) convertView.findViewById(R.id.subwayRouteName);
            TextView subwayStationId=(TextView) convertView.findViewById(R.id.subwayStationId);
            TextView subwayStationName=(TextView) convertView.findViewById(R.id.subwayStationName);

            if(subwayRouteName!=null){
                subwayRouteName.setText("지하철 호선: "+si.getSubwayRouteName());
            }
            if(subwayStationId!=null){
                subwayStationId.setText("지하철역 Id: "+si.getSubwayStationId());
            }
            if(subwayStationName!=null){
                subwayStationName.setText("지하철역: "+si.getSubwayStationName());
            }
        }

        return convertView;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
