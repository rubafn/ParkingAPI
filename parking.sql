-- Database: parking

-- DROP DATABASE IF EXISTS parking;
--drop table if exists parking_ticket;
--drop table if exists spot;
--drop table if exists vehicle;
--drop table if exists branch;

create table branch (
	branch_id serial primary key,
	location varchar(50) not null
);
create table vehicle (
	vehicle_id serial primary key,
	licence_plate varchar(32),
	Unique(licence_plate),
	type varchar(20) not null
);
create table spot (
	branch_id int,
	spot_id serial primary key,
	spot_number int not null,
	type varchar(20) not null,
	is_available bool not null default true,
	UNIQUE (branch_id, spot_number),
	foreign key(branch_id) references branch(branch_id)
);
create table parking_ticket (
	ticket_id serial primary key,
	vehicle_id int not null,
	spot_id int not null,
	fee numeric(10,2) not null,
	entry_time timestamp not null,
	exit_time timestamp,
	foreign key(spot_id) references spot(spot_id),
	foreign key(vehicle_id) references vehicle(vehicle_id)
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

INSERT INTO vehicle (licence_plate, type) VALUES
('12-345-67', 'CAR'),
('23-456-78', 'CAR'),
('34-567-89', 'CAR'),
('45-678-90', 'MOTORCYCLE'),
('56-789-01', 'MOTORCYCLE'),
('67-890-12', 'TRUCK'),
('78-901-23', 'TRUCK');


-- =========================
-- SPOTS
-- =========================


INSERT INTO spot (branch_id, spot_number, type, is_available) VALUES
(1, 1, 'CAR', true),
(1, 2, 'CAR', false),
(1, 3, 'CAR', true),
(1, 4, 'MOTORCYCLE', true),
(1, 5, 'TRUCK', true),
(2, 1, 'CAR', true),
(2, 2, 'CAR', false),
(2, 3, 'MOTORCYCLE', true),
(2, 4, 'TRUCK', true),
(3, 1, 'CAR', true),
(3, 2, 'CAR', true),
(3, 3, 'MOTORCYCLE', true),
(3, 4, 'TRUCK', false);

-- =========================
-- PARKING TICKETS
-- =========================

INSERT INTO parking_ticket
(vehicle_id, spot_id, fee, entry_time, exit_time) VALUES
(1, 1, 8.00, '2026-08-10 07:30:00', '2026-08-10 09:30:00'),
(3, 3, 12.00, '2026-08-10 08:00:00', '2026-08-10 11:00:00'),
(4, 4, 5.00, '2026-08-10 09:00:00', '2026-08-10 10:30:00'),
(5, 8, 6.00, '2026-08-10 10:15:00', '2026-08-10 12:00:00'),
(6, 12, 30.00, '2026-08-10 06:30:00', '2026-08-10 10:30:00'),
(2, 2, 0, '2026-08-11 08:30:00', NULL);


