package cn.itcast.order.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.math.BigDecimal;
import java.util.List;

import cn.itcast.order.R;
import cn.itcast.order.bean.FoodBean;

public class OrderAdapter extends BaseAdapter {
    private final Context mContext;
    private List<FoodBean> fbl;

    public OrderAdapter(Context context) {
        this.mContext = context;
    }
    public void setData(List<FoodBean> fbl){
        this.fbl=fbl;
    }
    @Override
    public int getCount() {
        return fbl == null ? 0 : fbl.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.order_item, null);
            vh.tv_food_name = convertView.findViewById(R.id.tv_food_name);
            vh.tv_count = convertView.findViewById(R.id.tv_count);
            vh.tv_money = convertView.findViewById(R.id.tv_money);
            vh.iv_food_pic = convertView.findViewById(R.id.iv_food_pic);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final FoodBean bean = (FoodBean) getItem(position);
        if (bean != null) {
            vh.tv_food_name.setText(bean.getFoodName());
            vh.tv_count.setText("x" + bean.getCount());
            vh.tv_money.setText("￥" + bean.getPrice().multiply(BigDecimal.valueOf(bean.getCount())));
            int resId;
            try {
                resId = (Integer) R.drawable.class.getField(bean.getFoodPic()).get(null);
                vh.iv_food_pic.setImageResource(resId);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }  //设置店铺图片
        }

        return convertView;
    }

    class ViewHolder {
        public TextView tv_food_name, tv_count, tv_money;
        public ImageView iv_food_pic;
    }
}
