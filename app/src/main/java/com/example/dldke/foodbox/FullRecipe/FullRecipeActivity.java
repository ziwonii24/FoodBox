package com.example.dldke.foodbox.FullRecipe;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.dldke.foodbox.Activity.RefrigeratorMainActivity;
import com.example.dldke.foodbox.DataBaseFiles.Mapper;
import com.example.dldke.foodbox.DataBaseFiles.RecipeDO;
import static com.example.dldke.foodbox.DataBaseFiles.Mapper.createIngredient;
import com.example.dldke.foodbox.MyRecipe.RecipeBoxHalfRecipeDetailActivity;
import com.example.dldke.foodbox.PencilRecipe.PencilCartAdapter;
import com.example.dldke.foodbox.PencilRecipe.PencilCartItem;
import com.example.dldke.foodbox.PencilRecipe.PencilRecipeActivity;
import com.example.dldke.foodbox.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FullRecipeActivity extends AppCompatActivity implements View.OnClickListener {

    private final int CAMERA_CODE = 1;
    private final int GALLERY_CODE = 2;
    private static boolean isCookingClass, isHalfRecipe;

    private String imagePath, recipeId;
    private Toolbar toolbar;
    private EditText foodtitle;
    private Spinner spinner;
    private Button ingredient_add, spec_add, ok_btn;
    private ImageView food_img, food_img_real;
    private RecyclerView fullrecipeRecyclerView, recipe_ingredient_view;

    private List<RecipeDO.Ingredient> data = new ArrayList<>();
    static ArrayList<FullRecipeData> mArrayList;
    static FullRecipeAdapter mAdapter;
    private FullRecipeIngredientAdapter recipeIngredientAdapter;
    private PencilCartAdapter pencilCartAdapter = new PencilCartAdapter();
    private ArrayList<PencilCartItem> clickItems;

    private RecipeBoxHalfRecipeDetailActivity recipeBoxHalfRecipeDetailActivity = new RecipeBoxHalfRecipeDetailActivity();
    private PencilRecipeActivity pencilRecipeActivity = new PencilRecipeActivity();
    private RefrigeratorMainActivity refrigeratorMainActivity = new RefrigeratorMainActivity();
    private FullRecipeStepDialog stepDialog;


    private final String TAG = "FullRecipe DB Test";

    public FullRecipeActivity(){}

    public void setIsHalfRecipe(boolean isHalfRecipe){
        this.isHalfRecipe = isHalfRecipe;
    }
    public boolean getIsHalfRecipe(){
        return isHalfRecipe;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_recipe);
        isCookingClass = refrigeratorMainActivity.getisCookingClass();

        toolbar = (Toolbar) findViewById(R.id.fullrecipe_toolbar);
        foodtitle = (EditText) findViewById(R.id.food_title);
        food_img = (ImageView)findViewById(R.id.food_img);
        food_img_real = (ImageView)findViewById(R.id.food_img_real);
        ingredient_add = (Button)findViewById(R.id.ingredient_add);
        spec_add = (Button) findViewById(R.id.spec_insert_btn);
        ok_btn = (Button) findViewById(R.id.recipe_ok);
        spinner = (Spinner) findViewById(R.id.food_spinner);
        recipe_ingredient_view = (RecyclerView) findViewById(R.id.recipe_ingredient_recyclerview);
        fullrecipeRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);  //기존 toolbar없애기
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //뒤로가기 버튼 생성


        if(isCookingClass && !isHalfRecipe){
            //쿠킹 클래스 풀레시피 작성
            ingredient_add.setVisibility(View.VISIBLE);

            clickItems = pencilCartAdapter.getCartItems();
            try{
                for(int i=0; i<clickItems.size(); i++){
                    data.add(createIngredient(clickItems.get(i).getFoodName(), clickItems.get(i).getFoodCount()));
                }
            }catch(NullPointerException e){}

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recipe_ingredient_view.setLayoutManager(mLayoutManager);
            recipeIngredientAdapter = new FullRecipeIngredientAdapter(this, data);
            recipe_ingredient_view.setAdapter(recipeIngredientAdapter);

        }
        else{
            //간이 레시피에서 풀레시피로 작성
            recipeId = recipeBoxHalfRecipeDetailActivity.getRecipeId();
            String title = Mapper.searchRecipe(recipeId).getSimpleName();
            foodtitle.setText(title);
            ingredient_add.setVisibility(View.INVISIBLE);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            recipe_ingredient_view.setLayoutManager(mLayoutManager);
            data = Mapper.searchRecipe(recipeId).getIngredient();
            recipeIngredientAdapter = new FullRecipeIngredientAdapter(this, data);
            recipe_ingredient_view.setAdapter(recipeIngredientAdapter);
        }


        /*
        요리 카테고리 만드는 spinner
         */
        String[] foodrecipe_list = new String[]{"메인요리", "국/찌개", "반찬", "간식"};
        ArrayAdapter<String> spinnerAdapter;
        spinnerAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, foodrecipe_list);
        spinner.setAdapter(spinnerAdapter);


        /*
        FullRecipe SpecList Init
        */
        fullrecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mArrayList = new ArrayList<>();
        mAdapter = new FullRecipeAdapter(this, mArrayList);
        fullrecipeRecyclerView.setAdapter(mAdapter);


        food_img.setOnClickListener(this);
        ingredient_add.setOnClickListener(this);
        spec_add.setOnClickListener(this);
        ok_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.food_img:
                selectGallery();
                break;

            case R.id.spec_insert_btn:
                if(isCookingClass && !isHalfRecipe){
                    //stepDialog = new FullRecipeStepDialog(this, clickItems);
                    stepDialog = new FullRecipeStepDialog(this, data);
                    stepDialog.show();
                }
                else{
                    stepDialog = new FullRecipeStepDialog(this, recipeId);
                    stepDialog.show();
                }
                break;

            case R.id.recipe_ok:
                registerSpec();
                break;

            case R.id.ingredient_add:
                pencilRecipeActivity.setIsFull(true);
                Intent PencilRecipeActivity = new Intent(getApplicationContext(), PencilRecipeActivity.class);
                startActivity(PencilRecipeActivity);
                break;
            default:
                break;
        }

    }

    public void registerSpec(){
        String FoodTitle;
        List<RecipeDO.Spec> specList;

        FoodTitle= foodtitle.getText().toString();
        specList = stepDialog.getSpecList();


        if(isCookingClass && !isHalfRecipe){
            recipeId = Mapper.createChefRecipe(FoodTitle, specList);
            Mapper.addRecipeInMyCommunity(recipeId);
        }
        else{
            Mapper.createFullRecipe(recipeId, FoodTitle, specList);
        }

        Mapper.attachRecipeImage(recipeId, imagePath);
        Mapper.updatePointInfo(10);

        Intent RefrigeratorActivity = new Intent(getApplicationContext(), RefrigeratorMainActivity.class);
        startActivity(RefrigeratorActivity);
        specList.clear();
    }

    public void selectGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch(requestCode){
                case GALLERY_CODE:
                    sendPicture(data.getData());
                    break;
                default:
                    break;
            }
        }
    }

    private void sendPicture(Uri imgUri){

        imagePath = getRealPathFromURI(imgUri);
        ExifInterface exif = null;
        try{
            exif = new ExifInterface(imagePath);
        }catch(IOException e){
            e.printStackTrace();
        }
        //int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        //int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        //food_img.setImageBitmap(rotate(bitmap, exifDegree));
        food_img.setVisibility(View.INVISIBLE);
        food_img_real.setVisibility(View.VISIBLE);
        food_img_real.setImageBitmap(bitmap);

    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    private Bitmap rotate(Bitmap src, float degree) {
        // Matrix 객체 생성
        Matrix matrix = new Matrix();
        // 회전 각도 셋팅
        matrix.postRotate(degree);
        // 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }
        return cursor.getString(column_index);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent RefrigeratorMainActivity = new Intent(getApplicationContext(), com.example.dldke.foodbox.Activity.RefrigeratorMainActivity.class);
            RefrigeratorMainActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(RefrigeratorMainActivity);
        }
        return super.onOptionsItemSelected(item);
    }

}
