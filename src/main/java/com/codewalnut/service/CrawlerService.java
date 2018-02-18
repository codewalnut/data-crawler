package com.codewalnut.service;

import com.saysth.commons.http.HttpHelper;
import com.saysth.commons.utils.LogUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Weway-RG001 on 2018-02-05.
 */
@Service
public class CrawlerService {
    private static Logger log = LoggerFactory.getLogger(CrawlerService.class);

    @Autowired
    private ApplicationProperties applicationProperties;

    /**
     * 返回文件
     *
     * @param height
     * @param filepath
     * @return
     */
    public boolean fetchBlockFile(int height, String filepath) {
        long bgn = System.currentTimeMillis();

        String ua = applicationProperties.getCrawlerBrowserUserAgent();
        String url = applicationProperties.getCrawlerTargetApiAddr();

        String target = StringUtils.replaceOnce(url, "{$height}", height + "");
        String filename = filepath + height + ".json";
        try {
            File file = new File(filename);
            if (file.exists()) {
                String content = FileUtils.readFileToString(new File(filename), Charset.forName("UTF-8"));
                if (!StringUtils.startsWith(content, "http")) {
                    return true;
                }
            }
            FileUtils.writeStringToFile(new File(filename), target, Charset.forName("UTF-8"));
            HttpHelper.Response res = HttpHelper.connect(target).timeout(1000 * 120).userAgent(ua).maxBodySize(Integer.MAX_VALUE).get();
            if (res.statusCode() == 200) {
                String json = res.html();
                FileUtils.writeStringToFile(new File(filename), json, Charset.forName("UTF-8"));
                long end = System.currentTimeMillis();
                String gap = LogUtils.getElapse(bgn, end);
                log.info("fetched: {} {}", target, gap);
                return true;
            } else {
                log.info("fetch {} response status code not 200", url);
                return false;
            }
        } catch (IOException ex) {
            log.error("writeFile Failed!", ex);
            return false;
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
            return false;
        }
    }

    public String fetchAddressInfo(String address) {
        long bgn = System.currentTimeMillis();

        String ua = applicationProperties.getCrawlerBrowserUserAgent();
//        String url = "https://blockchain.info/rawaddr/";
        String url = "https://blockchain.info/multiaddr?active=";
//        String url = "https://chain.api.btc.com/v3/address/";

        String target = url + address;
        try {
            HttpHelper.Response res = HttpHelper.connect(target).timeout(1000 * 120).userAgent(ua).get();
            if (res.statusCode() == 200) {
                String json = res.html();
                long end = System.currentTimeMillis();
                String gap = LogUtils.getElapse(bgn, end);
                log.info("fetched: {} {}", target, gap);
                return json;
            } else {
                log.info("fetch {} response status code not 200", url);
                return null;
            }
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
            return null;
        }
    }

}
