CREATE TABLE employee (
	emp_id SERIAL primary key , 
	fName varChar not null, 
	lName varChar not null);
	
INSERT INTO employee (1, 'Rob', 'Pierz');

CREATE TABLE customers (
	cust_id SERIAL primary key, 
	fName varChar not null, 
	lName varChar not null,
	gender character,
	dob varChar(8) not null,
	emp_id integer foreign key references employee(emp_id) not null
);

CREATE TABLE accounts (
	account_id integer primary key,
	owner_id integer not null,
	joint_id integer,
	account_type varChar,
	balance money
);