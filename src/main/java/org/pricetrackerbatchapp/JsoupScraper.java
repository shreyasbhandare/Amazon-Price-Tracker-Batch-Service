package org.pricetrackerbatchapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsoupScraper {
    private static final String ASIN_REGEX = "https?:\\/\\/(www\\.)?(.*)amazon\\.([a-z\\.]{2,5})(\\/d\\/(.*)|\\/(.*)\\/?(?:dp|o|gp|-)\\/)(aw\\/d\\/|product\\/)?(B[0-9]{2}[0-9A-Z]{7}|[0-9]{9}(?:X|[0-9]))";

    public static Product scrapeProductFromUrl(String productId, String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Chrome")
                    .timeout(5000)
                    .get();

            Element productTitleElement = doc.getElementById("productTitle");

            Product product = new Product(productId, url);

            if(productTitleElement != null) {
                product.setName(productTitleElement.text().trim());
            }

            Element priceOuterElement = doc.getElementById("apex_offerDisplay_desktop");
            Elements priceInnerElement = null;

            if(priceOuterElement != null) {
                priceInnerElement = priceOuterElement.getElementsByClass("a-offscreen");
            }

            if(priceInnerElement != null && !priceInnerElement.isEmpty() && priceInnerElement.get(0) != null) {
                product.setPrice(priceInnerElement.text().trim());
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
}
