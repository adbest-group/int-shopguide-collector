package com.bt.shopguide.collector.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by caiting on 2017/9/26.
 */
public class URLUtil {
    public static String getDomain(String urlStr){
        String domain = null;
        try {
            URL url = new URL(urlStr);
            domain = url.getHost();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return domain;
    }

    public static String getMainDomain(String urlStr) {
        String ret = null;
        String host = getDomain(urlStr);
        if(host==null) return ret;
        Pattern pattern = Pattern.compile("[^\\.]+(\\.com\\.cn|\\.net\\.cn|\\.org\\.cn|\\.gov\\.cn|\\.com|\\.net|\\.cn|\\.org|\\.cc|\\.me|\\.tel|\\.mobi|\\.asia|\\.biz|\\.info|\\.name|\\.tv|\\.hk|\\.公司|\\.中国|\\.网络)");
        Matcher matcher = pattern.matcher(host);
        while (matcher.find()) {
            ret = matcher.group();
        }
        return ret;
    }

    public static String getParameter(String queryString,String name,String charset){
//        String paramString = queryString.split("#")[0];
//        Matcher match = Pattern.compile("(^|&)" + name + "=([^&]*)").matcher(paramString);
//        match.lookingAt();
        String value = queryString.replaceAll(".*(^|&)" + name + "=([^&]*).*","$2");
        try {
            value = URLDecoder.decode(URLDecoder.decode(value,charset),charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            value =  null;
        }
        return value;
    }

    public static String getMainDomain1(String url){
        String domain = "";
        domain = URLUtil.getMainDomain(url);
        //如果是乐天特殊处理
        if("linksynergy.com".equalsIgnoreCase(domain)){
            try {
                URL url1 = new URL(url);
                String path = url1.getQuery();
                String realUrl = getParameter(path,"RD_PARM1","utf-8");
                if(realUrl != null){
                    domain = getMainDomain(realUrl);
                }
            } catch (MalformedURLException e) {

            }
        }
        return domain;
    }

    public static void main(String[] args) {
        System.out.println(getMainDomain1("https://click.linksynergy.com/fs-bin/click?id=OfrhIMk*DmY&subid=&offerid=493501.1&type=10&tmpid=1513&u1=doa&RD_PARM1=http%253A%252F%252Fwww.macys.com%252Fcatalog%252Fproduct%252Findex.ognc%253FID%253D2636812"));
    }
}
