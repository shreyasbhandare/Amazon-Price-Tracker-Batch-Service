package org.pricetrackerbatchapp;

import jakarta.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;

import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main( String[] args ) throws InterruptedException, MalformedURLException {
        URL fixie = new URL(System.getenv("FIXIE_SOCKS_HOST"));
        String[] fixieUserInfo = fixie.getUserInfo().split(":");
        String fixieUser = fixieUserInfo[0];
        String fixiePassword = fixieUserInfo[1];
        System.setProperty("http.proxyHost", fixie.getHost());
        System.setProperty("http.proxyPort", fixie.getPort() + "");
        Authenticator.setDefault(new ProxyAuthenticator(fixieUser, fixiePassword));

        doBatch();
    }

    public static void doBatch() throws InterruptedException {
        Map<String, List<String>> userProductsMap = AstraDbUtil.getUserProductsMapFromDB();
        Map<String, Product> productPricesMap = new HashMap<>();

        if(!userProductsMap.isEmpty()) {
            for(String email : userProductsMap.keySet()) {
                List<String> productUrls = userProductsMap.get(email);
                List<Product> productList = new ArrayList<>();

                for(String productUrl : productUrls) {
                    String productId = JsoupScraper.getProductIdFromUrl(productUrl);

                    if(StringUtils.isEmpty(productId)) {
                        continue;
                    }

                    if(productPricesMap.containsKey(productId)) {
                        productList.add(productPricesMap.get(productId));
                        continue;
                    }

                    // scrape product
                    Product product = JsoupScraper.scrapeProductFromUrl(productId, productUrl);

                    if(product != null) {
                        productPricesMap.put(productId, product);
                        productList.add(product);
                    }
                }

                // send email
                String htmlMessage = MailSender.createHtmlMessage(email, productList);

                if(!StringUtils.isEmpty(email) && !StringUtils.isEmpty(htmlMessage)) {
                    try {
                        MailSender.sendEmail(email, htmlMessage);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                }

                Thread.sleep(3000);
            }
        }
    }
}
