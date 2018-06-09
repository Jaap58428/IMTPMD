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

public class ListAdapterOpt extends ArrayAdapter<CourseModel> {



    public ListAdapterOpt(Context context, int resource, List<CourseModel> objects){
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
        if (cm.getIsActive().equals("1")) {
            sOpt = getContext().getString(R.string.courseOpt_following);
        } else {
            sOpt = getContext().getString(R.string.courseOpt_notFollowing);
        }

        String sGrade;
        Double dGrade;
        if (cm.getGrade() == null) {
            sGrade = getContext().getString(R.string.courseList_blank_grade);
        } else {
            sGrade = cm.getGrade();

//             Removed this implementation due to recycling list making incorrect colours appear

//            dGrade = Double.valueOf(sGrade);
//            if (dGrade >= 5.5) {
//                vh.title.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGradeSuccess));
//            } else if (dGrade < 5.5){
//                vh.title.setTextColor(ContextCompat.getColor(getContext(), R.color.colorGradeFailed));
//            }
        }




        String rowTitle = cm.getName() + " - " + sGrade;
        vh.title.setText(rowTitle);

        String rowInfo =
                getContext().getString(R.string.courseList_year) + " " +
                cm.getYear() + " - " +
                getContext().getString(R.string.courseList_period) + " " +
                cm.getPeriod() + " - " +
                sOpt;
        vh.info.setText(rowInfo);

        return convertView;
    }

    private static class ViewHolder {
        TextView title;
        TextView info;
    }
}
