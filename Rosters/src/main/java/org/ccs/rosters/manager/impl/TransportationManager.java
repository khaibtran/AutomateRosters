package org.ccs.rosters.manager.impl;

import org.ccs.rosters.generator.DailyTransportationUpdate;
import org.ccs.rosters.manager.ITransportationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransportationManager implements ITransportationManager {

    @Autowired
    DailyTransportationUpdate dailyTransportationUpdate;

    @Override
    public String automateTransportation() {
        return dailyTransportationUpdate.generateUpdate();
    }
}
