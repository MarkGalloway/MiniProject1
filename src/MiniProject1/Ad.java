package MiniProject1;

import java.sql.Date;

/*
 * COMMENT ME!
 */
public class Ad {
    
    private String aid; //Maybe remove, not used (yet)
    private String atype;
    private String title;
    private Integer price;
    private String descr;
    private String location;
    private Date pdate;
    private String cat;
    private String poster;
    private double avgRating;
    
    public Ad(String aid, String atype, String title, Integer price, String descr, 
              String location, Date date, String cat, String poster, double avgRating) {
        super();
        this.setAid(aid);
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
    
    /*
     * Returns a string containing a printable version of the Ad Type, Title, Price, and Posting Date of an Ad.
     */
    public String toStringListing() {
        return "Ad Type: " + this.getAtype() + ", Title: " + this.getTitle() + ", Price: " + this.getPrice() + ", Posting Date: " + this.getPdate();
    }
    
    /*
     * Returns a string containing a printable version of the Description, Location, Ad Category, Poster, 
     * and Posters Avg Rating(to one decimal place) of an Ad
     */
    public String toStringDetails() {
        return "Description: " + this.getDescr() + ", Location: " + this.getLocation() + ", Ad Category: " + this.getCat() + 
               ", Poster: " + this.getPoster() + ", Posters Average Rating: " + (((double)((int)(this.getAvgRating() * 10))) / 10);
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

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }
}
