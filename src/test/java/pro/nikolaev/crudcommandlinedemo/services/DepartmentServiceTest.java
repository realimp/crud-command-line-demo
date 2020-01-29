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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DepartmentService.class)
public class DepartmentServiceTest {

    @Autowired
    private DepartmentService departmentService;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private DepartmentRepository departmentRepository;

    @MockBean
    private EmployeeRepository employeeRepository;

    private Department department;
    private Employee employee;

    @Before
    public void setUp(){
        Department department = new Department();
        department.setName("some department");
        this.department = department;

        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Иванов Иван Иванович");
        this.employee = employee;
    }

    @Test
    public void createDepartmentFail1Test() throws Exception {
        Assert.assertEquals("Неверный ввод данных! Повторите попытку.",
                departmentService.createDepartment(null));
    }

    @Test
    public void createDepartmentFail2Test() throws Exception {
        Assert.assertEquals("Неверный ввод данных! Повторите попытку.",
                departmentService.createDepartment(new String[1]));
    }

    @Test
    public void createDepartmentSuccess1Test() throws Exception {
        when(employeeRepository.findById(1)).thenReturn(java.util.Optional.ofNullable(employee));
        when(departmentRepository.saveAndFlush(any(Department.class))).thenReturn(department);
        Assert.assertEquals("Отдел some department успешно создан.\nРуководитель отдела - Иванов Иван Иванович",
                departmentService.createDepartment(new String[] {"some department", "1"}));
    }

    @Test
    public void createDepartmentSuccess2Test() throws Exception {
        when(employeeRepository.saveAndFlush(any(Employee.class))).thenReturn(employee);
        when(departmentRepository.saveAndFlush(any(Department.class))).thenReturn(department);
        Assert.assertEquals("Отдел some department успешно создан.\nРуководитель отдела - Иванов Иван Иванович",
                departmentService.createDepartment(new String[] {"some department", "Иванов Иван Иванович", "11-11-2011", "должность"}));
    }

    @Test
    public void renameDepartmentFail1Test(){
        Assert.assertEquals("Неверный ввод данных! Повторите попытку.",
                departmentService.renameDepartment("department"));
    }

    @Test
    public void renameDepartmentFail2Test(){
        Assert.assertEquals("Неверный ввод данных! Повторите попытку.",
                departmentService.renameDepartment("de:part:ment"));
    }

    @Test
    public void renameDepartmentFail3Test(){
        Assert.assertEquals("Неверный ввод данных! Повторите попытку.",
                departmentService.renameDepartment("department:new department"));
    }

    @Test
    public void renameDepartmentSuccessTest(){
        when(departmentRepository.findByName("some department")).thenReturn(java.util.Optional.ofNullable(department));
        Assert.assertEquals("Отдел some department успешно переименован в new department",
                departmentService.renameDepartment("some department:new department"));
    }

    @Test
    public void deleteDepartmentTest() {
        when(departmentRepository.findByName("some department")).thenReturn(java.util.Optional.ofNullable(department));
        Assert.assertEquals("Отдел some department удален. Также удалено 0 сотрудников!",
                departmentService.deleteDepartment("some department"));
    }
}
