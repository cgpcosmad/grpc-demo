package es.carlosgarcia.grpc.UnarySyncGRPCDemo;

import es.carlosgarcia.AggregationRequest;
import es.carlosgarcia.CalcRequest;
import es.carlosgarcia.CalcResponse;
import es.carlosgarcia.CalcServiceGrpc;
import es.carlosgarcia.grpc.es.carlosgarcia.grpc.client.AggregationResponseClientImpl;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@SpringBootTest
class UnarySyncGrpcDemoApplicationTests {

	private CalcServiceGrpc.CalcServiceBlockingStub blockingStub;
	private CalcServiceGrpc.CalcServiceStub nonBlockingStub;

	private ManagedChannel managedChannel;


	@BeforeEach
	void setup() {
		this.managedChannel = ManagedChannelBuilder.forAddress("localhost", 8080)
				.usePlaintext()
				.build();

		this.nonBlockingStub = CalcServiceGrpc.newStub(managedChannel);
		this.blockingStub = CalcServiceGrpc.newBlockingStub(managedChannel);

	}

	@AfterEach
	void release() {
		managedChannel.shutdown();
	}


	@Test
	void callCalculate() {
		CalcRequest req = CalcRequest.newBuilder()
				.setN1(2)
				.setN2(3)
				.setOperation(CalcRequest.Operation.SUM)
				.build();
		CalcResponse resp = this.blockingStub.calculate(req);

		Assertions.assertEquals(5, resp.getValue());
	}


	@Test
	void calcAggregateTest() throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(1);
		List<String> words = Arrays.asList("HOLA", "ADIOS", "HELLO", "THANKS");

		AggregationResponseClientImpl serverResponseObserver = new AggregationResponseClientImpl(latch);
		StreamObserver<AggregationRequest> requestsStub = nonBlockingStub.aggregate(serverResponseObserver);

		for (String word : words) {
			AggregationRequest r = AggregationRequest.newBuilder().setMessage(word).build();
			requestsStub.onNext(r);
		}
		requestsStub.onCompleted();
		latch.await();


		Assertions.assertEquals("HOLA, ADIOS, HELLO, THANKS", serverResponseObserver.getResponse());
	}

}
