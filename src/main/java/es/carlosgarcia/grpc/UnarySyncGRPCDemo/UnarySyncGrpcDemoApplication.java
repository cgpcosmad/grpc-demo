package es.carlosgarcia.grpc.UnarySyncGRPCDemo;


import es.carlosgarcia.grpc.CalcSrv;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class UnarySyncGrpcDemoApplication {

	public static void main(String[] args) throws IOException, InterruptedException {


			Server server = ServerBuilder.forPort(8080).addService(new CalcSrv()).build();

			System.out.println("Starting server...");
			server.start();
			System.out.println("Server started!");
			server.awaitTermination();



		// SpringApplication.run(UnarySyncGrpcDemoApplication.class, args);
	}

}
