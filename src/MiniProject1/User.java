package MiniProject1;

public class User {
    private String name;
    private String email;
    private Integer numberOfAds;
    private Double avgRating;
    
    public User(String name, String email, Integer numberOfAds, Double avgRating) {
        super();
        this.setName(name);
        this.setEmail(email);
        this.setNumberOfAds(numberOfAds);
        this.setAvgRating(avgRating);
    }

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
