import java.util.Date;

public abstract class Plant {
    // Data fields
    private String name;
    private String species;
    private boolean watered;
    private boolean fullSun;
    private boolean partSun;
    private boolean shade;
    private double height;
    private Date datePlanted;
    private boolean canBeOutdoors;
    private boolean winter;
    private boolean spring;
    private boolean summer;
    private boolean fall;

    // Constructors
    public Plant() {
        this.datePlanted = new Date(); // Default to current date
    }
    
    public Plant(String name, String species) {
        this.name = name;
        this.species = species;
        this.datePlanted = new Date();
    }

    // Get age of plant in years
    public int getAge() {
        Date currentDate = new Date();
        long ageInMillis = currentDate.getTime() - datePlanted.getTime();
        return (int) (ageInMillis / (1000L * 60 * 60 * 24 * 365));
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getSpecies() { return species; }
    public void setSpecies(String species) { this.species = species; }
    
    public boolean isWatered() { return watered; }
    public void setWatered(boolean watered) { this.watered = watered; }
    
    public boolean isFullSun() { return fullSun; }
    public void setFullSun(boolean fullSun) { this.fullSun = fullSun; }
    
    public boolean isPartSun() { return partSun; }
    public void setPartSun(boolean partSun) { this.partSun = partSun; }
    
    public boolean isShade() { return shade; }
    public void setShade(boolean shade) { this.shade = shade; }
    
    public double getHeight() { return height; }
    public void setHeight(double height) { this.height = height; }
    
    public Date getDatePlanted() { return datePlanted; }
    public void setDatePlanted(Date datePlanted) { this.datePlanted = datePlanted; }
    
    public boolean isCanBeOutdoors() { return canBeOutdoors; }
    public void setCanBeOutdoors(boolean canBeOutdoors) { this.canBeOutdoors = canBeOutdoors; }
    
    public boolean isWinter() { return winter; }
    public void setWinter(boolean winter) { this.winter = winter; }
    
    public boolean isSpring() { return spring; }
    public void setSpring(boolean spring) { this.spring = spring; }
    
    public boolean isSummer() { return summer; }
    public void setSummer(boolean summer) { this.summer = summer; }
    
    public boolean isFall() { return fall; }
    public void setFall(boolean fall) { this.fall = fall; }
}
