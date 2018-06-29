package org.ccs.rosters.generator;

import org.ccs.rosters.google.sheets.GTransportation;
import org.springframework.beans.factory.annotation.Autowired;
import org.ccs.rosters.schoolrunner.transportation.SRTransportation;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class DailyTransportationUpdate {

    @Autowired
    SRTransportation srTransportation = new SRTransportation();

    @Autowired
    GTransportation gTransportation = new GTransportation();

    private final String spreadsheetId = "1fZXtsNbWI3_SldT1-So4FRQMxWEtCGshYHw80Fnd4Bg";
    private final String PK_RANGE = "'Copy of Grade Band Template'!A1:B3";
    private final String K2_RANGE = "'Copy of Grade Band Template'!A5:B7";
    private final String THREE_FIVE_RANGE = "'Copy of Grade Band Template'!A9:B11";
    private final String SIX_EIGHT_RANGE = "'Copy of Grade Band Template'!A13:B15";

    public String generateUpdate() {
        List<Object> writeArray = srTransportation.getTransportationUpdates();

        if(writeArray == null) {
            return "{\"response\": \"No updates\"}";
        }

        List<List<Object>> writeDataPK = (List<List<Object>>) writeArray.get(0);
        List<List<Object>> writeDataK2 = (List<List<Object>>) writeArray.get(1);
        List<List<Object>> writeData35 = (List<List<Object>>) writeArray.get(2);
        List<List<Object>> writeData68 = (List<List<Object>>) writeArray.get(3);
        List<List<Object>> writeDataRoster = (List<List<Object>>) writeArray.get(4);

        try {
            String[] copyData = gTransportation.copyUpdateTemplate();
            gTransportation.executeDailyTransportUpdate(copyData[0], SIX_EIGHT_RANGE, writeData68);
            gTransportation.executeDailyTransportUpdate(copyData[0], THREE_FIVE_RANGE, writeData35);
            gTransportation.executeDailyTransportUpdate(copyData[0], K2_RANGE, writeDataK2);
            gTransportation.executeDailyTransportUpdate(copyData[0], PK_RANGE, writeDataPK);

            for(int i = 0; i < writeDataRoster.size(); i++) {
                List<Object> writeData = writeDataRoster.get(i);
                gTransportation.executeBusRosterUpdate(writeData.get(0).toString(), writeData.get(1).toString());
            }

            return "{\"response\": \"" + copyData[1] + "\"}";

        } catch (IOException e) {
            e.printStackTrace();
            return "Failed";
        }

    }
}
