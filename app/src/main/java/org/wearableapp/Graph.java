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

import java.util.Random;

public class Graph extends Fragment {
    private final Handler mHandler = new Handler();
    private Runnable mTimer1;
    private Runnable mTimer2;
    private LineGraphSeries<DataPoint> oxygenSeries;
    private LineGraphSeries<DataPoint> pulseSeries;
    private double graph2LastXValue = 5d;

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
        pulseGraph.getViewport().setMaxY(200);
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
        mTimer1 = new Runnable() {
            @Override
            public void run() {
                oxygenSeries.resetData(generateData());
                mHandler.postDelayed(this, 300);
            }
        };
        mHandler.postDelayed(mTimer1, 300);

        mTimer2 = new Runnable() {
            @Override
            public void run() {
                graph2LastXValue += 1d;
                pulseSeries.appendData(new DataPoint(graph2LastXValue, getRandom()), true, 40);
                mHandler.postDelayed(this, 200);
            }
        };
        mHandler.postDelayed(mTimer2, 1000);
    }

    @Override
    public void onPause() {
        mHandler.removeCallbacks(mTimer1);
        mHandler.removeCallbacks(mTimer2);
        super.onPause();
    }

    private DataPoint[] generateData() {
        double mLastRandom = 80;
        int count = 3;
        DataPoint[] values = new DataPoint[count];
        int sinal = -1;
        for (int i=0; i<count; i++) {
            double x = i;
            double y = mLastRandom + mRand.nextDouble() * 5.5 * sinal;
            if (sinal == 1) {
                sinal = -1;
            }
            else {
                sinal = 1;
            }
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    double mLastRandom = 80;
    int sinal = -1;
    Random mRand = new Random();

    private double getRandom() {
        if (sinal == 1) {
            sinal = -1;
        }
        else {
            sinal = 1;
        }
        return mLastRandom += mRand.nextDouble() * 20.5 * sinal;
    }
}
