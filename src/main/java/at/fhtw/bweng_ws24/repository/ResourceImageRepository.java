package at.fhtw.bweng_ws24.repository;

import at.fhtw.bweng_ws24.model.ResourceImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResourceImageRepository extends CrudRepository<ResourceImage, UUID> {
}
