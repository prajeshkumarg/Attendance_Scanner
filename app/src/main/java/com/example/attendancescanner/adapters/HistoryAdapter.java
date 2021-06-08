package com.example.attendancescanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendancescanner.R;
import com.example.attendancescanner.models.AttendanceObject;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryView> {


    private List<AttendanceObject> attendanceObjects_list;

    public HistoryAdapter(List<AttendanceObject> attendanceObjects_list) {
        this.attendanceObjects_list = attendanceObjects_list;
    }

    @NonNull
    @Override
    public HistoryView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.history_adapter,parent,false);
        return new HistoryView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryView holder, int position) {
        holder.title.setText(attendanceObjects_list.get(position).getAttendanceTitle());
        List<String> contents=attendanceObjects_list.get(position).getAttendanceContent();
        int size=contents.size();
        holder.name.setText(getData(size,contents));
    }
    private String getData(int n,List<String> arr) {
        StringBuilder names= new StringBuilder();
        for(int i=0;i<n;i++){
            if(i<n-1)
                names.append(arr.get(i)).append("\n");
            else
                names.append(arr.get(i));
        }
        return names.toString();
    }
    @Override
    public int getItemCount() {
        return attendanceObjects_list.size();
    }

    public static class  HistoryView extends RecyclerView.ViewHolder{

        TextView title,name;
        public HistoryView(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.title);
            name=itemView.findViewById(R.id.names);
        }
    }
}
