package com.kefu.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author:付风松
 * @Description:
 * @Date:Created in  10:02 2019/1/8
 * @ModefiedBy:
 */
@Controller
@RequestMapping("/wxRequest")
public class WXController {

    private static final String appid = "wxfc626b9914fdcceb";

    private static final String secret = "11ce8e3ad736c6ea173c45c4c609b890";

    private static final String noncestr = "Wm3WZYTPz0wzccnW";

    private static final String ACCESS_TOKEN = "accessToken";

    private Cache<String, String> cache;

    @Autowired
    public WXController(EhCacheManager ehCacheManager) {
        cache = ehCacheManager.getCache("cacheToken");
    }

    @RequestMapping("/getByUrl")
    public @ResponseBody
    Map<String, Object> getResult(@RequestParam("local_href") String localhref) {
        String accessToken = null;
        if (cache.get(ACCESS_TOKEN) == null) {
            accessToken = getAccess_Token();
            cache.put(ACCESS_TOKEN, accessToken);
        } else {
            accessToken = cache.get(ACCESS_TOKEN);
        }

        String ticket = getApiTicket(accessToken);
        int timeStamp = getSecondTimestampTwo(new Date());
        String signature = getSignature(ticket, localhref, timeStamp);
        Map<String, Object> map = new HashMap<>();
        map.put("appId", appid);
        map.put("timestamp", timeStamp);
        map.put("nonceStr", noncestr);
        map.put("signature", signature);
        return map;
    }

    //   获取access_token
    private String getAccess_Token() {
        String rqeust_trl_acctoken = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appid + "&secret=" + secret;
        try {
            URL url = new URL(rqeust_trl_acctoken);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            String ret = IOUtils.toString(httpsURLConnection.getInputStream(), "UTF-8");
            System.out.println(ret);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(ret);
            return node.get("access_token").asText();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //获取Api_Ticket
    private String getApiTicket(String accesstoken) {
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accesstoken + "&type=jsapi";
        try {
            URL url1 = new URL(url);
            HttpsURLConnection connection = (HttpsURLConnection) url1.openConnection();
            connection.setRequestMethod("GET");
            String ret = IOUtils.toString(connection.getInputStream(), "UTF-8");
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode node = objectMapper.readTree(ret);
            return node.get("ticket").asText();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //生成signature
    private String getSignature(String ticket, String url, int timeStamp) {
        String param = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + timeStamp + "&url=" + url;
        return DigestUtils.sha1Hex(param);
    }

    private int getSecondTimestampTwo(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime() / 1000);
        System.out.println("  timeStamp---> " + timestamp);
        return Integer.valueOf(timestamp);
    }
}
