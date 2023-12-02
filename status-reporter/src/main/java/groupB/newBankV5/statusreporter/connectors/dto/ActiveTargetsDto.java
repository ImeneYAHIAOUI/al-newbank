package groupB.newBankV5.statusreporter.connectors.dto;

import java.util.List;

public class ActiveTargetsDto {

    List<ActiveTargetDto> activeTargets;

    public List<ActiveTargetDto> getActiveTargets() {
        return activeTargets;
    }

    public void setActiveTargets(List<ActiveTargetDto> activeTargets) {
        this.activeTargets = activeTargets;
    }
}
