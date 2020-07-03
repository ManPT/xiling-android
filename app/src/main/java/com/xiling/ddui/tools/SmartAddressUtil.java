package com.xiling.ddui.tools;

import android.text.TextUtils;
import android.util.Log;

import com.xiling.ddui.bean.AddressListBean;
import com.xiling.ddui.manager.AddressPicker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 智能匹配地址
 */
public class SmartAddressUtil {

    /**
     * 智能解析地址
     * @param text
     * @param addressPicker
     * @return
     */
    public static AddressListBean.DatasBean smartAddressParse(String text, AddressPicker addressPicker) {
        AddressListBean.DatasBean addressBean = new AddressListBean.DatasBean();

        text = stripscript(text);
        String[] textArr = text.split("\\s+");
        List<String> dataList = new ArrayList<>(Arrays.asList(textArr));

        for (String str : dataList) {

            if (isPhoneNumber(str)) {
                addressBean.setPhone(str);
                continue;
            }

            AddressPicker.Province province = getProvince(str, addressPicker);
            if (province != null) {
                addressBean.setProvinceId(province.getCode());
                addressBean.setProvinceName(province.getName());

                //特殊城市排除
                if(!province.getName().equals("北京") && !province.getName().equals("上海")
                    && !province.getName().equals("天津") && !province.getName().equals("重庆")
                ){
                    str = str.replaceFirst(addressBean.getProvinceName(),"");
                }
                Log.i("ysl","address去除区以后====" + str);

                AddressPicker.City city = getCity(str, addressPicker, province.getCode());
                if (city != null) {
                    addressBean.setCityId(city.getCode());
                    addressBean.setCityName(city.getName());
                    str = str.replaceFirst(addressBean.getCityName(),"");
                    Log.i("ysl","address去除区以后====" + str);
                    AddressPicker.Area area = getArea(str, addressPicker, city.getCode());
                    if (area != null){
                        addressBean.setDistrictId(area.getCode());
                        addressBean.setDistrictName(area.getName());
                        str = str.replaceFirst(addressBean.getDistrictName(),"");
                    }
                    Log.i("ysl","address去除区以后====" + str);
                    addressBean.setDetail(str);
                }

            }else{
                //如果不是电话或者地址，则为姓名
                addressBean.setContact(str);
            }

        }


        return addressBean;
    }

    /**
     * 匹配省
     * @param address
     * @param addressPicker
     * @return
     */
    private static AddressPicker.Province getProvince(String address, AddressPicker addressPicker) {
        AddressPicker.Province matchProvince = null;//粗略匹配上的省份
        String matchAddress = "";
        for (int endIndex = 0; endIndex < address.length(); endIndex++) {
            if (endIndex + 1 <= address.length()) {
                matchAddress = address.subSequence(0, endIndex + 1).toString();
                Log.i("ysl", "省匹配==" + endIndex + "| " + matchAddress);
                List<AddressPicker.Province> resultList = addressPicker.getProvinceListByName(matchAddress);
                if (resultList.size() == 1) {
                    matchProvince = resultList.get(0);
                    break;
                }
            }
        }
        return matchProvince;
    }

    /**
     * 匹配市
     * @param address
     * @param addressPicker
     * @return
     */
    private static AddressPicker.City getCity(String address, AddressPicker addressPicker, String provinceId) {
        AddressPicker.City matchCity = null;
        String matchAddress = "";
        for (int endIndex = 0; endIndex < address.length(); endIndex++) {
            if (endIndex + 1 <= address.length()) {
                matchAddress = address.subSequence(0, endIndex + 1).toString();
                Log.i("ysl", "省匹配==" + endIndex + "| " + matchAddress);
                List<AddressPicker.City> resultList = addressPicker.getCityListByName(provinceId, matchAddress);
                if (resultList.size() == 1) {
                    matchCity = resultList.get(0);
                    break;
                }
            }
        }

        return matchCity;
    }
    /**
     * 匹配区
     * @param address
     * @param addressPicker
     * @return
     */
    private static AddressPicker.Area getArea(String address, AddressPicker addressPicker, String cityId) {
        AddressPicker.Area matchCity = null;
        String matchAddress = "";
        for (int endIndex = 0; endIndex < address.length(); endIndex++) {
            if (endIndex + 1 <= address.length()) {
                matchAddress = address.subSequence(0, endIndex + 1).toString();
                Log.i("ysl", "省匹配==" + endIndex + "| " + matchAddress);
                List<AddressPicker.Area> resultList = addressPicker.getAreaListByName(cityId, matchAddress);
                if (resultList.size() == 1) {
                    matchCity = resultList.get(0);
                    break;
                }
            }
        }

        return matchCity;

    }


    /**
     * 过滤特殊字符
     *
     * @param s
     */
    private static String stripscript(String s) {
        Log.i("ysl", "s 处理前===" + s);
        Pattern p = Pattern.compile("(\\d{3})\\-(\\d{4})\\-(\\d{4})$*");
        Matcher m = p.matcher(s);
        s = m.replaceAll("$1$2$3");
        Log.i("ysl", "s 处理中===" + s);

        String pattern = "[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：”“’。，、？-]";
        String rs = "";
        for (int i = 0; i < s.length(); i++) {
            rs = rs + s.substring(i, i + 1);
            rs = regReplace(rs, pattern, " ");
        }
        rs = regReplace(rs, "/[\\r\\n]/g", "");

        Log.i("ysl", "s 处理后==" + rs);
        return rs;
    }

    /**
     * 正则表达式字符串替换
     *
     * @param content   字符串
     * @param pattern   正则表达式
     * @param newString 新的替换字符串
     * @return 返回替换后的字符串
     */
    public static String regReplace(String content, String pattern, String newString) {
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(content);
        String result = m.replaceAll(newString);
        return result;
    }

    /**
     * 是否为合法手机号
     *
     * @param number
     * @return
     */
    public static boolean isPhoneNumber(String number) {
        String telRegex = "(86-[1][0-9]{10}) | (86[1][0-9]{10})|([1][0-9]{10})";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
//        String telRegex = "[1][1234567890]\\d{9}";
        if (TextUtils.isEmpty(number))
            return false;
        else
            return number.matches(telRegex);
    }
}
