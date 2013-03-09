package MiniProject1;

import java.sql.Date;

/*
 * COMMENT ME!
 */
public class Ad {
    
    private String aid;
    private String atype;
    private String title;
    private Integer price;
    private String descr;
    private String location;
    private Date date;
    private String cat;
    private String poster;
    
    public Ad(String aid, String atype, String title, Integer price, String descr, 
              String location, Date date, String cat, String poster) {
        super();
        this.setAid(aid);
        this.setAtype(atype);
        this.setTitle(title);
        this.setPrice(price);
        this.setDescr(descr);
        this.setLocation(location);
        this.setDate(date);
        this.setCat(cat);
        this.setPoster(poster);
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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
}
