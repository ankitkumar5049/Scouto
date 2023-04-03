package com.example.scouto.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.scouto.OnRecyclerItemClick;
import com.example.scouto.R;
import com.example.scouto.RecyclerAdapter;
import com.example.scouto.database.DatabaseHelper;
import com.example.scouto.model.Car;
import com.example.scouto.model.Results;
import com.example.scouto.onClickListtner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    DatabaseHelper db;
    Button btnAddCar;
    AutoCompleteTextView tv;
    AutoCompleteTextView tv1;
    ArrayList<String> list1;
    ArrayList<Car> listCar;
    ArrayList<String> list2;
    ArrayList<Car> list;

    Button addCarImg;
    Button btnLogout;
    Button deleteCar;
    TextView carMake;
    TextView tvOverview;
    ImageView carImage;
    public static final int PICK_IMAGE = 1;
    public static final int GALLERY_REQ_CODE = 1000;
    Map<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listCar = new ArrayList<>();
        list1 = new ArrayList<>();
        list2 = new ArrayList<>();

        db = new DatabaseHelper(MainActivity.this, "cars", null, 1);
        recyclerView = findViewById(R.id.recylerView);
        tv = findViewById(R.id.autoCompleteTV);
        tv1 = findViewById(R.id.autoCompleteTV1);
        btnAddCar = findViewById(R.id.btnAddCar);
        btnLogout = findViewById(R.id.btnLogout);
        deleteCar = recyclerView.findViewById(R.id.btnDeleteCar);
        carImage = recyclerView.findViewById(R.id.imageCar);
        tvOverview = findViewById(R.id.tvOverview);

        list = db.getRecords();

        final int[] id = {-1};
        final String[] value = {""};
        final String[] value1 = {""};

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                value[0] = adapterView.getItemAtPosition(i).toString();
                id[0] = Integer.parseInt(map.get(value[0]));
                api2(id[0]);
                Log.e("TAG", "onItemSelected2: " + id[0], null);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.item_file, list2);
        adapter.setDropDownViewResource(R.layout.item_file);
        tv1.setAdapter(adapter);

        tv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                value1[0] = adapterView.getItemAtPosition(i).toString();
                onResume();
            }
        });

        layoutManager = new LinearLayoutManager(this);
        if (list != null && list.size() > 0) {
            tvOverview.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerAdapter = new RecyclerAdapter(list);
            recyclerAdapter.setOnRecyclerItemClick(new OnRecyclerItemClick() {
                @Override
                public void onClick(int position) {
                    Log.e("TAG", "onClick: add car image  button" + position, null);
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, GALLERY_REQ_CODE);
                }

                @Override
                public void onDeleteClick(int position) {

                }
            });
            recyclerView.setAdapter(recyclerAdapter);
            recyclerView.setLayoutManager(layoutManager);

        }


        btnAddCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (value[0].equals("") || value[0] == null || value1[0].equals("") || value1[0] == null) {
                    Toast.makeText(MainActivity.this, "Select both the fields", Toast.LENGTH_SHORT).show();
                } else {
                    long id = db.saveCarData(value[0], value1[0]);
                    if (id > 0) {
                        Toast.makeText(MainActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        list = db.getRecords();
                        if (list != null && list.size() > 0) {
                            tvOverview.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                        } else {
                            tvOverview.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        recyclerAdapter = new RecyclerAdapter(list);
                        recyclerView.setAdapter(recyclerAdapter);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerAdapter.notifyItemInserted(list.size() - 1);

                    } else {
                        Toast.makeText(MainActivity.this, "not added Successfully", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        api(listCar);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.item_file, list1);
        adapter.setDropDownViewResource(R.layout.item_file);
        tv.setAdapter(adapter);
    }

    public void api(List<Car> listCar) {
        String url = "https://vpic.nhtsa.dot.gov/api/vehicles/getallmakes?format=json";
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest
                stringRequest
                = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response.toString());
                            JSONArray array = object.getJSONArray("Results");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jo = array.getJSONObject(i);
                                String makeId = jo.getString("Make_ID");
                                String makeName = jo.getString("Make_Name");
                                list1.add(makeName);
                                map.put(makeName, makeId);
                                listCar.add(new Car(makeId, makeName));
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", "onResponse: error", null);
                    }
                });
        requestQueue.add(stringRequest);


    }

    public void api2(int id) {
        list2.clear();
        String url = "https://vpic.nhtsa.dot.gov/api/vehicles/GetModelsForMakeId/" + id + "?format=json";

        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);

        StringRequest
                stringRequest
                = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response.toString());
                            JSONArray array = object.getJSONArray("Results");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jo = array.getJSONObject(i);
//                                String makeId = jo.getString("Make_ID");
//                                String makeName = jo.getString("Make_Name");
//                                String modelId = jo.getString("Model_ID");
                                String modelName = jo.getString("Model_Name");
                                list2.add(modelName);
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", "onResponse: error", null);
                    }
                });
        requestQueue.add(stringRequest);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == GALLERY_REQ_CODE) {
                carImage.setImageURI(data.getData());
            }
        }
    }
}