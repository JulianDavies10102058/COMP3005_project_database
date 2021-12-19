create table author /*author has id, name, and date of birth with the primary key being the id*/
	(ID			numeric(5,0),
	 name		varchar(30),
	 DOB		varchar(30),
	 primary key (ID)
	);
create table publisher /*publisher has id, name, address info, email, phone_number, and banking_account represented as an account number*/
	(ID					numeric(5,0),
	 name				varchar(30),
	 st_number			numeric(5,0),
	 st_name			varchar(30),
	 city				varchar(30),
	 email				varchar(30),
	 phone_number 		numeric(10,0),
	 banking_account	numeric(7,0),
	 primary key (ID)
	);
create table book /*book has ISBN, title, genre, pages, price, stock, author_id, publisher_id, and publisher_fee which as to be under 1*/
	(ISBN			numeric(7,0),
	 title			varchar(30),
	 genre			varchar(30),
	 pages			numeric(4,0),
	 price			numeric(5,2),
	 stock			numeric(2,0),
	 publisher_fee	numeric check (publisher_fee < 1), /*the number below 1 determines what percentage the publisher gets. (0.2 = 20%)*/
	 a_id			numeric(5,0),
	 p_id			numeric(5,0),
	 primary key (ISBN),
	 foreign key (a_id) references author (ID) /*delete books associated with author if author is deleted*/
	 	on delete cascade,
	 foreign key (p_id) references publisher (ID) /*delete books associated with publisher if publisher is deleted*/
	 	on delete cascade
	);
create table db_user
	(ID		numeric(8,0),
	 name	varchar(30),
	 st_number	numeric(5,0),
	 st_name	varchar(30),
	 city		varchar(30),
	 phone_number	numeric(10,0),
	 owner		boolean default false, /*owner determines if the user if the owner or a customer, default is that the user is not the owner*/
	 primary key (ID)
	);
create table ordered
	(order_number		numeric(10,0),
	 date				date, /*date the order was placed*/
	 b_st_number		numeric(5,0),
	 b_st_name			varchar(30),
	 b_city				varchar(30),
	 s_st_number		numeric(5,0),
	 s_st_name			varchar(30),
	 s_city				varchar(30),
	 current_location	varchar(30), /*current location of the delivery represented by a city, (Ex. 'Ottawa')*/
	 c_id				numeric(8,0), /*id of the customer who made the order*/
	 primary key (order_number),
	 foreign key (c_id) references db_user /*if a user is deleted, set the id to null*/
	 	on delete set null
	 );
create table book_order /*represents books ordered for a specific order. Both are the primary key since there can be many books to one order and many orders for one book*/
	(order_number	numeric(10,0),
	 ISBN			numeric(7,0),
	 primary key (order_number, ISBN),
	 foreign key (order_number) references ordered,
	 foreign key (ISBN) references book
	 );