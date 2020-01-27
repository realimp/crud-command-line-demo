package pro.nikolaev.crudcommandlinedemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class CrudCommandLineDemoApplication implements CommandLineRunner {

	private static final String[] commands = new String[] {"CREATE", "READ", "UPDATE", "DELETE"};

	public static void main(String[] args) {
		SpringApplication.run(CrudCommandLineDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("Description");

		Scanner scanner = new Scanner(System.in);
		while (true) {
			// ToDo: do something useful here
		}
	}
}
