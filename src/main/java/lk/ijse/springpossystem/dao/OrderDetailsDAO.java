package lk.ijse.springpossystem.dao;

import lk.ijse.springpossystem.entity.OrderDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailsDAO extends JpaRepository<OrderDetailsEntity,Long> {
}