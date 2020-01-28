package pro.nikolaev.crudcommandlinedemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.nikolaev.crudcommandlinedemo.entities.Department;
import pro.nikolaev.crudcommandlinedemo.entities.Employee;
import pro.nikolaev.crudcommandlinedemo.mappers.EmployeeMapper;
import pro.nikolaev.crudcommandlinedemo.repositories.DepartmentRepository;
import pro.nikolaev.crudcommandlinedemo.repositories.EmployeeRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    private Pattern numericPattern = Pattern.compile("\\d+");
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public Employee hireEmployee(String departmentName, Scanner scanner) {
        Employee employee = new Employee();
        String userInput;
        Optional<Department> department = departmentRepository.findByName(departmentName.trim());
        if (department != null) {
            employee.setDepartment(department.get());
        } else {
            System.out.println("Отдел не найден! Введите название отдела:");
            while (true) {
                userInput = scanner.nextLine();
                department = departmentRepository.findByName(userInput.trim());
                if (department != null) {
                    employee.setDepartment(department.get());
                    break;
                }
                System.out.println("Неверный ввод данных! Повторите попытку.");
            }
        }
        System.out.println("Введите ФИО сотрудника, например Иванов Иван Иванович");
        while (true) {
            userInput = scanner.nextLine();
            if (userInput.trim().split(" ").length == 3){
                employee.setName(userInput.trim());
                break;
            }
            System.out.println("Неверный ввод данных! Повторите попытку.");
        }
        System.out.println("Введите дату рождения в формате ДД-ММ-ГГГГ");
        while (true) {
            userInput = scanner.nextLine();
            try {
                Date birthDate = dateFormat.parse(userInput);
                employee.setBirthDate(birthDate);
                break;
            } catch (ParseException e) {
                e.printStackTrace();
                System.out.println("Неверный ввод данных! Повторите попытку.");
            }
        }
        employee.setHiredDate(new Date());
        System.out.println("Укажите должность сотрудника:");
        while (true) {
            userInput = scanner.nextLine();
            if (userInput.length() > 1) {
                employee.setPosition(userInput);
                break;
            }
            System.out.println("Неверный ввод данных! Повторите попытку.");
        }
        Employee savedEmployee = employeeRepository.saveAndFlush(employee);
        System.out.println("Новый сотрудник " + savedEmployee.getName() + " добавлен в отдел " + savedEmployee.getDepartment().getName());
        return savedEmployee;
    }

    public void findEmployee(String name, Scanner scanner) {
        int itemsPerPage = 3;
        int offset = 0;
        Page<Employee> employees = employeeRepository.findByName(name, PageRequest.of(offset, itemsPerPage));
        if (employees.hasContent()) {
            long totalResults = employees.getTotalElements();
            System.out.println("Найдено " + totalResults + " результатов");

            if (totalResults >= itemsPerPage) {
                for (; offset < employees.getTotalPages(); offset++) {
                    System.out.println("Отображаются результаты с " + (offset + 1) + " по " + (itemsPerPage * offset + employees.getNumberOfElements()));
                    employees.get().forEach(employee -> EmployeeMapper.getMapping(employee));
                    System.out.println("Загузить следующаю страницу результатов? (Y/N)");
                    String userInput = scanner.nextLine();
                    if (userInput.trim().equalsIgnoreCase("Y") || userInput.trim().equalsIgnoreCase("YES")) {
                        continue;
                    }
                    break;
                }
            } else {
                employees.get().forEach(employee -> System.out.println(EmployeeMapper.getMapping(employee)));
            }
        } else {
            System.out.println("Сотрудников не найдено");
        }
    }

    public void fireEmployee(String id) {
        if (numericPattern.matcher(id.trim()).matches()) {
            Optional<Employee> employeeToFire = employeeRepository.findById(Integer.valueOf(id.trim()));
            if (employeeToFire.isPresent()) {
                employeeRepository.delete(employeeToFire.get());
                System.out.println("Сотрудник уволен.");
            } else {
                System.out.println("Сотрудник с таким ID не найден!");
            }
        } else {
            System.out.println("Неверный ввод данных! Повторите попытку.");
        }
    }

    public void listEmployeesByDepartment(String departmentName) {
        Optional<Department> department = departmentRepository.findByName(departmentName.trim());
        if (department.isPresent()) {
            if (department.get().getEmployees().size() > 0) {
                employeeRepository.findByDepartmentId(department.get().getId()).stream()
                        .forEach(employee -> System.out.println(EmployeeMapper.getMapping(employee)));
            } else {
                System.out.println("В этом отделе нет сотрудников!");
            }
        } else {
            System.out.println("Такой отдел не найден!");
        }
    }
}
