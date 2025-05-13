package com.fwitter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fwitter.models.Poll;

@Repository
public interface PollRepository extends JpaRepository<Poll, Integer>{

}
