package cn.itcast.order.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.itcast.order.R;
import cn.itcast.order.adapter.CarAdapter;
import cn.itcast.order.adapter.MenuAdapter;
import cn.itcast.order.bean.FoodBean;
import cn.itcast.order.bean.ShopBean;

public class ShopDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int MSG_COUNT_OK = 1;// 获取购物车中商品的数量
    private ShopBean bean;
    private TextView tv_shop_name, tv_time, tv_notice, tv_title, tv_back,
            tv_settle_accounts, tv_count, tv_money, tv_distribution_cost,
            tv_not_enough, tv_clear;
    private ImageView iv_shop_pic, iv_shop_car;
    private ListView lv_list, lv_car;
    private MHandler mHandler;
    private  int totalCount = 0;
    private BigDecimal totalMoney;            //购物车中菜品的总价格
    private List<FoodBean> carFoodList;      //购物车中的菜品数据
    private MenuAdapter adapter;
    private CarAdapter carAdapter;
    private RelativeLayout rl_car_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);
        bean = (ShopBean) getIntent().getSerializableExtra("shop");
        mHandler = new MHandler();
        totalMoney = new BigDecimal(0.0);
        carFoodList = new ArrayList<>();
        initView();
        initAdapter();
        setData();

    }

    public void initView() {
        tv_back = findViewById(R.id.tv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("店铺详情");
        tv_shop_name = findViewById(R.id.tv_shop_name);
        tv_time = findViewById(R.id.tv_time);
        tv_notice = findViewById(R.id.tv_notice);
        iv_shop_pic = findViewById(R.id.iv_shop_pic);
        lv_list = findViewById(R.id.lv_list);
        tv_settle_accounts = findViewById(R.id.tv_settle_accounts);
        tv_distribution_cost = findViewById(R.id.tv_distribution_cost);
        tv_count = findViewById(R.id.tv_count);
        iv_shop_car = findViewById(R.id.iv_shop_car);
        tv_money = findViewById(R.id.tv_money);
        tv_not_enough = findViewById(R.id.tv_not_enough);
        tv_clear = findViewById(R.id.tv_clear);
        lv_car = findViewById(R.id.lv_car);
        rl_car_list = findViewById(R.id.rl_car_list);
        rl_car_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (rl_car_list.getVisibility() == View.VISIBLE) {
                    rl_car_list.setVisibility(View.GONE);
                }
                return false;
            }
        });
        tv_back.setOnClickListener(this);
        tv_settle_accounts.setOnClickListener(this);
        iv_shop_car.setOnClickListener(this);
        tv_clear.setOnClickListener(this);

    }
    public void initAdapter(){
        carAdapter=new CarAdapter(this, new CarAdapter.OnSelectListener() {
            @Override
            public void onSelectAdd(int position, TextView tv_food_price, TextView tv_food_count) {
                FoodBean bean = carFoodList.get(position);
                tv_food_count.setText(bean.getCount() + 1 + "");
                BigDecimal count = BigDecimal.valueOf(bean.getCount() + 1);
                tv_food_price.setText("￥" + bean.getPrice().multiply(count));//菜品总价格
                bean.setCount(bean.getCount() + 1);//将当前菜品在购物车中的数量设置给菜品对象
                Iterator<FoodBean> iterator = carFoodList.iterator();
                while (iterator.hasNext()){
                    FoodBean food = iterator.next();
                    if (food.getFoodId() == bean.getFoodId()) {//找到当前菜品
                        iterator.remove();   //删除购物车中当前菜品的旧数据
                    }
                }
                carFoodList.add(position, bean);
                totalCount++;
                totalMoney=totalMoney.add(bean.getPrice());
                carDataMsg();
            }

            @Override
            public void onSelectMis(int position, TextView tv_food_price, TextView tv_food_count) {
                FoodBean bean = carFoodList.get(position);
                tv_food_count.setText(bean.getCount() - 1 + "");//设置当前菜品的数量
                BigDecimal count = BigDecimal.valueOf(bean.getCount() - 1);
                tv_food_price.setText("￥" + bean.getPrice().multiply(count));//菜品总价格
                minusCarData(bean, position);
                carDataMsg();
            }
        });
        adapter = new MenuAdapter(this, position -> {
            FoodBean fb=bean.getFoodList().get(position);
            fb.setCount(fb.getCount()+1);
            Iterator<FoodBean> iterator = carFoodList.iterator();
            while (iterator.hasNext()){
                FoodBean food=iterator.next();
                if (food.getFoodId() == fb.getFoodId()) {
                    iterator.remove();
                }
            }
            carFoodList.add(fb);
            totalCount = totalCount + 1;
            totalMoney = totalMoney.add(fb.getPrice());
            carDataMsg();
        });
        lv_list.setAdapter(adapter);

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back: {
                finish();
                    break;
            }
            case R.id.tv_settle_accounts: {
                if(totalCount>0){
                    Intent intent=new Intent(ShopDetailActivity.this,OrderActivity.class);
                    intent.putExtra("totalMoney",totalMoney+"");
                    intent.putExtra("distributionCost",bean.getDistributionCost()+"");
                    intent.putExtra("carFoodList",(Serializable)carFoodList );
                    startActivity(intent);
                    break;
                }
            }
            case R.id.iv_shop_car:{
                if(totalCount<=0) return;
                if(rl_car_list!=null){
                    if(rl_car_list.getVisibility()==View.VISIBLE){
                        rl_car_list.setVisibility(View.GONE);
                    }else{
                        rl_car_list.setVisibility(View.VISIBLE);
                        Animation animation= AnimationUtils.loadAnimation(ShopDetailActivity.this,R.anim.slide_bottm_to_top);
                        rl_car_list.startAnimation(animation);
                    }
                }
                carAdapter.setData(carFoodList);
                lv_car.setAdapter(carAdapter);
                break;
            }
            case R.id.tv_clear :{
                dialogClear();
                break;
            }
        }
    }
    private  void dialogClear(){
        final Dialog dialog=new Dialog(ShopDetailActivity.this,R.style.Dialog_Style);
        dialog.setContentView(R.layout.dialog_clear);
        dialog.show();
        TextView tv_clear=findViewById(R.id.tv_clear);
        TextView tv_cancel=findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(view -> {
            dialog.dismiss();//关闭对话框
        });
        tv_clear.setOnClickListener(view -> {
            if(carFoodList==null) return;
            for(FoodBean foodBean:carFoodList){
                foodBean.setCount(0);
            }
            carFoodList.clear();
            carAdapter.notifyDataSetChanged();
            totalCount=0;
            totalMoney = BigDecimal.valueOf(0.0);
            carDataMsg();
            dialog.dismiss();

        });
    }
    private void carDataMsg(){
        Message message=new Message();
        message.what=MSG_COUNT_OK;
        Bundle bundle=new Bundle();
        bundle.putString("totalCount",totalCount+"");
        bundle.putString("totalMoney",totalMoney+"");
        message.setData(bundle);
        mHandler.sendMessage(message);
    }
    private  void minusCarData(FoodBean foodBean,int position){
        int count=foodBean.getCount()-1;
        foodBean.setCount(count);
        Iterator<FoodBean> iterable=carFoodList.iterator();
        while (iterable.hasNext()){
            FoodBean food=iterable.next();
            if(food.getFoodId()==foodBean.getFoodId()){
                iterable.remove();
            }
        }
        if(count>=1){
            carFoodList.add(position,foodBean);
        }else{
            carAdapter.notifyDataSetChanged();
        }
        totalCount=totalCount-1;
        totalMoney=totalMoney.subtract(foodBean.getPrice());
        carDataMsg();
    }
    class MHandler extends Handler {
       @Override
        public void dispatchMessage(Message message){
           super.dispatchMessage(message);
          switch (message.what){
              case MSG_COUNT_OK:{
                  Bundle bundle=message.getData();
                  String totalCount=bundle.getString("totalCount");
                  String totalMoney=bundle.getString("totalMoney");
                  if(Integer.parseInt(totalCount)>0){
                      iv_shop_car.setImageResource(R.drawable.shop_car);
                      tv_count.setVisibility(View.VISIBLE);
                      tv_distribution_cost.setVisibility(View.VISIBLE);
                      tv_money.setTextColor(Color.parseColor("#ffffff"));
                      tv_money.getPaint().setFakeBoldText(true);//加粗字体
                      tv_money.setText("￥" + totalMoney);//设置购物车中菜品总价格
                      tv_count.setText(totalCount);        //设置购物车中菜品总数量
                      tv_distribution_cost.setText("另需配送费￥" + bean.getDistributionCost());
                      BigDecimal bdm = new BigDecimal(totalMoney);
                      int result = bdm.compareTo(bean.getOfferPrice());
                      if (0 > result) { //总价格<起送价格
                          tv_settle_accounts.setVisibility(View.GONE);//隐藏去结算按钮
                          tv_not_enough.setVisibility(View.VISIBLE);   //显示差价文本
                          //差价=起送价格-总价格
                          BigDecimal m = bean.getOfferPrice().subtract(bdm);
                          tv_not_enough.setText("还差￥" + m + "起送");
                      }else{
                          tv_settle_accounts.setVisibility(View.VISIBLE);
                          tv_not_enough.setVisibility(View.GONE); //隐藏差价文本
                      }
                  }else{ //购物车没有商品
                      tv_count.setText(totalCount);
                      if(rl_car_list.getVisibility()==View.VISIBLE){
                          rl_car_list.setVisibility(View.GONE); //隐藏购物车列表
                      }else{
                          rl_car_list.setVisibility(View.VISIBLE);//显示购物车列表
                      }
                      iv_shop_car.setImageResource(R.drawable.shop_car_empty);
                      tv_settle_accounts.setVisibility(View.GONE);//隐藏去结算按钮
                      tv_not_enough.setVisibility(View.VISIBLE);   //显示差价文本
                      tv_not_enough.setText("￥" + bean.getOfferPrice() + "起送");
                      tv_money.setTextColor(getResources().getColor(R.color.light_gray));
                      tv_money.setText("未选购商品");
                  }
              }
          }
       }

    }
    private void setData(){
        if(bean==null) return;
        tv_shop_name.setText(bean.getShopName());
        tv_time.setText(bean.getTime());            //设置配送时间
        tv_notice.setText(bean.getShopNotice());
        tv_not_enough.setText("￥" + bean.getOfferPrice() + "起送"); //设置起送价格
        int resId = 0;
        try {
            resId = (Integer) R.drawable.class.getField(bean.getShopPic()).get(null);
            iv_shop_pic.setImageResource(resId);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }        //设置店铺图片

        adapter.setData(bean.getFoodList());
    }
}