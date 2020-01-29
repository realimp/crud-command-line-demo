package pro.nikolaev.crudcommandlinedemo.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import pro.nikolaev.crudcommandlinedemo.entities.Department;
import pro.nikolaev.crudcommandlinedemo.entities.Employee;
import pro.nikolaev.crudcommandlinedemo.repositories.DepartmentRepository;
import pro.nikolaev.crudcommandlinedemo.repositories.EmployeeRepository;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmployeeService.class)
public class EmployeeServiceTest {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private DepartmentService departmentService;

    @MockBean
    private DepartmentRepository departmentRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    private Department department;
    private Employee employee;
    private List<Employee> employees;

    @Before
    public void setUp(){
        Department department = new Department();
        department.setName("some department");
        this.department = department;

        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Иванов Иван Иванович");
        employee.setDepartment(department);
        employee.setPosition("position");
        employee.setBirthDate(new Date());
        employee.setHiredDate(new Date());
        this.employee = employee;

        this.employees = new ArrayList<>();
        employees.add(employee);

        this.department.setEmployees(employees);
    }

    @Test
    public void hireEmployeeFail1Test() throws Exception {
        Assert.assertNull(employeeService.hireEmployee(null));
    }

    @Test
    public void hireEmployeeFail2Test() throws Exception {
        Assert.assertNull(employeeService.hireEmployee(new String[2]));
    }

    @Test
    public void hireEmployeeSuccessTest() throws Exception {
        when(employeeRepository.saveAndFlush(any(Employee.class))).thenReturn(employee);
        when(departmentRepository.findByName("some department")).thenReturn(java.util.Optional.ofNullable(department));
        Assert.assertEquals(employee, employeeService.hireEmployee(new String[] {"some department", "Иванов Иван Иванович", "11-11-2011", "должность"}));
    }

    @Test
    public void findByNameFailTest() {
        Assert.assertEquals(Collections.EMPTY_LIST, employeeService.findEmployee("Иванов"));
    }

    @Test
    public void findByNameSuccessTest() {
        when(employeeRepository.findByName("Иванов Иван Иванович")).thenReturn(employees);
        Assert.assertEquals(1, employeeService.findEmployee("Иванов Иван Иванович").size());
    }

    @Test
    public void fireEmployeeFail1Test() {
        Assert.assertEquals("Неверный ввод данных! Повторите попытку.",
                employeeService.fireEmployee("string"));
    }

    @Test
    public void fireEmployeeFail2Test() {
        Assert.assertEquals("Сотрудник с таким ID не найден!",
                employeeService.fireEmployee("2"));
    }

    @Test
    public void fireEmployeeSuccessTest() {
        when(employeeRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(employee));
        Assert.assertEquals("Сотрудник уволен.",
                employeeService.fireEmployee("1"));
    }

    @Test
    public void listEmployeesByDepartmentFailTest() {
        Assert.assertNull(employeeService.listEmployeesByDepartment("department"));
    }

    @Test
    public void listEmployeesByDepartmentSuccessTest() {
        when(departmentRepository.findByName("some department")).thenReturn(java.util.Optional.ofNullable(department));
        Assert.assertEquals(1, employeeService.listEmployeesByDepartment("some department").size());
    }
}
