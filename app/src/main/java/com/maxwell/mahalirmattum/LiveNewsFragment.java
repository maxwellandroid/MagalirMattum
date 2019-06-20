package com.maxwell.mahalirmattum;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import tcking.github.com.giraffeplayer2.VideoInfo;
import tcking.github.com.giraffeplayer2.VideoView;

public class LiveNewsFragment extends Fragment {

    View view;
    String url = "http://www.maxwellstreaming.com/hls/hlsurl.php";
    int aspectRatio = VideoInfo.AR_MATCH_PARENT;
    HttpURLConnection connection;
    InputStream stream;
    BufferedReader reader;
    Uri uri;

    String result;
    ViewPager viewPager;
    LinearLayout linearLayout,layoutProgress;
    ProgressBar progressBar;
    TextView tv_our_programs;


    LinearLayout sliderDotspanel;
    private int dotscount;
    private ImageView[] dots;



   // android.widget.VideoView videoView;


    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;




    private Integer[] images = {R.drawable.slide1, R.drawable.slide2, R.drawable.slide3, R.drawable.slide4, R.drawable.slide5, R.drawable.slide6, R.drawable.slide7, R.drawable.slide8, R.drawable.slide9, R.drawable.slide10, R.drawable.slide11, R.drawable.slide12, R.drawable.slide13, R.drawable.slide14, R.drawable.slide15, R.drawable.slide16, R.drawable.slide17, R.drawable.slide18};



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.content_home_page, container, false);

        try{
            linearLayout=(LinearLayout)view.findViewById(R.id.linear_layout) ;
            layoutProgress=(LinearLayout)view.findViewById(R.id.linear_progress);
            progressBar=(ProgressBar)view.findViewById(R.id.progressBar) ;
            tv_our_programs=(TextView)view.findViewById(R.id.text_our_programs);
        }catch (Exception e){
            Log.d("exception",e.getLocalizedMessage());
        }



      //  linearLayout.setVisibility(View.GONE);


       // videoView = (android.widget.VideoView) view.findViewById(R.id.videoView);
        if(isNetworkAvailable()){
          new JsonTasks().execute(url);
          //  new LiveTVOperation().execute();
        }else {
            showAlertDialog("Not connected to internet");
        }

        viewPager = (ViewPager) view.findViewById(R.id.viewPager);
       // sliderDotspanel = (LinearLayout) view.findViewById(R.id.SliderDots);
        imageModelArrayList = new ArrayList<>();


        for(int i = 0; i < images.length; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(images[i]);
            imageModelArrayList.add(imageModel);
        }

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getContext());

        viewPager.setAdapter(viewPagerAdapter);


        CirclePageIndicator indicator = (CirclePageIndicator)
                view.findViewById(R.id.indicator);

        indicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES = imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });


        return view;
    }

  @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checking the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           /* //First Hide other objects (listview or recyclerview), better hide them using Gone.
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) videoView.getLayoutParams();
            params.width=params.MATCH_PARENT;
            params.height=params.MATCH_PARENT;
            videoView.setLayoutParams(params);*/
           tv_our_programs.setVisibility(View.GONE);
            if(getActivity().getActionBar()!=null) {
                getActivity().getActionBar().hide();
            }
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
           tv_our_programs.setVisibility(View.VISIBLE);
            if(getActivity().getActionBar()!=null) {
                getActivity().getActionBar().show();
            }
        }
    }
    public void showAlertDialog(String message){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setMessage(message);
        alertDialogBuilder.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        arg0.cancel();
                        getActivity().finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public boolean isNetworkAvailable(){
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    private class JsonTasks extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                String finaljson = buffer.toString();
                JSONObject finaljsonobject = new JSONObject(finaljson);
                JSONArray jsonArray = finaljsonobject.getJSONArray("hls");

                JSONObject jsonObject = jsonArray.getJSONObject(0);
                result = jsonObject.getString("magalirmattumtv");

                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result != null) {
                VideoView videoView = (VideoView)view. findViewById(R.id.video_view);
                videoView.setVideoPath(result).getPlayer().start();
                videoView.getPlayer().aspectRatio(aspectRatio);

                progressBar.setVisibility(View.GONE);
                linearLayout.setVisibility(View.VISIBLE);
                layoutProgress.setVisibility(View.GONE);
            }
        }
    }

}
