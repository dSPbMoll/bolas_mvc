package asteroid.life_generation;

public class EntityParams {
    public int minDiameter;
    public int maxDiameter;
    public int minSpeed;
    public int maxSpeed;

    public EntityParams(int minDiameter, int maxDiameter, int minSpeed, int maxSpeed) {
        this.minDiameter = minDiameter;
        this.maxDiameter = maxDiameter;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
    }

    public void setMaxDiameter(int maxDiameter) {
        this.maxDiameter = maxDiameter;
    }

    public void setMinDiameter(int minDiameter) {
        this.minDiameter = minDiameter;
    }

    public void setMinSpeed(int minSpeed) {
        this.minSpeed = minSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
