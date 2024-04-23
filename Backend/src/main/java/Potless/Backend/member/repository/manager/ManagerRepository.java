package Potless.Backend.member.repository.manager;

import Potless.Backend.member.entity.ManagerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<ManagerEntity, Long> {
}
