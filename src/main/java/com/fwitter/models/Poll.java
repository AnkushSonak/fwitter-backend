package com.fwitter.models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "polls")
public class Poll {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "poll_id")
	private Integer pollId;
	
	@Column(name = "end_date")
	private Date endDate;
	
	@OneToMany(mappedBy = "poll")
	private List<PollChoice> choices;

	public Poll() {
		super();
	}

	public Poll(Integer pollId, Date endDate, List<PollChoice> choices) {
		super();
		this.pollId = pollId;
		this.endDate = endDate;
		this.choices = choices;
	}

	public Integer getPollId() {
		return pollId;
	}

	public void setPollId(Integer pollId) {
		this.pollId = pollId;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<PollChoice> getChoices() {
		return choices;
	}

	public void setChoices(List<PollChoice> choices) {
		this.choices = choices;
	}

	@Override
	public String toString() {
		return "Poll [pollId=" + pollId + ", endDate=" + endDate + ", choices=" + choices + "]";
	}
	
}
