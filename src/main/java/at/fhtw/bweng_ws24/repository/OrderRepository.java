package at.fhtw.bweng_ws24.repository;

import at.fhtw.bweng_ws24.model.Order;
import at.fhtw.bweng_ws24.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;



import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends CrudRepository<User, UUID> {
    List<Order> findAllOrders();
    //List<Order> findAllOrdersByUser(User user);
}