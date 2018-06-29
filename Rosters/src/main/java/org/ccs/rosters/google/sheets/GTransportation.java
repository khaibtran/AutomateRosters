package org.ccs.rosters.google.sheets;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.ccs.rosters.google.credentials.IGoogleAuthService;
import org.ccs.rosters.google.credentials.impl.GoogleAuthService;
import org.ccs.rosters.Config;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class GTransportation {

    private IGoogleAuthService googleAuthService = new GoogleAuthService();


    private final String APPLICATION_NAME = "CCS Auth Service";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private NetHttpTransport HTTP_TRANSPORT;


    final String inputOption = "RAW";
    final String insertOption = "INSERT_ROWS";

    {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException g) {
            g.printStackTrace();
        }
    }

    Sheets sheetsService = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, googleAuthService.getCredential())
            .setApplicationName(APPLICATION_NAME)
            .build();

    public void executeDailyTransportUpdate(String spreadsheetId, String range, List<List<Object>> writeData) throws IOException{


        ValueRange requestBody = new ValueRange();
        requestBody.setValues(writeData);

        Sheets.Spreadsheets.Values.Append request =
                sheetsService.spreadsheets().values().append(spreadsheetId, range, requestBody);
        request.setValueInputOption(inputOption);
        request.setInsertDataOption(insertOption);

        AppendValuesResponse response = request.execute();

        System.out.println(response);

    }

    public void executeBusRosterUpdate(String sID, String comment) throws IOException{
        ValueRange requestBody = new ValueRange();
        List<List<Object>> writeData = new ArrayList<>();
        List<Object> inputData = new ArrayList<>();
        String[] input = {"=MATCH(" + sID + ",Import!A:A, 0)"};
        inputData.addAll(Arrays.asList(input));
        writeData.add(inputData);
        requestBody.setValues(writeData);

        Sheets.Spreadsheets.Values.Append request =
                sheetsService.spreadsheets().values().append(Config.BUS_ROSTER_ID, Config.RANGE_BUS_ROSTER, requestBody);
        request.setIncludeValuesInResponse(true);
        request.setResponseValueRenderOption("FORMATTED_VALUE");
        request.setValueInputOption("USER_ENTERED");
        request.setInsertDataOption(insertOption);

        AppendValuesResponse response = request.execute();


        List<List<Object>> writeDataUpdate = new ArrayList<>();
        List<Object> inputDataUpdate = new ArrayList<>();
        String[] inputUpdate = {comment};
        inputDataUpdate.addAll(Arrays.asList(inputUpdate));
        writeDataUpdate.add(inputDataUpdate);
        ValueRange requestBodyUpdate = new ValueRange();
        requestBodyUpdate.setValues(writeDataUpdate);

        String rowFound = response.getUpdates()
                .getUpdatedData().getValues().get(0).get(0).toString();
        String rangeUpdate = "Import!E" + rowFound;

        Sheets.Spreadsheets.Values.Update requestUpdate =
                sheetsService.spreadsheets().values().update(Config.BUS_ROSTER_ID, rangeUpdate, requestBodyUpdate);
        requestUpdate.setValueInputOption("USER_ENTERED");

        UpdateValuesResponse updateValuesResponse = requestUpdate.execute();
    }

    public String[] copyUpdateTemplate() {

        try {
            //Create new spreadsheet
            Spreadsheet requestBody = new Spreadsheet();
            Sheets.Spreadsheets.Create createRequest =
                    sheetsService.spreadsheets().create(requestBody);
            Spreadsheet response = createRequest.execute();
            String[] copyInfo = {response.getSpreadsheetId(), response.getSpreadsheetUrl()};

            //Copy template to new spreadsheet
            CopySheetToAnotherSpreadsheetRequest copyRequestBody = new CopySheetToAnotherSpreadsheetRequest();
            copyRequestBody.setDestinationSpreadsheetId(copyInfo[0]);
            Sheets.Spreadsheets.SheetsOperations.CopyTo copyRequest =
                    sheetsService.spreadsheets().sheets().copyTo(Config.SPREADSHEET_UPDATE_TEMPLATE, Config.SHEET_UPDATE_TEMPLATE, copyRequestBody);

            SheetProperties copyResponse = copyRequest.execute();

            return copyInfo;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}