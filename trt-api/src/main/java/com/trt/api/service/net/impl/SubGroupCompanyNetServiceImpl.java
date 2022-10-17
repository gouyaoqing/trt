package com.trt.api.service.net.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trt.api.service.net.SubGroupCompanyNetService;
import com.trt.common.data.model.SubGroupCompany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static com.trt.api.service.impl.PharmacyCompanyServiceImpl.DEFAULT_ENCODE;

@Service
@Slf4j
public class SubGroupCompanyNetServiceImpl implements SubGroupCompanyNetService {
    @Override
    public List<SubGroupCompany> getByGroupCompany(String groupCompanyName, String token) {
        String url = "https://ydt.sinohealth.com/pharmacy-center/data/chain/portrait/getChildChnPage";

        List<SubGroupCompany> results = Lists.newArrayList();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new StringEntity(JSON.toJSONString(new SubGroupCompanyQuery(groupCompanyName, 1, 1000)), Charset.forName(DEFAULT_ENCODE)));
            httpPost.addHeader("token", token);
            httpPost.addHeader("channel", "web");
            httpPost.addHeader("Content-type", "application/json;charset=UTF-8");
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("accept-language", "zh-CN,zh;q=0.9");

            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            String response = EntityUtils.toString(httpResponse.getEntity(), DEFAULT_ENCODE);

            JSONObject contents = JSON.parseObject(response).getJSONObject("content");
            if (contents != null) {
                JSONArray list = contents.getJSONArray("list");
                log.error("{} sub group company size is {}", groupCompanyName, list.size());
                for (int i = 0; i < list.size(); i++) {
                    JSONObject content = list.getJSONObject(i);
                    SubGroupCompany subGroupCompany = new SubGroupCompany();
                    results.add(subGroupCompany);

                    subGroupCompany.setName(content.getString("parent"));
                    subGroupCompany.setProvince(content.getString("province"));
                    subGroupCompany.setCity(content.getString("city"));
                    subGroupCompany.setStoreNum(content.getInteger("storeNum"));
                    subGroupCompany.setTotalMarketPotential(content.getInteger("totalMarketPotential"));
                }
            } else {
                log.error(JSON.parseObject(response).getString("msg"));
            }

        } catch (Exception e) {
            log.error("net get sub group company error", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    static class SubGroupCompanyQuery {
        private String chnName;
        private Integer currPage = 1;
        private Integer pageSize = 500;
    }
}
