package com.arastta;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.ImageOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;

public class ProductDetailsActivity extends Master2Activity
{
    Context context;
    String ScreenName = "";

    ViewPager ImagePager;
    ImagePagerAdapter ImageAdapter;
    ArrayList<String> ImageList = new ArrayList<String>();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        activePage = 3;

        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_product_details);

        context = ProductDetailsActivity.this;
        ScreenName = "ProductDetailsActivity";

        //unUsed
        TextView ProductInfo = (TextView)findViewById(R.id.ProductInfo);
        ProductInfo.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView ProductInfoModel = (TextView)findViewById(R.id.ProductInfoModel);
        ProductInfoModel.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView ProductInfoPrice = (TextView)findViewById(R.id.ProductInfoPrice);
        ProductInfoPrice.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView ProductInfoManufacturer = (TextView)findViewById(R.id.ProductInfoManufacturer);
        ProductInfoManufacturer.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            TextView tv = (TextView)findViewById(R.id.tv);
            tv.setText(extras.getString("object"));

            try
            {
                JSONObject jsonObject = (JSONObject) new JSONTokener(String.valueOf(extras.getString("object"))).nextValue();

                MenuTitle.setText(String.format(getResources().getString(R.string.details_title), jsonObject.getString("name"),jsonObject.getString("product_id")));

                JSONArray imagesArray = new JSONArray(jsonObject.getJSONArray("images").toString());
                ImageList.clear();
                ImageList = new ArrayList<String>();
                for (int i = 0; i < imagesArray.length(); i++) {
                    ImageList.add(imagesArray.getString(i));
                    Log.e("json", i + "=" + imagesArray.getString(i));
                }
                CircleIndicator DotIndicator = (CircleIndicator)findViewById(R.id.DotIndicator);
                ImagePager = (ViewPager) this.findViewById(R.id.ImagePager);
                ImageAdapter = new ImagePagerAdapter();
                ImagePager.setAdapter(ImageAdapter);
                DotIndicator.setViewPager(ImagePager);

                TextView ProductInfoModelValue = (TextView)findViewById(R.id.ProductInfoModelValue);
                ProductInfoModelValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                ProductInfoModelValue.setText(jsonObject.getString("model"));

                TextView ProductInfoPriceValue = (TextView)findViewById(R.id.ProductInfoPriceValue);
                ProductInfoPriceValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                ProductInfoPriceValue.setText("$"+String.valueOf(Float.valueOf(jsonObject.getString("price"))));

                TextView ProductInfoManufacturerValue = (TextView)findViewById(R.id.ProductInfoManufacturerValue);
                ProductInfoManufacturerValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                ProductInfoManufacturerValue.setText(jsonObject.getString("manufacturer"));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }

    private class ImagePagerAdapter extends PagerAdapter
    {
        @Override
        public int getCount()
        {
            return ImageList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == ((ImageView) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {
            ImageView imageView = new ImageView(context);
            String imageUrl = ConstantsAndFunctions.getHttpOrHttps() + MasterActivity.url +"/"+ ImageList.get(position);
            ImageOptions avatarOptions = new ImageOptions();
            avatarOptions.fileCache = true;
            avatarOptions.memCache = true;
            AQuery AvatarAQ = new AQuery(context);
            AvatarAQ.id(imageView).image(imageUrl, avatarOptions);

            ((ViewPager) container).addView(imageView, 0);

            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            ((ViewPager) container).removeView((ImageView) object);
        }
    }

}
