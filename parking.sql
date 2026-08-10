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
	spotID serial,
	type char not null,
	free bool not null,
	primary key(branchID, spotID),
	foreign key(branchID) references branch(branchID)
);
create table reservation (
	reservationID serial primary key,
	branchID int,
	licencePlate varchar(32) not null,
	spotID int not null,
	fee float not null,
	entryTime time not null,
	exitTime time not null,
	foreign key(branchID,spotID) references spot(branchID, spotID),
	foreign key(licencePlate) references vehicle(licencePlate)
);
