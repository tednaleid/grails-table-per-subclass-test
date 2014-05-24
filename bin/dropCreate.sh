#!/bin/bash

echo "Dropping/Recreating databases"
mysql -u root << EOF
drop database if exists tablepersubclass;
create database tablepersubclass;
grant all privileges on tablepersubclass.* to 'tablepersubclass'@'localhost' identified by 'tablepersubclass';

drop database if exists tablepersubclass_test;
create database tablepersubclass_test;
grant all privileges on tablepersubclass_test.* to 'tablepersubclass'@'localhost' identified by 'tablepersubclass';
EOF