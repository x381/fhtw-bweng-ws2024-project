package at.fhtw.bweng_ws24.repository;

import at.fhtw.bweng_ws24.model.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository extends CrudRepository<Product, UUID> {
    List<Product> findAll();
}
