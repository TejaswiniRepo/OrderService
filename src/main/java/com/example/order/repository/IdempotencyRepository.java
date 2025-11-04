package com.example.order.repository;

import com.example.order.domain.IdempotencyKey;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IdempotencyRepository extends MongoRepository<IdempotencyKey, String> { }
