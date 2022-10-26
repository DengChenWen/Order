package cn.itcast.order.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import cn.itcast.order.R;
import cn.itcast.order.adapter.ShopAdapter;
import cn.itcast.order.bean.ShopBean;
import cn.itcast.order.utils.Constant;
import cn.itcast.order.utils.JsonParse;
import cn.itcast.order.views.ShopListView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//background 引入
public class MainActivity extends AppCompatActivity {
    private TextView tv_back,tv_title;         //返回键与标题控件
    private ShopListView slv_list;              //列表控件
    private ShopAdapter adapter;                //列表的适配器
    public static final int MSG_SHOP_OK = 1; //获取数据
//    private MHandler mHandler;
    private RelativeLayout rl_title_bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mHandler=new MHandler();
//        initData();
        init();
    }
    public void init(){
        tv_back = findViewById(R.id.tv_back);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("店铺");
        rl_title_bar = findViewById(R.id.title_bar);
        rl_title_bar.setBackgroundColor(getResources().getColor(R.color.blue_color));
        tv_back.setVisibility(View.GONE);
        slv_list= findViewById(R.id.slv_list);
        adapter=new ShopAdapter(this);
        adapter.setData(JsonParse.getInstance().getInfosFromJson(MainActivity.this));
        slv_list.setAdapter(adapter);
    }
/*    private void initData() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder().url(Constant.WEB_SITE +
                Constant.REQUEST_SHOP_URL).build();
        Call call = okHttpClient.newCall(request);
        // 开启异步线程访问网络
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String res = response.body().string(); //获取店铺数据
                Message msg = new Message();
                msg.what = MSG_SHOP_OK;
                msg.obj = res;
                mHandler.sendMessage(msg);
            }
            @Override
            public void onFailure(Call call, IOException e) {

            }
        });
    }
    class MHandler extends Handler {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case MSG_SHOP_OK:
                    if (msg.obj != null) {
                        String vlResult = (String) msg.obj;
                        //解析获取的JSON数据
                        List<ShopBean> pythonList = JsonParse.getInstance().
                                getShopList(vlResult);
                        adapter.setData(pythonList);
                    }
                    break;
            }
        }
    }*/
    protected long exitTime;//记录第一次点击时的时间
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(MainActivity.this, "再按一次退出订餐应用",
                        Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                MainActivity.this.finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}