package pro.nikolaev.crudcommandlinedemo.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.nikolaev.crudcommandlinedemo.entities.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
    Page<Employee> findByName(String name, Pageable pageable);

    List<Employee> findByDepartmentId(Integer id);
}
