package com.harmonymodeler.api.repositories;

import com.harmony.modeler.core.models.repository.SchemaRepository;
import org.springframework.data.repository.kotlin.CoroutineCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SchemaRepositoryRepository extends CoroutineCrudRepository<SchemaRepository, UUID> {

}
