package lk.ijse.springpossystem.dao;

import lk.ijse.springpossystem.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDAO extends JpaRepository<OrderEntity,String> {
}