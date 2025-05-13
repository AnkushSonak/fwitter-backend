package com.fwitter.services;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Poll;
import com.fwitter.models.PollChoice;
import com.fwitter.repositories.PollChoiceRepository;
import com.fwitter.repositories.PollRepository;

@Service
public class PollService {
	
	private final PollRepository pollRepository;
	private final PollChoiceRepository pollChoiceRepository;
	
	public PollService(PollRepository pollRepository, PollChoiceRepository pollChoiceRepository) {
		this.pollRepository = pollRepository;
		this.pollChoiceRepository = pollChoiceRepository;
	}
	
	//Create all the poll options before they are attached to the post
	public PollChoice generateChoice(PollChoice pc) {
		return pollChoiceRepository.save(pc);
	}
	
	//Create a poll before it gets attached to the post 
	public Poll generatePoll(Poll poll) {
		return pollRepository.save(poll);
	}
	
	//Place a vote on a poll
	public Poll voteForChoice(PollChoice choice, ApplicationUser user) {
		//update the choice itself
		Set<ApplicationUser> currentVotes = choice.getVotes();
		currentVotes.add(user);
		choice.setVotes(currentVotes);
		pollChoiceRepository.save(choice);
		
		return pollRepository.findById(choice.getPoll().getPollId()).get();
	}
}
