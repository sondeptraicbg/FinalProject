package com.project.swp.repository;

import com.project.swp.entity.Restaurant;
import com.project.swp.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface StaffRepo extends JpaRepository<Staff, Integer> {

    // CRUD // ===============================================================================
    Staff findOneByEmpId(int id);

    Staff findStaffByEmpId(int id);

    Staff findByUserNameAndPasswordAndRole_RoleName(String username, String password, String roleName);

    Staff findFirstByUserNameAndPassword(String username, String password);

    Staff deleteByEmpId(int id);

    // Manager // ===============================================================================

    List<Staff> findByRole_RoleId_Restaurant_ResID(int redID);

}
