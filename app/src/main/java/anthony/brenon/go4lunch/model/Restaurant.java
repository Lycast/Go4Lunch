package anthony.brenon.go4lunch.model;

/**
 * Created by Lycast on 29/01/2022.
 */
class Restaurant {

    private String urlImage;
    private String name;
    private String type;
    private String address;
    private String openingTime;
    private String website;
    private Integer range;
    private Integer phoneNumber;
    private Integer note;
    private Boolean isChoice;
    private Boolean isLike;

    public Restaurant(String urlImage, String name, String type, String address, String openingTime, String website, Integer range, Integer phoneNumber, Integer note, Boolean isChoice, Boolean isLike) {
        this.urlImage = urlImage;
        this.name = name;
        this.type = type;
        this.address = address;
        this.openingTime = openingTime;
        this.website = website;
        this.range = range;
        this.phoneNumber = phoneNumber;
        this.note = note;
        this.isChoice = isChoice;
        this.isLike = isLike;
    }

    public String getUrlImage() { return urlImage; }
    public void setUrlImage(String urlImage) { this.urlImage = urlImage; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getOpeningTime() { return openingTime; }
    public void setOpeningTime(String openingTime) { this.openingTime = openingTime; }

    public String getWebsite() { return website; }

    public void setWebsite(String website) { this.website = website; }

    public Integer getRange() { return range; }
    public void setRange(Integer range) { this.range = range; }

    public Integer getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(Integer phoneNumber) { this.phoneNumber = phoneNumber; }

    public Integer getNote() { return note; }
    public void setNote(Integer note) { this.note = note; }

    public Boolean getChoice() { return isChoice; }
    public void setChoice(Boolean choice) { isChoice = choice; }

    public Boolean getLike() { return isLike; }
    public void setLike(Boolean like) { isLike = like; }
}
