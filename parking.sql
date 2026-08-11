-- Database: parking

-- DROP DATABASE IF EXISTS parking;
--drop table if exists parking_ticket;
--drop table if exists spot;
--drop table if exists vehicle;
--drop table if exists branch;

create table branch (
	branchID serial primary key,
	location varchar(50) not null
);
create table vehicle (
	vehicleID serial primary key,
	licencePlate varchar(32),
	Unique(licencePlate),
	type varchar(20) not null
);
create table spot (
	branchID int,
	spotID serial primary key,
	spotNumber int not null,
	type varchar(20) not null,
	isAvailable bool not null default true,
	UNIQUE (branchID, spotNumber),
	foreign key(branchID) references branch(branchID)
);
create table parking_ticket (
	ticketID serial primary key,
	vehicleID int not null,
	spotID int not null,
	fee numeric(10,2) not null,
	entryTime timestamp not null,
	exitTime timestamp,
	foreign key(spotID) references spot(spotID),
	foreign key(vehicleID) references vehicle(vehicleID)
);

-- =========================
-- BRANCHES
-- =========================

INSERT INTO branch (location) VALUES
('Ramallah'),
('Nablus'),
('Bethlehem');


-- =========================
-- VEHICLES
-- =========================

INSERT INTO vehicle (licencePlate, type) VALUES
('12-345-67', 'CAR'),
('23-456-78', 'CAR'),
('34-567-89', 'CAR'),
('45-678-90', 'MOTORCYCLE'),
('56-789-01', 'MOTORCYCLE'),
('67-890-12', 'TRUCK'),
('78-901-23', 'TRUCK'),
('89-012-34', 'BUS');


-- =========================
-- SPOTS
-- =========================


INSERT INTO spot (branchID, spotNumber, type, isAvailable) VALUES
(1, 1, 'CAR', true),
(1, 2, 'CAR', false),
(1, 3, 'CAR', true),
(1, 4, 'MOTORCYCLE', true),
(1, 5, 'TRUCK', true),
(2, 1, 'CAR', true),
(2, 2, 'CAR', false),
(2, 3, 'MOTORCYCLE', true),
(2, 4, 'TRUCK', true),
(2, 5, 'BUS', true),
(3, 1, 'CAR', true),
(3, 2, 'CAR', true),
(3, 3, 'MOTORCYCLE', true),
(3, 4, 'TRUCK', false),
(3, 5, 'BUS', true);

-- =========================
-- PARKING TICKETS
-- =========================

-- Completed tickets
INSERT INTO parking_ticket
(vehicleID, spotID, fee, entryTime, exitTime) VALUES
(1, 1, 8.00, '2026-08-10 07:30:00', '2026-08-10 09:30:00'),
(3, 3, 12.00, '2026-08-10 08:00:00', '2026-08-10 11:00:00'),
(4, 4, 5.00, '2026-08-10 09:00:00', '2026-08-10 10:30:00'),
(5, 8, 6.00, '2026-08-10 10:15:00', '2026-08-10 12:00:00'),
(7, 13, 30.00, '2026-08-10 06:30:00', '2026-08-10 10:30:00');


-- =========================
-- ONGOING PARKING
-- exitTime = NULL
-- =========================

INSERT INTO parking_ticket
(vehicleID, spotID, fee, entryTime, exitTime) VALUES
(2, 2, 10.00, '2026-08-11 08:30:00', NULL),
(6, 9, 25.00, '2026-08-11 09:15:00', NULL),
(8, 14, 20.00, '2026-08-11 10:00:00', NULL);


