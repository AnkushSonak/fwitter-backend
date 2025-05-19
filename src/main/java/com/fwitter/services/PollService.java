package com.fwitter.services;

import java.util.ArrayList;
import java.util.List;
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
	private final UserService userService;
	
	public PollService(PollRepository pollRepository, PollChoiceRepository pollChoiceRepository, UserService userService) {
		this.pollRepository = pollRepository;
		this.pollChoiceRepository = pollChoiceRepository;
		this.userService = userService;
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
	public Poll voteForChoice(Integer choiceId, Integer userId) {
		ApplicationUser user = userService.getUserById(userId);

		// get the entire poll from choice
		PollChoice pc = pollChoiceRepository.findById(choiceId).orElseThrow();
		Poll poll = pc.getPoll();
		List<ApplicationUser> votes = new ArrayList<ApplicationUser>();
		poll.getChoices().forEach(choice -> {
			choice.getVotes().forEach(voteUser -> {
				votes.add(voteUser);
			});
		});
		if (votes.contains(user))
			return poll;

		Set<ApplicationUser> currentVotes = pc.getVotes();
		currentVotes.add(user);
		pc.setVotes(currentVotes);
		pollChoiceRepository.save(pc);
		System.out.println(pc);

		List<PollChoice> pcList = poll.getChoices();
		pcList.set(poll.getChoices().indexOf(pc), pc);
 		
		return pollRepository.save(poll);
	}
}
