<<<<<<<< HEAD:src/main/java/com/team/Application.java
package com.team;
========
package com.team.project;
>>>>>>>> develop:src/main/java/com/team/project/Application.java

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
