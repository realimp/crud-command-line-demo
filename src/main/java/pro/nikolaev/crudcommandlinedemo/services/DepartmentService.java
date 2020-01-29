package pro.nikolaev.crudcommandlinedemo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pro.nikolaev.crudcommandlinedemo.entities.Department;
import pro.nikolaev.crudcommandlinedemo.entities.Employee;
import pro.nikolaev.crudcommandlinedemo.repositories.DepartmentRepository;
import pro.nikolaev.crudcommandlinedemo.repositories.EmployeeRepository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public String createDepartment(String[] args) throws ParseException {
        if (args != null && args.length == 2) {
            Department department = new Department();
            department.setName(args[0]);
            Employee departmentHead = employeeRepository.findById(Integer.valueOf(args[1])).get();
            department.setDepartmentHead(departmentHead);
            departmentRepository.saveAndFlush(department);
            return "Отдел " + args[0] + " успешно создан.\nРуководитель отдела - " + departmentHead.getName();
        }
        if (args != null && args.length == 4) {
            Department department = new Department();
            department.setName(args[0]);
            Department savedDepartment = departmentRepository.saveAndFlush(department);
            Employee employee = new Employee();
            employee.setName(args[1]);
            employee.setBirthDate(dateFormat.parse(args[2]));
            employee.setHiredDate(new Date());
            employee.setDepartment(savedDepartment);
            employee.setPosition(args[3]);
            savedDepartment.setDepartmentHead(employeeRepository.saveAndFlush(employee));
            departmentRepository.saveAndFlush(savedDepartment);
            return "Отдел " + args[0] + " успешно создан.\nРуководитель отдела - " + args[1];
        }
        return "Неверный ввод данных! Повторите попытку.";
    }

    public String renameDepartment(String args) {
        if (!args.contains(":")) {
            return "Неверный ввод данных! Повторите попытку.";
        }
        String[] names = args.split(":");
        if (names.length != 2) {
            return "Неверный ввод данных! Повторите попытку.";
        }
        Optional<Department> optionalDepartment = departmentRepository.findByName(names[0].trim());
        if (optionalDepartment.isPresent()) {
            Department department = optionalDepartment.get();
            department.setName(names[1].trim());
            departmentRepository.saveAndFlush(department);
            return "Отдел " + names[0].trim() + " успешно переименован в " + names[1].trim();
        }
        return "Неверный ввод данных! Повторите попытку.";
    }

    public String deleteDepartment(String name) {
        Department departmentToDelete = departmentRepository.findByName(name).get();
        int employeesDeleted = departmentToDelete.getEmployees().size();
        departmentRepository.delete(departmentToDelete);
        return "Отдел " + name.trim() + " удален. Также удалено " + employeesDeleted + " сотрудников!";
    }

    public boolean departmentExists(String name) {
        Optional<Department> departmentNameToCheck = departmentRepository.findByName(name.trim());
        return  departmentNameToCheck.isPresent();
    }
}
