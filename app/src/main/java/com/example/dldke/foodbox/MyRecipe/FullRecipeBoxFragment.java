package com.example.dldke.foodbox.MyRecipe;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.R;

import java.util.ArrayList;
import java.util.List;

public class FullRecipeBoxFragment extends Fragment {
    public FullRecipeBoxFragment(){ }


    private RecyclerView recyclerview;
    private MyRecipeBoxFullRecipeAdapter adapter;
    private ArrayList<RecipeBoxData> data = new ArrayList<>();
    private boolean isRecipe = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(isRecipe){
            View view = inflater.inflate(R.layout.recipe_box_fragment_fullrecipe, container, false);

            recyclerview = (RecyclerView)view.findViewById(R.id.recycler_view);
            recyclerview.setHasFixedSize(true);
            adapter = new MyRecipeBoxFullRecipeAdapter(data);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerview.setLayoutManager(layoutManager);
            recyclerview.setItemAnimator(new DefaultItemAnimator());
            recyclerview.setAdapter(adapter);

            enableSwipeToDeleteAndUndo();
            return view;
        }
        else{
            View view = inflater.inflate(R.layout.recipe_box_fragment_none, container, false);
            return view;
        }

    }

    public void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        prepareData();
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                adapter.removeItem(position);

            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerview);
    }


    //Detail이 없으면 간이레시피, Detail이 있으면 풀레시피 fragment로 보여지기 위한 작업
    private void prepareData(){
        List<String> my_recipe = Mapper.searchMyCommunity().getMyRecipes();
        for(int i = 0 ; i< my_recipe.size(); i++){
            try{
                String recipeId = my_recipe.get(i);
                String foodName = Mapper.searchRecipe(recipeId).getDetail().getFoodName();
                boolean isshared = Mapper.searchRecipe(recipeId).getIsShare();
                String imgUrl = Mapper.getImageUrlRecipe(recipeId);
                boolean isPost = Mapper.searchRecipe(recipeId).getIsPost();

                if(!isPost){
                    data.add(new RecipeBoxData(recipeId, imgUrl, foodName, isshared));
                    isRecipe = true;
                }
            }catch(NullPointerException e){

            }
        }
    }
}
