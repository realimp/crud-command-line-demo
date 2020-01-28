package pro.nikolaev.crudcommandlinedemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.nikolaev.crudcommandlinedemo.entities.Department;
import pro.nikolaev.crudcommandlinedemo.entities.Employee;
import pro.nikolaev.crudcommandlinedemo.repositories.DepartmentRepository;
import pro.nikolaev.crudcommandlinedemo.repositories.EmployeeRepository;

import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Pattern;

@Service
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    private Pattern numericPattern = Pattern.compile("\\d+");

    public String createDepartment(String name, Scanner scanner) {
        Department department = new Department();
        department.setName(name);
        Department savedDepartment = departmentRepository.saveAndFlush(department);
        Employee departmentHead;
        System.out.println("Введите id руководителя или HIRE, чтобы создать нового сотрудника");
        while (true) {
            String userInput = scanner.nextLine();
            if (userInput == null) {
                return "Invalid input!";
            }
            if (userInput.trim().equalsIgnoreCase("HIRE")) {
                departmentHead = employeeService.hireEmployee(name, scanner);
            } else if (numericPattern.matcher(userInput.trim()).matches()) {
                Optional<Employee> employee = employeeRepository.findById(Integer.valueOf(userInput.trim()));
                if (employee != null) {
                    departmentHead = employee.get();
                } else {
                    return "Invalid input!";
                }
            } else {
                return "Invalid input";
            }

            savedDepartment.setDepartmentHead(departmentHead);
            departmentRepository.saveAndFlush(savedDepartment);

            return "Отдел " + name + " успешно создан.\nРуководитель отдела - " + departmentHead.getName();
        }
    }

    public String renameDepartment(String args) {
        if (!args.contains(":")) {
            return "Invalid input!";
        }
        String[] names = args.split(":");
        if (names.length != 2) {
            return "Invalid input!";
        }
        Optional<Department> optionalDepartment = departmentRepository.findByName(names[0]);
        if (optionalDepartment != null) {
            Department department = optionalDepartment.get();
            department.setName(names[1]);
            departmentRepository.saveAndFlush(department);
        }
        return "Отдел " + names[0] + " успешно переименован в " + names[1];
    }

    public String deleteDepartment(String name, Scanner scanner) {
        Optional<Department> departmentToDelete = departmentRepository.findByName(name.trim());
        if (departmentToDelete != null) {
            System.out.println("При удалении отдела все сотрудники этого отдела будут уволены!\n" +
                    "Вы уверены что хотите удалить отдел " + name + "? (Yes/No");
            String userInput = scanner.nextLine();
            if (userInput.trim().equalsIgnoreCase("Y") || userInput.trim().equalsIgnoreCase("YES")) {
                int employeesDeleted = departmentToDelete.get().getEmployees().size();
                departmentRepository.delete(departmentToDelete.get());
                return "Отдел " + name.trim() + "удален. Также удалено " + employeesDeleted + " сотрудников!";
            } else {
                return "Отдел " + name.trim() + " не был удален";
            }
        }
        return "Invalid input!";
    }
}
