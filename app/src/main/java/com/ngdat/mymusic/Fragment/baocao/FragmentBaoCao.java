package com.ngdat.mymusic.Fragment.baocao;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.ngdat.mymusic.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentBaoCao extends Fragment implements IBaoCaoContract.IView {
    private LineChart lineChart;
    private RecyclerView recycleSong;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_baocao, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lineChart = view.findViewById(R.id.lineChart);
        recycleSong = view.findViewById(R.id.recycleSong);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycleSong.setLayoutManager(linearLayoutManager);
        IBaoCaoContract.IPresenter presenter = new BaoCaoPresenter(this);
        presenter.getSongs();
    }

    @Override
    public void showSongs(List<Song> songs1,
                          List<Song> songs2,
                          List<Song> songs3,
                          List<Song> songs4,
                          List<Song> songs5) {
        List<Entry> lineEntries1 = new ArrayList<>();
        List<Entry> lineEntries2 = new ArrayList<>();
        List<Entry> lineEntries3 = new ArrayList<>();
        List<Entry> lineEntries4 = new ArrayList<>();
        List<Entry> lineEntries5 = new ArrayList<>();

        for (int i = 0; i < songs1.size(); i++) {
            lineEntries1.add(new Entry(i + 1, songs1.get(i).getRank()));
            lineEntries2.add(new Entry(i + 1, songs2.get(i).getRank()));
            lineEntries3.add(new Entry(i + 1, songs3.get(i).getRank()));
            lineEntries4.add(new Entry(i + 1, songs4.get(i).getRank()));
            lineEntries5.add(new Entry(i + 1, songs5.get(i).getRank()));
        }

        LineDataSet dataSet1 = new LineDataSet(lineEntries1, null);
        dataSet1.setValueTextSize(0f);
        dataSet1.setCircleColor(Color.TRANSPARENT);
        dataSet1.setCircleHoleColor(Color.GREEN);
        dataSet1.setColor(Color.GREEN);

        LineDataSet dataSet2 = new LineDataSet(lineEntries2, null);
        dataSet2.setValueTextSize(0f);
        dataSet2.setCircleColor(Color.TRANSPARENT);
        dataSet2.setCircleHoleColor(Color.RED);
        dataSet2.setColor(Color.RED);

        LineDataSet dataSet3 = new LineDataSet(lineEntries3, null);
        dataSet3.setValueTextSize(0f);
        dataSet3.setCircleColor(Color.TRANSPARENT);
        dataSet3.setCircleHoleColor(Color.BLUE);
        dataSet3.setColor(Color.BLUE);

        LineDataSet dataSet4 = new LineDataSet(lineEntries4, null);
        dataSet4.setValueTextSize(0f);
        dataSet4.setCircleColor(Color.TRANSPARENT);
        dataSet4.setCircleHoleColor(Color.YELLOW);
        dataSet4.setColor(Color.YELLOW);

        LineDataSet dataSet5 = new LineDataSet(lineEntries5, null);
        dataSet5.setValueTextSize(0f);
        dataSet5.setCircleColor(Color.TRANSPARENT);
        dataSet5.setCircleHoleColor(Color.BLACK);
        dataSet5.setColor(Color.BLACK);

        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet1);
        dataSets.add(dataSet2);
        dataSets.add(dataSet3);
        dataSets.add(dataSet4);
        dataSets.add(dataSet5);
        LineData lineData = new LineData(dataSets);
        lineChart.setData(lineData);
// Hiển thị nền kẻ ô
        lineChart.setDrawGridBackground(false);
        // Mô tả của biểu đồ
//        Description description = new Description();
        lineChart.setDescription(null);
        // Có thể chạm vào biểu đồ hay không
        lineChart.setTouchEnabled(false);

        // Lấy ra trục Ox
        XAxis xAxis = lineChart.getXAxis();
        // Đặt trục Ox ở vị trí dưới
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        // Vẽ các đường thẳng dọc mỗi giá trị trên Ox
        xAxis.setDrawGridLines(false);
        // Vẽ đường thẳng Ox
        xAxis.setDrawAxisLine(true);
        // Giá trị nhỏ nhất trên Ox: MIN
        xAxis.setAxisMinimum(1f);
        // Giá trị lớn nhất trên Ox: MAX
        xAxis.setAxisMaximum(12);
        // Số cột trên Ox, 2 giá trị tạo 1 cột: COUNT -> (MAX - MIN)/COUNT = độ chia nhỏ nhất
        xAxis.setLabelCount(12, true);

        // Lấy ra trục 0y bên trái
        YAxis yAxis = lineChart.getAxisLeft();
        // Hiển thị đường nét đứt
        yAxis.enableGridDashedLine(5.0f, 5.0f, 0.0f);
        // Màu của trục Oy
        yAxis.setAxisLineColor(Color.TRANSPARENT);
//        yAxis.setAxisLineColor(Color.BLACK);
        // Giá trị nhỏ nhất trên Oy: MIN
        yAxis.setAxisMinimum(0f);
        // Giá trị lớn nhất trên Oy: MAX
        yAxis.setAxisMaximum(20f);
        // Số dòng trên Oy, mỗi giá trị 1 dòng
        yAxis.setLabelCount(6, true);
        yAxis.setDrawLabels(false);

        // Hiển thị chú thích trên biểu đồ hay không
        lineChart.getLegend().setEnabled(false);
        // Ẩn trục Oy bên phải
        lineChart.getAxisRight().setEnabled(false);

        lineChart.notifyDataSetChanged();
        lineChart.invalidate();

        List<Song> songs = new ArrayList<>();
        songs.add(songs1.get(0));
        songs.add(songs2.get(0));
        songs.add(songs3.get(0));
        songs.add(songs4.get(0));
        songs.add(songs5.get(0));
        SongAdapter songAdapter = new SongAdapter(songs);
        recycleSong.setAdapter(songAdapter);
    }
}
