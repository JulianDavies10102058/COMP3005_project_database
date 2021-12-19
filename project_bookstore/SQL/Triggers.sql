create trigger stock_update
after update of book on stock /*trigger after stock as been updated*/
referencing new row as nrow
for each row
when nrow.stock < 10 /*only if stock has gone below 10*/
begin
    /*update stock of book*/
	update book
	set stock = stock +
	    /*check which book was for which order_number and only add it if the current date minus the ordered date is below 30 (1 month)*/
		(select order_number
		from book_order natural join ordered
		where book_order.ISBN = nrow.ISBN and (CURRENT_DATE-ordered.date <= 30)
	where book.ISBN = nrow.ISBN; /*only do this for the current book*/
end; 
