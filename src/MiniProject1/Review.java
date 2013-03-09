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
        return "Review Date: " + this.getRdate() +", Rating: " + this.getRating() +", Review Text(40 chars): " + DBConnector.stringChop(this.getText(), 40);
    }
    
    public String toStringFullText() {
        return "Full Review Text: " + this.getText();
    }
    
    private Integer getRno() {
        return rno;
    }

    private void setRno(Integer rno) {
        this.rno = rno;
    }

    private Integer getRating() {
        return rating;
    }

    private void setRating(Integer rating) {
        this.rating = rating;
    }

    private String getText() {
        return text;
    }

    private void setText(String text) {
        this.text = text;
    }

    private String getReviewer() {
        return reviewer;
    }

    private void setReviewer(String reviewer) {
        this.reviewer = reviewer;
    }

    private String getReviewee() {
        return reviewee;
    }

    private void setReviewee(String reviewee) {
        this.reviewee = reviewee;
    }

    private Date getRdate() {
        return rdate;
    }

    private void setRdate(Date rdate) {
        this.rdate = rdate;
    }
}
