/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package bakalaurinis.database;

/**
 *
 * @author AzureDragon
 */
public class H2Db {
     /*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package lt.dekbera.mobileClient.model.dbSservice;
//
//import java.io.File;
//import java.math.BigDecimal;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import lt.dekbera.mobileClient.logger.Log4jService;
//import lt.dekbera.mobileClient.model.controllTypes.Destination;
//import lt.dekbera.mobileClient.model.dbEntity.Sds;
//import lt.dekbera.mobileClient.model.dbEntity.SdsStatus;
//import lt.dekbera.mobileClient.model.dbEntity.Service;
//import lt.dekbera.mobileClient.model.dbEntity.SysInfo;
//import lt.dekbera.mobileClient.model.deviceControllService.CommunicationDataService.StatusState;
//import lt.dekbera.mobileClient.model.dbEntity.SdsInfo;
//import lt.dekbera.mobileClient.model.dbSservice.LocalDbInterface.RequestForm;
//import lt.dekbera.mobileClient.model.dbEntity.ErrorReport;
//import lt.dekbera.mobileClient.ws.AvType;
//
///**
// *
// * @author o.ziukas
// */
//public class H2localDb implements LocalDbInterface {
//
//    protected static Connection conn = null;
//
//    public H2localDb(String type, String name, String param) {
//        connectToDB(type, name, param);
//    }
//
//    @Override
//    public List<SdsStatus> getStatusFromDB(RequestForm status) {
//        String sqlStatement;
//        sqlStatement = "SELECT " + sdsStatusTableName + ".ID, "
//                + sdsStatusTableName + ".STATUS, "
//                + sdsInfoTableName + ".EVENT_ID, "
//                + sdsInfoTableName + ".MISSION_ID, "
//                + sdsStatusTableName + ".EVENT_TIME, "
//                + sdsStatusTableName + "." + sendingState
//                + " FROM " + sdsStatusTableName
//                + " LEFT JOIN " + sdsInfoTableName
//                + " ON " + sdsStatusTableName + ".EVENT_ID = " + sdsInfoTableName + ".ID "
//                + " WHERE " + sendingState + (status.equals(RequestForm.ALL) ? " " : " = ") + requestForms[status.ordinal()];
//        if (checkIfExist(sdsStatusTableName) && checkIfExist(sdsInfoTableName)) {
//            Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//            try {
//                return parseStatusResultSetInformation(createNewStatement().executeQuery(sqlStatement));
//            } catch (SQLException ex) {
//                Log4jService.writeFatalError("ERROR WHILE EXECUTING " + sdsStatusTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public Boolean writeServiceToDB(Service service) {
//
//        if (isHaveAllNeccesaryValues(service)) {
//            if (checkIfExist(gpsServiceTableName)) {
//                return insertToDB(prepareServiceTransaction(service));
//            } else {
//                boolean t = createNewTable(gpsServiceTableName);
//                if (t == Boolean.TRUE) {
//                    return writeServiceToDB(service);
//                } else {
//                    return Boolean.FALSE;
//                }
//            }
//        }
//        return Boolean.FALSE;
//    }
//
//    @Override
//    public Boolean writeStatusToDB(SdsStatus sdsStatus) {
//        if (checkIfExist(sdsStatusTableName)) {
//            return insertToDB(prepareSdsStatusTransaction(sdsStatus));
//        } else {
//            boolean t = createNewTable(sdsStatusTableName);
//            if (t == Boolean.TRUE) {
//                return writeStatusToDB(sdsStatus);
//            }
//        }
//        return Boolean.FALSE;
//    }
//
//    @Override
//    public Boolean writeInfoToDB(SdsInfo sdsInfo) {
//        if (checkIfExist(sdsInfoTableName)) {
//            return insertToDB(prepareSdsInfoTransaction(sdsInfo));
//        } else {
//            boolean t = createNewTable(sdsInfoTableName);
//            if (t == Boolean.TRUE) {
//                return writeInfoToDB(sdsInfo);
//            }
//        }
//        return Boolean.FALSE;
//    }
//
//    @Override
//    public Boolean writeSystemInfo(SysInfo info) {
//        if (checkIfExist(systemInformationTableName)) {
//            try {
//                createNewStatement().executeUpdate("TRUNCATE TABLE " + systemInformationTableName);
//            } catch (SQLException ex) {
//                Log4jService.writeFatalError("ERROR WHILE TRUNCATING " + systemInformationTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            }
//            return insertToDB(prepareSystemInfoTransaction(info));
//        } else {
//            boolean t = createNewTable(systemInformationTableName);
//            if (t == Boolean.TRUE) {
//                return writeSystemInfo(info);
//            }
//        }
//        return Boolean.FALSE;
//    }
//
//    @Override
//    public SysInfo getSysInfoFromDB() {
//        String sqlStatement = "SELECT * FROM " + systemInformationTableName;
//        SysInfo result = null;
//        if (checkIfExist(systemInformationTableName)) {
//            Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//            try {
//                result = parseSystemResultSetInformation(createNewStatement().executeQuery(sqlStatement));
//            } catch (SQLException ex) {
//                Log4jService.writeFatalError("ERROR WHILE GETTING " + systemInformationTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            }
//            return result;
//        } else {
//            return null;
//        }
//    }
//
//    @Override
//    public void disconnectFromDB() {
//        if (conn != null) {
//            try {
//                createNewStatement().close();
//                conn.close();
//            } catch (SQLException ex) {
//                Log4jService.writeFatalError("CAN'T COMPLETE DISCONNECTION FROM LOCALDB " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            }
//        }
//    }
//
//    @Override
//    public Boolean removeCompletedFromDB() {
//        String sqlStatement = "DELETE FROM " + gpsServiceTableName + " WHERE " + sendingState + " = " + requestForms[RequestForm.SENT.ordinal()];
//        if (checkIfExist(gpsServiceTableName)) {
//            Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//            return tryToExecuteUpdateInLocalDb(sqlStatement);
//        } else {
//            return Boolean.FALSE;
//        }
//    }
//
//    @Override
//    public Boolean updateStatusSendingStateInDB(long id, RequestForm state) {
//        String sqlStatement = "UPDATE " + sdsStatusTableName + " SET " + sendingState + " = " + requestForms[state.ordinal()] + " WHERE ID=" + id;
//        if (checkIfExist(sdsStatusTableName)) {
//            Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//            return tryToExecuteUpdateInLocalDb(sqlStatement);
//        } else {
//            return Boolean.FALSE;
//        }
//    }
//
//    @Override
//    public Boolean updateServiceSendingStateInDB(List<Service> serviceList) {
//        String sqlStatement;
//        Boolean status = Boolean.FALSE;
//
//        for (Service element : serviceList) {
//            sqlStatement = "DELETE FROM " + gpsServiceTableName + " WHERE ID = " + element.getId();
//            if (checkIfExist(gpsServiceTableName)) {
//                Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//                status = tryToExecuteUpdateInLocalDb(sqlStatement);
//            } else {
//                return Boolean.FALSE;
//            }
//        }
//        return status;
//    }
//
//    @Override
//    public Integer getServiceSeq() {
//        String sqlStatement = "SELECT NEXTVAL('" + seqGpsServiceTableName + "') as \"SEQ_ID\" ";
//        ResultSet rs;
//        Integer uniqueId = null;
//        try {
//            rs = createNewStatement().executeQuery(sqlStatement);
//            rs.next();
//            uniqueId = rs.getInt("SEQ_ID");
//        } catch (SQLException ex) {
//            try {
//                createNewStatement().execute(getServiceSequanceProperties());
//                rs = createNewStatement().executeQuery(sqlStatement);
//                rs.next();
//                uniqueId = rs.getInt("SEQ_ID");
//            } catch (SQLException ex1) {
//                Log4jService.writeFatalError("EXCEPTION WAS ENCOUNTERED WHILE CREATING SERVICE SEQUENCE -" + Log4jService.getStackTrace(ex1), H2localDb.class.getName());
//            }
//        }
//        return uniqueId;
//    }
//
//    @Override
//    public Integer getStatusSeq() {
//        String sqlStatement = "SELECT NEXTVAL('" + seqSdsStatusTableName + "') as \"SEQ_ID\" ";
//        ResultSet rs;
//        Integer uniqueId = null;
//        try {
//            rs = createNewStatement().executeQuery(sqlStatement);
//            rs.next();
//            uniqueId = rs.getInt("SEQ_ID");
//        } catch (SQLException ex) {
//            try {
//                createNewStatement().execute(getStatusSequanceProperties());
//                rs = createNewStatement().executeQuery(sqlStatement);
//                rs.next();
//                uniqueId = rs.getInt("SEQ_ID");
//            } catch (SQLException ex1) {
//                Log4jService.writeFatalError("EXCEPTION WAS ENCOUNTERED WHILE CREATING STATUS SEQUENCE -" + Log4jService.getStackTrace(ex1), H2localDb.class.getName());
//            }
//        }
//        return uniqueId;
//    }
//
//    @Override
//    public Integer getInfoSeq() {
//        String sqlStatement = "SELECT NEXTVAL('" + seqSdsInfoTableName + "') as \"SEQ_ID\" ";
//        ResultSet rs;
//        Integer uniqueId = null;
//        try {
//            rs = createNewStatement().executeQuery(sqlStatement);
//            rs.next();
//            uniqueId = rs.getInt("SEQ_ID");
//        } catch (SQLException ex) {
//            try {
//                createNewStatement().execute(getInfoSequanceProperties());
//                rs = createNewStatement().executeQuery(sqlStatement);
//                rs.next();
//                uniqueId = rs.getInt("SEQ_ID");
//            } catch (SQLException ex1) {
//                Log4jService.writeFatalError("EXCEPTION WAS ENCOUNTERED WHILE CREATING INFO SEQUENCE -" + Log4jService.getStackTrace(ex1), H2localDb.class.getName());
//            }
//        }
//        return uniqueId;
//    }
//
//    @Override
//    public Integer getReportSeq() {
//        String sqlStatement = "SELECT NEXTVAL('" + seqReportTableName + "') as \"SEQ_ID\" ";
//        ResultSet rs;
//        Integer uniqueId = null;
//        try {
//            rs = createNewStatement().executeQuery(sqlStatement);
//            rs.next();
//            uniqueId = rs.getInt("SEQ_ID");
//        } catch (SQLException ex) {
//            try {
//                createNewStatement().execute(getInfoSequanceProperties());
//                rs = createNewStatement().executeQuery(sqlStatement);
//                rs.next();
//                uniqueId = rs.getInt("SEQ_ID");
//            } catch (SQLException ex1) {
//                Log4jService.writeFatalError("EXCEPTION WAS ENCOUNTERED WHILE CREATING REPORT SEQUENCE -" + Log4jService.getStackTrace(ex1), H2localDb.class.getName());
//            }
//        }
//        return uniqueId;
//    }
//
//    @Override
//    public Boolean removeReport(ErrorReport errorReport) {
//        String sqlStatement = "DELETE FROM " + reportsTableName + " WHERE ID = '" + errorReport.getId() + "'";
//        if (checkIfExist(reportsTableName)) {
//            Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//            return tryToExecuteUpdateInLocalDb(sqlStatement);
//        } else {
//            return Boolean.FALSE;
//        }
//    }
//
//    @Override
//    public Boolean writeErrorReport(ErrorReport errorReport) {
//        if (checkIfExist(reportsTableName)) {
//            return insertToDB(prepareReportsTransaction(errorReport));
//        } else {
//            boolean t = createNewTable(reportsTableName);
//            if (t == Boolean.TRUE) {
//                return writeErrorReport(errorReport);
//            }
//        }
//        return Boolean.FALSE;
//    }
//
//    @Override
//    public void removeOldSdsEvents(Sds event) {
//        List<SdsStatus> temporaryList = getStatusFromDB(RequestForm.ALL);
//        List<SdsStatus> eventStatusList = new ArrayList<>();
//
//        if (isNotNull(temporaryList) && !temporaryList.isEmpty()) {
//            Long tmpLong = event.getEventId();
//            Integer tmpInt = event.getMissionId();
//            for (SdsStatus element : temporaryList) {
//                if (tmpLong.equals(element.getEventId()) && tmpInt.equals(element.getMissionId())) {
//                    eventStatusList.add(element);
//                }
//            }
//        }
//
//        removeOldStatusFromDb(eventStatusList);
//        removeOldInfoFromDb(getInfoFromDB(event.getEventId() + "-" + event.getMissionId()));
//    }
//
//    @Override
//    public List<Sds> getSdsFromDB() {
//        String sqlStatement = "SELECT DISTINCT "
//                + sdsInfoTableName + ".EVENT_ID, "
//                + sdsInfoTableName + ".MISSION_ID, "
//                + sdsInfoTableName + ".RECEIVE_TIME FROM " + sdsInfoTableName + " ORDER BY EVENT_ID DESC";
//        if (checkIfExist(sdsInfoTableName)) {
//            Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//            try {
//                return parseSdsResultSetInformation(createNewStatement().executeQuery(sqlStatement));
//            } catch (SQLException ex) {
//                Log4jService.writeFatalError("ERROR WHILE GETTING INFORMATION FROM " + sdsInfoTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<SdsInfo> getInfoFromDB(String number) {
//        Log4jService.writeInfo("SELECT * FROM " + sdsInfoTableName + " WHERE EVENT_ID=" + "'" + Long.parseLong(number.split("-")[0]) + (!number.split("-")[1].isEmpty() ? "' AND MISSION_ID = '" + number.split("-")[1] + "';" : "';"), H2localDb.class.getName());
//        String sqlStatement = "SELECT * FROM " + sdsInfoTableName + " WHERE EVENT_ID=" + "'" + Long.parseLong(number.split("-")[0]) + (!number.split("-")[1].isEmpty() ? "' AND MISSION_ID = '" + number.split("-")[1] + "';" : "';");
//        if (checkIfExist(sdsInfoTableName)) {
//            Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//            try {
//                return parseInfoResultSetInformation(createNewStatement().executeQuery(sqlStatement));
//            } catch (SQLException ex) {
//                Log4jService.writeFatalError("ERROR WHILE GETTING INFORMATION FROM " + sdsInfoTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<Service> getService(RequestForm status) {
//        String sqlStatement = "SELECT * FROM " + gpsServiceTableName + " WHERE " + sendingState + " = " + requestForms[status.ordinal()] + ";";
//        if (checkIfExist(gpsServiceTableName)) {
//            Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//            try {
//                return parseServiceResultSetInformation(createNewStatement().executeQuery(sqlStatement));
//            } catch (SQLException ex) {
//                Log4jService.writeFatalError("ERROR WHILE GETTING INFORMATION FROM " + gpsServiceTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            }
//        }
//        return null;
//    }
//
//    @Override
//    public List<ErrorReport> getReportsFromDB() {
//        String sqlStatement = "SELECT * FROM " + reportsTableName;
//        if (checkIfExist(reportsTableName)) {
//            Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//            try {
//                return parseReportResultSetInformation(createNewStatement().executeQuery(sqlStatement));
//            } catch (SQLException ex) {
//                Log4jService.writeFatalError("ERROR WHILE GETTING INFORMATION FROM " + reportsTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            }
//        }
//        return null;
//    }
//
//    private boolean createNewTable(String tableName) {
//        boolean status;
//
//        switch (tableName) {
//            case gpsServiceTableName: {
//                status = createNewServiceTable();
//                break;
//            }
//            case sdsInfoTableName: {
//                status = createNewInfoTable();
//                break;
//            }
//            case sdsStatusTableName: {
//                status = createNewStatusTable();
//            }
//            break;
//            case systemInformationTableName: {
//                status = createNewSysInfoTable();
//            }
//            break;
//            case reportsTableName: {
//                status = createNewReportTable();
//            }
//            break;
//            default: {
//                status = Boolean.FALSE;
//                break;
//            }
//        }
//        return status;
//    }
//
//    private boolean isNull(Object object) {
//        return object == null;
//    }
//
//    private String getInfoSequanceProperties() {
//        return "CREATE SEQUENCE IF NOT EXISTS " + seqSdsInfoTableName;
//    }
//
//    private String getStatusSequanceProperties() {
//        return "CREATE SEQUENCE IF NOT EXISTS " + seqSdsStatusTableName;
//    }
//
//    private String getServiceSequanceProperties() {
//        return "CREATE SEQUENCE IF NOT EXISTS " + seqGpsServiceTableName;
//    }
//
//    private String getReportSequanceProperties() {
//        return "CREATE SEQUENCE IF NOT EXISTS " + seqReportTableName;
//    }
//
//    private String getServiceTableProperties() {
//        return "CREATE TABLE " + gpsServiceTableName
//                + " (ID INT DEFAULT " + seqGpsServiceTableName + ".nextval primary key, "
//                + "LATITUDE DOUBLE, "
//                + "LONGITUDE DOUBLE, "
//                + "GPS_TIME DATETIME, "
//                + "A_V CHAR(1), "
//                + "COURSE DOUBLE, "
//                + "SPEED DOUBLE, "
//                + "DBM INTEGER, "
//                + "BASE_STATION_ID INTEGER, "
//                + sendingState + " INTEGER);";
//    }
//
//    private String getSdsInfoTableProperties() {
//        return "CREATE TABLE ROBOTS "
//                + " (ID INT DEFAULT " + seqSdsInfoTableName + ".nextval primary key, "
//                + "EVENT_ID LONG, "
//                + "MISSION_ID INT, "
//                + "PATIENT_ID INT, "
//                + "RECEIVE_TIME DATETIME, "
//                + "ASSIGN_TIME DATETIME, "
//                + "DESCRIPTION CHAR(255), "
//                + "LIGHTS CHAR(255), "
//                + "GENDER_IDENTITY CHAR(255), "
//                + "CONSCIOUS CHAR(255), "
//                + "BREATHING CHAR(255), "
//                + "SYMPTOM CHAR(2048), "
//                + "PHONE_NO CHAR(255), "
//                + "DEADLINE_TIME INT, "
//                + "CATEGORY INTEGER,"
//                + "DISPECHERID CHAR(255),"
//                + "ADDRESS CHAR(255), "
//                + "CROSSING_STREET CHAR(255),"
//                + "LOCATION_NOTES CHAR(255),"
//                + "LONGITUDE DOUBLE,"
//                + "LATITUDE DOUBLE,"
//                + "CREATED_AT DATETIME,"
//                + "SOURCE CHAR(255));";
//    }
//
//    private String getSdsStatusTableProperties() {
//        if (!checkIfExist(sdsInfoTableName)) {
//            createNewInfoTable();
//        }
//
//        return "CREATE TABLE " + sdsStatusTableName
//                + " (ID LONG DEFAULT " + seqSdsStatusTableName + ".nextval primary key, "
//                + "STATUS CHAR(255), "
//                + "EVENT_TIME DATETIME, "
//                + "EVENT_ID LONG, "
//                + sendingState + " INTEGER,"
//                + "FOREIGN KEY (EVENT_ID) REFERENCES " + sdsInfoTableName + "(ID));";
//    }
//
//    private String getReportTableProperties() {
//        return "CREATE TABLE " + reportsTableName
//                + " (ID INT DEFAULT " + seqReportTableName + ".nextval primary key, "
//                + "MAIL CHAR(255), "
//                + "SUBJECT CHAR(255), "
//                + "MESSAGE CHAR(8048), "
//                + "REPORT_TIME DATETIME, "
//                + "PATH_TO_FILE CHAR(255), "
//                + sendingState + " INTEGER);";
//    }
//
//    private String getSystemInfoTableProperties() {
//        return "CREATE TABLE " + systemInformationTableName
//                + " (ID INT NOT NULL AUTO_INCREMENT, "
//                + "LANGUAGE CHAR(255), "
//                + "CONSTRAINT " + systemInformationTableName + " PRIMARY KEY ( ID ));";
//    }
//
//    private String getSdsInfoRecordBlank() {
//        return "INSERT INTO "
//                + sdsInfoTableName
//                + "(ID, EVENT_ID, MISSION_ID, PATIENT_ID, RECEIVE_TIME, ASSIGN_TIME, DESCRIPTION, LIGHTS, GENDER_IDENTITY, CONSCIOUS, BREATHING, SYMPTOM, PHONE_NO, DEADLINE_TIME, CATEGORY, DISPECHERID, ADDRESS, CROSSING_STREET, LOCATION_NOTES, LONGITUDE, LATITUDE, CREATED_AT, SOURCE) "
//                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    }
//
//    private String getServiceRecordBlank() {
//        return "INSERT INTO "
//                + gpsServiceTableName
//                + "(ID, LATITUDE, LONGITUDE, GPS_TIME, A_V, COURSE, SPEED, DBM, BASE_STATION_ID, " + sendingState + ")"
//                + "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//    }
//
//    private String getSdsStatusRecordBlank() {
//        return "INSERT INTO "
//                + sdsStatusTableName
//                + "(ID, STATUS, EVENT_TIME, EVENT_ID, " + sendingState + ") "
//                + "VALUES(?, ?, ?, ?, ?)";
//    }
//
//    private String getSystemInfoRecordBlank() {
//        return "INSERT INTO "
//                + systemInformationTableName
//                + "(LANGUAGE)"
//                + " VALUES(?)";
//    }
//
//    private String getReportRecordBlank() {
//        return "INSERT INTO "
//                + reportsTableName
//                + "(ID, MAIL, SUBJECT, MESSAGE, REPORT_TIME, PATH_TO_FILE, " + sendingState + ")"
//                + " VALUES(?, ?, ?, ?, ?, ?, ?)";
//    }
//
//    private PreparedStatement prepareSdsInfoTransaction(SdsInfo sdsInfo) {
//
//        Log4jService.writeInfo(sdsInfo.toString(), H2localDb.class.getName());
//
//        try {
//            PreparedStatement sdsInfoTransactionStatement;
//            sdsInfoTransactionStatement = conn.prepareStatement(getSdsInfoRecordBlank());
//            sdsInfoTransactionStatement.setInt(1, (sdsInfo.getId() == -1 ? getInfoSeq() : sdsInfo.getId()));
//            sdsInfoTransactionStatement.setLong(2, sdsInfo.getEventId());
//            sdsInfoTransactionStatement.setInt(3, isNotNull(sdsInfo.getMissionId()) ? sdsInfo.getMissionId() : 1);
//            sdsInfoTransactionStatement.setInt(4, isNotNull(sdsInfo.getPatientId()) ? sdsInfo.getPatientId() : 0);
//            sdsInfoTransactionStatement.setString(5, generateProperTimeValueForStatus(sdsInfo.getReceiveTime()));
//            sdsInfoTransactionStatement.setString(6, generateProperTimeValueForStatus(sdsInfo.getAssignmentTime()));
//            sdsInfoTransactionStatement.setString(7, sdsInfo.getDescription());
//            sdsInfoTransactionStatement.setString(8, sdsInfo.getLightsOn());
//            sdsInfoTransactionStatement.setString(9, sdsInfo.getGenderIdentity());
//            sdsInfoTransactionStatement.setString(10, sdsInfo.getConscious());
//            sdsInfoTransactionStatement.setString(11, sdsInfo.getBreathing());
//            sdsInfoTransactionStatement.setString(12, sdsInfo.getSymptom());
//            sdsInfoTransactionStatement.setString(13, sdsInfo.getPhoneNumbers());
//            sdsInfoTransactionStatement.setInt(14, sdsInfo.getDeadlineTime());
//            sdsInfoTransactionStatement.setInt(15, sdsInfo.getEventCategory());
//            sdsInfoTransactionStatement.setString(16, sdsInfo.getDispatcherId());
//            sdsInfoTransactionStatement.setString(17, sdsInfo.getDestination().getAddress());
//            sdsInfoTransactionStatement.setString(18, sdsInfo.getCrossingStreat());
//            sdsInfoTransactionStatement.setString(19, sdsInfo.getLocationNotes());
//            if (isNull(sdsInfo.getDestination().getLongitude()) || sdsInfo.getDestination().getLongitude().isNaN()) {
//                sdsInfoTransactionStatement.setNull(20, java.sql.Types.DOUBLE);
//            } else {
//                sdsInfoTransactionStatement.setBigDecimal(20, BigDecimal.valueOf(sdsInfo.getDestination().getLongitude()));
//            }
//            if (isNull(sdsInfo.getDestination().getLatitude()) || sdsInfo.getDestination().getLatitude().isNaN()) {
//                sdsInfoTransactionStatement.setNull(21, java.sql.Types.DOUBLE);
//            } else {
//                sdsInfoTransactionStatement.setBigDecimal(21, BigDecimal.valueOf(sdsInfo.getDestination().getLatitude()));
//            }
//            sdsInfoTransactionStatement.setString(22, generateProperTimeValueForStatus(sdsInfo.getCreatedAt()));
//            sdsInfoTransactionStatement.setString(23, sdsInfo.getSource());
//
//            return sdsInfoTransactionStatement;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR ENCOUNTERED DURING SDS INFO FORMATING! - " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//
//            return null;
//        }
//    }
//
//    private PreparedStatement prepareSdsStatusTransaction(SdsStatus sdsStatus) {
//
//        Log4jService.writeInfo(sdsStatus.toString(), H2localDb.class.getName());
//
//        try {
//            PreparedStatement sdsStatusTransactionStatement;
//            sdsStatusTransactionStatement = conn.prepareStatement(getSdsStatusRecordBlank());
//            sdsStatusTransactionStatement.setInt(1, (sdsStatus.getId() == -1 ? getStatusSeq() : sdsStatus.getId()));
//            sdsStatusTransactionStatement.setString(2, states[sdsStatus.getStatusNumber().ordinal()]);
//            sdsStatusTransactionStatement.setString(3, generateProperTimeValueForStatus(sdsStatus.getStatusTime()));
//            Long id = getEventID(sdsStatus.getEventId(), sdsStatus.getMissionId());
//            if (isNull(id)) {
//                sdsStatusTransactionStatement.setNull(4, java.sql.Types.LONGVARCHAR);
//            } else {
//                sdsStatusTransactionStatement.setLong(4, id);
//            }
//            sdsStatusTransactionStatement.setInt(5, Integer.parseInt(requestForms[sdsStatus.getState().ordinal()]));
//
//            return sdsStatusTransactionStatement;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR ENCOUNTERED DURING SDS STATUS FORMATING! - " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//
//            return null;
//        }
//    }
//
//    private Long getEventID(Long eventId, Integer missionId) {
//        String sqlStatement = "SELECT * FROM " + sdsInfoTableName + " WHERE EVENT_ID = " + eventId + " AND MISSION_ID = " + missionId + ";";
//        if (checkIfExist(sdsInfoTableName)) {
//            Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//            try {
//                return parseInfoIdResultSetInformation(createNewStatement().executeQuery(sqlStatement));
//            } catch (SQLException ex) {
//                Log4jService.writeFatalError("ERROR WHILE GETTING INFORMATION FROM " + gpsServiceTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            }
//        }
//        return null;
//    }
//
//    private PreparedStatement prepareServiceTransaction(Service service) {
//
//        Log4jService.writeInfo(service.toString(), H2localDb.class.getName());
//
//        if (!isHaveAllNeccesaryValues(service)) {
//            return null;
//        }
//        try {
//            PreparedStatement serviceTransactionStatement;
//            serviceTransactionStatement = conn.prepareStatement(getServiceRecordBlank());
//            serviceTransactionStatement.setInt(1, (service.getId() == -1 ? getServiceSeq() : service.getId()));
//            if (isNull(service.getLatitude()) || service.getLatitude().isNaN()) {
//                serviceTransactionStatement.setNull(2, java.sql.Types.DOUBLE);
//            } else {
//                serviceTransactionStatement.setBigDecimal(2, BigDecimal.valueOf(service.getLatitude()));
//            }
//            if (isNull(service.getLongitude()) || service.getLongitude().isNaN()) {
//                serviceTransactionStatement.setNull(3, java.sql.Types.DOUBLE);
//            } else {
//                serviceTransactionStatement.setBigDecimal(3, BigDecimal.valueOf(service.getLongitude()));
//            }
//
//            serviceTransactionStatement.setString(4, generateProperTimeValueForStatus(service.getGpsTime()));
//            serviceTransactionStatement.setString(5, AvType.A.toString());
//            if (isNull(service.getCourse()) || service.getCourse().isNaN()) {
//                serviceTransactionStatement.setNull(6, java.sql.Types.DOUBLE);
//            } else {
//                serviceTransactionStatement.setBigDecimal(6, BigDecimal.valueOf(service.getCourse()));
//            }
//            if (isNull(service.getSpeed()) || service.getSpeed().isNaN()) {
//                serviceTransactionStatement.setNull(7, java.sql.Types.DOUBLE);
//            } else {
//                serviceTransactionStatement.setBigDecimal(7, BigDecimal.valueOf(service.getSpeed()));
//            }
//
//            serviceTransactionStatement.setInt(8, service.getDbm());
//            if (isNull(service.getBaseStationId())) {
//                serviceTransactionStatement.setNull(9, java.sql.Types.DOUBLE);
//            } else {
//                serviceTransactionStatement.setBigDecimal(9, BigDecimal.valueOf(service.getBaseStationId()));
//            }
//            serviceTransactionStatement.setInt(10, Integer.parseInt(requestForms[service.getSended().ordinal()]));
//            return serviceTransactionStatement;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR ENCOUNTERED DURING SYSTEM SERVICE FORMATING! - " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//
//            return null;
//        }
//    }
//
//    private PreparedStatement prepareSystemInfoTransaction(SysInfo sysInfo) {
//
//        Log4jService.writeInfo(sysInfo.toString(), H2localDb.class.getName());
//
//        try {
//            PreparedStatement systemTransactionStatement;
//            systemTransactionStatement = conn.prepareStatement(getSystemInfoRecordBlank());
//            systemTransactionStatement.setString(1, sysInfo.getLocale().toString());
//
//            return systemTransactionStatement;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR ENCOUNTERED DURING SYSTEM INFO FORMATING! - " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//
//            return null;
//        }
//    }
//
//    private PreparedStatement prepareReportsTransaction(ErrorReport errorReport) {
//
//        Log4jService.writeInfo(errorReport.toString(), H2localDb.class.getName());
//
//        try {
//            PreparedStatement reportTransactionStatement;
//            reportTransactionStatement = conn.prepareStatement(getReportRecordBlank());
//            reportTransactionStatement.setInt(1, (errorReport.getId() != -1 ? errorReport.getId() : getReportSeq()));
//            reportTransactionStatement.setString(2, errorReport.getMail());
//            reportTransactionStatement.setString(3, errorReport.getSubject());
//            reportTransactionStatement.setString(4, errorReport.getMessage());
//            reportTransactionStatement.setString(5, generateProperTimeValueForStatus(errorReport.getEventTime()));
//            reportTransactionStatement.setString(6, errorReport.getPathToFile());
//            reportTransactionStatement.setInt(7, Integer.parseInt(requestForms[errorReport.getSending().ordinal()]));
//
//            return reportTransactionStatement;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR ENCOUNTERED DURING REPORT FORMATING! - " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//
//            return null;
//        }
//    }
//
//    private boolean isHaveAllNeccesaryValues(Service service) {
//        return !String.valueOf(service.getLatitude()).isEmpty() && !String.valueOf(service.getLongitude()).isEmpty();
//    }
//
//    private String generateProperTimeValueForStatus(Date time) {
//
//        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date newDate = Calendar.getInstance().getTime();
//        newDate.setHours(time.getHours());
//        newDate.setMinutes(time.getMinutes());
//        newDate.setSeconds(time.getSeconds());
//        String currentTime = sdf.format(newDate);
//        return currentTime;
//    }
//
//    private boolean isNotNull(Object object) {
//        return object != null;
//    }
//
//    private void removeOldStatusFromDb(List<SdsStatus> eventStatusList) {
//        if (eventStatusList.size() > 0) {
//            for (SdsStatus status : eventStatusList) {
//                String sqlStatement = "DELETE FROM " + sdsStatusTableName + " WHERE ID = '" + status.getId() + "'";
//                if (checkIfExist(sdsStatusTableName)) {
//                    Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//                    tryToExecuteUpdateInLocalDb(sqlStatement);
//                }
//            }
//            Log4jService.writeInfo("DELETE FROM " + sdsStatusTableName + " WHERE ID = '" + eventStatusList.get(0).getId() + "'", H2localDb.class.getName());
//            tryToExecuteUpdateInLocalDb("DELETE FROM " + sdsStatusTableName + " WHERE ID <= " + eventStatusList.get(0).getId());
//        }
//    }
//
//    private void removeOldInfoFromDb(List<SdsInfo> infoList) {
//        for (SdsInfo info : infoList) {
//            String sqlStatement = "DELETE FROM " + sdsInfoTableName + " WHERE ID = '" + info.getId() + "'";
//            if (checkIfExist(sdsInfoTableName)) {
//                Log4jService.writeInfo(sqlStatement, H2localDb.class.getName());
//                tryToExecuteUpdateInLocalDb(sqlStatement);
//            }
//        }
//    }
//
//    private Long parseInfoIdResultSetInformation(ResultSet rs) throws SQLException {
//        Long id = null;
//        while (rs.next()) {
//            id = rs.getLong("ID");
//        }
//
//        return id;
//    }
//
//    private RequestForm convertFromNumberToRequest(Integer request) {
//        return (request == 100 ? RequestForm.PENDING : request == 200 ? RequestForm.SENT : RequestForm.UNSENT);
//    }
//
//    private Boolean insertToDB(PreparedStatement statement) {
//        if (!isNull(statement)) {
//            return insertData(statement);
//        }
//        return Boolean.FALSE;
//    }
//
//    public boolean checkIfExist(String tableName) {
//        try {
//            return (conn.getMetaData().getTables(null, null, tableName, null).next()) ? Boolean.TRUE : Boolean.FALSE;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR WHILE CHECKING IF " + tableName + " EXIST " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//        }
//        return Boolean.FALSE;
//    }
//
//    private List<SdsStatus> parseStatusResultSetInformation(ResultSet rs) throws SQLException {
//        List<SdsStatus> result = new ArrayList<>();
//        while (rs.next()) {
//            SdsStatus dbResult = new SdsStatus();
//            dbResult.setId(rs.getInt("ID"));
//            dbResult.setEventId(rs.getLong("EVENT_ID"));
//            dbResult.setMissionId(rs.getInt("MISSION_ID"));
//            dbResult.setState(convertFromNumberToRequest(rs.getInt(sendingState)));
//            dbResult.setStatusNumber(StatusState.valueOf(rs.getString("STATUS")));
//            dbResult.setStatusTime(rs.getTimestamp("EVENT_TIME"));
//            result.add(dbResult);
//        }
//
//        return result.isEmpty() ? null : result;
//    }
//
//    private List<Sds> parseSdsResultSetInformation(ResultSet rs) throws SQLException {
//        List<Sds> result = new ArrayList<>();
//        while (rs.next()) {
//            result.add(new Sds(rs.getLong("EVENT_ID"), rs.getInt("MISSION_ID"), rs.getTimestamp("RECEIVE_TIME")));
//        }
//        return result.isEmpty() ? null : result;
//
//    }
//
//    private List<SdsInfo> parseInfoResultSetInformation(ResultSet rs) throws SQLException {
//        List<SdsInfo> result = new ArrayList<>();
//        while (rs.next()) {
//            SdsInfo infoResult = new SdsInfo();
//            infoResult.setId((Integer) rs.getInt("ID"));
//            infoResult.setEventId(rs.getLong("EVENT_ID"));
//            infoResult.setMissionId(rs.getInt("MISSION_ID"));
//            infoResult.setPatientId(rs.getInt("PATIENT_ID"));
//            infoResult.setDestination(new Destination(rs.getDouble("LONGITUDE"), rs.getDouble("LATITUDE"), rs.getString("ADDRESS")));
//            infoResult.setCrossingStreet(rs.getString("CROSSING_STREET"));
//            infoResult.setLocationNotes(rs.getString("LOCATION_NOTES"));
//            infoResult.setPhoneNumbers(rs.getString("PHONE_NO"));
//            infoResult.setDescription(rs.getString("DESCRIPTION"));
//            infoResult.setLightsOn(rs.getString("LIGHTS"));
//            infoResult.setGenderIdentity(rs.getString("GENDER_IDENTITY"));
//            infoResult.setConscious(rs.getString("CONSCIOUS"));
//            infoResult.setBreathing(rs.getString("BREATHING"));
//            infoResult.setSymptom(rs.getString("SYMPTOM"));
//            infoResult.setReceiveTime(rs.getTimestamp("RECEIVE_TIME"));
//            infoResult.setAssignmentTime(rs.getTimestamp("ASSIGN_TIME"));
//            infoResult.setDeadlineTime((Integer) rs.getInt("DEADLINE_TIME"));
//            infoResult.setEventCategory((Integer) rs.getInt("CATEGORY"));
//            infoResult.setDispatcherId(rs.getString("DISPECHERID"));
//            infoResult.setSource(rs.getString("SOURCE"));
//            infoResult.setCreatedAt(rs.getTimestamp("CREATED_AT"));
//            result.add(infoResult);
//        }
//        return result.isEmpty() ? null : result;
//    }
//
//    private List<Service> parseServiceResultSetInformation(ResultSet rs) throws SQLException {
//        List<Service> result = new ArrayList<>();
//        while (rs.next()) {
//            Service serviceResult = new Service();
//            serviceResult.setId((Integer) rs.getInt("ID"));
//            serviceResult.setLatitude(rs.getDouble("LATITUDE"));
//            serviceResult.setLongitude(rs.getDouble("LONGITUDE"));
//            serviceResult.setGpsTime(rs.getTimestamp("GPS_TIME"));
//            serviceResult.setaV(AvType.fromValue(rs.getString("A_V")));
//            serviceResult.setCourse(rs.getDouble("COURSE"));
//            serviceResult.setSpeed(rs.getDouble("SPEED"));
//            serviceResult.setDbm(rs.getInt("DBM"));
//            serviceResult.setBaseStationId(rs.getInt("BASE_STATION_ID"));
//            serviceResult.setSended(convertFromNumberToRequest(rs.getInt(sendingState)));
//            result.add(serviceResult);
//        }
//        return result.isEmpty() ? null : result;
//    }
//
//    private List<ErrorReport> parseReportResultSetInformation(ResultSet rs) throws SQLException {
//        List<ErrorReport> result = new ArrayList<>();
//        while (rs.next()) {
//            ErrorReport reportResult = new ErrorReport();
//            reportResult.setId((Integer) rs.getInt("ID"));
//            reportResult.setMail(rs.getString("MAIL"));
//            reportResult.setSubject(rs.getString("SUBJECT"));
//            reportResult.setMessage(rs.getString("MESSAGE"));
//            reportResult.setEventTime(rs.getTimestamp("REPORT_TIME"));
//            reportResult.setFilePath(rs.getString("PATH_TO_FILE"));
//            reportResult.setSending(convertFromNumberToRequest(rs.getInt(sendingState)));
//
//            result.add(reportResult);
//        }
//        return result.isEmpty() ? null : result;
//    }
//
//    private Boolean insertData(PreparedStatement statement) {
//        Log4jService.writeInfo("INSERTING STATEMENT INTO DB " + statement.toString(), sendingState);
//        Boolean status = Boolean.FALSE;
//        try {
//            conn.setAutoCommit(Boolean.FALSE);
//            statement.executeUpdate();
//            conn.commit();
//            conn.setAutoCommit(Boolean.TRUE);
//            status = Boolean.TRUE;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR ENCOUNTERED DURING DATA INSERTION! - " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            try {
//                conn.rollback();
//            } catch (SQLException ex1) {
//                Log4jService.writeFatalError("ERROR ENCOUNTERED DURING ROLLBACK! - " + Log4jService.getStackTrace(ex1), H2localDb.class.getName());
//            }
//        }
//        return status;
//    }
//
//    private boolean createNewServiceTable() {
//        Log4jService.writeInfo(getServiceSequanceProperties() + " " + getServiceTableProperties(), H2localDb.class.getName());
//        try {
//            try {
//                createNewStatement().execute(getServiceSequanceProperties());
//            } catch (SQLException ex1) {
//                Log4jService.writeError("EXCEPTION WAS ENCOUNTERED WHILE CREATING STATUS SEQUENCE -" + Log4jService.getStackTrace(ex1), H2localDb.class.getName());
//            }
//            createNewStatement().execute(getServiceTableProperties());
//            return Boolean.TRUE;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR WHILE CREATING " + gpsServiceTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//        }
//        return Boolean.FALSE;
//    }
//
//    private boolean createNewReportTable() {
//        Log4jService.writeInfo(getReportSequanceProperties() + " " + getReportTableProperties(), H2localDb.class.getName());
//        try {
//            try {
//                createNewStatement().execute(getReportSequanceProperties());
//            } catch (SQLException ex1) {
//                Log4jService.writeError("EXCEPTION WAS ENCOUNTERED WHILE CREATING REPORT SEQUENCE -" + Log4jService.getStackTrace(ex1), H2localDb.class.getName());
//            }
//            createNewStatement().execute(getReportTableProperties());
//            return Boolean.TRUE;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR WHILE CREATING " + reportsTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//        }
//        return Boolean.FALSE;
//    }
//
//    private boolean createNewInfoTable() {
//        Log4jService.writeInfo(getInfoSequanceProperties() + " " + getSdsInfoTableProperties(), H2localDb.class.getName());
//        try {
//            try {
//                createNewStatement().execute(getInfoSequanceProperties());
//            } catch (SQLException ex1) {
//                Log4jService.writeError("EXCEPTION WAS ENCOUNTERED WHILE CREATING STATUS SEQUENCE -" + Log4jService.getStackTrace(ex1), H2localDb.class.getName());
//            }
//            createNewStatement().execute(getSdsInfoTableProperties());
//            return Boolean.TRUE;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR WHILE CREATING " + sdsInfoTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//        }
//        return Boolean.FALSE;
//    }
//
//    private boolean createNewStatusTable() {
//        Log4jService.writeInfo(getStatusSequanceProperties() + " " + getSdsStatusTableProperties(), H2localDb.class.getName());
//        try {
//            try {
//                createNewStatement().execute(getStatusSequanceProperties());
//            } catch (SQLException ex1) {
//                Log4jService.writeError("EXCEPTION WAS ENCOUNTERED WHILE CREATING STATUS SEQUENCE -" + Log4jService.getStackTrace(ex1), H2localDb.class.getName());
//            }
//
//            createNewStatement().execute(getSdsStatusTableProperties());
//            return Boolean.TRUE;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR WHILE CREATING " + sdsStatusTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//        }
//        return Boolean.FALSE;
//    }
//
//    private boolean createNewSysInfoTable() {
//        Log4jService.writeInfo(getSystemInfoTableProperties(), H2localDb.class.getName());
//        try {
//            createNewStatement().execute(getSystemInfoTableProperties());
//            return Boolean.TRUE;
//        } catch (SQLException ex) {
//            Log4jService.writeFatalError("ERROR WHILE CREATING " + systemInformationTableName + " TABLE " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//        }
//        return Boolean.FALSE;
//    }
//
//    private void connectToDB(String type, String name, String param) {
//        if (checkIfDriverExist()) {
//            try {
//                conn = DriverManager.getConnection(param.isEmpty() ? type+name : type+name+param);
//            } catch (SQLException ex) {
//                File f = new File(name+".mv.db");
//                if(f.exists())
//                    f.delete();
//                connectToDB(type, name, param);
//                Log4jService.writeError("CAN'T MAKE CONNCETION TO LOCALDB H2 " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            }
//        }
//    }
//    
//    private Boolean checkIfDriverExist() {
//        try {
//            Class.forName("org.h2.Driver");
//        } catch (ClassNotFoundException ex) {
//            Log4jService.writeFatalError("NO DRIVER FOUND FOR LOCALDB H2 " + Log4jService.getStackTrace(ex), H2localDb.class.getName());
//            return Boolean.FALSE;
//        }
//        return Boolean.TRUE;
//    }
//
//    private Statement createNewStatement() throws SQLException {
//        return conn.createStatement();
//    }
}
