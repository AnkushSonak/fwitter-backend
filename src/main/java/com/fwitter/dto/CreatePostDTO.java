package com.fwitter.dto;

import java.util.Date;
import java.util.Set;

import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Audience;
import com.fwitter.models.Post;
import com.fwitter.models.ReplyRestriction;

public class CreatePostDTO {
	
	private String content;
	private ApplicationUser author;
	Set<Post> replies;
	private Boolean scheduled;
	private Date scheduledDate;
	private Audience audience;
	private ReplyRestriction replyRestriction;
	
	public CreatePostDTO() {
		super();
	}

	public CreatePostDTO(String content, ApplicationUser author, Set<Post> replies, Boolean scheduled,
			Date scheduledDate, Audience audience, ReplyRestriction replyRestriction) {
		super();
		this.content = content;
		this.author = author;
		this.replies = replies;
		this.scheduled = scheduled;
		this.scheduledDate = scheduledDate;
		this.audience = audience;
		this.replyRestriction = replyRestriction;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ApplicationUser getAuthor() {
		return author;
	}

	public void setAuthor(ApplicationUser author) {
		this.author = author;
	}

	public Set<Post> getReplies() {
		return replies;
	}

	public void setReplies(Set<Post> replies) {
		this.replies = replies;
	}

	public Boolean getScheduled() {
		return scheduled;
	}

	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}

	public Date getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(Date scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public Audience getAudience() {
		return audience;
	}

	public void setAudience(Audience audience) {
		this.audience = audience;
	}

	public ReplyRestriction getReplyRestriction() {
		return replyRestriction;
	}

	public void setReplyRestriction(ReplyRestriction replyRestriction) {
		this.replyRestriction = replyRestriction;
	}

	@Override
	public String toString() {
		return "CreatePostDTO [content=" + content + ", author=" + author + ", replies=" + replies + ", scheduled="
				+ scheduled + ", scheduledDate=" + scheduledDate + ", audience=" + audience + ", replyRestriction="
				+ replyRestriction + "]";
	}
}
