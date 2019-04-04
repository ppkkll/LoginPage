package com.fantasy_travel.loginpage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fantasy_travel.loginpage.PlanModel;
import com.fantasy_travel.loginpage.RecyclerViewAdapter;
import com.fantasy_travel.loginpage.R;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    RequestOptions options ;
    private Context mContext ;
    private List<PlanModel> planData ;


    public RecyclerViewAdapter(Context mContext, List<PlanModel> planData) {


        this.mContext = mContext;
        this.planData = planData;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater mInflater = LayoutInflater.from(mContext);
        View view = mInflater.inflate(R.layout.activity_plans,null,false);
        // click listener here
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        holder.preferredSex.setText(planData.get(position).getPreferredSex());
        holder.startLocation.setText(planData.get(position).getStartLocation());
        holder.endLocation.setText(planData.get(position).getEndLocation());
        holder.preferredMode.setText(planData.get(position).getPreferredMode());
        holder.planName.setText(planData.get(position).getPlanName());
        holder.time.setText(planData.get(position).getTime());
        //holder.preferredMode.setText(planData.get(position).getPreferredMode());

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(mContext,"You Clicked "+ planData.get(position).getPlanName(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), DailyCommuteMapActivity.class);
                v.getContext().startActivity(intent);



            }
        });


    }


    @Override
    public int getItemCount() {
        return planData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView startLat;
        public TextView startLong;
        public TextView startLocation;
        public TextView endLat;
        public TextView endLong;
        public TextView endLocation;
        public TextView emailId;
        public TextView preferredSex;
        public TextView preferredMode;
        public TextView planName;
        public TextView time;
        public LinearLayout linearLayout;



        public MyViewHolder(View itemView) {
            super(itemView);
           // startLat = itemView.findViewById(R.id.start_location_plan_name);
           // startLong = itemView.findViewById(R.id.studio);
            startLocation = itemView.findViewById(R.id.start_location_plan_name);
           // endLat = itemView.findViewById(R.id.categorie);
           // endLong = itemView.findViewById(R.id.categorie);
            endLocation = itemView.findViewById(R.id.destination_location_plan_name);
            //emailId = itemView.findViewById(R.id.categorie);
            preferredSex = itemView.findViewById(R.id.preferredSex);
            preferredMode = itemView.findViewById(R.id.travelMode);
            planName = itemView.findViewById(R.id.planName);
            time = itemView.findViewById(R.id.starttime);

            linearLayout = (LinearLayout) itemView.findViewById(R.id.planLinearLayout);
        }
    }
}
