package com.ngdat.mymusic.Adapter;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.ngdat.mymusic.Model.User;
import com.ngdat.mymusic.R;
import com.ngdat.mymusic.utils.DatabaseHelper;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    Context context;
    DatabaseHelper databaseHelper;
    List<User> list;

    public UserAdapter(Context context,List<User> list) {
        this.context = context;
        this.list = list;
        this.databaseHelper = new DatabaseHelper(context);  // Khởi tạo databaseHelper
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

        h.itemView.setOnLongClickListener(v -> {
            h.btnDelete.setTranslationX(200); // Di chuyển sang phải (ẩn ngoài màn hình)
            h.btnDelete.setAlpha(0f);         // Ẩn
            h.btnDelete.setVisibility(View.VISIBLE);

            h.btnDelete.animate()
                    .translationX(0)          // Di chuyển về vị trí gốc
                    .alpha(1f)                // Hiện mờ dần
                    .setDuration(300)
                    .start();

            // Tự động ẩn sau 3s
            new Handler().postDelayed(() -> {
                h.btnDelete.animate()
                        .translationX(200)
                        .alpha(0f)
                        .setDuration(300)
                        .withEndAction(() -> h.btnDelete.setVisibility(View.GONE))
                        .start();
            }, 2000);

            return true;
        });



        h.btnDelete.setOnClickListener(v -> {
            int userId = u.getId(); // Lấy ID của người dùng cần xóa
            if (userId != 0) { // Kiểm tra nếu ID không phải là 0
                boolean isDeleted = databaseHelper.deleteUserById(userId); // Gọi phương thức xóa
                if (isDeleted) {
                    list.remove(pos); // Xóa người dùng khỏi danh sách
                    notifyItemRemoved(pos); // Cập nhật lại danh sách
                    notifyItemRangeChanged(pos, list.size()); // Cập nhật lại các vị trí còn lại
                } else {
                    // Hiển thị thông báo nếu không xóa được
                    Toast.makeText(context, "Xóa thất bại!", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Thông báo nếu ID không hợp lệ
                Toast.makeText(context, "ID không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvHoTen, tvTaiKhoan, tvEmail, tvRole;
        Button btnDelete;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHoTen = itemView.findViewById(R.id.tvhoten);
            tvTaiKhoan = itemView.findViewById(R.id.tvtaikhoan);
            tvEmail = itemView.findViewById(R.id.tvemail);
            tvRole = itemView.findViewById(R.id.tvrole);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
