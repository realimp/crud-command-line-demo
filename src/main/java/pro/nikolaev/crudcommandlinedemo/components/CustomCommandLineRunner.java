package pro.nikolaev.crudcommandlinedemo.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import pro.nikolaev.crudcommandlinedemo.services.DepartmentService;
import pro.nikolaev.crudcommandlinedemo.services.EmployeeService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

@Component
public class CustomCommandLineRunner implements CommandLineRunner {

    private static final String[] COMMANDS = new String[] {"CREATE", "DELETE", "FIND", "FIRE", "HIRE", "HELP", "LIST", "RENAME"};

    private Pattern numericPattern = Pattern.compile("\\d+");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private EmployeeService employeeService;

    public static Scanner scanner = new Scanner(System.in);

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Добро пожаловать!\n" +
                "Ниже представлен список доступных команд");
        printHelp();

        while (true) {
            String userInput = scanner.nextLine().trim();
            if (checkCommandInput(userInput)) {
                String command = userInput.contains(" ") ? userInput.substring(0, userInput.indexOf(" ")) : userInput;
                String commandParameter = userInput.replace(command, "").trim();
                switch (command.toUpperCase()) {
                    case "CREATE":
                        System.out.println(departmentService.createDepartment(getNewDepartmentData(commandParameter.trim(), scanner)));
                        break;
                    case "DELETE":
                        if (confirmDeleteDepartment(commandParameter.trim(), scanner)) {
                            System.out.println(departmentService.deleteDepartment(commandParameter.trim()));
                        }
                        break;
                    case "FIND":
                        employeeService.findEmployee(commandParameter.trim());
                        break;
                    case "FIRE":
                        employeeService.fireEmployee(commandParameter.trim());
                        break;
                    case "HIRE":
                        employeeService.hireEmployee(getNewEmployeeData(commandParameter, scanner));
                        break;
                    case "HELP":
                        printHelp();
                        break;
                    case "LIST":
                        employeeService.listEmployeesByDepartment(commandParameter.trim());
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
        System.out.println("Команда " + command.toUpperCase() + " отсутствует! Введите HELP для просмтра списка команд.");
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

    private String[] getNewEmployeeData(String hireParameters, Scanner scanner) {
        if (departmentService.departmentExists(hireParameters)) {
            String userInput;
            StringBuilder builder = new StringBuilder();
            System.out.println("Введите ФИО сотрудника, например Иванов Иван Иванович");
            while (true) {
                userInput = scanner.nextLine();
                if (userInput.trim().split(" ").length == 3){
                    builder.append(userInput.trim()).append("-@-");
                    break;
                }
                System.out.println("Неверный ввод данных! Повторите попытку.");
            }
            System.out.println("Введите дату рождения в формате ДД-ММ-ГГГГ");
            while (true) {
                userInput = scanner.nextLine();
                try {
                    Date birthDate = dateFormat.parse(userInput.trim());
                    builder.append(userInput.trim()).append("-@-");
                    break;
                } catch (ParseException e) {
                    System.out.println("Неверный ввод данных! Повторите попытку.");
                }
            }
            System.out.println("Укажите должность сотрудника:");
            while (true) {
                userInput = scanner.nextLine();
                if (userInput.trim().length() > 1) {
                    builder.append(userInput.trim());
                    break;
                }
                System.out.println("Неверный ввод данных! Повторите попытку.");
            }
            return builder.toString().split("-@-");
        }
        System.out.println("Отдел не найден! Повторите попытку");
        return null;
    }

    private String[] getNewDepartmentData(String departmentName, Scanner scanner) throws ParseException {
        if (!departmentService.departmentExists(departmentName)) {
            StringBuilder builder = new StringBuilder();
            builder.append(departmentName).append("-@-");
            System.out.println("Введите id руководителя или HIRE, чтобы создать нового сотрудника");
            String userInput;
            while (true) {
                userInput = scanner.nextLine().trim();
                if (userInput != null) {
                    if (userInput.equalsIgnoreCase("HIRE")) {
                        System.out.println("Введите ФИО сотрудника, например Иванов Иван Иванович");
                        while (true) {
                            userInput = scanner.nextLine();
                            if (userInput.trim().split(" ").length == 3){
                                builder.append(userInput.trim()).append("-@-");
                                break;
                            }
                            System.out.println("Неверный ввод данных! Повторите попытку.");
                        }
                        System.out.println("Введите дату рождения в формате ДД-ММ-ГГГГ");
                        while (true) {
                            userInput = scanner.nextLine();
                            try {
                                Date birthDate = dateFormat.parse(userInput.trim());
                                builder.append(userInput.trim()).append("-@-");
                                break;
                            } catch (ParseException e) {
                                System.out.println("Неверный ввод данных! Повторите попытку.");
                            }
                        }
                        System.out.println("Укажите должность сотрудника:");
                        while (true) {
                            userInput = scanner.nextLine();
                            if (userInput.trim().length() > 1) {
                                builder.append(userInput.trim());
                                break;
                            }
                            System.out.println("Неверный ввод данных! Повторите попытку.");
                        }
                        return builder.toString().split("-@-");
                    } else if (numericPattern.matcher(userInput).matches()) {
                        if (employeeService.employeeExists(Integer.valueOf(userInput))) {
                            builder.append(userInput).append("-@-");
                            return builder.toString().split("-@-");
                        }
                    }
                    System.out.println("Неверный ввод данных! Повторите попытку.");
                }
            }
        }
        System.out.println("Этот отдел уже существует!");
        return null;
    }

    private boolean confirmDeleteDepartment(String departmentName, Scanner scanner) {
        if (departmentService.departmentExists(departmentName)) {
            System.out.println("При удалении отдела все сотрудники этого отдела будут уволены!\n" +
                    "Вы уверены что хотите удалить отдел " + departmentName + "? (Yes/No)");
            String userInput = scanner.nextLine();
            if (userInput.trim().equalsIgnoreCase("Y") || userInput.trim().equalsIgnoreCase("YES")) {
                return true;
            }
        }
        return false;
    }
}
