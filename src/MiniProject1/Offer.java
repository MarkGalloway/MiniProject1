package MiniProject1;

public class Offer {

    private String Ono;
    private Integer ndays;
    private Float price;
    
    public Offer(String Ono, Integer ndays, Float price) {
        super();
        this.setOno(Ono);
        this.setNdays(ndays);
        this.setPrice(price);
    }
    
    public String toString() {
        return "Number of Days: " + this.getNdays() + ", Price: "+ this.getPrice();
    }
    
    public String getOno() {
        return Ono;
    }

    public void setOno(String ono) {
        Ono = ono;
    }

    public Integer getNdays() {
        return ndays;
    }

    public void setNdays(Integer ndays) {
        this.ndays = ndays;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }
}
