package com.masi.employee.service;

import com.masi.employee.domain.EmployeeProfile;
import com.masi.employee.domain.TimeKeeping;
import com.masi.employee.domain.enumeration.TimeKeepingType;
import com.masi.employee.repository.EmployeeProfileRepository;
import com.masi.employee.service.dto.EmployeeProfileDTO;
import com.masi.employee.service.dto.TimeKeepingRecordDTO;
import com.masi.employee.web.rest.errors.BadRequestAlertException;
import com.masi.employee.service.dto.ZkbioTimeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.sql.*;
import java.time.Duration;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Service
@Transactional
public class ZkbioTimeService {

    private static final Logger log = LoggerFactory.getLogger(ZkbioTimeService.class);
    private static final String URL_DEFAULT = "jdbc:postgresql://192.168.1.19:5432/biotime";
    private static final String USER_DEFAULT = "basso";
    private static final String PASSWORD_DEFAULT = "123123";
    private static final ZonedDateTime TIME_DEFAULT = ZonedDateTime.now(ZoneId.of("UTC"));

    private final TimeKeepingRecordService timeKeepingRecordService;
    private final EmployeeProfileRepository employeeProfileRepository;

    public ZkbioTimeService(TimeKeepingRecordService timeKeepingRecordService, EmployeeProfileRepository employeeProfileRepository) {
        this.timeKeepingRecordService = timeKeepingRecordService;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    public Mono<Void> saveTimeKeeping(String urlDb, String userDb, String pwDb, ZonedDateTime timeStart, ZonedDateTime timeEnd) {
        log.info("Save data time keeping to db");

        // Sử dụng giá trị mặc định nếu không cung cấp
        if (urlDb == null || urlDb.isEmpty()) {
            urlDb = URL_DEFAULT;
        }
        if (userDb == null || userDb.isEmpty()) {
            userDb = USER_DEFAULT;
        }
        if (pwDb == null || pwDb.isEmpty()) {
            pwDb = PASSWORD_DEFAULT;
        }
        if (timeStart == null) {
            timeStart = TIME_DEFAULT;
        }
        if (timeEnd == null) {
            timeEnd = TIME_DEFAULT;
        }
        timeStart = ZonedDateTime.of(timeStart.getYear(), timeStart.getMonthValue(), timeStart.getDayOfMonth(), 0, 0, 0, 0, ZoneId.of("UTC"));
        timeEnd = ZonedDateTime.of(timeEnd.getYear(), timeEnd.getMonthValue(), timeEnd.getDayOfMonth(), 23, 59, 59, 999999999, ZoneId.of("UTC"));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        String formattedTimeStart = timeStart.plusHours(7).toLocalDateTime().format(formatter);
//        String formattedTimeEnd = timeEnd.plusHours(7).toLocalDateTime().format(formatter);

        String formattedTimeStart = timeStart.toLocalDateTime().format(formatter);
        String formattedTimeEnd = timeEnd.toLocalDateTime().format(formatter);
        formattedTimeStart = formattedTimeStart + "+00";
        formattedTimeEnd = formattedTimeEnd + "+00";

        String SQL_SELECT =
                "SELECT t1.* " +
                        "FROM iclock_transaction t1 " +
                        "INNER JOIN ( " +
                        "    SELECT emp_code, MAX(punch_time) AS max_punch_time " +
                        "    FROM iclock_transaction " +
                        "    WHERE punch_time >= '" + formattedTimeStart + "' " +
                        "      AND punch_time <= '" + formattedTimeEnd + "' " +
                        "      AND emp_code != '' AND emp_code IS NOT NULL " +
                        "    GROUP BY emp_code " +
                        ") t2 ON t1.emp_code = t2.emp_code AND t1.punch_time = t2.max_punch_time";


        System.out.println(SQL_SELECT + "\n");
        String finalUserDb = userDb;
        String finalUrlDb = urlDb;
        String finalPwDb = pwDb;
        String finalFormattedTimeEnd = formattedTimeEnd;
        String finalFormattedTimeStart = formattedTimeStart;
        return Mono.fromCallable(() -> {
            try (Connection connection = DriverManager.getConnection(finalUrlDb, finalUserDb, finalPwDb)) {
                log.info("Kết nối thành công với cơ sở dữ liệu!");
                PreparedStatement selectStmt = connection.prepareStatement(SQL_SELECT);
                ResultSet rs = selectStmt.executeQuery();

                Map<String, Collection<ZkbioTimeDto>> dataMap = new HashMap<>();

                while (rs.next()) {
                    ZkbioTimeDto dto = new ZkbioTimeDto();
                    dto.setId(rs.getString("id"));
                    dto.setPin(rs.getString("emp_code"));
//                    dto.setReaderName(rs.getString("reader_name"));
                    dto.setEventTime(rs.getTimestamp("punch_time")
                            .toLocalDateTime()
                            .atZone(ZoneId.of("UTC"))
                            .minusHours(7));
                    dataMap.computeIfAbsent(dto.getPin(), k -> new ArrayList<>()).add(dto);
                }
                log.info("Dữ liệu đã lấy thành công!");

                rs.close();
                selectStmt.close();
                return dataMap;
            } catch (SQLException e) {
                log.error("Lỗi khi thao tác với cơ sở dữ liệu", e);
                throw new RuntimeException(e);
            }
        }).flatMap(dataMap -> {
            Collection<String> pins = dataMap.keySet();
            if (pins == null || pins.isEmpty()) {
                log.info("Không có dữ liệu nào để lưu!");
                return Mono.empty();
            }

            return employeeProfileRepository.findByPinInAndTimeKeeping(pins, finalFormattedTimeStart, finalFormattedTimeEnd)
                    .collectList()
                    .flatMap(employeeProfiles -> {
                        if (employeeProfiles.isEmpty()) {
                            log.info("Không có dữ liệu nhân viên nào để lưu!");
                            return Mono.empty();
                        }
                        log.info("Có dữ liệu nhân viên để lưu!");
                        employeeProfiles.forEach(employeeProfile -> {
                            Collection<ZkbioTimeDto> data = dataMap.get(employeeProfile.getPin());
                            if (data != null) {
                                data.forEach(dto -> {
                                    try{
                                        log.info("CheckIn {} ",employeeProfile.getCheckIn().toString());
                                        log.info("CheckIn {} ",dto.getEventTime().toString());
                                        if ( employeeProfile.getCheckIn().equals(dto.getEventTime())) {
                                            log.info("Nhân viên {} bị lặp checkin {}", employeeProfile.getPin(), dto.getEventTime());
                                        }
                                        else {
                                            log.info("Nhân viên {} checkout vào lúc {}", employeeProfile.getPin(), dto.getEventTime());
                                            dto.setEmployeeId(employeeProfile.getId());
                                        }
                                    }catch (Exception e){
                                        log.info("Nhân viên {} checkin vào lúc {}", employeeProfile.getPin(), dto.getEventTime());
                                        dto.setEmployeeId(employeeProfile.getId());
                                    }

                                });
                            }
                        });


                        dataMap.values().forEach(collection -> {
                            collection.removeIf(dto -> dto.getEmployeeId() == null);
                        });

                        List<Mono<Void>> saveOperations = dataMap.values().stream()
                                .flatMap(Collection::stream)
                                .map(dto -> {
                                    TimeKeepingRecordDTO timeKeepingRecordDTO = new TimeKeepingRecordDTO();
                                    timeKeepingRecordDTO.setId(UUID.randomUUID());
                                    timeKeepingRecordDTO.setEmployeeId(dto.getEmployeeId());
                                    timeKeepingRecordDTO.setCheckIn(dto.getEventTime());
//                                    timeKeepingRecordDTO.setTimeKeepingType(TimeKeepingType.HOUR);
                                    return timeKeepingRecordService.save(timeKeepingRecordDTO).then();
                                })
                                .collect(Collectors.toList());

                        return Mono.when(saveOperations);
                    });
        });
    }


    public Mono<Void> updateNameZkbioTime(ZonedDateTime timeStart, ZonedDateTime timeEnd) {
        log.info("Update name ZkbioTime");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        String timeStartFormat = timeStart.format(formatter);
        String timeEndFormat = timeEnd.format(formatter);

        return employeeProfileRepository.findAllByLastUpdate(timeStartFormat, timeEndFormat)
                .collectList()
                .flatMap(employeeProfiles -> {
                    if (employeeProfiles.isEmpty()) {
                        log.info("Không có dữ liệu nhân viên nào để cập nhật!");
                        return Mono.empty();
                    }

                    String SQL_UPDATE = "UPDATE acc_transaction SET name = ? WHERE pin = ?";
                    return Mono.fromCallable(() -> {
                        try (Connection conn = DriverManager.getConnection(URL_DEFAULT, USER_DEFAULT, PASSWORD_DEFAULT)) {
                            log.info("Kết nối thành công với cơ sở dữ liệu!");
                            for (EmployeeProfile employee : employeeProfiles) {
                                try (PreparedStatement updateStmt = conn.prepareStatement(SQL_UPDATE)) {
                                    updateStmt.setString(1, employee.getFullName()); // Đặt tên
                                    updateStmt.setString(2, employee.getPin());  // Đặt pin
                                    int affectedRows = updateStmt.executeUpdate();
                                    log.info("Đã cập nhật {} hàng cho pin {}", affectedRows, employee.getPin());
                                }
                            }
                            return employeeProfiles;
                        } catch (SQLException e) {
                            log.error("Lỗi khi thao tác với cơ sở dữ liệu", e);
                            throw new RuntimeException(e);
                        }
                    }).then();
                });
    }

    ///////////////////////////////////////////////////////////////////
//
//    public static void main(String[] args) {
//        final String url = "http://192.168.1.51:8087";
//        String param = buildParams("admin", "Abc@1234");
//        String result = getBioToken(url, param);
//        System.out.println(result);
//        String user = createEmployeeBio(url, result);
//        System.out.println( user);
//    }
//
//    public static String createEmployeeBio(String httpUrl, String token) {
//        httpUrl = httpUrl + "/personnel/api/employees/";
//        System.out.println(httpUrl);
//
//
//        HttpURLConnection connection = null;
//        InputStream is = null;
//        OutputStream os = null;
//        BufferedReader br = null;
//        String result = null;
//
//        String requestBody = "{\"emp_code\": \"111111111\", \"department\": 1, \"area\": [1, 2]}";
//
//        try {
//            URL url = new URL(httpUrl);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("Content-Type", "application/json");
//            connection.setRequestProperty("Authorization", "Token " + token);
//            connection.setDoOutput(true);
//
//            os = connection.getOutputStream();
//            os.write(requestBody.getBytes());
//            os.flush();
//
//            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
//                br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//                StringBuilder response = new StringBuilder();
//                String line;
//                while ((line = br.readLine()) != null) {
//                    response.append(line);
//                }
//                result = response.toString();
//            } else {
//                throw new RuntimeException("Failed : HTTP error code : " + connection.getResponseCode());
//            }
//        } catch (Exception e) {
//            log.error("Error during HTTP request: {}", e.getMessage());
//            throw new RuntimeException(e);
//        } finally {
//            try {
//                if (os != null) os.close();
//                if (br != null) br.close();
//                if (connection != null) connection.disconnect();
//            } catch (IOException e) {
//                log.error("Error closing resources: {}", e.getMessage());
//            }
//        }
//        return result;
//    }
//
//
//
//    public static String buildParams(String username, String password) {
//        String tmp = "{\"username\": \"" + username + "\"," +
//                " \"password\": \"" + password + "\"}";
//        return tmp;
//    }
//
//    public static String getBioToken(String httpUrl, String param) {
//        httpUrl = httpUrl + "/api-token-auth/";
//        System.out.println(httpUrl);
//        HttpURLConnection connection = null;
//        InputStream is = null;
//        OutputStream os = null;
//        BufferedReader br = null;
//        String result = null;
//        try {
//            URL url = new URL(httpUrl);
//            connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("POST");
//            connection.setConnectTimeout(15000);
//            connection.setReadTimeout(60000);
//
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            connection.setRequestProperty("Content-Type", "application/json");
//            os = connection.getOutputStream();
//            os.write(param.getBytes());
//            if (connection.getResponseCode() == 200) {
//
//                is = connection.getInputStream();
//                br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//
//                StringBuffer sbf = new StringBuffer();
//                String temp = null;
//                while ((temp = br.readLine()) != null) {
//                    sbf.append(temp);
//                    sbf.append("\r\n");
//                }
//                result = sbf.toString();
//            }
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (null != br) {
//                try {
//                    br.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (null != os) {
//                try {
//                    os.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            if (null != is) {
//                try {
//                    is.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            connection.disconnect();
//        }
//        int start = result.indexOf(":\"") + 2;
//        int end = result.indexOf("\"", start);
//
//        return result.substring(start, end);
//    }


}
