package ozoriani.empleomadrynbackend.home.service;

import java.util.List;
import java.util.UUID;

public interface AdminService {

    List<?> getAllFromEntity(String entityName);

    Object getByIdFromEntity(String entityName, UUID id);

    Object updateEntity(String entityName, UUID id, Object updatedEntity);

    void deleteEntity(String entityName, UUID id);

    void enableJobOffer(UUID id);
}
