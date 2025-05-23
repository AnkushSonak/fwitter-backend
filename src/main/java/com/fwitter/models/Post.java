package com.fwitter.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="posts")
public class Post implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="post_id")
	private Integer postId;
	
	@Column(length =256, nullable=false)
	private String content;
	
	@Column(name="posted_date")
	private Date postDate;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private ApplicationUser author;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="post_likes_junction",
			joinColumns = {@JoinColumn(name="post_id")},
			inverseJoinColumns= {@JoinColumn(name="user_id")}
			)
	private Set<ApplicationUser> likes;
	
	@OneToMany
	private List<Image> images;
	
	//TODO: Figure out video uploads
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="post_reply_junction",
			joinColumns = {@JoinColumn(name="post_id")},
			inverseJoinColumns = {@JoinColumn(name="reply_id")}
			)
	@JsonIgnore
	private Set<Post> replies;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="post_repost_junction",
			joinColumns= {@JoinColumn(name="post_id")},
			inverseJoinColumns = {@JoinColumn(name="user_id")}
			)
	private Set<ApplicationUser> reposts;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="post_bookmark_junction",
			joinColumns = {@JoinColumn(name="post_id")},
			inverseJoinColumns = {@JoinColumn(name="user_id")}
			)
	private Set<ApplicationUser> bookmarks;
	
	@ManyToMany(fetch=FetchType.EAGER)
	@JoinTable(
			name="post_view_junction",
			joinColumns = {@JoinColumn(name="post_id")},
			inverseJoinColumns = {@JoinColumn(name="user_id")}
			)
	private Set<ApplicationUser> views;
	
	private Boolean scheduled;
	
	@Column(name="scheduled_date", nullable=true)
	private Date scheduleDate;
	
	@Enumerated(EnumType.ORDINAL)
	private Audience audience;
	
	@Enumerated(EnumType.ORDINAL)
	@Column(name="reply_restriction")
	private ReplyRestriction replyRestriction;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="poll_id", referencedColumnName = "poll_id")
	private Poll poll;

	public Post() {
		super();
		this.likes = new HashSet<>();
		this.images = new ArrayList<>();
		this.replies = new HashSet<>();
		this.reposts = new HashSet<>();
		this.bookmarks = new HashSet<>();
		this.views = new HashSet<>();
	}

	public Post(Integer postId, String content, Date postDate, ApplicationUser author, Set<ApplicationUser> likes,
			List<Image> images, Set<Post> replies, Set<ApplicationUser> reposts, Set<ApplicationUser> bookmarks,
			Set<ApplicationUser> views, Boolean scheduled, Date scheduleDate, Audience audience,
			ReplyRestriction replyRestriction, Poll poll) {
		super();
		this.postId = postId;
		this.content = content;
		this.postDate = postDate;
		this.author = author;
		this.likes = likes;
		this.images = images;
		this.replies = replies;
		this.reposts = reposts;
		this.bookmarks = bookmarks;
		this.views = views;
		this.scheduled = scheduled;
		this.scheduleDate = scheduleDate;
		this.audience = audience;
		this.replyRestriction = replyRestriction;
		this.poll = poll;
	}

	public Integer getPostId() {
		return postId;
	}

	public void setPostId(Integer postId) {
		this.postId = postId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public ApplicationUser getAuthor() {
		return author;
	}

	public void setAuthor(ApplicationUser author) {
		this.author = author;
	}

	public Set<ApplicationUser> getLikes() {
		return likes;
	}

	public void setLikes(Set<ApplicationUser> likes) {
		this.likes = likes;
	}

	public List<Image> getImages() {
		return images;
	}

	public void setImages(List<Image> images) {
		this.images = images;
	}

	public Set<Post> getReplies() {
		return replies;
	}

	public void setReplies(Set<Post> replies) {
		this.replies = replies;
	}

	public Set<ApplicationUser> getReposts() {
		return reposts;
	}

	public void setReposts(Set<ApplicationUser> reposts) {
		this.reposts = reposts;
	}

	public Set<ApplicationUser> getBookmarks() {
		return bookmarks;
	}

	public void setBookmarks(Set<ApplicationUser> bookmarks) {
		this.bookmarks = bookmarks;
	}

	public Set<ApplicationUser> getViews() {
		return views;
	}

	public void setViews(Set<ApplicationUser> views) {
		this.views = views;
	}

	public Boolean getScheduled() {
		return scheduled;
	}

	public void setScheduled(Boolean scheduled) {
		this.scheduled = scheduled;
	}

	public Date getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(Date scheduleDate) {
		this.scheduleDate = scheduleDate;
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

	public Poll getPoll() {
		return poll;
	}

	public void setPoll(Poll poll) {
		this.poll = poll;
	}

	@Override
	public String toString() {
		return "Post [postId=" + postId + ", content=" + content + ", postDate=" + postDate + ", author=" + author
				+ ", likes=" + likes + ", images=" + images + ", replies=" + replies + ", reposts=" + reposts
				+ ", bookmarks=" + bookmarks + ", views=" + views + ", scheduled=" + scheduled + ", scheduleDate="
				+ scheduleDate + ", audience=" + audience + ", replyRestriction=" + replyRestriction + ", poll=" + poll
				+ "]";
	}

}
