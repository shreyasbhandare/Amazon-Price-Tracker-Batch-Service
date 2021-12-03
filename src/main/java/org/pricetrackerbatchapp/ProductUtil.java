package org.pricetrackerbatchapp;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductUtil {
    private static final String ASIN_REGEX = "https?:\\/\\/(www\\.)?(.*)amazon\\.([a-z\\.]{2,5})(\\/d\\/(.*)|\\/(.*)\\/?(?:dp|o|gp|-)\\/)(aw\\/d\\/|product\\/)?(B[0-9]{2}[0-9A-Z]{7}|[0-9]{9}(?:X|[0-9]))";

    public static Product fetchProductFromUrl(String productId, String productUrl) {
        try {
            Product product = null;

            final String X_RAPIDAPI_HOST = System.getenv("X_RAPIDAPI_HOST") != null ? System.getenv("X_RAPIDAPI_HOST") : PropsConfig.getAppProps().getProperty("X_RAPIDAPI_HOST");
            final String X_RAPIDAPI_KEY = System.getenv("X_RAPIDAPI_KEY") != null ? System.getenv("X_RAPIDAPI_KEY") : PropsConfig.getAppProps().getProperty("X_RAPIDAPI_KEY");

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://axesso-axesso-amazon-data-service-v1.p.rapidapi.com/amz/amazon-lookup-product?url=" + productUrl))
                    .header("x-rapidapi-host", X_RAPIDAPI_HOST)
                    .header("x-rapidapi-key", X_RAPIDAPI_KEY)
                    .method("GET", HttpRequest.BodyPublishers.noBody())
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() / 100 == 2 && response.body() != null) {
                JSONObject productObj = new JSONObject(response.body());

                String name = getProductNameFromJson(productObj);
                String imageUrl = getProductImageFromJson(productObj);
                String price = getProductPriceFromJson(productObj);

                if (!StringUtils.isEmpty(productId) && !StringUtils.isEmpty(productUrl)
                        && !StringUtils.isEmpty(name) && !StringUtils.isEmpty(imageUrl) && !StringUtils.isEmpty(price)) {
                    product = new Product(productId, name, price, productUrl, imageUrl);
                }
            }

            return product;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String getProductNameFromJson(JSONObject productObj) {
        String name = null;

        if(productObj != null) {
            name = StringUtils.substring(productObj.getString("productTitle"), 0, 50);
            if(name.length() == 50) {
                name += "...";
            }
        }

        return name;
    }

    private static String getProductImageFromJson(JSONObject productObj) {
        String image = null;

        if(productObj != null) {
            JSONObject imageObject = productObj.getJSONObject("mainImage");
            if(imageObject != null) {
                image = imageObject.getString("imageUrl");
            }
        }

        return image;
    }

    private static String getProductPriceFromJson(JSONObject productObj) {
        String priceStr = null;

        if(productObj != null) {
            if(productObj.has("priceRange") && !productObj.isNull("priceRange")) {
                priceStr = productObj.getString("priceRange");
            } else {
                priceStr= productObj.getDouble("price") + "";
            }
        }

        return priceStr;
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
