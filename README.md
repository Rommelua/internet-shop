# Internet-Shop
![Internet-shop](/images/internetShop.jpg)

## Table of contents
* [Project purpose](#purpose)
* [Project structure](#structure)
* [For developer](#for_developer)
* [Authors](#authors)

# <a name="purpose"></a>Project purpose
This project is a simple version of internet-shop. 

It reproduces the main functions and opportunities 
of online shopping.
<hr>

#### The main functions of Internet-Shop: 
* Registration and log in forms
* Shopping cart and order services
* Two roles: User and Admin

### Depending on the role you will have such opportunities:

#### Functions available for all users (unlogined include):
* log in
* register
* view the main menu
* view the products
* inject mock data to DB

#### Functions available for any-role users:
* log out

#### Functions available only for users with a USER-role:
* add products to cart
* delete products from cart
* make orders
* view the list of all your orders
* view the details of your orders

#### Functions available only for users with a ADMIN-role:
* add products to the store
* delete products from the store
* view the list of orders
* delete users from the store
* view the list of users
* delete users from the store

<hr>
In order to add some security and give the access to appropriate resources,
depending on the role,
authentication and authorization filters were implemented. 

DAO layer has two implementations which gives you a possibility to test it both
on inner storage and on storage based on PostgreSQL DataBase.
Hashing and salting of user's password are implemented, so nobody has access to your secret data.

# <a name="structure"></a>Project structure
* Java 11
* Maven 4.0.0
* javax.servlet 3.1.0
* jstl 1.2
* maven-checkstyle-plugin
* PostgreSQL 12
<hr> 

# <a name="for_developer"></a>For developer
#### To run this project you need to install:
* [Java 11](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html)
* [Tomcat](https://tomcat.apache.org/download-90.cgi)
* [PostgreSQL 12](https://www.postgresql.org/download/)

#### After installation, you should do the following:
Add this project to your IDE as Maven project.

Configure Tomcat : 
* Add artifact
* Add Java SDK 11

#### If you want to test it using SQL DataBase, you should: 

* Use file src/main/resources/init_db.sql to create the DataBase all the tables required by this app.

* At src/main/resources/db.properties specify username and password for your DB to create a Connection.

* If your DB is not PosgreSQL add the dependency for the connector to your DBMS to POM.XML. You should write in your DBMS,
 establish a connection, having downloaded the driver for it and specify the version of the DBMS.

#### If you want to test it using inner storage, you should:
* Remove the annotations @Dao from all the classes in src/main/java/com/internet/shop/dao/jdbc
and add it to classes in src/main/java/com/internet/shop/dao/impl

Run the project and register.

If it's your first visit, click on Inject Data.
Then, by default two users will be generated.

The first one – with an ADMIN role (login = admin, password = 1111).
The second one - with a USER role (login = steve, password = 1111).
<hr>

# <a name="authors"></a>Authors
* [Leonid Sivko](https://github.com/Rommelua)
