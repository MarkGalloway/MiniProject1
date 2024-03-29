package MiniProject1;

/*
 * A data storage class which contains the fields of an
 * Offer object corresponding to a row in the offers table in the database
 */
public class Offer {

    private String Ono;
    private Integer ndays;
    private Float price;
    
    /*
     * Constructor to make an Offer object
     */
    public Offer(String Ono, Integer ndays, Float price) {
        super();
        this.setOno(Ono);
        this.setNdays(ndays);
        this.setPrice(price);
    }
    
    /*
     * Prints the string representation of the object
     */
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
