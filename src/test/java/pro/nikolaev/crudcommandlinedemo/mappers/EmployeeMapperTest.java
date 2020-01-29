package pro.nikolaev.crudcommandlinedemo.mappers;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import pro.nikolaev.crudcommandlinedemo.entities.Department;
import pro.nikolaev.crudcommandlinedemo.entities.Employee;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@SpringBootTest(classes = EmployeeMapper.class)
public class EmployeeMapperTest {

    private Employee employee;
    private SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");

    @Before
    public void setUp() throws ParseException {
        Department department = new Department();
        department.setName("some department");

        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("Иванов Иван Иванович");
        employee.setDepartment(department);
        employee.setPosition("position");
        employee.setBirthDate(format.parse("11.11.1999"));
        employee.setHiredDate(format.parse("11.11.2019"));
        this.employee = employee;
    }

    @Test
    public void getMappingTest() {
        Assert.assertEquals("1 - Иванов Иван Иванович 11.11.1999 - some department - position работает с 11.11.2019",
                EmployeeMapper.getMapping(employee));
    }
}
