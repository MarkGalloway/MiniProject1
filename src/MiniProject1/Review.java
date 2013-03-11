package MiniProject1;

import java.sql.Date;

/*
 * A data storage class which contains the fields of a
 * Review object corresponding to a row in the reviews table in the database
 */
public class Review {

    private Integer rating;
    private String text;
    private String reviewer;
    private String reviewee;
    private Date rdate;
    
    /*
     * Constructor for creating a new review (no date field)
     */
    public Review(Integer rating, String text, String reviewer, String reviewee) {
        super();
        this.setRating(rating);
        this.setText(text);
        this.setReviewer(reviewer);
        this.setReviewee(reviewee);
    }
    
    /*
     * Constructor for a review got off the Database (has a date field)
     */
    public Review(Integer rating, String text, String reviewer, String reviewee, Date rdate) {
        super();
        this.setRating(rating);
        this.setText(text);
        this.setReviewer(reviewer);
        this.setReviewee(reviewee);
        this.setRdate(rdate);
    }

    //String representation with only 40 chars of review text
    public String toStringListing() {
        return "Review Date: " + this.getRdate() +", Rating: " + this.getRating() +", Review Text(40 chars): " + Utils.stringChop(this.getText(), 40);
    }
    
    //String representation of the full review text
    public String toStringFullText() {
        return "Full Review Text: " + this.getText();
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
