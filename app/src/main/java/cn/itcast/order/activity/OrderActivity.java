package cn.itcast.order.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.util.List;

import cn.itcast.order.R;
import cn.itcast.order.adapter.OrderAdapter;
import cn.itcast.order.bean.FoodBean;

public class OrderActivity extends AppCompatActivity {
    private ListView lv_order;
    private OrderAdapter adapter;
    private List<FoodBean> carFoodList;
    private TextView tv_title, tv_back, tv_distribution_cost, tv_total_cost,
            tv_cost, tv_payment;
    private RelativeLayout rl_title_bar;
    private BigDecimal money, distributionCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        carFoodList = (List<FoodBean>) getIntent().getSerializableExtra("carFoodList");
        money = new BigDecimal(getIntent().getStringExtra("totalMoney"));
        distributionCost = new BigDecimal(getIntent().getStringExtra(
                "distributionCost"));
        initView();
        setData();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("订单");
        rl_title_bar = (RelativeLayout) findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tv_back = (TextView) findViewById(R.id.tv_back);
        lv_order= (ListView) findViewById(R.id.lv_order);
        tv_distribution_cost = (TextView) findViewById(R.id.tv_distribution_cost);
        tv_total_cost = (TextView) findViewById(R.id.tv_total_cost);
        tv_cost = (TextView) findViewById(R.id.tv_cost);
        tv_payment = (TextView) findViewById(R.id.tv_payment);
        tv_payment.setOnClickListener(view -> {
            Dialog dialog = new Dialog(OrderActivity.this, R.style.Dialog_Style);
            dialog.setContentView(R.layout.qr_code);
            dialog.show();
        });
        tv_back.setOnClickListener(view -> {
            finish();
        });
    }

    private void setData() {
        adapter=new OrderAdapter(this);
        lv_order.setAdapter(adapter);
        adapter.setData(carFoodList);
        tv_cost.setText("￥"+money);
        tv_distribution_cost.setText("￥"+distributionCost);
        tv_total_cost.setText("￥"+(money.add(distributionCost)));
    }
}