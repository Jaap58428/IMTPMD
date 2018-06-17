package nl.itsjaap.pmdfinal;

import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;

import nl.itsjaap.pmdfinal.database.DatabaseHelper;
import nl.itsjaap.pmdfinal.database.DatabaseInfo;

public class GraphActivity extends AppCompatActivity {

    private PieChart mChart;
    public static final int MAX_ECTS = 240;
    public static int currentEcts = 0;
    public static final int SLA_BARIER = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        mChart = (PieChart) findViewById(R.id.chart);
        mChart.setDescription(getString(R.string.graph_description));
        mChart.setDescriptionTextSize(14);
        mChart.setCenterTextSize(24);
        mChart.setTouchEnabled(false);
        mChart.setDrawSliceText(false);
        mChart.getLegend().setEnabled(true);
        mChart.getLegend().setTextSize(14);
        mChart.setTransparentCircleColor(Color.rgb(130, 130, 130));
        mChart.animateY(1800, Easing.EasingOption.EaseInOutQuad);


        // implement how much points the user currently has
        final String CURRENTUSER = getIntent().getExtras().getString(getString(R.string.currentUser));
        int mainPoints = 0;
        int extraPoints = 0;

        DatabaseHelper db = DatabaseHelper.getHelper(getApplicationContext());
        Cursor rs = db.query(DatabaseInfo.CourseTable.COURSETABLE, new String[]{"*"}, "user=? AND (isOpt=? OR (isOpt=? AND isActive=?))", new String[] { CURRENTUSER, "0", "1", "1"}, null, null, null);

        Log.d("db list", rs.getCount()+"");
        if (rs.getCount() > 0) {
            rs.moveToFirst();
            for (int i = 0 ; i < rs.getCount() ; i++) {
                String sGrade = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.GRADE));
                String sCredit = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.CREDITS));
                String isOpt = rs.getString(rs.getColumnIndex(DatabaseInfo.CourseColumn.ISOPT));


                if (sGrade == null) {
                    sGrade = "0";
                }

                Log.d("db entry", sGrade + " " + sCredit);

                double grade = Double.parseDouble(sGrade);
                int ects = Integer.parseInt(sCredit);

                // Only grades higher then 5.5 count toward ects
                if(grade >= 5.5 && isOpt.equals("0")) {
                    mainPoints += ects;
                } else if (grade >= 5.5 && isOpt.equals("1")) {
                    extraPoints += ects;
                }
                rs.moveToNext();
            }
        }

        // You can have a maximum of 4 x 3 ects from 4 completed optional courses
        if (extraPoints > 12) {
            extraPoints = 12;
            Toast.makeText(getApplicationContext(), R.string.graph_toast_tooManyOpt, Toast.LENGTH_LONG).show();
        }

        // The user completed their studies!
        if (extraPoints + mainPoints == MAX_ECTS) {
            Toast.makeText(getApplicationContext(), R.string.graph_toast_allPoints, Toast.LENGTH_LONG).show();
        }

        setData(mainPoints, extraPoints);


    }

    private void setData(int mainPoints, int extraPoints) {
        currentEcts = mainPoints + extraPoints;
        int remainingEcts = MAX_ECTS - currentEcts;
        ArrayList<Entry> yValues = new ArrayList<>();
        ArrayList<String> xValues = new ArrayList<>();

        //  http://www.materialui.co/colors
        ArrayList<Integer> colors = new ArrayList<>();

        yValues.add(new Entry(mainPoints, 0));
        xValues.add(getString(R.string.graph_completed));
        // color for mainPoints
        colors.add(Color.rgb(33,150,243));

        if (extraPoints != 0) {
            yValues.add(new Entry(extraPoints, 1));
            xValues.add(getString(R.string.graph_extra_completed));

            // color for extraPoints
            colors.add(Color.rgb(0,188,212));
        }

        yValues.add(new Entry(remainingEcts, 2));
        xValues.add(getString(R.string.graph_remaining));

        if (remainingEcts <= SLA_BARIER) {
            colors.add(Color.rgb(76,175,80));
        } else if (remainingEcts <= 60){
            colors.add(Color.rgb(139,195,74));
        } else if (remainingEcts <= 90){
            colors.add(Color.rgb(205,220,57));
        } else if (remainingEcts <= 120){
            colors.add(Color.rgb(255,235,59));
        } else if  (remainingEcts <= 150) {
            colors.add(Color.rgb(255,193,7));
        } else if  (remainingEcts <= 180) {
            colors.add(Color.rgb(255,152,0));
        } else if  (remainingEcts <= 210) {
            colors.add(Color.rgb(255,87,34));
        } else {
            colors.add(Color.rgb(244,67,54));
        }

        PieDataSet dataSet = new PieDataSet(yValues, "Credits");
        dataSet.setColors(colors);

        PieData data = new PieData(xValues, dataSet);
        data.setValueTextSize(15);
        mChart.setCenterText(Math.round(((double)currentEcts / MAX_ECTS) * 100) + "%");
        mChart.setData(data); // bind dataset aan chart.
        mChart.invalidate();  // Aanroepen van een redraw
    }
}
