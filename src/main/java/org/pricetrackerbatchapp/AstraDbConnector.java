package org.pricetrackerbatchapp;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.stargate.grpc.StargateBearerToken;
import io.stargate.proto.StargateGrpc;

import java.util.concurrent.TimeUnit;

public final class AstraDbConnector {
    private static StargateGrpc.StargateBlockingStub blockingStub;

    public static StargateGrpc.StargateBlockingStub buildBlockingStub() {
        try {
            if (blockingStub == null) {
                String ASTRA_DB_ID = PropsConfig.getAppProps().getProperty("ASTRA_DB_ID");
                String ASTRA_DB_REGION = PropsConfig.getAppProps().getProperty("ASTRA_DB_REGION");
                String ASTRA_TOKEN = PropsConfig.getAppProps().getProperty("ASTRA_TOKEN");

                ManagedChannel channel = ManagedChannelBuilder
                        .forAddress(ASTRA_DB_ID + "-" + ASTRA_DB_REGION + ".apps.astra.datastax.com", 443)
                        .useTransportSecurity()
                        .build();

                // blocking stub version
                blockingStub = StargateGrpc.newBlockingStub(channel)
                        .withDeadlineAfter(10, TimeUnit.SECONDS)
                        .withCallCredentials(new StargateBearerToken(ASTRA_TOKEN));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return blockingStub;
    }
}
