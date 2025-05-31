package com.example.edullm.recyclerViewEnfants;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.example.edullm.Models.User;
import com.example.edullm.R;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private List<User> userList;

    public UserAdapter(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_enfant, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.textEmail.setText(user.getEmail());
        holder.modif_pw.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("id",user.getId());
            Navigation.findNavController(v).navigate(R.id.action_gestion_enfants_to_change_pwd,bundle);
        });
        holder.textRole.setText("Enfant");
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView textEmail, textRole, modif_pw;


        public UserViewHolder(View itemView) {
            super(itemView);
            textEmail = itemView.findViewById(R.id.child_name);
            textRole = itemView.findViewById(R.id.child_level);
            modif_pw = itemView.findViewById(R.id.modif_pw);

        }
    }
}
