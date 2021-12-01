package org.pricetrackerbatchapp;

import jakarta.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    public static void main(String[] args) throws InterruptedException {
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
                    String productId = ProductUtil.getProductIdFromUrl(productUrl);

                    if(StringUtils.isEmpty(productId)) {
                        continue;
                    }

                    if(productPricesMap.containsKey(productId)) {
                        productList.add(productPricesMap.get(productId));
                        continue;
                    }

                    // scrape product
                    Product product = ProductUtil.fetchProductFromUrl(productId, productUrl);

                    if(product != null) {
                        productPricesMap.put(productId, product);
                        productList.add(product);
                    }

                    // 1.5 sec gap between fetching products.
                    Thread.sleep(1500);
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
            }
        }
    }
}
