import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Name: Julian Davies
 * ID: 101020508
 * This is for the Project
 */
public class project_bookstore {
    public static void main(String args[]){

        project_application("postgres", "postgres");
    }

    public static void project_application(String userid, String passwd)
    {
        try (
                Connection conn = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/bookstore",
                        userid, passwd);
                Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);//Use type scroll so that rset.first() can be used

        ) {
            ResultSet rset;
            Scanner s = new Scanner(System.in);
            System.out.println("Please input your customer ID to login.\nIf you do not have a customer ID, please input 0 to register.");
            int c_id = s.nextInt();
            if(c_id == 0){
                s.nextLine();
                System.out.println("Please input your name:");
                String name = s.nextLine();
                System.out.println("Please input your street number:");
                int st_number = s.nextInt();
                s.nextLine();
                System.out.println("Please input your street name:");
                String st_name = s.nextLine();
                System.out.println("Please input your city:");
                String city = s.next();
                System.out.println("Please input your phone number:");
                long phone_number = s.nextLong();
                boolean owner = false;
                rset = stmt.executeQuery(
                        "select id from db_user");
                rset.last();
                int max_id = rset.getRow();
                int new_id = max_id+1;
                stmt.executeUpdate(
                        "insert into db_user values"+
                                " ("+new_id+",\'"+name+"\',"+st_number+",\'"+st_name+"\',\'"+city+"\',"+phone_number+","+owner+")");
                c_id = new_id;
            }
            rset = stmt.executeQuery(
                    "select * from db_user where id = "+c_id);
            rset.next();
            String welcome = rset.getString("name");
            System.out.println("Welcome "+welcome+"!");
            if (rset.getBoolean("owner")) {
                String option;
                do {
                    System.out.println("What would you like to do?\n(A) Add a new book\n(B) Delete a book\n(C) See reports");
                    option = s.next();
                    if (option.equals("A")) {
                        System.out.println("Please input the ISBN:");
                        int isbn = s.nextInt();
                        s.nextLine();
                        System.out.println("Please input the title:");
                        String title = s.nextLine();
                        System.out.println("Please input the genre:");
                        String genre = s.nextLine();
                        System.out.println("Please input the number of pages:");
                        int pages = s.nextInt();
                        System.out.println("Please input the price:");
                        double price = s.nextDouble();
                        System.out.println("Please input the current stock amount:");
                        int stock = s.nextInt();
                        System.out.println("Please input the publisher_fee (between 0 and 1):");
                        double pub_fee = s.nextDouble();
                        System.out.println("Please input the author's id:");
                        int a_id = s.nextInt();
                        System.out.println("Please input the p_id:");
                        int p_id = s.nextInt();
                        rset = stmt.executeQuery(
                                "select id from author where id = " + a_id);
                        if (rset.next() == false) {
                            System.out.println("No author with that id. A new author will be created.");
                            System.out.println("Please insert the author's name:");
                            s.nextLine();
                            String auth_name = s.nextLine();
                            System.out.println("Please insert date of birth:");
                            String dob = s.nextLine();
                            stmt.executeUpdate("insert into author values" +
                                    " (" + a_id + ",\'" + auth_name + "\',\'" + dob + "\')");
                            System.out.println(auth_name + " was inserted into the database.");
                        }
                        rset = stmt.executeQuery(
                                "select id from publisher where id = " + p_id);
                        if (rset.next() == false) {
                            System.out.println("No publisher with that id. A new publisher will be created.");
                            System.out.println("Please input the name:");
                            String pub_name = s.nextLine();
                            System.out.println("Please input the street number:");
                            int st_number = s.nextInt();
                            s.nextLine();
                            System.out.println("Please input the street name:");
                            String st_name = s.nextLine();
                            System.out.println("Please input the city:");
                            String city = s.next();
                            System.out.println("Please input the email address:");
                            String email = s.next();
                            System.out.println("Please input the phone number:");
                            long phone_number = s.nextLong();
                            System.out.println("Please input the banking account number:");
                            int bank_account = s.nextInt();
                            stmt.executeUpdate("insert into publisher values" +
                                    " (" + p_id + ",\'" + pub_name + "\'," + st_number + ",\'" + st_name + "\',\'" + city + "\',\'" + email + "\'," + phone_number + "," + bank_account + ")");
                            System.out.println(pub_name + " was inserted into the database.");
                        }
                        stmt.executeUpdate("insert into book values" +
                                " (" + isbn + ",\'" + title + "\',\'" + genre + "\'," + pages + "," + price + "," + stock + "," + pub_fee + "," + a_id + "," + p_id + ")");
                        System.out.println(title + " was inserted into the database.");
                    }
                    else if (option.equals("B")) {
                        System.out.println("Please input the ISBN for the book you would like to delete:");
                        int isbn = s.nextInt();
                        stmt.executeUpdate("delete from book where isbn = "+isbn);
                        System.out.println("The book with the ISBN: "+isbn+" was deleted.");
                    }
                    else if (option.equals("C")){
                        System.out.println("Which report would you like to see?\n(A) Total Sales\n(B) Sales by Genre\n(C) Sales by Author");
                        String report_option = s.next();
                        if(report_option.equals("A")){
                            stmt.executeUpdate("refresh materialized view total_sales");//get updated info
                            rset = stmt.executeQuery(
                                    "select * from total_sales");
                            while(rset.next()) {
                                System.out.println(rset.getString(1)+": "+rset.getString(2));
                            }
                        } else if(report_option.equals("B")) {
                            stmt.executeUpdate("refresh materialized view sale_by_genre");//get updated info
                            rset = stmt.executeQuery(
                                    "select * from sale_by_genre");
                            while(rset.next()) {
                                System.out.println(rset.getString(1)+": "+rset.getString(2));
                            }
                        }
                        else if(report_option.equals("C")){
                            stmt.executeUpdate("refresh materialized view sale_by_author");//get updated info
                            rset = stmt.executeQuery(
                                    "select * from sale_by_author");
                            while(rset.next()) {
                                System.out.println(rset.getString(1)+": "+rset.getString(2));
                            }
                        }
                    }
                }while(!option.equals("exit"));
            } else {
                String option;
                ArrayList<Integer> cart = new ArrayList();
                do {
                    System.out.println("What would you like to do?\n(A) Search for a book\n(B) Check order status\n(C) Checkout Cart");
                    option = s.next();
                    if (option.equals("A")) {
                        System.out.println("How would you like to search for a book?\n(A) ISBN\n(B) Author\n(C) Title\n(D) Genre");
                        String search_option = s.next();
                        if (search_option.equals("A")) {
                            System.out.println("Please input ISBN:");
                            int search_isbn = s.nextInt();
                            rset = stmt.executeQuery(
                                    "select title from book where isbn = " + search_isbn);
                            if (rset.next() == false) {
                                System.out.println("There is no book with that isbn.");
                            } else {
                                System.out.println("Here is the information on the book:");
                                int isbn = book_info(stmt, rset.getString(1));
                                System.out.println("Would you like to add this book to your cart?");
                                String cart_agree = s.next();
                                if (cart_agree.equals("Yes")) {
                                    cart.add(isbn);
                                }
                            }
                        } else if (search_option.equals("B")) {
                            System.out.println("Please input the name of the author:");
                            s.nextLine();
                            String search_auth_name = s.nextLine();
                            rset = stmt.executeQuery(
                                    "select title from book join author on book.a_id = author.id " +
                                            "where author.name = \'" + search_auth_name + "\'");
                            if (rset.next() == false) {
                                System.out.println("There is no book with that author's name.");
                            } else {
                                rset.beforeFirst();
                                System.out.println("You found:");
                                while (rset.next()) {
                                    System.out.println(rset.getString(1));
                                }
                                System.out.println("Which book do you want?");
                                String book_selected = s.nextLine();
                                System.out.println("Here is the information on the book:");
                                int isbn = book_info(stmt, book_selected);
                                System.out.println("Would you like to add this book to your cart?");
                                String cart_agree = s.next();
                                if (cart_agree.equals("Yes")) {
                                    cart.add(isbn);
                                }
                            }
                        } else if (search_option.equals("C")) {
                            System.out.println("Please input the title:");
                            s.nextLine();
                            String search_title = s.nextLine();
                            rset = stmt.executeQuery(
                                    "select title from book where title = \'" + search_title + "\'");
                            if (rset.next() == false) {
                                System.out.println("There is no book with that title.");
                            } else {
                                System.out.println("Here is the information on the book:");
                                int isbn = book_info(stmt, rset.getString(1));
                                System.out.println("Would you like to add this book to your cart?");
                                String cart_agree = s.next();
                                if (cart_agree.equals("Yes")) {
                                    cart.add(isbn);
                                }
                            }
                        } else if (search_option.equals("D")) {
                            System.out.println("Please input the genre:");
                            s.nextLine();
                            String search_genre = s.nextLine();
                            rset = stmt.executeQuery(
                                    "select title from book " +
                                            "where genre = \'" + search_genre + "\'");
                            if (rset.next() == false) {
                                System.out.println("There is no book with that author's name.");
                            } else {
                                rset.beforeFirst();
                                System.out.println("You found:");
                                while (rset.next()) {
                                    System.out.println(rset.getString(1));
                                }
                                System.out.println("Which book do you want?");
                                String book_selected = s.nextLine();
                                System.out.println("Here is the information on the book:");
                                int isbn = book_info(stmt, book_selected);
                                System.out.println("Would you like to add this book to your cart?");
                                String cart_agree = s.next();
                                if (cart_agree.equals("Yes")) {
                                    cart.add(isbn);
                                }
                            }
                        }
                    } else if (option.equals("B")){
                        rset = stmt.executeQuery(
                                "select order_number from ordered where c_id = "+c_id);
                        if (rset.next() == false) {
                            System.out.println("You currently have no orders.");
                        } else {
                            rset.beforeFirst();
                            System.out.println("You have these orders:");
                            while (rset.next()) {
                                System.out.println(rset.getString(1));
                            }
                            System.out.println("Which order would you like to view?");
                            int order_selected = s.nextInt();
                            System.out.println("Here is the information on the order:");
                            rset = stmt.executeQuery(
                                    "select current_location from ordered where order_number = "+order_selected);
                            rset.first();
                            System.out.println("Your order is currently at: "+rset.getString(1));
                        }
                    }else if (option.equals("C")){
                        if(cart.isEmpty()){
                            System.out.println("Your cart is empty!");
                        } else{
                            System.out.println("To checkout, please input the following information:");
                            System.out.println("Billing Address Street Number");
                            int b_st_number = s.nextInt();
                            System.out.println("Billing Address Street Name");
                            s.nextLine();
                            String b_st_name = s.nextLine();
                            System.out.println("Billing Address City");
                            String b_city = s.next();
                            System.out.println("Shipping Address Street Number");
                            int s_st_number = s.nextInt();
                            System.out.println("Shipping Address Street Name");
                            s.nextLine();
                            String s_st_name = s.nextLine();
                            System.out.println("Shipping Address City");
                            String s_city = s.next();

                            rset = stmt.executeQuery(
                                    "select order_number from ordered");
                            rset.last();
                            int max_id = rset.getRow();
                            int new_id = max_id+1;
                            LocalDate current_date = LocalDate.now();
                            stmt.executeUpdate(
                                    "insert into ordered values"+
                                            " ("+new_id+", \'"+current_date+"\',"+b_st_number+",\'"+b_st_name+"\',\'"+b_city+"\'," +
                                            s_st_number+",\'"+s_st_name+"\',\'"+s_city+"\', \'Warehouse\', "+c_id+")");
                            for(int i = 0; i < cart.size(); i++){
                                stmt.executeUpdate(
                                        "insert into book_order values " +
                                                "("+new_id+","+cart.get(i)+")");
                            }
                            cart = new ArrayList<>();
                            System.out.println("Your order has been placed!");
                        }

                    }
                }while(!option.equals("exit"));
            }
        }
        catch (Exception sqle)
        {
            System.out.println("Exception : " + sqle);
        }

    }
    public static int book_info(Statement stmt, String book_title){
        try {
            ResultSet book_info_set = stmt.executeQuery(
                    "select isbn,title,genre,pages,price,stock,author.name as author,publisher.name as publisher " +
                            "from book join author on book.a_id = author.id, publisher " +
                            "where p_id = publisher.id and title = \'"+book_title+"\'");
            ResultSetMetaData rsmd = book_info_set.getMetaData();
            book_info_set.first();
            for(int i = 1; i <= rsmd.getColumnCount(); i++){
                System.out.println(rsmd.getColumnLabel(i) + ": " + book_info_set.getString(i));
            }
            return book_info_set.getInt("isbn");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }


}
