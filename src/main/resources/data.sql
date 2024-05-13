SET search_path TO client,public;
DELETE FROM loyality_class;
insert into loyality_class (minimum_points,reduction,loyality_class_id,name) values (0,0,1,'brons');
insert into loyality_class (minimum_points,reduction,loyality_class_id,name) values (1000,0.05,2,'zilver');
insert into loyality_class(minimum_points,reduction,loyality_class_id,name) values (5000,0.10,3,'goud');
insert into loyality_class (minimum_points,reduction,loyality_class_id,name) values (10000,0.20,4,'platina')


