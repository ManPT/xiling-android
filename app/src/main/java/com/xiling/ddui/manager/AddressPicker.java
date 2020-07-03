package com.xiling.ddui.manager;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;

import com.bigkoo.pickerview.OptionsPickerView;
import com.xiling.ddui.tools.DLog;
import com.xiling.shared.util.GetJsonDataUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddressPicker {

    public class Province implements Serializable {
        private String name;
        private String code;

        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public class City implements Serializable {
        private String name;
        private String code;
        private String pId;

        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getpId() {
            return pId;
        }

        public void setpId(String pId) {
            this.pId = pId;
        }
    }

    public class Area implements Serializable {
        private String name;
        private String code;
        private String cId;

        @Override
        public String toString() {
            return name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getcId() {
            return cId;
        }

        public void setcId(String cId) {
            this.cId = cId;
        }
    }

    public void setListener(AddressPickerListener listener) {
        this.listener = listener;
    }

    public interface AddressPickerListener {
        void onAddressSelected(Province province, City city, Area area);
    }

    public interface AddressDataLoadListener {
        void onLoadFinish();
    }

    public void setDataLoadListener(AddressDataLoadListener dataLoadListener) {
        this.dataLoadListener = dataLoadListener;
    }

    AddressPickerListener listener = null;
    AddressDataLoadListener dataLoadListener = new AddressDataLoadListener() {
        @Override
        public void onLoadFinish() {
            //默认加载完成后展开选择框
            showPickerView();
        }
    };

    Context context = null;

    boolean isLoadData = false;

    ArrayList<Province> provinces = new ArrayList<>();
    ArrayList<City> citys = new ArrayList<>();
    ArrayList<Area> areas = new ArrayList<>();

    ArrayList<Province> pData = new ArrayList<>();
    ArrayList<ArrayList<City>> cData = new ArrayList<>();
    ArrayList<ArrayList<ArrayList<Area>>> aData = new ArrayList<>();

    public AddressPicker(Context context) {
        this.context = context;
    }

    String provinceId;
    String cityId;
    String areaId;

    /**
     * 设置默认数据
     */
    public void setDefaultData(String provinceId, String cityId, String areaId) {
        this.provinceId = provinceId;
        this.cityId = cityId;
        this.areaId = areaId;
    }

    public void showPickerView() {
        if (!isLoadData) {
            readData();
            return;
        }

        OptionsPickerView pvOptions = new OptionsPickerView.Builder(context, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                Province province = pData.get(options1);
                City city = cData.get(options1).get(options2);
                Area area = aData.get(options1).get(options2).get(options3);
                DLog.i("onOptionsSelect:" + options1 + "," + options2 + "," + options3);
                if (listener != null) {
                    listener.onAddressSelected(province, city, area);
                }
            }
        })
                .setTitleText("所在地区")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setSubmitColor(Color.parseColor("#FF4647"))
                .setCancelColor(Color.parseColor("#999999"))
                .setContentTextSize(20)
                .setOutSideCancelable(true)// default is true
                .build();
        pvOptions.setPicker(pData, cData, aData);

        if (!TextUtils.isEmpty(provinceId) ) {
            Province currentProvince = getProvinceById(provinceId);
            if (currentProvince != null) {
                int pIndex = pData.indexOf(currentProvince);
                int cIndex = 0, aIndex = 0;
                if (pIndex > -1) {
                    if (!TextUtils.isEmpty(cityId)){
                        City currentCity = getCityById(pIndex, cityId);
                        if (currentCity != null) {
                             cIndex = cData.get(pIndex).indexOf(currentCity);
                            if (cIndex > -1) {
                                if (!TextUtils.isEmpty(areaId)){
                                    Area currentArea = getAreaById(pIndex, cIndex, areaId);
                                    if (currentArea != null) {
                                         aIndex = aData.get(pIndex).get(cIndex).indexOf(currentArea);
                                    }
                                }
                            }
                        }
                    }


                    pvOptions.setSelectOptions(pIndex, cIndex, aIndex);
                }
            }
        }

        pvOptions.show();
    }

    public void readData() {

        String pJsonData = new GetJsonDataUtil().getJson(context, "addr_provinces.json");
        String cJsonData = new GetJsonDataUtil().getJson(context, "addr_citys.json");
        String aJsonData = new GetJsonDataUtil().getJson(context, "addr_areas.json");

        Type pType = new TypeToken<ArrayList<Province>>() {
        }.getType();
        Type cType = new TypeToken<ArrayList<City>>() {
        }.getType();
        Type aType = new TypeToken<ArrayList<Area>>() {
        }.getType();

        Gson gson = new Gson();
        provinces = gson.fromJson(pJsonData, pType);
        citys = gson.fromJson(cJsonData, cType);
        areas = gson.fromJson(aJsonData, aType);

        DLog.d("" + provinces.size());
        DLog.d("" + citys.size());
        DLog.d("" + areas.size());

        pData.addAll(provinces);
        for (Province p : pData) {
            //获取省下面的所有市
            ArrayList<City> citys = getCityByProvinceId(p.getCode());
            cData.add(citys);

            ArrayList<ArrayList<Area>> areas_2 = new ArrayList<>();
            for (City city : citys) {
                //获取是下面的所有区
                ArrayList<Area> areas_3 = getAreaByProvinceId(city.getCode());
                areas_2.add(areas_3);
            }
            aData.add(areas_2);
        }

        isLoadData = true;

        if (dataLoadListener != null) {
            dataLoadListener.onLoadFinish();
        }
    }

    Province getProvinceById(String pid) {
        Province province = null;
        for (Province item : pData) {
            if (pid.equals(item.getCode())) {
                province = item;
            }
        }
        return province;
    }

    public Province getProvinceByName(String name) {
        Province province = null;
        for (Province item : pData) {
            String itemName = item.getName();
            if (itemName.equals(name) || name.startsWith(itemName)) {
                province = item;
            }
        }
        return province;
    }

    public  List<Province> getProvinceListByName(String name) {
        List<Province> provinceList = new ArrayList<>();
        for (Province item : provinces) {
            String itemName = item.getName();
            if (itemName.equals(name)) {
                provinceList .add(item);
            }
        }
        return provinceList;
    }

    public int getProvinceIndex(Province province) {
        return pData.indexOf(province);
    }

    City getCityById(int pIndex, String cid) {
        City city = null;
        ArrayList<City> data = cData.get(pIndex);
        for (City item : data) {
            if (cid.equals(item.getCode())) {
                city = item;
            }
        }
        return city;
    }

    public City getCityByName(int pIndex, String name) {
        City city = null;
        ArrayList<City> data = cData.get(pIndex);
        for (City item : data) {
            if (name.equals(item.getName())) {
                city = item;
            }
        }
        return city;
    }

    public List<City> getCityListByName(String provinceId, String name) {
        List<City> city = new ArrayList<>();
        ArrayList<City> data = new ArrayList<>();

        for (City item : citys){
            if (item.pId.equals(provinceId)){
                data.add(item);
            }
        }

        for (City item : data) {
            if (name.equals(item.getName())) {
                city.add(item);
            }
        }
        return city;
    }

    public int getCityIndex(int pIndex, City city) {
        ArrayList<City> data = cData.get(pIndex);
        return data.indexOf(city);
    }

    Area getAreaById(int pIndex, int cIndex, String aid) {
        Area area = null;
        ArrayList<Area> data = aData.get(pIndex).get(cIndex);
        for (Area item : data) {
            if (aid.equals(item.getCode())) {
                area = item;
            }
        }
        return area;
    }

    public Area getAreaByName(int pIndex, int cIndex, String name) {
        Area area = null;
        ArrayList<Area> data = aData.get(pIndex).get(cIndex);
        for (Area item : data) {
            if (name.equals(item.getName())) {
                area = item;
            }
        }
        return area;
    }

    public List<Area> getAreaListByName(String cityId, String name) {
        List<Area> area = new ArrayList<>();
        ArrayList<Area> data = new ArrayList<>();
        for (Area item : areas){
            if (item.cId.equals(cityId)){
                data.add(item);
            }
        }

        for (Area item : data) {
            if (name.equals(item.getName())) {
                area.add(item) ;
            }
        }
        return area;
    }

    ArrayList<City> getCityByProvinceId(String pid) {
        ArrayList<City> response = new ArrayList<>();
        for (City city : citys) {
            if (pid.equals(city.getpId())) {
                response.add(city);
            }
        }
        return response;
    }

    ArrayList<Area> getAreaByProvinceId(String cid) {
        ArrayList<Area> response = new ArrayList<>();
        for (Area area : areas) {
            if (cid.equals(area.getcId())) {
                response.add(area);
            }
        }
        return response;
    }

}
