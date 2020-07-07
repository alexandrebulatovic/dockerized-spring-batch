package com.example.batch.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.batch.model.Data;

@Repository
public interface DataRepository extends JpaRepository<Data,Integer> {
}
