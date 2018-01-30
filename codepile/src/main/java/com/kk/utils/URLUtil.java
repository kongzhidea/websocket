package com.kk.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class URLUtil {


    public static String getParameter(String url, String parameter) {
        if (url == null) {
            return null;
        }
        String reg = "(^|&|\\?)" + parameter + "=([^&]*)(&|$)";
        Pattern pattern = Pattern.compile(reg);
        Matcher matcher = pattern.matcher(url);
        if (matcher.find()) {
            try {
                String src = URLDecoder.decode(matcher.group(2), "UTF-8");
                return src;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    // 获取当前 域名和端口号
    public static String getCurrentDomain(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        sb.append(request.getServerName());
        if (request.getServerPort() != 80) {
            sb.append(":");
            sb.append(request.getServerPort());
        }
        return sb.toString();
    }


}