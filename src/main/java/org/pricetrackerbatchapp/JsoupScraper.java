package org.pricetrackerbatchapp;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsoupScraper {
    private static final String ASIN_REGEX = "https?:\\/\\/(www\\.)?(.*)amazon\\.([a-z\\.]{2,5})(\\/d\\/(.*)|\\/(.*)\\/?(?:dp|o|gp|-)\\/)(aw\\/d\\/|product\\/)?(B[0-9]{2}[0-9A-Z]{7}|[0-9]{9}(?:X|[0-9]))";

    public static Product scrapeProductFromUrl(String productId, String productUrl) {
        try {
            Product product = null;

            Document doc = Jsoup
                    .connect(productUrl)
                    .get();

            String name = getProductNameFromDoc(doc);
            String imageUrl = getProductImageFromDoc(doc);
            String price = getProductPriceFromDoc(doc);

            if(!StringUtils.isEmpty(productId) && !StringUtils.isEmpty(productUrl)
                    && !StringUtils.isEmpty(name) && !StringUtils.isEmpty(imageUrl) && !StringUtils.isEmpty(price)) {
                product = new Product(productId, name, price, productUrl, imageUrl);
            }

            return product;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getProductIdFromUrl(String url) {
        final Pattern pattern = Pattern.compile(ASIN_REGEX, Pattern.CASE_INSENSITIVE);
        final Matcher matcher = pattern.matcher(url);

        if (matcher.find()) {
            return matcher.group(8);
        }

        return null;
    }

    private static String getProductNameFromDoc(Document doc) {
        Element productTitleElement = doc.getElementById("productTitle");
        String name = null;

        if(productTitleElement != null) {
            name = StringUtils.substring(productTitleElement.text().trim(), 0, 50);

            if(name.length() == 50) {
                name += "...";
            }
        }
        return name;
    }

    private static String getProductImageFromDoc(Document doc) {
        Element imageElement = doc.getElementById("landingImage");
        String price = null;

        if(imageElement != null) {
            price = imageElement.attr("src");
        }
        return price;
    }

    private static String getProductPriceFromDoc(Document doc) {
        StringBuilder priceSb = new StringBuilder();
        Element priceOuterElement = doc.getElementById("apex_offerDisplay_desktop");

        if(priceOuterElement == null) {
            priceOuterElement = doc.getElementById("corePrice_desktop");
        }

        if(priceOuterElement != null) {
            Elements priceInnerElements = priceOuterElement.getElementsByClass("a-offscreen");
            if(!priceInnerElements.isEmpty() && priceInnerElements.get(0) != null) {
                priceSb.append(priceInnerElements.get(0).text().trim());
                for(int i = 1; i < priceInnerElements.size(); ++i) {
                    priceSb.append(" - ").append(priceInnerElements.get(i).text().trim());
                }
            }
        }

        return priceSb.toString();
    }
}
