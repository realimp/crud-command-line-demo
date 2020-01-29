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

    public void findEmployee(String name) {
        List<Employee> employees = employeeRepository.findByName(name);
        if (employees.size() > 0) {
            employees.stream().forEach(employee -> System.out.println(EmployeeMapper.getMapping(employee)));
        } else {
            System.out.println("Сотрудников не найдено");
        }
    }

    public void fireEmployee(String id) {
        if (numericPattern.matcher(id).matches()) {
            Optional<Employee> employeeToFire = employeeRepository.findById(Integer.valueOf(id));
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
        Optional<Department> department = departmentRepository.findByName(departmentName);
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

    public boolean employeeExists(int id) {
        Optional<Employee> employee = employeeRepository.findById(id);
        return employee.isPresent();
    }
}
