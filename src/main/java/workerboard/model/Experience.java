package workerboard.model;

import com.fasterxml.jackson.annotation.JsonView;
import workerboard.model.dto.ViewsForApplicationUser;

import javax.persistence.*;

@Entity
public class Experience {

    @Id
    private Long id;
    @JsonView(ViewsForApplicationUser.Basic.class)
    private long level;
    @JsonView(ViewsForApplicationUser.Basic.class)
    private long expTotalEarned;
    @JsonView(ViewsForApplicationUser.Basic.class)
    private long expToNextLevel;
    @JsonView(ViewsForApplicationUser.Basic.class)
    private double percentToNextLvl;
    @JsonView(ViewsForApplicationUser.Basic.class)
    private int attack;
    @JsonView(ViewsForApplicationUser.Basic.class)
    private int defence;
    @JsonView(ViewsForApplicationUser.Basic.class)
    private int knowledge;
    @JsonView(ViewsForApplicationUser.Basic.class)
    private int speedAttack;
    @JsonView(ViewsForApplicationUser.Basic.class)
    private int pointsToAdd;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    ApplicationUser applicationUser;

    //to change
    public Experience() {
        this.level = 1;
        this.expTotalEarned = 0;
        this.expToNextLevel = 1000;
    }


    public void setApplicationUser(ApplicationUser applicationUser) {
        this.applicationUser = applicationUser;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getLevel() {
        return level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public long getExpTotalEarned() {
        return expTotalEarned;
    }

    public void setExpTotalEarned(long expTotalEarned) {
        this.expTotalEarned = expTotalEarned;
    }

    public long getExpToNextLevel() {
        return expToNextLevel;
    }

    public void setExpToNextLevel(long expToNextLevel) {
        this.expToNextLevel = expToNextLevel;
    }

    public double getPercentToNextLvl() {
        return percentToNextLvl;
    }

    public void setPercentToNextLvl(double percentToNextLvl) {
        this.percentToNextLvl = percentToNextLvl;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefence() {
        return defence;
    }

    public void setDefence(int defence) {
        this.defence = defence;
    }

    public int getKnowledge() {
        return knowledge;
    }

    public void setKnowledge(int knowledge) {
        this.knowledge = knowledge;
    }

    public int getSpeedAttack() {
        return speedAttack;
    }

    public void setSpeedAttack(int speedAttack) {
        this.speedAttack = speedAttack;
    }

    public int getPointsToAdd() {
        return pointsToAdd;
    }

    public void setPointsToAdd(int pointsToAdd) {
        this.pointsToAdd = pointsToAdd;
    }
}
