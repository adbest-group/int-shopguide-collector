-- 插入测试商城信息
insert into MALLS (name,domain,logo_image_url) values ('Amazon','amazon.com','amazon_logo.png');
insert into MALLS (name,domain,logo_image_url) values ('eBay','ebay.com','ebay_logo.png');
insert into MALLS (name,domain,logo_image_url) values ('Walmart','walmart.com','walmart_logo.png');
insert into MALLS (name,domain,logo_image_url) values ('Neiman Marcus','neimanmarcus.com','neimanmarcus_logo.png');

-- 插入测试热词
insert into HOTWORDS (hotword,seq) values ('baby',1);
insert into HOTWORDS (hotword,seq) values ('clothing',2);
insert into HOTWORDS (hotword,seq) values ('watch',3);
insert into HOTWORDS (hotword,seq) values ('beauty',4);

--插入测试类别
insert into CATEGORYS(name,seq) values ('Clothing, Jewelry & Bags',1);
insert into CATEGORYS(name,seq) values ('Beauty',2);
insert into CATEGORYS(name,seq) values ('Personal Care',3);
insert into CATEGORYS(name,seq) values ('Nutrition',4);
insert into CATEGORYS(name,seq) values ('Baby',5);
insert into CATEGORYS(name,seq) values ('Kids',6);
insert into CATEGORYS(name,seq) values ('At Home',7);