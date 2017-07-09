insert into PRODUCT_TYPE (name) values ('SOAP');
insert into PRODUCT_TYPE (name) values ('OIL');
insert into COUNTRY (name,COUNTRY_CODE) values ('India','IN');
insert into COUNTRY (name,COUNTRY_CODE) values ('USA','US');
insert into PRODUCT (name, DISPLAY_NAME, TYPE_ID) values  ('LIFEBOY', 'Life Boy',(SELECT ID FROM PRODUCT_TYPE WHERE NAME = 'SOAP'));
insert into PRODUCT (name, DISPLAY_NAME, TYPE_ID) values  ('LUX', 'Lux',(SELECT ID FROM PRODUCT_TYPE WHERE NAME = 'SOAP'));
insert into PRODUCT (name, DISPLAY_NAME, TYPE_ID) values  ('OLIVE', 'Olive Oil',(SELECT ID FROM PRODUCT_TYPE WHERE NAME = 'OIL'));
insert into PRODUCT_COUNTRY_XREF (PRODUCT_ID,COUNTRY_ID) values ( SELECT ID FROM PRODUCT WHERE NAME = 'LIFEBOY', SELECT ID FROM COUNTRY WHERE COUNTRY_CODE = 'IN');
insert into PRODUCT_COUNTRY_XREF (PRODUCT_ID,COUNTRY_ID) values ( SELECT ID FROM PRODUCT WHERE NAME = 'LUX', SELECT ID FROM COUNTRY WHERE COUNTRY_CODE = 'IN');
insert into PRODUCT_COUNTRY_XREF (PRODUCT_ID,COUNTRY_ID) values ( SELECT ID FROM PRODUCT WHERE NAME = 'LUX', SELECT ID FROM COUNTRY WHERE COUNTRY_CODE = 'US');
insert into PRODUCT_COUNTRY_XREF (PRODUCT_ID,COUNTRY_ID) values ( SELECT ID FROM PRODUCT WHERE NAME = 'OLIVE', SELECT ID FROM COUNTRY WHERE COUNTRY_CODE = 'US');