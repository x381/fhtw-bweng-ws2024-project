package at.fhtw.bweng_ws24.repository;

import at.fhtw.bweng_ws24.model.Cart;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CartRepository extends CrudRepository<Cart, UUID> {
    List<Cart> findAll();
}