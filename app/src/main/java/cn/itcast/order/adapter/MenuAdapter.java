package cn.itcast.order.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import cn.itcast.order.R;
import cn.itcast.order.activity.FoodActivuty;
import cn.itcast.order.activity.ShopDetailActivity;
import cn.itcast.order.bean.FoodBean;

public class MenuAdapter extends BaseAdapter {
    private Context context;
    private List<FoodBean> fbl;
    private OnSelectListener onSelectListener;

    public MenuAdapter(Context context, OnSelectListener onSelectListener) {
        this.context = context;
        this.onSelectListener=onSelectListener;
    }

    @Override
    public int getCount() {
        return fbl==null?0:fbl.size();
    }
    public void setData(List<FoodBean> fbl){
        this.fbl=fbl;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return fbl==null?null:fbl.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder vh;
       if(convertView==null){
           vh=new ViewHolder();
           convertView= LayoutInflater.from(context).inflate(R.layout.menu_item,null);
           vh.tv_food_name = (TextView) convertView.findViewById(R.id.tv_food_name);
           vh.tv_taste = (TextView) convertView.findViewById(R.id.tv_taste);
           vh.tv_sale_num = (TextView) convertView.findViewById(R.id.tv_sale_num);
           vh.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
           vh.btn_add_car = (Button) convertView.findViewById(R.id.btn_add_car);
           vh.iv_food_pic = (ImageView) convertView.findViewById(R.id.iv_food_pic);
           convertView.setTag(vh);

       }else{
           vh=(ViewHolder) convertView.getTag();

       }
        final FoodBean bean = (FoodBean) getItem(i);
       if(bean!=null){
           vh.tv_food_name.setText(bean.getFoodName());
           vh.tv_taste.setText(bean.getTaste());
           vh.tv_sale_num.setText("月售" + bean.getSaleNum());
           vh.tv_price.setText("￥"+bean.getPrice());
           int resId;
           try {
               resId = (Integer) R.drawable.class.getField(bean.getFoodPic()).get(null);
               vh.iv_food_pic.setImageResource(resId);
           } catch (IllegalAccessException | NoSuchFieldException e) {
               e.printStackTrace();
           }
       }
       convertView.setOnClickListener(view -> {
           if(bean==null) return;
           Intent intent=new Intent(context, FoodActivuty.class);
           intent.putExtra("food",bean);
           context.startActivity(intent);

       });
        vh.btn_add_car.setOnClickListener(view -> {
            onSelectListener.onSelectAddCar(i);
        });

        return convertView;
    }
    class ViewHolder {
        public TextView tv_food_name, tv_taste, tv_sale_num, tv_price;
        public Button btn_add_car;
        public ImageView iv_food_pic;
    }
    public interface OnSelectListener{
        void onSelectAddCar(int position);
    }
}
