-- Database: parking

-- DROP DATABASE IF EXISTS parking;

create table branch (
	branchID serial primary key,
	location varchar(50) not null
);
create table vehicle (
	licencePlate varchar(32) primary key
);
create table car (
	licencePlate varchar(32) primary key,
	color varchar(32) not null default 'Undefined',
	foreign key(licencePlate) references vehicle(licencePlate)
);
create table truck (
	licencePlate varchar(32) primary key,
	height float not null,
	foreign key(licencePlate) references vehicle(licencePlate)
);
create table motocycle (
	licencePlate varchar(32) primary key,
	brand varchar(32) not null,
	foreign key(licencePlate) references vehicle(licencePlate)
);
create table spot (
	branchID int,
	spotID serial primary key,
	type char not null,
	free bool not null,
	foreign key(branchID) references branch(branchID)
);
create table reservation (
	reservationID serial primary key,
	licencePlate varchar(32) not null,
	spotID int not null,
	fee float not null,
	entryTime time not null,
	exitTime time not null,
	foreign key(spotID) references spot(spotID),
	foreign key(licencePlate) references vehicle(licencePlate)
);
-- Branches
insert into branch (location) values
('Ramallah'),
('Nablus');

-- Vehicles
insert into vehicle (licencePlate) values
('ABC-123'),
('XYZ-789'),
('TRK-555'),
('MOTO-1');

-- Car
insert into car (licencePlate, color) values
('ABC-123', 'Red'),
('XYZ-789', 'Blue');

-- Truck
insert into truck (licencePlate, height) values
('TRK-555', 3.5);

-- Motorcycle
insert into motocycle (licencePlate, brand) values
('MOTO-1', 'Yamaha');

-- Spots
insert into spot (branchID, type, free) values
(1, 'C', true),
(1, 'C', false),
(1, 'T', true),
(2, 'C', true),
(2, 'M', true);

-- Reservations
insert into reservation (licencePlate, spotID, fee, entryTime, exitTime) values
('ABC-123', 1, 10.5, '08:00', '10:00'),
('XYZ-789', 2, 8.0, '09:00', '11:00'),
('TRK-555', 3, 15.0, '07:30', '12:00'),
('MOTO-1', 5, 5.0, '10:00', '12:00');

