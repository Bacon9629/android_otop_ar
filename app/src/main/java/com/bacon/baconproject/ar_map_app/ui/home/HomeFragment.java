package com.bacon.baconproject.ar_map_app.ui.home;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bacon.baconproject.ar_map_app.R;
import com.bacon.baconproject.ar_map_app.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private Context context;

    private FragmentHomeBinding binding;
    private ImageView map_point_img;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = getActivity();

        map_point_img = root.findViewById(R.id.map_point);
        map_point_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                View v = LayoutInflater.from(context).inflate(R.layout.alert_map, null);
                builder.setView(v);
                builder.show().getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}