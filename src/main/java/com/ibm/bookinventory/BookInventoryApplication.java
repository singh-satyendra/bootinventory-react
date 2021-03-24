package com.ibm.bookinventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import io.jaegertracing.Configuration;
import io.jaegertracing.Configuration.ReporterConfiguration;
import io.jaegertracing.Configuration.SamplerConfiguration;
import io.jaegertracing.internal.samplers.ConstSampler;

@SpringBootApplication
public class BookInventoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookInventoryApplication.class, args);
	}
	
	@Bean // Jaegar Tracer bean
	public io.opentracing.Tracer jaegerTracer() {
		SamplerConfiguration samplerConfig = 	SamplerConfiguration.fromEnv().withType(ConstSampler.TYPE).withParam(1);
		ReporterConfiguration reporterConfig = 	ReporterConfiguration.fromEnv().withLogSpans(true);
		return new Configuration("book-inventory")
			.withSampler(samplerConfig).withReporter(reporterConfig).getTracer();
	}
}
