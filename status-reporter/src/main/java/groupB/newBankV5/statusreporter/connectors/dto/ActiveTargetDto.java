package groupB.newBankV5.statusreporter.connectors.dto;

public class ActiveTargetDto {

    String health;

    LabelsDto labels;



    public void setHealth(String health) {
        this.health = health;
    }

    public String getHealth() {
        return health;
    }

    public void setLabels(LabelsDto labels) {
        this.labels = labels;
    }

    public LabelsDto getLabels() {
        return labels;
    }
}
