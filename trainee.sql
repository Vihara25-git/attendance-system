create database traineeDB;
use traineeDB;

create table trainee (
traineeID int primary key,
fullName varchar(50) not null,
address varchar(50),
contactNo int
);

create table attendance(
attendanceID int primary key,
traineeID int,
timeIn time,
timeOut time,
date DATE,
foreign key (traineeID) references trainee(traineeID)
);

create table trainee_leave(
leaveID int primary key,
traineeID int,
leave_date DATE,
leave_type varchar(20),
foreign key (traineeID) references trainee(traineeID)
);

INSERT INTO trainee (traineeID, fullName, address, contactNo) VALUES
(1, 'Nimal Perera', 'Colombo', 771234567),
(2, 'Kamal Silva', 'Kandy', 712345678),
(3, 'Saman Kumara', 'Galle', 781234567),
(4, 'Amila Fernando', 'Kurunegala', 761234567),
(5, 'Kasuni Madushani', 'Matara', 751234567),
(6, 'Dilani Perera', 'Jaffna', 701234567),
(7, 'Ruwan Jayasinghe', 'Anuradhapura', 781112233),
(8, 'Tharindu Lakshan', 'Negombo', 772223344),
(9, 'Sachini Fernando', 'Badulla', 755556666),
(10, 'Isuru Bandara', 'Ratnapura', 719998877);

select * from trainee_leave;

truncate table trainee;

DELETE FROM trainee;

TRUNCATE TABLE trainee;

TRUNCATE TABLE trainee_leave;

select * from trainee_leave;

TRUNCATE TABLE attendance;

TRUNCATE TABLE trainee;

SET FOREIGN_KEY_CHECKS = 0;

TRUNCATE TABLE trainee;

SET FOREIGN_KEY_CHECKS = 1;


drop table trainee;

INSERT INTO trainee (traineeID, fullName, address, contactNo) VALUES
(1, 'Nimal Perera', 'Colombo', 771234567),
(2, 'Kamal Silva', 'Kandy', 712345678),
(3, 'Saman Kumara', 'Galle', 781234567),
(4, 'Amila Fernando', 'Kurunegala', 761234567),
(5, 'Kasuni Madushani', 'Matara', 751234567),
(6, 'Dilani Perera', 'Jaffna', 701234567),
(7, 'Ruwan Jayasinghe', 'Anuradhapura', 781112233),
(8, 'Tharindu Lakshan', 'Negombo', 772223344),
(9, 'Sachini Fernando', 'Badulla', 755556666),
(10, 'Isuru Bandara', 'Ratnapura', 719998877),
(11, 'Ashan Perera', 'Kalutara', 771112234),
(12, 'Nadeesha Silva', 'Hambantota', 772223345),
(13, 'Supun Fernando', 'Monaragala', 773334456),
(14, 'Kavindi Senanayake', 'Polonnaruwa', 774445567),
(15, 'Madhusha Jayawardena', 'Trincomalee', 775556678);

select * from trainee;
