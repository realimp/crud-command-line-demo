package pro.nikolaev.crudcommandlinedemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pro.nikolaev.crudcommandlinedemo.services.DepartmentService;
import pro.nikolaev.crudcommandlinedemo.services.EmployeeService;

import java.util.Scanner;

@SpringBootApplication
public class CrudCommandLineDemoApplication implements CommandLineRunner {

	private static final String[] COMMANDS = new String[] {"CREATE", "DELETE", "FIND", "FIRE", "HIRE", "HELP", "LIST", "RENAME"};

	@Autowired
	private DepartmentService departmentService;

	@Autowired
	private EmployeeService employeeService;

	public static void main(String[] args) {
		SpringApplication.run(CrudCommandLineDemoApplication.class, args);
	}

	public static Scanner scanner = new Scanner(System.in);

	@Override
	public void run(String... args) throws Exception {

		System.out.println("Добро пожаловать!\n" +
				"Ниже представлен список доступных команд");
		printHelp();

		//Scanner scanner = new Scanner(System.in);
		while (true) {
			String userInput = scanner.nextLine().trim();
			if (checkCommandInput(userInput)) {
				String command = userInput.contains(" ") ? userInput.substring(0, userInput.indexOf(" ")) : userInput;
				String commandParameter = userInput.replace(command, "").trim();
				switch (command.toUpperCase()) {
					case "CREATE":
						System.out.println(departmentService.createDepartment(commandParameter, scanner));
						break;
					case "DELETE":
						System.out.println(departmentService.deleteDepartment(commandParameter, scanner));
						break;
					case "FIND":
						employeeService.findEmployee(commandParameter);
						break;
					case "FIRE":
						employeeService.fireEmployee(commandParameter);
						break;
					case "HIRE":
						employeeService.hireEmployee(commandParameter, scanner);
						break;
					case "HELP":
						printHelp();
						break;
					case "LIST":
						employeeService.listEmployeesByDepartment(commandParameter);
						break;
					case "RENAME":
						System.out.println(departmentService.renameDepartment(commandParameter));
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
				"\tCREATE название отдела - создает новый отдел компании\n" +
				"\tDELETE название отдела - удаляет отдел компании\n" +
				"\tFIND имя сотрудника - поиск сотрудников по имени\n" +
				"\tFIRE id сотрудника - удалить сотрудника\n" +
				"\tHIRE название отдела - нанять сотрудника в отдел\n" +
				"\tHELP - отображает сообщение с описанием команд\n" +
				"\tLIST название отдела - показать список сотрудников отдела\n" +
				"\tRENAME старое название отдела: новое название отдела - переименовать отдел");
	}
}
