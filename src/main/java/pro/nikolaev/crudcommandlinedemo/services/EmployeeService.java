package pro.nikolaev.crudcommandlinedemo.services;

import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;
import java.util.Optional;
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

    public Employee hireEmployee(String[] args) throws ParseException {
        if (args != null && args.length == 4) {
            Employee employee = new Employee();
            employee.setDepartment(departmentRepository.findByName(args[0]).get());
            employee.setName(args[1]);
            employee.setBirthDate(dateFormat.parse(args[2]));
            employee.setHiredDate(new Date());
            employee.setPosition(args[3]);

            Employee savedEmployee = employeeRepository.saveAndFlush(employee);
            System.out.println("Новый сотрудник " + savedEmployee.getName() + " добавлен в отдел " + savedEmployee.getDepartment().getName());
            return savedEmployee;
        }
        System.out.println("Введены не верные данные! Повторите попытку");
        return null;
    }

    public List<Employee> findEmployee(String name) {
        List<Employee> employees = employeeRepository.findByName(name);
        if (employees.size() > 0) {
            employees.stream().forEach(employee -> System.out.println(EmployeeMapper.getMapping(employee)));
        } else {
            System.out.println("Сотрудников не найдено");
        }
        return employees;
    }

    public String fireEmployee(String id) {
        if (numericPattern.matcher(id).matches()) {
            Optional<Employee> employeeToFire = employeeRepository.findById(Integer.valueOf(id));
            if (employeeToFire.isPresent()) {
                employeeRepository.delete(employeeToFire.get());
                return "Сотрудник уволен.";
            }
            return "Сотрудник с таким ID не найден!";
        }
        return "Неверный ввод данных! Повторите попытку.";
    }

    public List<Employee> listEmployeesByDepartment(String departmentName) {
        Optional<Department> department = departmentRepository.findByName(departmentName);
        if (department.isPresent()) {
            List<Employee> employees = department.get().getEmployees();
            if (employees.size() > 0) {
                employees.stream().forEach(employee -> System.out.println(EmployeeMapper.getMapping(employee)));
                return employees;
            } else {
                System.out.println("В этом отделе нет сотрудников!");
            }
        } else {
            System.out.println("Такой отдел не найден!");
        }
        return null;
    }

    public boolean employeeExists(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.isPresent();
    }
}
