-- Database: parking

-- DROP DATABASE IF EXISTS parking;

create table users (
	user_id serial primary key,
	username varchar(100) not null,
	password varchar(255) not null,
	role varchar(100) not null
);
create table parking_ticket (
	ticket_id serial primary key,
	vehicle_id int not null,
	spot_id int not null,
	user_id int not null,
	fee numeric(10,2) not null,
	entry_time timestamp not null,
	exit_time timestamp,
	foreign key(spot_id) references spot(spot_id),
	foreign key(vehicle_id) references vehicle(vehicle_id),
	foreign key(user_id) references users(user_id)
);

INSERT INTO branch (location) VALUES
('Jericho'),
('Hebron'),
('Jenin'),
('Tulkarm');

INSERT INTO vehicle (licence_plate, type) VALUES
('89-012-34', 'CAR'),
('90-123-45', 'CAR'),
('11-234-56', 'CAR'),
('22-345-67', 'CAR'),
('33-456-78', 'CAR'),
('44-567-89', 'MOTORCYCLE'),
('55-678-90', 'MOTORCYCLE'),
('66-789-01', 'MOTORCYCLE'),
('77-890-12', 'MOTORCYCLE'),
('88-901-23', 'TRUCK'),
('99-012-34', 'TRUCK'),
('10-123-45', 'TRUCK'),
('21-234-56', 'TRUCK');

-- JERICHO (branch 4)
INSERT INTO spot (branch_id, spot_number, type, is_available) VALUES
(4, 1, 'CAR', true),
(4, 2, 'CAR', true),
(4, 3, 'CAR', false),
(4, 4, 'CAR', true),
(4, 5, 'CAR', true),
(4, 6, 'MOTORCYCLE', true),
(4, 7, 'MOTORCYCLE', false),
(4, 8, 'TRUCK', true),
(4, 9, 'TRUCK', true),
(4, 10, 'TRUCK', false);

INSERT INTO spot (branch_id, spot_number, type, is_available) VALUES
(5, 1, 'CAR', true),
(5, 2, 'CAR', false),
(5, 3, 'CAR', true),
(5, 4, 'CAR', true),
(5, 5, 'CAR', false),
(5, 6, 'MOTORCYCLE', true),
(5, 7, 'MOTORCYCLE', true),
(5, 8, 'MOTORCYCLE', false),
(5, 9, 'TRUCK', true),
(5, 10, 'TRUCK', true);

INSERT INTO spot (branch_id, spot_number, type, is_available) VALUES
(6, 1, 'CAR', true),
(6, 2, 'CAR', true),
(6, 3, 'CAR', false),
(6, 4, 'CAR', true),
(6, 5, 'MOTORCYCLE', true),
(6, 6, 'MOTORCYCLE', true),
(6, 7, 'TRUCK', false),
(6, 8, 'TRUCK', true),
(6, 9, 'TRUCK', true);


-- TULKARM (branch 7)
INSERT INTO spot (branch_id, spot_number, type, is_available) VALUES
(7, 1, 'CAR', false),
(7, 2, 'CAR', true),
(7, 3, 'CAR', true),
(7, 4, 'CAR', false),
(7, 5, 'CAR', true),
(7, 6, 'MOTORCYCLE', true),
(7, 7, 'MOTORCYCLE', false),
(7, 8, 'TRUCK', true),
(7, 9, 'TRUCK', false),
(7, 10, 'TRUCK', true);

INSERT INTO spot (branch_id, spot_number, type, is_available) VALUES
(1, 6, 'CAR', true),
(1, 7, 'CAR', true),
(1, 8, 'CAR', false),
(1, 9, 'CAR', true),
(1, 10, 'MOTORCYCLE', true),
(1, 11, 'MOTORCYCLE', false),
(1, 12, 'TRUCK', true),
(1, 13, 'TRUCK', true),
(2, 5, 'CAR', true),
(2, 6, 'CAR', false),
(2, 7, 'CAR', true),
(2, 8, 'CAR', true),
(2, 9, 'MOTORCYCLE', true),
(2, 10, 'MOTORCYCLE', true),
(2, 11, 'TRUCK', false),
(2, 12, 'TRUCK', true),
(3, 5, 'CAR', true),
(3, 6, 'CAR', true),
(3, 7, 'CAR', false),
(3, 8, 'CAR', true),
(3, 9, 'MOTORCYCLE', true),
(3, 10, 'MOTORCYCLE', false),
(3, 11, 'TRUCK', true),
(3, 12, 'TRUCK', true);

INSERT INTO users (username, password, role) VALUES
('admin', '$2a$10$exampleAdminPasswordHash', 'ROLE_ADMIN'),
('ruba', '$2a$10$exampleUserPasswordHash', 'ROLE_USER'),
('ahmad', '$2a$10$exampleUserPasswordHash', 'ROLE_USER'),
('sara', '$2a$10$exampleUserPasswordHash', 'ROLE_USER'),
('mohammad', '$2a$10$exampleUserPasswordHash', 'ROLE_USER');

INSERT INTO users (username, password, role) VALUES
('admin2', '$2a$10$exampleAdminPasswordHash', 'ROLE_ADMIN'),
('yousef', '$2a$10$exampleUserPasswordHash', 'ROLE_USER'),
('layla', '$2a$10$exampleUserPasswordHash', 'ROLE_USER'),
('omar', '$2a$10$exampleUserPasswordHash', 'ROLE_USER'),
('lina', '$2a$10$exampleUserPasswordHash', 'ROLE_USER'),
('khaled', '$2a$10$exampleUserPasswordHash', 'ROLE_USER'),
('maya', '$2a$10$exampleUserPasswordHash', 'ROLE_USER'),
('tariq', '$2a$10$exampleUserPasswordHash', 'ROLE_USER');