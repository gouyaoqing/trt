package com.trt.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.trt.api.model.PharmacyInsertModel;
import com.trt.api.service.PharmacyCompanyService;
import com.trt.common.data.model.GroupCompany;
import com.trt.common.data.model.Pharmacy;
import com.trt.common.data.model.SubGroupCompany;
import com.trt.common.data.model.query.QPharmacy;
import com.trt.common.data.service.PharmacyService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.List;

/**
 * @author gouyaoqing
 * @since 2022-10-12
 */
@Service
@Slf4j
public class PharmacyCompanyServiceImpl implements PharmacyCompanyService {
    public static final String DEFAULT_ENCODE = "UTF-8";

    @Resource
    private PharmacyService pharmacyService;

    @Override
    public void importPharmacyByNet(Integer cityNum, Integer pageNum, String token) {
        List<String> cities = getAllCities(token);
        log.error("全部城市:{}", JSON.toJSON(cities));
        int pageSize = 500;
        String city = cities.get(cityNum);
        int currPage = pageNum <= 0 ? 1 : pageNum;
        List<PharmacyInsertModel> list = Lists.newArrayList();
        for (int i = cityNum; i < cities.size(); i++) {
            city = cities.get(i);
            cityNum = i;
            currPage = 1;
            do {
                try {
                    list = getPharmacies(currPage, pageSize, city, token);
                    list.forEach(pharmacyInsertModel -> pharmacyService.getOrInsert(pharmacyInsertModel.getPharmacy(), pharmacyInsertModel.getGroupCompany(), pharmacyInsertModel.getSubGroupCompany()));

                    log.error("第{}个城市 {} 的第{}页已完成", cityNum, city, currPage);
                    currPage++;
                    if (list.size() < pageSize) {
                        break;
                    }
                } catch (Exception e) {
                    log.error("*******************第{}个城市 {} 的第{}页异常*****************", cityNum, city, currPage, e);
                } finally {
//                if (list.size() < pageSize) {
//                    break;
//                }
                    try {
                        Thread.sleep(Duration.ofSeconds(30).toMillis());
                    } catch (InterruptedException e) {

                    }
                }

            } while (true);
            log.error("{} 已全部完成", city);
        }


    }

    private List<String> getAllCities(String token) {
        String url = "https://ydt.sinohealth.com/pharmacy-center/province/city/searchCityList?cityName=";
        List<String> allCities = Lists.newArrayList();

        CloseableHttpClient httpClient = HttpClients.createDefault();

        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        try {
            //upStatistical
            HttpGet httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("token", token);
            httpGet.addHeader("channel", "web");
            httpGet.addHeader("Content-type", "application/json;charset=UTF-8");
            httpGet.addHeader("Accept", "application/json");
            httpGet.addHeader("accept-language", "zh-CN,zh;q=0.9");

            CloseableHttpResponse httpResponse = httpClient.execute(httpGet);
            String response = EntityUtils.toString(httpResponse.getEntity(), DEFAULT_ENCODE);

            JSONArray contents = JSON.parseObject(response).getJSONArray("content");
            if (contents != null) {
                for (int i = 0; i < contents.size(); i++) {
                    JSONObject content = contents.getJSONObject(i);
                    allCities.add(content.getString("cityName"));
                }
            }

        } catch (Exception e) {
            log.error("get all city error", e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.error("get all city close http client error", e);
            }
        }
        return allCities;
    }

    @Data
    @Accessors(chain = true)
    @AllArgsConstructor
    static class PharmacyQuery {
        private String city;
        private Integer currPage = 1; //有用
        private Integer currentPage = 1;
        private Integer pageSize = 500;
    }

    private List<PharmacyInsertModel> getPharmacies(int pageNum, int pageSize, String city, String token) {
        String url = "https://ydt.sinohealth.com/pharmacy-center/data/store/map/info";

        List<PharmacyInsertModel> results = Lists.newArrayList();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(10000).setConnectTimeout(10000).build();
        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            httpPost.setEntity(new StringEntity(JSON.toJSONString(new PharmacyQuery(city, pageNum, 1, pageSize)), Charset.forName(DEFAULT_ENCODE)));
            httpPost.addHeader("token", token);
            httpPost.addHeader("channel", "web");
            httpPost.addHeader("Content-type", "application/json;charset=UTF-8");
            httpPost.addHeader("Accept", "application/json");
            httpPost.addHeader("accept-language", "zh-CN,zh;q=0.9");

            CloseableHttpResponse httpResponse = httpClient.execute(httpPost);
            String response = EntityUtils.toString(httpResponse.getEntity(), DEFAULT_ENCODE);

            JSONObject contents = JSON.parseObject(response).getJSONObject("content");
            if (contents != null) {
                JSONObject pageUtils = contents.getJSONObject("pageUtils");
                JSONArray list = pageUtils.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    JSONObject content = list.getJSONObject(i);

                    GroupCompany groupCompany = null;
                    SubGroupCompany subGroupCompany = null;
                    Pharmacy pharmacy = new Pharmacy();
                    if (StringUtils.isNotBlank(content.getString("chn"))) {
                        groupCompany = new GroupCompany();
                        groupCompany.setName(content.getString("chn"));
                    }
                    if (StringUtils.isNotBlank(content.getString("parentName"))) {
                        subGroupCompany = new SubGroupCompany();
                        subGroupCompany.setName(content.getString("parentName"));
                    }
                    pharmacy.setAddress(content.getString("address"));
                    pharmacy.setArea(content.getString("area"));
                    pharmacy.setGroupCompanyType(content.getString("chnBool"));
                    pharmacy.setCity(content.getString("city"));
                    pharmacy.setDistrict(content.getString("district"));
                    pharmacy.setPopNum(Long.parseLong(content.getString("popNum")));
                    pharmacy.setProvince(content.getString("province"));
                    pharmacy.setStoreId(content.getString("stoId"));
                    pharmacy.setName(content.getString("stoName"));
                    pharmacy.setStoreArea(content.getString("storeArea"));
                    pharmacy.setLevel(content.getString("storeLevel"));
                    pharmacy.setType(content.getString("storeType"));
                    pharmacy.setStreet(content.getString("street"));
                    pharmacy.setTelephone(content.getString("telephone"));

                    JSONObject storeAroundDto = content.getJSONObject("storeAroundDto");
                    if (storeAroundDto != null) {
                        pharmacy.setAroundSpecial(storeAroundDto.getString("special"));
                        pharmacy.setAroundBusinessCount(storeAroundDto.getString("business") == null ? 0 : Integer.parseInt(storeAroundDto.getString("business")));
                        pharmacy.setAroundCommunityCount(storeAroundDto.getString("community") == null ? 0 : Integer.parseInt(storeAroundDto.getString("community")));
                        pharmacy.setAroundDistanceCount(storeAroundDto.getString("distance") == null ? 0 : Integer.parseInt(storeAroundDto.getString("distance")));
                        pharmacy.setAroundHospitalCount(storeAroundDto.getString("hospital") == null ? 0 : Integer.parseInt(storeAroundDto.getString("hospital")));
                        pharmacy.setAroundIndustryCount(storeAroundDto.getString("industry") == null ? 0 : Integer.parseInt(storeAroundDto.getString("industry")));
                        pharmacy.setAroundOfficeCount(storeAroundDto.getString("office") == null ? 0 : Integer.parseInt(storeAroundDto.getString("office")));
                    }

                    results.add(new PharmacyInsertModel().setPharmacy(pharmacy).setGroupCompany(groupCompany).setSubGroupCompany(subGroupCompany));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
