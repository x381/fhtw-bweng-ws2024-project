package at.fhtw.bweng_ws24.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceImage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String externalId;

    private String name;

    private String contentType;
}
