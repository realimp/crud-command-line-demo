package pro.nikolaev.crudcommandlinedemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pro.nikolaev.crudcommandlinedemo.entities.Department;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    Department findByName();
}
