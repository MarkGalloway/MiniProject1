package MiniProject1;

import java.sql.Date;

/*
 * Comment me!!!!!!!!
 */
public class Review {

    private Integer rno; //remove?
    private Integer rating;
    private String text;
    private String reviewer; //remove?
    private String reviewee; // remove?
    private Date rdate;
    
    public Review(Integer rno, Integer rating, String text, String reviewer, String reviewee, Date rdate) {
        super();
        this.setRno(rno);
        this.setRating(rating);
        this.setText(text);
        this.setReviewer(reviewer);
        this.setReviewee(reviewee);
        this.setRdate(rdate);
    }

    public String toStringListing() {
        return "Review Date: " + this.getRdate() +", Rating: " + this.getRating() +", Review Text(40 chars): " + Utils.stringChop(this.getText(), 40);
    }
    
    public String toStringFullText() {
        return "Full Review Text: " + this.getText();
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
