package Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.raul.mychatapp.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Raul on 5/30/2017.
 */

public class MyAdapter extends ArrayAdapter<String> {

    private ArrayList<String> list;
    private final Context mContext;

    public MyAdapter(Context context, ArrayList<String> list){
        super(context,R.layout.item_room,list);
        mContext=context;
        this.list=list;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_room,parent,false);
        }
        TextView tv=(TextView)convertView.findViewById(R.id.textView4);
        tv.setText(list.get(position));



        return convertView;
    }
}
