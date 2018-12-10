package com.example.dldke.foodbox.Community;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.dldke.foodbox.PencilRecipe.SearchIngredientFragment;
import com.example.dldke.foodbox.R;

public class CommunityActivity extends AppCompatActivity implements View.OnClickListener {
    FrameLayout frag;
    String searchText;
    ImageButton deleteButton;
    EditText searchBar;
    ImageView homeBtn, favoriteBtn,recommendBtn;
    android.app.FragmentManager fm;
    android.app.FragmentTransaction tran;
    CommunityFragmentNewsfeed fragmentNewsfeed;
    CommunityFragmentFavorite fragmentFavorite;
    CommunityFragmentRecommend fragmentRecommend;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        fragmentNewsfeed = new CommunityFragmentNewsfeed();
        fragmentFavorite = new CommunityFragmentFavorite();
        fragmentRecommend = new CommunityFragmentRecommend();
        frag = (FrameLayout)findViewById(R.id.favorite_fragment_container); //검색시 나오는 화면

        searchText = "";
        searchBar = (EditText)findViewById(R.id.community_commentBar);
        deleteButton = (ImageButton)findViewById(R.id.community_delete_button);

        homeBtn = (ImageView)findViewById(R.id.home_btn);
        favoriteBtn =(ImageView)findViewById(R.id.favorite_btn);
        recommendBtn = (ImageView)findViewById(R.id.recommend_btn);

        homeBtn.setOnClickListener(this);
        favoriteBtn.setOnClickListener(this);
        recommendBtn.setOnClickListener(this);
        /****************search bar input *****************************/

        /*
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text;
                text = searchBar.getText().toString();
                if (text.length() == 0)
                    fragmentSearch.setVisibility(View.GONE);
                else
                    fragmentSearch.setVisibility(View.VISIBLE);
                SearchIngredientFragment.search(text);
            }
        });
        */


        /****************delete button***********************/
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //텍스트가 존재 시, 모두 지운다.
                if (searchBar.getText().length() != 0) {
                    searchBar.setHint(" 검색어를 입력하세요.");
                    searchBar.setText(null);
                }
            }
        });

        setFrag(0);
    }

    @Override
    public void onClick(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (view.getId()){
            case R.id.home_btn:
                frag.setVisibility(View.GONE);
                setFrag(0);
                setBackeGroundAccent(homeBtn, favoriteBtn,recommendBtn);
                break;
            case R.id.favorite_btn:
                Log.e("","favorite btn 누름 ");
                setBackeGroundAccent(favoriteBtn, homeBtn,recommendBtn);
                frag.setVisibility(View.VISIBLE);
                CommunityFragmentFavorite FavoriteFragment = new CommunityFragmentFavorite();
                transaction.replace(R.id.favorite_fragment_container, FavoriteFragment);
                transaction.commit();
                break;
            case R.id.recommend_btn:
                frag.setVisibility(View.GONE);
                setFrag(2);
                setBackeGroundAccent(recommendBtn, favoriteBtn,homeBtn);
                break;
        }
    }

    public void setBackeGroundAccent(ImageView firstBtn,ImageView secondBtn,ImageView thirdBtn){
        firstBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorAccent, null));
        secondBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWhite, null));
        thirdBtn.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorWhite, null));
    }
    public void setFrag(int n){    //프래그먼트를 교체하는 작업을 하는 메소드를 만들었습니다
        fm = getFragmentManager();
        tran = fm.beginTransaction();
        switch (n){
            case 0:
                tran.replace(R.id.frag_layout, fragmentNewsfeed);
                tran.commit();
                break;
            case 2:
                tran.replace(R.id.frag_layout, fragmentRecommend);
                tran.commit();
                break;
        }
    }

}
