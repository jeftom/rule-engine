package com.ctrip.infosec.rule.resource;

import static com.ctrip.infosec.common.SarsMonitorWrapper.afterInvoke;
import static com.ctrip.infosec.common.SarsMonitorWrapper.beforeInvoke;
import static com.ctrip.infosec.common.SarsMonitorWrapper.fault;
import com.ctrip.infosec.configs.rule.trace.logger.TraceLogger;
import com.ctrip.infosec.rule.Contexts;
import com.ctrip.infosec.rule.resource.ESB.ESBClient;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import org.apache.commons.lang3.StringUtils;

/*
 * Created by lpxie on 15-3-20.
 */
public class CardInfo {

    private static final Logger logger = LoggerFactory.getLogger(CardInfo.class);

    static final String serviceName = "SOAServiceClient";
    static final String operationName = "AccCashCreditCard_GetCreditCardInfo";

    public static Map query(String cardInfoId) {
        Map<String, Object> params = new HashMap<>();
        params.put("cardInfoId", cardInfoId);
        return DataProxy.queryForMap(serviceName, operationName, params);
    }

    /**
     * 这里的serviceName必须是“getinfo”
     */
    @Deprecated
    public static Map query(String serviceName, Map<String, Object> params) {
        beforeInvoke("CardInfo.query");
        Map<String, String> result = new HashMap();
        try {
            String cardInfoId = (String) params.get("cardInfoId");
            if (StringUtils.isBlank(cardInfoId) || StringUtils.equals("0", cardInfoId)) {
                return result;
            }

            // Cache
//            CacheProvider cache = CacheProviderFactory.getCacheProvider(clusterName);
//            String cacheKey = buildCacheKey(cardInfoId);
//            String cachedResult = cache.get(cacheKey);
//            if (cachedResult != null) {
//                return Utils.JSON.parseObject(cachedResult, Map.class);
//            }
            String xml = ESBClient.requestESB("AccCash.CreditCard.GetCreditCardInfo", "<GetCreditCardInfoRequest><CardInfoId>" + cardInfoId + "</CardInfoId></GetCreditCardInfoRequest>");
            if (xml == null || xml.isEmpty()) {
                return result;
            }
            Document document = DocumentHelper.parseText(xml);
            String xpath = "/Response/GetCreditCardInfoResponse/CreditCardItems/CreditCardInfoResponseItem";
            List<Element> list = document.selectNodes(xpath);
            if (list == null || list.isEmpty()) {
                return result;
            }

            for (Element creditCard : list) {
                Iterator iterator = creditCard.elements().iterator();
                while (iterator.hasNext()) {
                    Element element = (Element) iterator.next();
                    result.put(element.getName(), element.getStringValue());
                }
            }

            // Cache
//            if (!result.isEmpty()) {
//                cache.set(cacheKey, Utils.JSON.toJSONString(result));
//                cache.expire(cacheKey, cacheExpireTime);
//            }
        } catch (Exception ex) {
            fault("CardInfo.query");
            logger.error(Contexts.getLogPrefix() + "invoke CardInfo.query fault.", ex);
            TraceLogger.traceLog("执行GetCreditCardInfo异常: " + ex.toString());
        } finally {
            afterInvoke("CardInfo.query");
        }
        return result;
    }

    public static Map queryYA(String cardInfoId) {
        beforeInvoke("CardInfo.queryYA");
        Map<String, String> result = new HashMap();
        try {
            if (StringUtils.isBlank(cardInfoId) || StringUtils.equals("0", cardInfoId)) {
                return result;
            }

            // Cache
//            CacheProvider cache = CacheProviderFactory.getCacheProvider(clusterName);
//            String cacheKey = buildCacheKey(cardInfoId);
//            String cachedResult = cache.get(cacheKey);
//            if (cachedResult != null) {
//                return Utils.JSON.parseObject(cachedResult, Map.class);
//            }
            String xml = ESBClient.requestESB("AccCash.WOTCreditCard.GetCreditCardInfo", "<GetCreditCardInfoRequest><CardInfoId>" + cardInfoId + "</CardInfoId></GetCreditCardInfoRequest>");
            if (xml == null || xml.isEmpty()) {
                return result;
            }
            Document document = DocumentHelper.parseText(xml);
            String xpath = "/Response/GetCreditCardInfoResponse/CreditCardItems/CreditCardInfoResponseItem";
            List<Element> list = document.selectNodes(xpath);
            if (list == null || list.isEmpty()) {
                return result;
            }

            for (Element creditCard : list) {
                Iterator iterator = creditCard.elements().iterator();
                while (iterator.hasNext()) {
                    Element element = (Element) iterator.next();
                    result.put(element.getName(), element.getStringValue());
                }
            }

            // Cache
//            if (!result.isEmpty()) {
//                cache.set(cacheKey, Utils.JSON.toJSONString(result));
//                cache.expire(cacheKey, cacheExpireTime);
//            }
        } catch (Exception ex) {
            fault("CardInfo.queryYA");
            logger.error(Contexts.getLogPrefix() + "invoke CardInfo.queryYA fault.", ex);
            TraceLogger.traceLog("执行GetCreditCardInfo异常: " + ex.toString());
        } finally {
            afterInvoke("CardInfo.queryYA");
        }
        return result;
    }
}
