SET search_path TO client,public;
DELETE FROM loyalty_class;
insert into loyalty_class (minimum_points,reduction,loyalty_class_id,name) values (0,0,1,'bronze');
insert into loyalty_class (minimum_points,reduction,loyalty_class_id,name) values (1000,0.05,2,'silver');
insert into loyalty_class(minimum_points,reduction,loyalty_class_id,name) values (5000,0.10,3,'gold');
insert into loyalty_class (minimum_points,reduction,loyalty_class_id,name) values (10000,0.20,4,'platinum');


