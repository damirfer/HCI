package com.example.mydrink.ui.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mydrink.R;
import com.example.mydrink.data.model.CocktailModel;
import com.example.mydrink.data.model.HomeModel;
import com.example.mydrink.fragments.SingleDrinkActivity;
import com.example.mydrink.helper.MyApiService;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchFragment extends Fragment {

    private ListView lvDrinks;
    private ProgressBar spinner;
    private EditText editText;
    private TextView errorMessage;
    private MyApiService myApiService;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_search, container, false);

        lvDrinks = root.findViewById((R.id.resultList));
        spinner = root.findViewById((R.id.progressBarSearch));
        editText = root.findViewById(R.id.nameInput);
        errorMessage = root.findViewById(R.id.errorMessage);
        Button searchButton = root.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_searchButtonClick();
            }
        });
        spinner.setVisibility(View.GONE);
        errorMessage.setVisibility(View.GONE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.thecocktaildb.com/api/json/v2/9973533/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApiService = retrofit.create(MyApiService.class);

        return root;
    }


    private void do_searchButtonClick() {
        String query = editText.getText().toString();
        loadData(query);
    }

    private void loadData(String query) {
        spinner.setVisibility(View.VISIBLE);
        errorMessage.setVisibility(View.GONE);

        Call<HomeModel> call = myApiService.getByName(query);
        call.enqueue(new Callback<HomeModel>() {
            @Override
            public void onResponse(Call<HomeModel> call, Response<HomeModel> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                HomeModel drinks = response.body();
                List<CocktailModel> drinksList = drinks.getDrinks();

                if(drinksList == null) {
                    errorMessage.setText("No drinks have been found");
                    errorMessage.setVisibility(View.VISIBLE);
                    spinner.setVisibility(View.GONE);

                } else {
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


            }
            @Override
            public void onFailure(Call<HomeModel> call, Throwable t) {
            }
        });

    }




}
