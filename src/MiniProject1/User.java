package MiniProject1;

/*
 * A data storage class which contains the fields of a
 * User object corresponding to a row in the users table in the database
 */
public class User {
    private String name;
    private String email;
    private Integer numberOfAds;
    private Double avgRating;
    
    /*
     * Constructs a User object 
     */
    public User(String name, String email, Integer numberOfAds, Double avgRating) {
        super();
        this.setName(name);
        this.setEmail(email);
        this.setNumberOfAds(numberOfAds);
        this.setAvgRating(avgRating);
    }

    /*
     * Prints the string representation of a User object
     */
    public String toString() {
        
        String s = (this.getAvgRating().intValue() == 0)? "": ", Users Average Rating: " + String.valueOf(((double)((int)(this.getAvgRating() * 10))) / 10);
        
        return "Email: " + this.getEmail() + ", Name: " + this.getName() + 
                ", Number of Ads: " + this.getNumberOfAds() + s;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getNumberOfAds() {
        return numberOfAds;
    }

    public void setNumberOfAds(Integer numberOfAds) {
        this.numberOfAds = numberOfAds;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }
}
