package web;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

@ServletComponentScan
@SpringBootApplication
@ComponentScan("api")
public class TestMqApplication {
	public static void main(String[] args) {
		new SpringApplicationBuilder(TestMqApplication.class).run(args);
	}
}
