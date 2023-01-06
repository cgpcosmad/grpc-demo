package es.carlosgarcia.grpc;

import es.carlosgarcia.AggregationRequest;
import es.carlosgarcia.AggregationResponse;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class AggregationRequestServerStreamObserver implements StreamObserver<AggregationRequest> {

    private final ArrayList<String> words;
    private final StreamObserver<AggregationResponse> respObserver;

    public AggregationRequestServerStreamObserver(StreamObserver<AggregationResponse> respObserver) {
        this.words = new ArrayList<>(128);
        this.respObserver = respObserver;
    }

    @Override
    public void onNext(AggregationRequest aggregationRequest) {
        words.add(aggregationRequest.getMessage());

        System.out.println("-------------------------------------------------");
        System.out.println("Client has sent to SERVER : " + aggregationRequest.getMessage());
        System.out.println("WORDS size " + words.size());
        System.out.println("-------------------------------------------------");
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onCompleted() {

        String respMsg = words.stream().collect(Collectors.joining(", "));

        System.out.println("-------------------------------------------------");
        System.out.println("RETORNAMOS: " + respMsg);

        AggregationResponse aggrResponse = AggregationResponse.newBuilder().setMessage(respMsg).build();


        this.respObserver.onNext(aggrResponse);
        this.respObserver.onCompleted();
    }

    public String getText() {
        return words.stream().collect(Collectors.joining(", "));
    }
}
