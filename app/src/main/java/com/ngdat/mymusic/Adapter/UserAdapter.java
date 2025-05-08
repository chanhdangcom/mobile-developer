package com.ngdat.mymusic.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Model.User;
import com.ngdat.mymusic.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    Context context;
    List<User> list;

    public UserAdapter(Context context,List<User> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_taikhoan, parent, false);
        return new UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder h, int pos) {
        User u = list.get(pos);
        h.tvHoTen.setText(u.getFullName());
        h.tvTaiKhoan.setText(u.getUsername());
        h.tvEmail.setText(u.getEmail());
        h.tvRole.setText(u.getRole());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTen, tvTaiKhoan, tvEmail, tvRole;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tvhoten);
            tvTaiKhoan = itemView.findViewById(R.id.tvtaikhoan);
            tvEmail = itemView.findViewById(R.id.tvemail);
            tvRole = itemView.findViewById(R.id.tvrole);
        }
    }
}
