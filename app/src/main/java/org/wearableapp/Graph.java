package org.wearableapp;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class Graph extends Fragment {
    private final Handler mHandler = new Handler();
    private Runnable timer;
    private LineGraphSeries<DataPoint> oxygenSeries;
    private LineGraphSeries<DataPoint> pulseSeries;
    private double graph2LastXValue = 0d;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_graph, container, false);

        GraphView oxygenGraph = (GraphView) rootView.findViewById(R.id.oxygen);
        oxygenGraph.getViewport().setYAxisBoundsManual(true);
        oxygenGraph.getViewport().setMinY(0);
        oxygenGraph.getViewport().setMaxY(100);
        oxygenGraph.setTitle("Oxigenação Sanguínea");
        oxygenGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        oxygenSeries = new LineGraphSeries<>();
        oxygenSeries.setColor(Color.RED);
        oxygenGraph.addSeries(oxygenSeries);

        GraphView pulseGraph = (GraphView) rootView.findViewById(R.id.pulse);
        pulseGraph.getViewport().setYAxisBoundsManual(true);
        pulseGraph.getViewport().setMinY(0);
        pulseGraph.getViewport().setMaxY(1024);
        pulseGraph.setTitle("Frequência Cardíaca");
        pulseGraph.getGridLabelRenderer().setHorizontalLabelsVisible(false);

        pulseSeries = new LineGraphSeries<>();
        pulseSeries.setColor(Color.RED);
        pulseGraph.addSeries(pulseSeries);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        timer = new Runnable() {
            @Override
            public void run() {
                if(Measurement.REMEASUREMENT) {
                    graph2LastXValue += 1d;
                    pulseSeries.appendData(new DataPoint(graph2LastXValue, Measurement.PULSE_RATE), true, 40);
                    oxygenSeries.appendData(new DataPoint(graph2LastXValue, Measurement.OXYGEN), true, 40);
                    Measurement.REMEASUREMENT = false;
                }
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(timer, 1000);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(timer);
        super.onPause();
    }
}
