package com.fwitter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.fwitter.models.PollChoice;

@Repository
public interface PollChoiceRepository extends JpaRepository<PollChoice, Integer>{

}
