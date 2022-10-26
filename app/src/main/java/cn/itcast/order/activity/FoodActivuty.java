package cn.itcast.order.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import cn.itcast.order.R;
import cn.itcast.order.bean.FoodBean;

public class FoodActivuty extends AppCompatActivity {
    private FoodBean bean;
    private TextView tv_food_name, tv_sale_num, tv_price;
    private ImageView iv_food_pic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_activuty);
        bean = (FoodBean) getIntent().getSerializableExtra("food");
        initView();
        setData(bean);
    }
    public void initView(){
        tv_food_name = (TextView) findViewById(R.id.tv_food_name);
        tv_sale_num = (TextView) findViewById(R.id.tv_sale_num);
        tv_price = (TextView) findViewById(R.id.tv_price);
        iv_food_pic = (ImageView) findViewById(R.id.iv_food_pic);
    }
    public void setData(FoodBean foodBean){
        if (bean == null) return;
        tv_food_name.setText(bean.getFoodName());
        tv_sale_num.setText("月售" + bean.getSaleNum());
        tv_price.setText("￥" + bean.getPrice());
        int resId;
        try {
            resId = (Integer) R.drawable.class.getField(bean.getFoodPic()).get(null);
            iv_food_pic.setImageResource(resId);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

    }
}