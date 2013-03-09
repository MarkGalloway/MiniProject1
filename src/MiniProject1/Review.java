package MiniProject1;

import java.sql.Date;

/*
 * Comment me!!!!!!!!
 */
public class Review {

    private Integer rno;
    private Integer rating;
    private String text;
    private String reviewer;
    private String reviewee;
    private Date rdate;
    
    public Review(Integer rno, Integer rating, String text, String reviewer, String reviewee, Date rdate) {
        super();
        this.rno = rno;
        this.rating = rating;
        this.text = text;
        this.reviewer = reviewer;
        this.reviewee = reviewee;
        this.setRdate(rdate);
    }

    public Integer getRno() {
        return rno;
    }

    public void setRno(Integer rno) {
        this.rno = rno;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getReviewer() {
        return reviewer;
    }

    public void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    public String getReviewee() {
        return reviewee;
    }

    public void setReviewee(String reviewee) {
        this.reviewee = reviewee;
    }

    public Date getRdate() {
        return rdate;
    }

    public void setRdate(Date rdate) {
        this.rdate = rdate;
    }
}
