package com.fwitter.models;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="poll_choices")
public class PollChoice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "poll_choice_id")
	private Integer pollChoiceId; 
	
	@ManyToOne
	@JoinColumn(name="poll_id")
	private Poll poll;
	
	@Column(name = "poll_choice_text")
	private String pollText;
	
	@OneToMany
	private Set<ApplicationUser> votes;

	public PollChoice() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PollChoice(Integer pollChoiceId, Poll poll, String pollText, Set<ApplicationUser> votes) {
		super();
		this.pollChoiceId = pollChoiceId;
		this.poll = poll;
		this.pollText = pollText;
		this.votes = votes;
	}

	public Integer getPollChoiceId() {
		return pollChoiceId;
	}

	public void setPollChoiceId(Integer pollChoiceId) {
		this.pollChoiceId = pollChoiceId;
	}

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	public String getPollText() {
		return pollText;
	}

	public void setPollText(String pollText) {
		this.pollText = pollText;
	}

	public Set<ApplicationUser> getVotes() {
		return votes;
	}

	public void setVotes(Set<ApplicationUser> votes) {
		this.votes = votes;
	}

	@Override
	public String toString() {
		return "PollChoice [pollChoiceId=" + pollChoiceId + ", poll=" + poll + ", pollText=" + pollText + ", votes="
				+ votes + "]";
	}
	
}
