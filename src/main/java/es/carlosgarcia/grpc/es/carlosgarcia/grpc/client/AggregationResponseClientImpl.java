package es.carlosgarcia.grpc.es.carlosgarcia.grpc.client;

import es.carlosgarcia.AggregationResponse;
import io.grpc.stub.StreamObserver;

import java.util.concurrent.CountDownLatch;

public class AggregationResponseClientImpl implements StreamObserver<AggregationResponse> {


    private String response;
    private final CountDownLatch latch;

    public AggregationResponseClientImpl(CountDownLatch latch) {
        this.latch = latch;
    }

    @Override
    public void onNext(AggregationResponse aggregationResponse) {
        this.response = aggregationResponse.getMessage();
    }

    @Override
    public void onError(Throwable throwable) {
        this.latch.countDown();
    }

    @Override
    public void onCompleted() {
        this.latch.countDown();
    }

    public String getResponse() {
        return response;
    }
}
