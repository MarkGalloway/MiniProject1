package MiniProject1;

import java.sql.Date;

/*
 * COMMENT ME!
 */
public class Ad {
    
    private String aid = null;
    private String atype;
    private String title;
    private Integer price;
    private String descr = null;
    private String location = null;
    private Date pdate;
    private String cat = null;
    private String poster = null;
    private Double avgRating = null;
    private Integer daysLeft = null;
    
    /*
     * Constructor for new Ad
     */
    public Ad(String atype, String title, Integer price, String descr, String location, String cat) {
        super();
        this.setAtype(atype);
        this.setTitle(title);
        this.setPrice(price);
        this.setDescr(descr);
        this.setLocation(location);
        this.setCat(cat);
    }
    
    /*
     * Constructor for own listed Ad
     */
    public Ad(String aid, String atype, String title, Integer price, Date date, Integer daysLeft) {
        super();
        this.setAid(aid);
        this.setAtype(atype);
        this.setTitle(title);
        this.setPrice(price);
        this.setPdate(date);
        this.setDaysLeft(daysLeft);
    }
    
    /*
     * Constructor for a keyword searched ad
     */
    public Ad(String atype, String title, Integer price, String descr, 
              String location, Date date, String cat, String poster, Double avgRating) {
        super();
        this.setAtype(atype);
        this.setTitle(title);
        this.setPrice(price);
        this.setDescr(descr);
        this.setLocation(location);
        this.setPdate(date);
        this.setCat(cat);
        this.setPoster(poster);
        this.setAvgRating(avgRating);
    }
    
    public String toStringListOwnAds() {
        String s = "Ad Type: " + this.getAtype() + ", Title: " + this.getTitle() + ", Price: " + this.getPrice() + ", Posting Date: " + this.getPdate();
        if(this.getDaysLeft() != 0) {
            s = s + ", Days Left Until Promotion Ends: " + this.getDaysLeft();
        }
        return s;
    }
    
    /*
     * Returns a string containing a printable version of the Ad Type, Title, Price, and Posting Date of an Ad.
     */
    public String toStringKeywordSearch() {
        return "Ad Type: " + this.getAtype() + ", Title: " + this.getTitle() + ", Price: " + this.getPrice() + ", Posting Date: " + this.getPdate();
    }
    
    /*
     * Returns a string containing a printable version of the Description, Location, Ad Category, Poster, 
     * and Posters Avg Rating(to one decimal place) of an Ad
     * 
     * If the user has no ratings, will print none instead of 0 (since 0 is misleading)
     */
    public String toStringKeywordSearchAdvanced() {
        
        String s = (this.getAvgRating().intValue() == 0)? "none": String.valueOf(((double)((int)(this.getAvgRating() * 10))) / 10);
        
        return "Description: " + this.getDescr() + ", Location: " + this.getLocation() + ", Ad Category: " + this.getCat() + 
               ", Poster: " + this.getPoster() + ", Posters Average Rating: " + s;
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getAtype() {
        return atype;
    }

    public void setAtype(String atype) {
        this.atype = atype;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getPdate() {
        return pdate;
    }

    public void setPdate(Date date) {
        this.pdate = date;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Integer getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(Integer daysLeft) {
        this.daysLeft = daysLeft;
    }
}
