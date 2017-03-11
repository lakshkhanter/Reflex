package com.dank.reflex;

/**
 * Created by laksh on 3/11/2017.
 */

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;


;

public class CheckInActivity extends ListActivity {
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle arg0) {
        // TODO Auto-generated method stub
        super.onCreate(arg0);
        Bundle extras = getIntent().getExtras();
        latitude=extras.getDouble("latitude");
        longitude=extras.getDouble("longitude");
        new GetPlaces(this, getListView()).execute();

    }


    class GetPlaces extends AsyncTask<Void, Void, Void> {

        private ProgressDialog dialog;
        private Context context;
        private String[] placeName;
        private String[] imageUrl;
        private ListView listView;

        public GetPlaces(Context context, ListView listView) {
            // TODO Auto-generated constructor stub
            this.context = context;
            this.listView = listView;

        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            dialog.dismiss();

            listView.setAdapter(new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1,placeName));

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent intent = new Intent(CheckInActivity.this,Report.class);
                    intent.putExtra("latitude",latitude);
                    intent.putExtra("longitude",longitude);
                    //based on item add info to intent
                    startActivity(intent);
                    }


                //@Override
                //public void onItemClick(AdapterView<?>adapter,View v, int position){

                //}
            });

        }


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            dialog = new ProgressDialog(context);
            dialog.setCancelable(true);
            dialog.setMessage("Loading..");
            dialog.isIndeterminate();
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // TODO Auto-generated method stub
            PlacesService service = new PlacesService("AIzaSyAnNSw-apZi7QxFreXs2qorpAJIHgK7kPk");
            List<Places> findPlaces = service.findPlaces(latitude,longitude,"hospital");  // hospiral for hospital
            // atm for ATM

            placeName = new String[findPlaces.size()];
            imageUrl = new String[findPlaces.size()];

            for (int i = 0; i < findPlaces.size(); i++) {

                Places placeDetail = findPlaces.get(i);
                placeDetail.getIcon();

                System.out.println(placeDetail.getName());
                placeName[i] =placeDetail.getName();

                imageUrl[i] =placeDetail.getIcon();

            }
            return null;
        }

    }

}