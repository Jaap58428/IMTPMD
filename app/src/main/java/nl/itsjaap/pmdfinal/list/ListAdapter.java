package nl.itsjaap.pmdfinal.list;

/**
 * @author Jaap Kanbier s1100592
 * git: https://github.com/Jaap58428/IMTPMD
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import nl.itsjaap.pmdfinal.R;
import nl.itsjaap.pmdfinal.models.CourseModel;

public class ListAdapter extends ArrayAdapter<CourseModel> {

    public ListAdapter(Context context, int resource, List<CourseModel> objects){
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null ) {
            vh = new ViewHolder();
            LayoutInflater li = LayoutInflater.from(getContext());
            convertView = li.inflate(R.layout.view_content_row, parent, false);
            vh.title = (TextView) convertView.findViewById(R.id.subject_name);
            vh.info = (TextView) convertView.findViewById(R.id.subject_code);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        CourseModel cm = getItem(position);
        String sOpt;
        if (cm.getIsOpt().equals(1)) {
            sOpt = "Keuzevak";
        } else {
            sOpt = "Verplicht";
        }

        vh.title.setText(cm.getName() + " - " + cm.getYear() + "." + cm.getPeriod());
        vh.info.setText("Cijfer: " + cm.getGrade()+ "\t" + sOpt);
        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView info;
    }
}
