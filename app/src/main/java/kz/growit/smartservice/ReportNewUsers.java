package kz.growit.smartservice;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class ReportNewUsers extends AppCompatActivity {
    private HashMap<String, Integer> counter = new HashMap<>();
    private LineChart mChart;
    private int max = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_new_users);
        mChart = (LineChart) findViewById(R.id.chart);

        String url = AppController.getInstance().SMART_SERVICE_URL +
                "api/UserProfilesApi/SearchUserProfiles?SearchQuery=&CityId=0&RegionId=0&ServiceCategoryId=0";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                url,
                new JSONObject(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject profile;
                            try {
                                profile = response.getJSONObject(i);
//                                String tempStr = profile.getString("ProfileUpdateDate").substring(0,10);
                                Date someDate = AppController.getInstance().fromServerStringToDate(profile.getString("ProfileUpdateDate"));
                                String format = "dd.MM.yyyy";
                                SimpleDateFormat sdf = new SimpleDateFormat(format);
                                if (someDate != null) {
                                    String tempS = sdf.format(someDate);
                                    if (counter.containsKey(tempS)) {
                                        counter.put(tempS, counter.get(tempS) + 1);
                                        if (counter.get(tempS) > max)
                                            max = counter.get(tempS);
                                    } else {
                                        counter.put(tempS, 1);
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        doChart();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("error report", error.getLocalizedMessage());
                    }
                }
        );
        AppController.getInstance().addToRequestQueue(jsonArrayRequest, "reports request");
    }

    private void addEntry(String date) {
        //
        LineData data = mChart.getData();
        if (data != null) {
            LineDataSet set = data.getDataSetByIndex(0);
            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }
            // add a new value
            //
            data.addXValue(date);
            data.addEntry(new Entry(Float.valueOf(counter.get(date)), set.getEntryCount()), 0);
            mChart.notifyDataSetChanged();
            mChart.setVisibleXRange(6);
            mChart.moveViewToX(data.getXValCount() - 7);
        }
    }

    private LineDataSet createSet() {
        LineDataSet set = new LineDataSet(null, "test");
        set.setDrawCubic(true);
        set.setCubicIntensity(0.2f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(ColorTemplate.getHoloBlue());
        set.setCircleColor(ColorTemplate.getHoloBlue());
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighLightColor(getResources().getColor(R.color.primary));
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);
        return set;
    }

    private void doChart() {
        mChart.setDescription(getResources().getString(R.string.report_new_users_title_ru));
        mChart.setNoDataText(getResources().getString(R.string.no_data_for_report_ru));

        mChart.setHighlightEnabled(true);
        mChart.setTouchEnabled(true);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setPinchZoom(true);

        LineData data = new LineData();

        mChart.setData(data);

        Legend l = mChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);
        XAxis xl = mChart.getXAxis();
        xl.setTextColor(Color.BLUE);
        xl.setDrawGridLines(true);
        xl.setAvoidFirstLastClipping(true);

        YAxis yl = mChart.getAxisLeft();
        yl.setTextColor(Color.BLUE);
        yl.setAxisMaxValue(max * 1.2f);
        yl.setAxisMinValue(- max * 0.2f);
        yl.setDrawGridLines(true);

        YAxis yl2 = mChart.getAxisRight();
        yl2.setEnabled(false);
        Map<Date, Integer> m = new HashMap<Date, Integer>();
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        for (Object o : counter.entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            try {
                m.put(new Date(dateFormat.parse(pair.getKey().toString()).getTime()), Integer.parseInt(pair.getValue().toString()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Map<Date, Integer> m1 = new TreeMap(m);
        for (Map.Entry<Date, Integer> entry : m1.entrySet()) {
            addEntry(dateFormat.format(entry.getKey()));
        }
        mChart.notifyDataSetChanged();
    }

}
