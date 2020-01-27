package pro.nikolaev.crudcommandlinedemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class CrudCommandLineDemoApplication implements CommandLineRunner {

	private static final String[] COMMANDS = new String[] {"CREATE", "DELETE", "FIND", "FIRE", "LIST", "RENAME"};

	public static void main(String[] args) {
		SpringApplication.run(CrudCommandLineDemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		System.out.println("Добро пожаловать!\n " +
				"Ниже представлен список доступных команд");
		printHelp();

		Scanner scanner = new Scanner(System.in);
		while (true) {
			String userInput = scanner.nextLine().trim();
			if (checkCommandInput(userInput)) {
				String command = userInput.contains(" ") ? userInput.substring(0, userInput.indexOf(" ")) : userInput;
				switch (command.toUpperCase()) {
					case "CREATE":
						// ToDo implement method in DepartmentService
						break;
					case "DELETE":
						// ToDo implement method in DepartmentService
						break;
					case "FIND":
						// ToDo implement method in EmployeeService
						break;
					case "FIRE":
						// ToDo implement method in EmployeeService
						break;
					case "HIRE":
						// ToDo implement method in EmployeeService
						break;
					case "HELP":
						printHelp();
						break;
					case "LIST":
						// ToDo implement method in EmployeeService
						break;
					case "RENAME":
						// ToDo implement method in DepartmentService
						break;
				}
			}
		}
	}

	private static boolean checkCommandInput(String command) {
		command = command.contains(" ") ? command.substring(0, command.indexOf(" ")) : command;
		for (int i = 0; i < COMMANDS.length; i++) {
			if (command.equalsIgnoreCase(COMMANDS[i])) {
				return true;
			}
		}
		return false;
	}

	private static void printHelp(){
		System.out.println(
				"CREATE название отдела - создает новый отдел компании\n" +
				"DELETE название отдела - удаляет отдел компании\n" +
				"FIND имя сотрудника - поиск сотрудников по имени\n" +
				"FIRE id сотрудника - удалить сотрудника\n" +
				"HIRE название отдела - нанять сотрудника в отдел\n" +
				"HELP - отображает сообщение с описанием команд\n" +
				"LIST название отдела - показать список сотрудников отдела\n" +
				"RENAME старое название отдела: новое название отдела - переименовать отдел");
	}
}
