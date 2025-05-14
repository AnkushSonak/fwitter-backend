package com.fwitter.controllers;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fwitter.dto.PollVoteDTO;
import com.fwitter.models.Poll;
import com.fwitter.services.PollService;

@RestController
@RequestMapping("/poll")
public class PollController {

	private final PollService pollService;
	
	public PollController(PollService pollService) {
		this.pollService = pollService;
	}
	
	@PutMapping("/vote")
	public Poll castVote(@RequestBody PollVoteDTO vote) {
		return pollService.voteForChoice(vote.getChoiceId(), vote.getUserId());
	}
}
