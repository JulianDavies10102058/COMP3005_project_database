/*Materialized view for total sales within 1 month*/
create materialized view total_sales as
select sum(price)
from ordered natural join book_order natural join book, author
where CURRENT_DATE-date <= 30

/*Materialized view for sales by genre within 1 month*/
create materialized view sale_by_genre as
select genre, sum(price)
from ordered natural join book_order natural join book
where CURRENT_DATE-date <= 30
group by genre

/*Materialized view for sales by author within 1 month*/
create materialized view sale_by_author as
select author.name, sum(price)
from ordered natural join book_order natural join book, author
where CURRENT_DATE-date <= 30 and book.a_id = author.id
group by author.name