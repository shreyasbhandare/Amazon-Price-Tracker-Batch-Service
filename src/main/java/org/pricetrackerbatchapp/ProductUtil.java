package org.pricetrackerbatchapp;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProductUtil {
    private static final String ASIN_REGEX = "https?:\\/\\/(www\\.)?(.*)amazon\\.([a-z\\.]{2,5})(\\/d\\/(.*)|\\/(.*)\\/?(?:dp|o|gp|-)\\/)(aw\\/d\\/|product\\/)?(B[0-9]{2}[0-9A-Z]{7}|[0-9]{9}(?:X|[0-9]))";

    public static Product fetchProductFromUrl(String productId, String productUrl) {
        try {
            OkHttpClient client = AxessoConnector.getOkHttpClient();
            Product product = null;

            final String X_RAPIDAPI_HOST = System.getenv("X_RAPIDAPI_HOST") != null ? System.getenv("X_RAPIDAPI_HOST") : PropsConfig.getAppProps().getProperty("X_RAPIDAPI_HOST");
            final String X_RAPIDAPI_KEY = System.getenv("X_RAPIDAPI_KEY") != null ? System.getenv("X_RAPIDAPI_KEY") : PropsConfig.getAppProps().getProperty("X_RAPIDAPI_KEY");

            Request request = new Request.Builder()
                    .url("https://axesso-axesso-amazon-data-service-v1.p.rapidapi.com/amz/amazon-lookup-product?url=" + productUrl)
                    .get()
                    .addHeader("x-rapidapi-host", "axesso-axesso-amazon-data-service-v1.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", "184add03c2mshbbb9c80d52de11dp18b7edjsn78fe4a502d1d")
                    .build();

            Response response = client.newCall(request).execute();

            if(response.isSuccessful() && response.body() != null) {
                JSONObject productObj = new JSONObject(response.body().string());

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
