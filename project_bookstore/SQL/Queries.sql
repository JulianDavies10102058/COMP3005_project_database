/*Register a new user*/
insert into db_user values
(id, name, st_number, st_name, city, phone_number, owner)

/*Create new book (owner only)*/
insert into book values
(ISBN, title, genre, pages, price, stock, publisher_fee, a_id, p_id)

/*Delete book (owner only)*/
delete from book
where isbn = input_isbn

/*Get title of book based on author's name*/
select title 
from book join author on book.a_id = author.id
where author.name = 'author_name'

/*Get title of book based on ISBN*/
select title 
from book 
where isbn = input_isbn

/*Get information on book (customer)*/
select isbn,title,genre,pages,price,stock,author.name as author,publisher.name as publisher
from book join author on book.a_id = author.id, publisher
where p_id = publisher.id and title = 'book_title'

/*Refresh materialized view*/
refresh materialized view sale_by_author

