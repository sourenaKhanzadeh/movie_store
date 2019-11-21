#!/bin/sh
#export LD_LIBRARY_PATH=/usr/lib/oracle/12.1/client64/lib
sqlplus64 "user/password@(DESCRIPTION=(ADDRESS=(PROTOCOL=TCP)(Host=oracle.scs.ryerson.ca)(Port=1521))(CONNECT_DATA=(SID=orcl)))" <<EOF

DROP TABLE movie_user CASCADE CONSTRAINTS;
DROP TABLE admin CASCADE CONSTRAINTS;
DROP TABLE customer CASCADE CONSTRAINTS;
DROP TABLE product CASCADE CONSTRAINTS;
DROP TABLE movie CASCADE CONSTRAINTS;
DROP TABLE music CASCADE CONSTRAINTS;
DROP TABLE tvseries CASCADE CONSTRAINTS;
DROP TABLE subscription CASCADE CONSTRAINTS;
DROP TABLE streams CASCADE CONSTRAINTS;
DROP TABLE manages CASCADE CONSTRAINTS;
DROP TABLE purchases CASCADE CONSTRAINTS;

CREATE TABLE movie_user (
	UserID NUMBER PRIMARY KEY,
	FirstName VARCHAR2(30) NOT NULL,
	LastName VARCHAR2(30) NOT NULL,
	Email VARCHAR2(50) NOT NULL,
	Username VARCHAR2(30) NOT NULL
);

CREATE TABLE admin (
	AdminID NUMBER PRIMARY KEY REFERENCES movie_user(UserID)
);

CREATE TABLE customer (
	CustomerID NUMBER PRIMARY KEY REFERENCES movie_user(UserID)
);

CREATE TABLE product (
	ProductID NUMBER PRIMARY KEY,
	Name VARCHAR2(50) NOT NULL,
	Length NUMBER NOT NULL,
	Description VARCHAR2(280),
	Genre VARCHAR2(50),
	SellPrice NUMBER NOT NULL,
	ReleaseDate  DATE
);

CREATE TABLE movie (
	MovieID NUMBER PRIMARY KEY REFERENCES product(ProductID),
	Movie_Cast VARCHAR2(100),
	Languages VARCHAR2(100),
	Director VARCHAR2(50),
	Country VARCHAR2(50),
	MaturityRating VARCHAR2(10),
	RottenTomatoRating VARCHAR2(10)
);

CREATE TABLE music (
	SongID NUMBER PRIMARY KEY REFERENCES product(ProductID),
	ArtistName VARCHAR2(100) NOT NULL
);

CREATE TABLE tvseries (
	SeriesID NUMBER PRIMARY KEY REFERENCES product(ProductID),
	Seasons NUMBER NOT NULL,
	Episodes NUMBER NOT NULL,
	TV_Cast VARCHAR2(100),
	MaturityRating VARCHAR2(10),
	RottenTomatoRating VARCHAR2(10)
);


CREATE TABLE subscription (
	Type VARCHAR2(20) PRIMARY KEY,
	Price NUMBER NOT NULL,
	TVSeriesPerMonth NUMBER NOT NULL,
	MoviesPerMonth NUMBER NOT NULL,
	MusicPerMonth NUMBER NOT NULL
);

CREATE TABLE streams (
	CustomerID NUMBER NOT NULL REFERENCES customer(CustomerID),
	ProductID NUMBER NOT NULL REFERENCES product(ProductID)
);

CREATE TABLE manages (
	AdminID NUMBER NOT NULL REFERENCES admin(AdminID),
CustomerID NUMBER REFERENCES customer(CustomerID),
ProductID NUMBER REFERENCES product(ProductID)
);

CREATE TABLE purchases (
	CustomerID NUMBER NOT NULL REFERENCES customer(CustomerID),
	SubscriptionType VARCHAR2(20) REFERENCES subscription(Type),
	ProductID NUMBER REFERENCES product(ProductID),
	DateOfPurchase DATE
	--ORDER BY DateOfPurchase DESC
);

INSERT INTO movie_user(USERID, FIRSTNAME, LASTNAME, EMAIL, USERNAME)VALUES(1234, 'Amir','Ali','amir@yahoo.ca','amir221');
INSERT INTO movie_user(USERID, FIRSTNAME, LASTNAME, EMAIL, USERNAME)VALUES(5678, 'Kaitlyn','Rodriguez','k@yahoo.ca','k221');
INSERT INTO movie_user(USERID, FIRSTNAME, LASTNAME, EMAIL, USERNAME)VALUES(9000, 'April','Liu','AL@yahoo.ca','AL123');
INSERT INTO movie_user(USERID, FIRSTNAME, LASTNAME, EMAIL, USERNAME)VALUES(9304, 'Sam','Cook','thesamcook@yahoo.ca','thesamcook');


INSERT INTO ADMIN(ADMINID)VALUES(1234);
INSERT INTO CUSTOMER(CUSTOMERID)VALUES(5678);
INSERT INTO CUSTOMER(CUSTOMERID)VALUES(9000);
INSERT INTO CUSTOMER(CUSTOMERID)VALUES(9304);

