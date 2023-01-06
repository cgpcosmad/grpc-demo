package es.carlosgarcia.grpc;

import es.carlosgarcia.ChatRequest;
import es.carlosgarcia.ChatResponse;
import io.grpc.stub.StreamObserver;

import java.time.LocalTime;

public class ChatRequestServerStreamObserver implements StreamObserver<ChatRequest> {
    private final StreamObserver<ChatResponse> responseObserver;

    public ChatRequestServerStreamObserver(StreamObserver<ChatResponse> responseObserver) {
        this.responseObserver = responseObserver;
    }

    @Override
    public void onNext(ChatRequest chatRequest) {
        if (this.hasToAnswer(chatRequest.getMessage())) {
            this.responseObserver.onNext(this.buildChatResponse("Yes"));
        } else if (this.isGoodBye(chatRequest.getMessage())) {
            this.responseObserver.onNext(this.buildChatResponse("Bye"));
            this.responseObserver.onCompleted();
        } else {
            // We do not response anything
        }


    }


    @Override
    public void onError(Throwable throwable) {
        this.responseObserver.onCompleted();
    }

    @Override
    public void onCompleted() {
        this.responseObserver.onCompleted();
    }

    private ChatResponse buildChatResponse(String message) {
        return ChatResponse.newBuilder()
                .setMessage(message)
                .setTimestamp(LocalTime.now().toNanoOfDay())
                .build();
    }

    private boolean hasToAnswer(String message) {
        return message.contains("?");
    }

    private boolean isGoodBye(String message) {
        return message.toLowerCase().contains("bye");
    }
}
