package com.arastta;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    String product_id = "";

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
        TextView ProductInfoID = (TextView)findViewById(R.id.ProductInfoID);
        ProductInfoID.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView ProductInfoModel = (TextView)findViewById(R.id.ProductInfoModel);
        ProductInfoModel.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView ProductInfoSKU = (TextView)findViewById(R.id.ProductInfoSKU);
        ProductInfoSKU.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView ProductInfoPrice = (TextView)findViewById(R.id.ProductInfoPrice);
        ProductInfoPrice.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView ProductInfoQuantity = (TextView)findViewById(R.id.ProductInfoQuantity);
        ProductInfoQuantity.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView ProductInfoManufacturer = (TextView)findViewById(R.id.ProductInfoManufacturer);
        ProductInfoManufacturer.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView ProductInfoStatus = (TextView)findViewById(R.id.ProductInfoStatus);
        ProductInfoStatus.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));
        TextView ProductDescription = (TextView)findViewById(R.id.ProductDescription);
        ProductDescription.setTypeface(ConstantsAndFunctions.getTypeFace(context,true));

        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            TextView tv = (TextView)findViewById(R.id.tv);
            tv.setText(extras.getString("object"));

            try
            {
                JSONObject jsonObject = (JSONObject) new JSONTokener(String.valueOf(extras.getString("object"))).nextValue();

                MenuTitle.setText(jsonObject.getString("name"));

                product_id = jsonObject.getString("product_id");

                new getProductDetails().execute();
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
            String imageUrl = MasterActivity.url +"/"+ ImageList.get(position);
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

    private class getProductDetails extends AsyncTask<String, Void, String>
    {
        String ResultText = "";

        @Override
        protected void onProgressUpdate(Void... values){}

        @Override
        protected void onPreExecute(){}

        @Override
        protected String doInBackground(String... params)
        {
            ResultText = ConstantsAndFunctions.getHtml(MasterActivity.username,MasterActivity.password,MasterActivity.url,"products/"+product_id);

            return ResultText;
        }

        @Override
        protected void onPostExecute(String result)
        {
            Log.e(ScreenName+":"+String.valueOf(Thread.currentThread().getStackTrace()[2].getLineNumber()),
                    ResultText);

            if(ResultText.equals("error"))
            {
                Toast.makeText(context, getResources().getString(R.string.connection_failed), Toast.LENGTH_SHORT).show();
            }
            else
            {
                try
                {
                    JSONObject jsonObject = (JSONObject) new JSONTokener(String.valueOf(ResultText)).nextValue();

                    TextView ProductInfoIDValue = (TextView)findViewById(R.id.ProductInfoIDValue);
                    ProductInfoIDValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                    ProductInfoIDValue.setText(jsonObject.getString("product_id"));

                    TextView ProductInfoModelValue = (TextView)findViewById(R.id.ProductInfoModelValue);
                    ProductInfoModelValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                    ProductInfoModelValue.setText(jsonObject.getString("model"));

                    TextView ProductInfoSKUValue = (TextView)findViewById(R.id.ProductInfoSKUValue);
                    ProductInfoSKUValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                    ProductInfoSKUValue.setText(jsonObject.getString("sku"));

                    TextView ProductInfoPriceValue = (TextView)findViewById(R.id.ProductInfoPriceValue);
                    ProductInfoPriceValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                    ProductInfoPriceValue.setText("$"+String.valueOf(Float.valueOf(jsonObject.getString("price"))));

                    TextView ProductInfoQuantityValue = (TextView)findViewById(R.id.ProductInfoQuantityValue);
                    ProductInfoQuantityValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                    ProductInfoQuantityValue.setText(jsonObject.getString("quantity"));

                    TextView ProductInfoManufacturerValue = (TextView)findViewById(R.id.ProductInfoManufacturerValue);
                    ProductInfoManufacturerValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                    ProductInfoManufacturerValue.setText(jsonObject.getString("manufacturer"));

                    TextView ProductInfoStatusValue = (TextView)findViewById(R.id.ProductInfoStatusValue);
                    ProductInfoStatusValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                    if(jsonObject.getString("status").equals("0")){
                        ProductInfoStatusValue.setText(context.getResources().getString(R.string.disable));
                        ProductInfoStatusValue.setTextColor(context.getResources().getColor(R.color.colorStatus07));
                    }

                    TextView ProductDescriptionValue = (TextView)findViewById(R.id.ProductDescriptionValue);
                    ProductDescriptionValue.setTypeface(ConstantsAndFunctions.getTypeFace(context,false));
                    ProductDescriptionValue.setText(Html.fromHtml(jsonObject.getString("description")));

                    JSONArray imagesArray = new JSONArray(jsonObject.getJSONArray("images").toString());
                    ImageList.clear();
                    ImageList = new ArrayList<String>();
                    for (int i = 0; i < imagesArray.length(); i++) {
                        ImageList.add(imagesArray.getString(i));
                        Log.e("json", i + "=" + imagesArray.getString(i));
                    }
                    CircleIndicator DotIndicator = (CircleIndicator)findViewById(R.id.DotIndicator);
                    ImagePager = (ViewPager) findViewById(R.id.ImagePager);
                    ImageAdapter = new ImagePagerAdapter();
                    ImagePager.setAdapter(ImageAdapter);
                    DotIndicator.setViewPager(ImagePager);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                catch (ClassCastException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }

}
