package workerboard.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class RiskDto {
    private String name;
    private double sum;
    private PremiumDto premiumList;
    private String displayName;
    @JsonIgnore
    private boolean addedToCart;

    public RiskDto() {
    }

    public RiskDto(String name, double sum, PremiumDto premiumList, String displayName) {
        this.name = name;
        this.sum = sum;
        this.premiumList = premiumList;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public double getSum() {
        return sum;
    }

    public PremiumDto getPremiumList() {
        return premiumList;
    }

    public void setPremiumList(PremiumDto premiumList) {
        this.premiumList = premiumList;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean isAddedToCart() {
        return addedToCart;
    }
}
