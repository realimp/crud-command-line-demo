package pro.nikolaev.crudcommandlinedemo.mappers;

import pro.nikolaev.crudcommandlinedemo.entities.Employee;

import java.text.SimpleDateFormat;

public class EmployeeMapper {
    private EmployeeMapper(){}

    private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static String getMapping(Employee employee) {
        StringBuilder builder = new StringBuilder();
        builder.append(employee.getId())
                .append(" - ")
                .append(employee.getName())
                .append(" ")
                .append(dateFormat.format(employee.getBirthDate()))
                .append(" - ")
                .append(employee.getDepartment().getName())
                .append(" - ")
                .append(employee.getPosition())
                .append(" работает с ")
                .append(dateFormat.format(employee.getHiredDate()));
        return builder.toString();
    }
}
