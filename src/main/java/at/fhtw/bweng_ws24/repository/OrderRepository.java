package at.fhtw.bweng_ws24.repository;

import at.fhtw.bweng_ws24.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<Order, UUID> {
    List<Order> findAll();
    List<Order> findByCustomerName(String customerName);
}