INSERT INTO product (ProductID, Name, Length, Description, Genre, SellPrice, ReleaseDate)VALUES(9999, 'Firework', 4, 'An inspirational song.', 'Pop', 2, TO_DATE('2010/10/26 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));

INSERT INTO product (ProductID, Name, Length, Description, Genre, SellPrice, ReleaseDate)VALUES(8888, 'Set It Up', 105, 'Two corporate executive assistants hatch a plan to match-make their two bosses.', 'Romance', 4, TO_DATE('2018/06/15 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));

INSERT INTO product(ProductID, Name, Length, Description, Genre, SellPrice, ReleaseDate)VALUES(6767, 'Brooklyn Nine-Nine', 300, 'Brooklyn Nine-Nine is an American police procedural comedy television series.', 'Comedy', 5, TO_DATE('2013/09/17 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));

INSERT INTO movie(MovieID, Movie_Cast , Languages, Director, Country, MaturityRating, RottenTomatoRating)VALUES(8888, 'Zoey Deutch', 'Engish', 'Claire Scanlon', 'USA', 'PG', '94');

INSERT INTO music(SongID, ArtistName)VALUES(9999, 'Katy Perry');

INSERT INTO tvseries(SeriesID, Seasons, Episodes, TV_Cast, MaturityRating, RottenTomatoRating)VALUES(6767, 6, 130, 'Andy Samberg','PG', '89');

INSERT INTO subscription(Type, Price, TVSeriesPerMonth, MoviesPerMonth, MusicPerMonth)VALUES('Basic', 10.00 , 2 , 5, 20 );
INSERT INTO subscription(Type, Price, TVSeriesPerMonth, MoviesPerMonth, MusicPerMonth)VALUES('Standard', 20.00 , 10 , 20, 99999 );
INSERT INTO subscription(Type, Price, TVSeriesPerMonth, MoviesPerMonth, MusicPerMonth)VALUES('Premium', 50.00 , 99999 , 99999, 99999 );

INSERT INTO streams(CustomerID, ProductID)VALUES(5678,6767);
INSERT INTO streams(CustomerID, ProductID)VALUES(5678,8888);
INSERT INTO streams(CustomerID, ProductID)VALUES(9000,6767);
INSERT INTO streams(CustomerID, ProductID)VALUES(9304,9999);

INSERT INTO manages(AdminID,CustomerID)VALUES(1234,5678);
INSERT INTO manages(AdminID,ProductID)VALUES(1234,9999);
INSERT INTO manages(AdminID,ProductID)VALUES(1234,8888);
INSERT INTO manages(AdminID,ProductID)VALUES(1234,6767);
INSERT INTO manages(AdminID,CustomerID)VALUES(1234,9000);
INSERT INTO manages(AdminID,CustomerID)VALUES(1234,9304);


INSERT INTO purchases(CustomerID, SubscriptionType, ProductID, DateOfPurchase)VALUES (5678, 'Basic', NULL, TO_DATE('2003/05/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));

INSERT INTO purchases(CustomerID, SubscriptionType, ProductID, DateOfPurchase)VALUES (5678, NULL, 6767, TO_DATE('2008/09/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));

INSERT INTO purchases(CustomerID, SubscriptionType, ProductID, DateOfPurchase)VALUES (9000, 'Premium', NULL, TO_DATE('2008/06/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));

INSERT INTO purchases(CustomerID, SubscriptionType, ProductID, DateOfPurchase)VALUES (5678, 'Basic', NULL, TO_DATE('2010/06/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));

INSERT INTO purchases(CustomerID, SubscriptionType, ProductID, DateOfPurchase)VALUES (9304, NULL, 6767, TO_DATE('2010/06/03 21:02:44', 'yyyy/mm/dd hh24:mi:ss'));


SELECT CONCAT(CONCAT(FULL_NAME, ' purchases '), PRODUCT) AS "INVOICE" FROM(
	SELECT FULL_NAME, SUBSCRIPTIONTYPE, EMAIL, (SELECT CONCAT(CONCAT(NAME,' costs '), SELLPRICE)  FROM PRODUCT PD WHERE PURCHASES.PRODUCTID=PD.PRODUCTID) AS "PRODUCT"
FROM  PURCHASES
FULL JOIN (SELECT CONCAT(CONCAT(FIRSTNAME, ' '),LASTNAME)
AS "FULL_NAME" , EMAIL, USERID FROM MOVIE_USER)
ON CUSTOMERID=USERID);


(SELECT purchases.customerid AS "Customer ID", CONCAT(movie_user.FirstName, movie_user.LastName) AS "Customer Name", purchases.ProductID AS "Product ID"
FROM movie_user, purchases, product
WHERE purchases.CustomerId=movie_user.UserID
	AND purchases.ProductID=product.productid)
MINUS
(SELECT purchases.customerid, CONCAT(movie_user.FirstName, movie_user.LastName) AS "Customer Name", purchases.ProductID AS "Product ID"
FROM movie_user, purchases
WHERE purchases.subscriptiontype IS NOT NULL);


SELECT USERID AS ID, CONCAT(CONCAT(FIRSTNAME, ' '), LASTNAME) AS NAME, 'purchases' as " " , p.NAME AS PRODUCT_NAME FROM MOVIE_USER, PRODUCT p WHERE USERID=(SELECT CUSTOMERID FROM PURCHASES WHERE SUBSCRIPTIONTYPE IS NULL) AND p.PRODUCTID=(SELECT PRODUCTID FROM PURCHASES WHERE SUBSCRIPTIONTYPE IS NULL);

SELECT SUM(SELLPRICE) as TOTAL FROM  (SELECT s.CUSTOMERID, SELLPRICE FROM PRODUCT p LEFT JOIN STREAMS s ON p.PRODUCTID=s.PRODUCTID WHERE s.PRODUCTID IS NOT NULL);

SELECT CUSTOMERID, CONCAT(CONCAT(NAME, ' DIRECTED BY '),DIRECTOR) as movie FROM (SELECT p.PRODUCTID,p.NAME, m.DIRECTOR FROM PRODUCT p RIGHT JOIN MOVIE m ON p.PRODUCTID=m.MOVIEID) su LEFT JOIN STREAMS s ON s.PRODUCTID=su.PRODUCTID;

exit;
EOF
