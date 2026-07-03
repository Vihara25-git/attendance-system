import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {

        try {

            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3308/traineeDB",
                    "root",
                    "root"
            );

            System.out.println("Database Connected");

            BufferedReader br = new BufferedReader(new FileReader("attendance.csv"));
            br.readLine(); // skip header

            String line;

            Set<Integer> attendanceList = new HashSet<>();

            int leaveID = 1;

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm:ss");

            while ((line = br.readLine()) != null) {

                String[] data = line.split(",");

                int attendanceID = Integer.parseInt(data[0].trim());
                int traineeID = Integer.parseInt(data[1].trim());

                LocalTime timeIn = LocalTime.parse(data[2].trim(), timeFormatter);
                LocalTime timeOut = LocalTime.parse(data[3].trim(), timeFormatter);

                String[] d = data[4].trim().split("/");

                String sqlDateString =
                        d[2] + "-" +
                                String.format("%02d", Integer.parseInt(d[0])) + "-" +
                                String.format("%02d", Integer.parseInt(d[1]));

                Date sqlDate = Date.valueOf(sqlDateString);

                attendanceList.add(traineeID);

                // INSERT ATTENDANCE
                PreparedStatement checkAttendance = con.prepareStatement(
                        "SELECT * FROM attendance WHERE traineeID=? AND date=?"
                );

                checkAttendance.setInt(1, traineeID);
                checkAttendance.setDate(2, sqlDate);

                ResultSet rs = checkAttendance.executeQuery();

                if (!rs.next()) {

                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO attendance(attendanceID,traineeID,timeIn,timeOut,date) VALUES(?,?,?,?,?)"
                    );

                    ps.setInt(1, attendanceID);
                    ps.setInt(2, traineeID);
                    ps.setTime(3, Time.valueOf(timeIn));
                    ps.setTime(4, Time.valueOf(timeOut));
                    ps.setDate(5, sqlDate);

                    ps.executeUpdate();
                }

                // CALCULATE WORK HOURS
                long hours = Duration.between(timeIn, timeOut).toMinutes();
                System.out.println("Hours:- "+hours);
                String leaveType = "";

                if (hours < 240) {
                    leaveType = "Short Leave";
                } else if (hours >= 240 && hours < 360) {
                    leaveType = "Half Day";
                }else{
                    leaveType = "Present";
                }

                // INSERT LEAVE
                if (!leaveType.equals("Present")) {

                    PreparedStatement checkLeave = con.prepareStatement(
                            "SELECT * FROM trainee_leave WHERE traineeID=? AND leave_date=?"
                    );

                    checkLeave.setInt(1, traineeID);
                    checkLeave.setDate(2, sqlDate);

                    ResultSet lr = checkLeave.executeQuery();

                    if (!lr.next()) {

                        PreparedStatement ps2 = con.prepareStatement(
                                "INSERT INTO trainee_leave(leaveID,traineeID,leave_date,leave_type) VALUES(?,?,?,?)"
                        );

                        ps2.setInt(1, leaveID++);
                        ps2.setInt(2, traineeID);
                        ps2.setDate(3, sqlDate);
                        ps2.setString(4, leaveType);

                        ps2.executeUpdate();
                    }
                }
            }

            br.close();

            // ABSENT CHECK
            Statement st = con.createStatement();

            ResultSet trainees = st.executeQuery("SELECT traineeID FROM trainee");

            while (trainees.next()) {

                int traineeID = trainees.getInt("traineeID");

                if (!attendanceList.contains(traineeID)) {

                    Date today = Date.valueOf(LocalDate.now());

                    PreparedStatement ps = con.prepareStatement(
                            "INSERT INTO trainee_leave(leaveID,traineeID,leave_date,leave_type) VALUES(?,?,?,?)"
                    );

                    ps.setInt(1, leaveID++);
                    ps.setInt(2, traineeID);
                    ps.setDate(3, today);
                    ps.setString(4, "Full Day");

                    ps.executeUpdate();
                }
            }

            con.close();

            System.out.println("Attendance Processing Completed Successfully");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}