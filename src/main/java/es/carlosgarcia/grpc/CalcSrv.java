package es.carlosgarcia.grpc;


import es.carlosgarcia.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Duration;

@GrpcService
public class CalcSrv  extends CalcServiceGrpc.CalcServiceImplBase {

    @Override
    public void calculate(CalcRequest request, StreamObserver<CalcResponse> responseObserver) {
        long res;

        if ( CalcRequest.Operation.SUM.equals(request.getOperation()) ) {
            res = request.getN1() + request.getN2();
        } else if ( CalcRequest.Operation.SUSTRACT.equals(request.getOperation()) ) {
            res = request.getN1() - request.getN2();
        } else {
            responseObserver.onError(Status.INVALID_ARGUMENT.asException());
            return;
        }

        CalcResponse response = CalcResponse.newBuilder().setValue(res).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void ping(PingRequest request, StreamObserver<PingResponse> responseObserver) {
        try {
            for (int i = 1; i <= request.getCount(); i++) {
                String msg = String.format("%s: %d", request.getMessage(), i);
                PingResponse resp = PingResponse.newBuilder().setMessage(msg).build();
                responseObserver.onNext(resp);

                Thread.sleep(Duration.ofSeconds(1).toMillis());

            }
            responseObserver.onCompleted();
        } catch (InterruptedException e) {
            responseObserver.onError(Status.ABORTED.withDescription("Se ha interrumpido la ejecución").asRuntimeException());
        }

    }

    // The server side has to implement the request side (return method) and client the response side (request method)
    @Override
    public StreamObserver<AggregationRequest> aggregate(StreamObserver<AggregationResponse> responseObserver) {
        return new AggregationRequestServerStreamObserver(responseObserver);
    }

    @Override
    public StreamObserver<ChatRequest> chat(StreamObserver<ChatResponse> responseObserver) {
        return new ChatRequestServerStreamObserver(responseObserver);
    }
}

