package com.example.dldke.foodbox.Community;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.PostDO;
import com.example.dldke.foodbox.R;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CommunityFragmentRecommend extends Fragment implements CommunityLoadingAdapter.OnLoadMoreListener {
    private CommunityLoadingAdapter mAdapter;
    private ArrayList<CommunityItem> itemList;
    private static List<PostDO> postList;
    private TextView noneRecommend;
    private static boolean isLast;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_fragment_recommendation, container, false);

        noneRecommend = (TextView)view.findViewById(R.id.noneRecommend);
        itemList = new ArrayList<>();
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recommend_recycler);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommunityLoadingAdapter(this, getContext());
        mAdapter.setLinearLayoutManager(mLayoutManager);
        mAdapter.setRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        isLast = false;

        postList = Mapper.recommendRecipe();
        return view;
    }

    private class PostAsync extends AsyncTask<Void, Void, List<PostDO>> {

        protected void onPreExecute() { //2

            super.onPreExecute();
            mAdapter.setProgressMore(true);
        }
        protected List<PostDO> doInBackground(Void... params) {
            return postList;
        }

        protected void onPostExecute(List result) {
            if(postList.size() != 0 ){
                mAdapter.setProgressMore(false);
                loadData();
            }else{
                mAdapter.setProgressMore(false);
                mAdapter.setMoreLoading(false);
                isLast =true;
                noneRecommend.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        new PostAsync().execute();

    }


    @Override
    public void onLoadMore() {
        if(!isLast) {

            mAdapter.setProgressMore(true);
            Log.e("dd", "onLoadMore");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    itemList.clear();
                    mAdapter.setProgressMore(false);

                    int start = mAdapter.getItemCount() - 1;
                    int end = start + 2;
                    try {

                        if (end >= postList.size()) {
                            Log.e("if", "end : " + end + "start" + start);
                            end = postList.size() - 1;
                            isLast = true;
                        }
                        for (int i = start + 1; i < end + 1; i++) {
                            Log.e("for", "end : " + end + "start" + start);


                            String imgUrl = Mapper.getImageUrlRecipe(postList.get(i).getRecipeId());
                            Bitmap bm = new DownloadImageTask().execute(imgUrl).get();

                            String profileUrl = Mapper.getImageUrlUser(postList.get(i).getWriter());
                            Bitmap userBitmap;
                            if(profileUrl != "default") {
                                Log.e("loadmore","if");
                                userBitmap = new DownloadImageTask().execute(profileUrl).get();
                            }else{
                                Log.e("loadmore","else");
                                userBitmap = null;
                            }

                            itemList.add(new CommunityItem(postList.get(i).getWriter()
                                    , postList.get(i).getTitle()
                                    , Mapper.searchRecipe(postList.get(i).getRecipeId()).getDetail().getFoodName()
                                    , bm
                                    , userBitmap
                                    , Mapper.matchFavorite(postList.get(i).getPostId())
                                    , postList.get(i).getPostId()
                                    , postList.get(i).getRecipeId()
                            ));
                            Log.e("loadMore", "i" + i + ": " + postList.get(i).getTitle());
                        }

                    } catch (Exception e) {
                        Log.e("loadMore", "catch 들어엄");

                    }
                    mAdapter.addItemMore(itemList);
                    mAdapter.setMoreLoading(false);


                }
            }, 2000);
        }

    }




    private void loadData() {
        itemList.clear();

        int end ;
        if(postList.size() < 4){
            end = postList.size();
        }
        else{
            end = 4;
        }


        try {
            for (int i = 0; i < end; i++) {

                //비동기
                String imgUrl = Mapper.getImageUrlRecipe(postList.get(i).getRecipeId());
                Bitmap bm = new DownloadImageTask().execute(imgUrl).get();


                String profileUrl = Mapper.getImageUrlUser(postList.get(i).getWriter());
                Bitmap userBitmap;
                if(profileUrl != "default") {
                    Log.e("loadmore","if");
                    userBitmap = new DownloadImageTask().execute(profileUrl).get();
                }else{
                    Log.e("loadmore","else");
                    userBitmap = null;
                }
                itemList.add(new CommunityItem(postList.get(i).getWriter()
                        , postList.get(i).getTitle()
                        , Mapper.searchRecipe(postList.get(i).getRecipeId()).getDetail().getFoodName()
                        , bm
                        , userBitmap
                        , Mapper.matchFavorite(postList.get(i).getPostId())
                        , postList.get(i).getPostId()
                        , postList.get(i).getRecipeId()

                ));

                Log.e("load", "" + postList.get(i).getTitle());
            }

        } catch (Exception e) {

        }


        mAdapter.addAll(itemList);
    }



    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {
            String urlImg =urls[0];
            Bitmap foodImg = null;
            try {
                InputStream in = new java.net.URL(urlImg).openStream();
                foodImg = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return foodImg;
        }
        protected void onPostExecute(Bitmap result){
        }
    }



}
