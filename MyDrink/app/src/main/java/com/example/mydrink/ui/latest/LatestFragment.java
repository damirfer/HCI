package com.example.mydrink.ui.latest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.mydrink.R;
import com.example.mydrink.data.model.CocktailModel;
import com.example.mydrink.data.model.HomeModel;
import com.example.mydrink.fragments.SingleDrinkActivity;
import com.example.mydrink.helper.MyApiService;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LatestFragment extends Fragment {

    private ListView lvDrinks;
    private ProgressBar spinner;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_latest, container, false);

        lvDrinks = root.findViewById((R.id.lvLatestDrinks));
        spinner = root.findViewById((R.id.progressBarLatest));
        spinner.setVisibility(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.thecocktaildb.com/api/json/v2/9973533/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyApiService myApiService = retrofit.create(MyApiService.class);


        Call<HomeModel> call = myApiService.getLatest();
        call.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                HomeModel drinks = response.body();
                List<CocktailModel> drinksList = drinks.getDrinks();
                lvDrinks.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return drinksList.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return null;
                    }

                    @Override
                    public long getItemId(int position) {
                        return 0;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        if(convertView == null) {
                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            convertView = inflater.inflate(R.layout.drink_list_item, parent, false);
                        }
                        TextView drinkName = convertView.findViewById(R.id.drinkName);
                        TextView drinkAbout = convertView.findViewById(R.id.drinkAbout);

                        CocktailModel model = drinksList.get(position);
                        drinkName.setText(model.getStrDrink());
                        drinkAbout.setText(model.getStrCategory());

                        return convertView;
                    }
                });

                lvDrinks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getActivity(), SingleDrinkActivity.class);
                        Bundle bundle = new Bundle();
                        CocktailModel object = (CocktailModel) drinksList.get(position);
                        bundle.putSerializable("SingleDrink", (Serializable) object);
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }
                });
                spinner.setVisibility(View.GONE);


            }
            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
            }
        });

        return root;
    }
}
