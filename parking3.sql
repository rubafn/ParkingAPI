-- Database: parking
 drop table if exists parking_ticket;

 
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

insert into parking_ticket(vehicle_id,spot_id,fee,entry_time,exit_time) values
(1,7,21.0,'2026-8-23 7:00:00','2026-8-23 10:00:00'),
(1,1,14.0,'2026-8-23 8:00:00','2026-8-23 10:00:00'),
(4,20,15.0,'2026-8-23 9:00:00','2026-8-23 12:00:00'),
(5,31,0,'2026-8-23 14:00:00',NULL),
(6,5,10.0,'2026-8-23 11:00:00','2026-8-23 12:00:00'),
(7,9,30.0,'2026-8-23 8:00:00','2026-8-23 11:00:00');