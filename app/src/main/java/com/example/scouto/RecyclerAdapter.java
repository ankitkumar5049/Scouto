package com.example.scouto;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.scouto.database.DatabaseHelper;
import com.example.scouto.model.Car;

import java.util.ArrayList;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<Car> localDataSet;
    static DatabaseHelper db;
    private final int GALLERY_REQ_CODE = 1000;

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final TextView textView1;



        private  Button addImg;
        private  Button deleteCar;
        private  ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.tvCarMake);
            textView1 = (TextView) view.findViewById(R.id.tvCarId);
            deleteCar = view.findViewById(R.id.btnDeleteCar);
            addImg = view.findViewById(R.id.btnAddCarImage);
        }

        public TextView getTextView() {
            return textView;
        }
    }

    /**
     * Initialize the dataset of the Adapter
     *
     * @param dataSet String[] containing the data to populate views to be used
     * by RecyclerView
     */
    public RecyclerAdapter(ArrayList<Car> dataSet) {
        localDataSet = dataSet;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.base_car_view, viewGroup, false);

        ViewHolder holder = new ViewHolder(view);
        holder.addImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.textView.setText(localDataSet.get(position).getMakeName());
        viewHolder.textView1.setText(localDataSet.get(position).getMakeId());
        viewHolder.deleteCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DatabaseHelper(view.getContext(), "cars", null, 1);
                localDataSet.remove(viewHolder.getAdapterPosition());
                long r = db.deleteCarData(viewHolder.textView1.getText().toString());
                if(r>0) Log.e("TAG", "deleted Successfully: ", null);
                else Log.e("TAG", " not deleted Successfully: ", null);
                notifyDataSetChanged();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}