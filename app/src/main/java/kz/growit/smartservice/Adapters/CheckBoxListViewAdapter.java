package kz.growit.smartservice.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.rey.material.widget.CheckBox;
import com.rey.material.widget.TextView;

import java.util.ArrayList;

import kz.growit.smartservice.R;

/**
 * Created by Талгат on 07.07.2015.
 */
public class CheckBoxListViewAdapter extends ArrayAdapter<String> {
    private ArrayList<String> dataList;
    private Activity activity;
    public CheckBoxListViewAdapter(Activity activity, Context context, int textViewResId, ArrayList<String> someData){
        super(context,textViewResId,someData);
        this.dataList = new ArrayList<>();
        this.dataList.addAll(someData);
        this.activity = activity;
    }

    private class ViewHolder {
        TextView text;
        CheckBox cb;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView==null)
        {
            LayoutInflater vi = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.checkbox_listview_item, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.checkboxListViewItemText);
            holder.cb = (CheckBox) convertView.findViewById(R.id.checkboxListViewItemCheckBox);
            holder.cb.setTag(holder);
            convertView.setTag(holder.cb);

            holder.cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                }
            });
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v.getTag();
                    cb.setChecked(! cb.isChecked());
                }
            });
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(dataList.get(position));
        holder.cb.setCheckedImmediately(false);
        return convertView;
    }
}
