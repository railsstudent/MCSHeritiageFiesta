DROP TABLE IF EXISTS ALBUMIMAGE;
DROP TABLE IF EXISTS ALBUMCATEGORY;



CREATE TABLE ALBUMCATEGORY (
    ID INT NOT NULL auto_increment,
    NAME VARCHAR(100),
    CREATEDAT TIMESTAMP NOT NULL,
    COLSEQ INT NOT NULL,
    PRIMARY KEY (ID)
) CHARACTER SET utf8 COLLATE  UTF8_unicode_ci; 


CREATE TABLE ALBUMIMAGE (
    ID INT NOT NULL AUTO_INCREMENT,
    CATEGORY_ID INT NOT NULL,
    URL VARCHAR(500) NOT NULL,
    COLSEQ INT,
    PRIMARY KEY (ID),
    constraint foreign key fk_category_image (CATEGORY_ID)
         references ALBUMCATEGORY (ID)
) CHARACTER SET utf8 COLLATE  UTF8_unicode_ci; 

