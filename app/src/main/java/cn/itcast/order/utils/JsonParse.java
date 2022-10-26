package cn.itcast.order.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

import java.util.ArrayList;
import java.util.List;

import cn.itcast.order.bean.ShopBean;

public class JsonParse {
    private static JsonParse jsonParse;
    private JsonParse(){

    }
    public static JsonParse getInstance(){
        if(jsonParse==null){
            jsonParse=new JsonParse();
        }
        return jsonParse;
    }
    public List<ShopBean> getShopList(String json){

        Gson gson=new Gson();
        Type listType=new TypeToken<List<ShopBean>>(){}.getType();
        return gson.fromJson(json, listType);
    }
    private String read(InputStream in) {
        BufferedReader reader = null;
        StringBuilder sb = null;
        String line = null;
        try {
            sb = new StringBuilder();//实例化一个StringBuilder对象
            //用InputStreamReader把in这个字节流转换成字符流BufferedReader
            reader = new BufferedReader(new InputStreamReader(in));
            //判断从reader中读取的行内容是否为空
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        } finally {
            try {
                if (in != null) in.close();
                if (reader != null) reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public List<ShopBean> getInfosFromJson(Context context) {
        List<ShopBean> weatherInfos = new ArrayList<>();
        InputStream is = null;
        try {
            //从项目中的assets文件夹中获取json文件
            is = context.getResources().getAssets().open("shop_list_data.json");
            String json = read(is); //获取json数据
            Gson gson = new Gson(); //创建Gson对象
            // 创建一个TypeToken的匿名子类对象，并调用该对象的getType()方法
            Type listType = new TypeToken<List<ShopBean>>() {
            }.getType();
            // 把获取到的信息集合存到infoList中
            return gson.fromJson(json, listType);
        } catch (Exception e) {
        }
        return weatherInfos;
    }

}
