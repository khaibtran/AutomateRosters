package org.ccs.rosters.schoolrunner.transportation;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.ccs.rosters.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ccs.rosters.schoolrunner.service.ISRService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Component
public class SRTransportation {

    @Autowired
    private ISRService srService;


    LocalDate date = LocalDate.now();
    String dateFormat = date.format(DateTimeFormatter.ISO_LOCAL_DATE);

    public List<Object> getTransportationUpdates() {
        List<NameValuePair> parameters = new ArrayList<NameValuePair>();
        parameters.add(new BasicNameValuePair("demerit_type_ids", Config.TRANSPORT_DEMERIT_ID));
        parameters.add(new BasicNameValuePair("min_date", dateFormat));
        parameters.add(new BasicNameValuePair("max_date", dateFormat));
        parameters.add(new BasicNameValuePair("active", "1"));

        String json = srService.request(Config.ENDPOINT_BEHAVIORS, parameters);

        JsonParser jsonParser = new JsonParser();

        if(jsonParser.parse(json).getAsJsonObject().getAsJsonObject("meta").getAsJsonPrimitive("records").getAsInt() == 0) {
            return null;
        }

        JsonArray jsonArray = jsonParser.parse(json)
                .getAsJsonObject()
                .getAsJsonObject("results")
                .getAsJsonArray(Config.ENDPOINT_BEHAVIORS);


        String studentList = "";
        ArrayList<String> studentIDList = new ArrayList<>();
        HashMap<String, String> commentMap = new HashMap<>();

        for(int i = 0; i < jsonArray.size(); i++) {
            String studentId = jsonArray.get(i).getAsJsonObject().get("student_id").toString();
            String comment = jsonArray.get(i).getAsJsonObject().get("comments").toString();
            String sID = studentId.replace("\"", "");
            studentList = studentList + "," + sID;
            studentIDList.add(sID);
            commentMap.put(sID, comment.replace("\"", ""));
        }

        List<NameValuePair> parametersNames = new ArrayList<NameValuePair>();
        parametersNames.add(new BasicNameValuePair("student_ids", studentList));
        String jsonNames = srService.request(Config.ENDPOINT_STUDENTS, parametersNames);

        JsonArray jsonNamesArray = jsonParser.parse(jsonNames)
                .getAsJsonObject()
                .getAsJsonObject("results")
                .getAsJsonArray(Config.ENDPOINT_STUDENTS);

        HashMap<String, String> nameMap = new HashMap<>();
        HashMap<String, String> sisIDMap = new HashMap<>();
        ArrayList<String> studentIDListPK = new ArrayList<>();
        ArrayList<String> studentIDListK2 = new ArrayList<>();
        ArrayList<String> studentIDList35 = new ArrayList<>();
        ArrayList<String> studentIDList68 = new ArrayList<>();

        for(int i = 0; i < jsonNamesArray.size(); i++) {
            String studentId = jsonNamesArray.get(i).getAsJsonObject().get("student_id").toString();
            String sisId = jsonNamesArray.get(i).getAsJsonObject().get("school_id_code").toString();
            String studentName = jsonNamesArray.get(i).getAsJsonObject().get("first_name").toString()
                    + " "
                    + jsonNamesArray.get(i).getAsJsonObject().get("last_name").toString();
            String sID = studentId.substring(1, studentId.length() - 1);
            nameMap.put(sID, studentName.replace("\"", ""));
            sisIDMap.put(sID, sisId.replace("\"", ""));

            switch (jsonNamesArray.get(i).getAsJsonObject().get("grade_level_id").toString().replace("\"", "")){
                case Config.PK3:
                case Config.PreK:
                    studentIDListPK.add(sID);
                    break;

                case Config.K:
                case Config.FIRST:
                case Config.SECOND:
                    studentIDListK2.add(sID);
                    break;

                case Config.THIRD:
                case Config.FORTH:
                case Config.FIFTH:
                    studentIDList35.add(sID);
                    break;

                case Config.SIXTH:
                case Config.SEVENTH:
                case Config.EIGHTH:
                    studentIDList68.add(sID);
                    break;
                default:System.out.println("Grade Level ID not found:" +
                        jsonNamesArray.get(i).getAsJsonObject().get("grade_level_id").toString().replace("\"", ""));
            }
        }

        List<List<Object>> writeDataPK = new ArrayList<>();
        List<List<Object>> writeDataK2 = new ArrayList<>();
        List<List<Object>> writeData35 = new ArrayList<>();
        List<List<Object>> writeData68 = new ArrayList<>();
        List<List<Object>> writeDataRoster = new ArrayList<>();
        List<Object> writeArray = new ArrayList<>();

        for(int i = 0; i < studentIDListPK.size(); i++) {
            String[] input = {nameMap.get(studentIDListPK.get(i)), commentMap.get(studentIDListPK.get(i))};
            List<Object> inputData = new ArrayList<>();
            inputData.addAll(Arrays.asList(input));
            writeDataPK.add(inputData);

            String[] inputRoster = {sisIDMap.get(studentIDListPK.get(i)), commentMap.get(studentIDListPK.get(i))};
            List<Object> inputDataRoster = new ArrayList<>();
            inputDataRoster.addAll(Arrays.asList(inputRoster));
            writeDataRoster.add(inputDataRoster);
        }
        writeArray.add(writeDataPK);

        for(int i = 0; i < studentIDListK2.size(); i++) {
            String[] input = {nameMap.get(studentIDListK2.get(i)), commentMap.get(studentIDListK2.get(i))};
            List<Object> inputData = new ArrayList<>();
            inputData.addAll(Arrays.asList(input));
            writeDataK2.add(inputData);

            String[] inputRoster = {sisIDMap.get(studentIDListK2.get(i)), commentMap.get(studentIDListK2.get(i))};
            List<Object> inputDataRoster = new ArrayList<>();
            inputDataRoster.addAll(Arrays.asList(inputRoster));
            writeDataRoster.add(inputDataRoster);
        }
        writeArray.add(writeDataK2);

        for(int i = 0; i < studentIDList35.size(); i++) {
            String[] input = {nameMap.get(studentIDList35.get(i)), commentMap.get(studentIDList35.get(i))};
            List<Object> inputData = new ArrayList<>();
            inputData.addAll(Arrays.asList(input));
            writeData35.add(inputData);

            String[] inputRoster = {sisIDMap.get(studentIDList35.get(i)), commentMap.get(studentIDList35.get(i))};
            List<Object> inputDataRoster = new ArrayList<>();
            inputDataRoster.addAll(Arrays.asList(inputRoster));
            writeDataRoster.add(inputDataRoster);
        }
        writeArray.add(writeData35);

        for(int i = 0; i < studentIDList68.size(); i++) {
            String[] input = {nameMap.get(studentIDList68.get(i)), commentMap.get(studentIDList68.get(i))};
            List<Object> inputData = new ArrayList<>();
            inputData.addAll(Arrays.asList(input));
            writeData68.add(inputData);

            String[] inputRoster = {sisIDMap.get(studentIDList68.get(i)), commentMap.get(studentIDList68.get(i))};
            List<Object> inputDataRoster = new ArrayList<>();
            inputDataRoster.addAll(Arrays.asList(inputRoster));
            writeDataRoster.add(inputDataRoster);
        }
        writeArray.add(writeData68);

        writeArray.add(writeDataRoster);

        return writeArray;
    }

}
