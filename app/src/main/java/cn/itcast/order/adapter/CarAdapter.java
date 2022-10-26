package cn.itcast.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.math.BigDecimal;
import java.util.List;

import cn.itcast.order.R;
import cn.itcast.order.activity.ShopDetailActivity;
import cn.itcast.order.bean.FoodBean;

public class CarAdapter extends BaseAdapter {
    private Context context;
    private List<FoodBean> fbl;
    private OnSelectListener onSelectListener;
    public CarAdapter(Context context, OnSelectListener onSelectListener) {
        this.context = context;
        this.onSelectListener=onSelectListener;
    }
    @Override
    public int getCount() {
        return fbl == null ? 0 : fbl.size();
    }

    public void setData(List<FoodBean> fbl) {
        this.fbl = fbl;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return fbl == null ? null : fbl.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.car_item, null);
            vh.tv_food_name = convertView.findViewById(R.id.tv_food_name);
            vh.tv_food_count = convertView.findViewById(R.id.tv_food_count);
            vh.tv_food_price = convertView.findViewById(R.id.tv_food_price);
            vh.iv_add = convertView.findViewById(R.id.iv_add);
            vh.iv_minus = convertView.findViewById(R.id.iv_minus);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final FoodBean bean = (FoodBean) getItem(i);
        if (bean != null) {
            vh.tv_food_name.setText(bean.getFoodName());
            vh.tv_food_count.setText(bean.getCount() + "");
            BigDecimal count = BigDecimal.valueOf(bean.getCount());
            vh.tv_food_price.setText("￥" + bean.getPrice().multiply(count));
        }
        vh.iv_add.setOnClickListener(view -> onSelectListener.onSelectAdd(i, vh.tv_food_price, vh.tv_food_count));
        vh.iv_minus.setOnClickListener(view -> onSelectListener.onSelectMis(i, vh.tv_food_price, vh.tv_food_count));
        return convertView;

    }

    public interface OnSelectListener {
        void onSelectAdd(int position, TextView tv_food_price, TextView tv_food_count);

        void onSelectMis(int position, TextView tv_food_price, TextView tv_food_count);
    }

    class ViewHolder {
        public TextView tv_food_name, tv_food_count, tv_food_price;
        public ImageView iv_add, iv_minus;
    }
}
