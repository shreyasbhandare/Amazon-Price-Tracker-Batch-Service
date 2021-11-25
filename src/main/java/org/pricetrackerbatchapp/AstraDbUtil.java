package org.pricetrackerbatchapp;

import io.stargate.proto.QueryOuterClass;
import io.stargate.proto.StargateGrpc;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AstraDbUtil {
    private static final String ALL_SUBSCRIPTION_QUERY = "select * from amzn_price_tracker.subscription";

    public static Map<String, List<String>> getUserProductsMapFromDB() {
        StargateGrpc.StargateBlockingStub blockingStub = AstraDbConnector.buildBlockingStub();
        Map<String, List<String>> userProductsMap = new HashMap<>();

        QueryOuterClass.Response queryString = blockingStub.executeQuery(QueryOuterClass
                .Query.newBuilder()
                .setCql(ALL_SUBSCRIPTION_QUERY)
                .build());

        QueryOuterClass.ResultSet rs = queryString.getResultSet();

        if(rs.isInitialized()) {
            for (QueryOuterClass.Row row : rs.getRowsList()) {
                String email = row.getValues(0).getString();
                List<String> productList = new ArrayList<>();

                QueryOuterClass.Collection collection = row.getValues(1).getCollection();
                for(QueryOuterClass.Value value : collection.getElementsList()) {
                    productList.add(value.getString());
                }

                System.out.println(email + " - " + productList);

                if(!StringUtils.isEmpty(email) && !productList.isEmpty()) {
                    userProductsMap.put(email, productList);
                }
            }
        }

        return userProductsMap;
    }
}
