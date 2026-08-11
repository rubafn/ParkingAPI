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
	type char not null,
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



