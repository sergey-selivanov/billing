-- mysql timezones cheat sheet
-- https://stackoverflow.com/questions/19023978/should-mysql-have-its-timezone-set-to-utc

-- https://rietta.com/blog/2012/03/03/best-data-types-for-currencymoney-in/


drop table if exists Customer;
create table Customer (
    id int not null auto_increment primary key,

    name varchar(255) not null default '',
    description text,
    address text,
    isActive boolean not null default 1,

    originalId int unique
);


drop table if exists Project;
create table Project (
    id int not null auto_increment primary key,

    name varchar(255) not null default '',
    customerId int, -- allow project without customer
    defaultRate decimal(18, 4) not null default 0,
    createdDateUTC datetime not null,    -- no fractional seconds by default here. This is original creation date.
    isActive boolean not null default 1,

    originalId int unique,
    originalCustomerId int

    -- foreign key (customerId) references Customer (id)
);


drop table if exists Invoice;
create table Invoice (
    id int not null auto_increment primary key,

    name varchar(255) not null default '',	-- invoice No
    description text,	-- title
    invoiceDate date,
    createdDateUTC datetime not null, -- using sql, insert utc_timestamp here instead of current_timestamp

    customerId int not null,
    projectId int, -- allow invoice without project, e.g only custom items

    workFromDate date not null,
    workToDate date not null,

    invoicedTimeHours decimal(9, 2) not null default 0,

    total decimal(18, 4) not null default 0,

    toAddress text,
    payableTo varchar(255),
    terms varchar(255),
    mailCheckToAddress text


--    foreign key (recipientId) references Recipient (id),
--    foreign key (customerId) references Customer (id),
--    foreign key (projectId) references Project (id)
);

drop table if exists InvoiceItem;
create table InvoiceItem (
    id int not null auto_increment primary key,
    invoiceId int not null,

    taskNumber varchar(32) not null default '',
    originalTaskId int not null default 0,
    name varchar(255) not null default '',

    invoicedTimeHours decimal(9, 2) not null default 0,
    rate decimal(18, 4) not null default 0,
    total decimal(18, 4) not null default 0,

    isCustom boolean not null default 0,
    isIncluded boolean not null default 1

    -- foreign key (invoiceId) references Invoice (id)
);

drop table if exists Settings;
create table Settings (
    id int not null auto_increment primary key,

    name varchar(255) not null unique,
    val text
);

-- insert unicode chars
-- https://stackoverflow.com/questions/3632410/mysql-unicode-literals
-- https://r12a.github.io/app-conversion/
-- concat('sdcsd ', _utf8 x'E28093', ' sdfvdf')

insert into Settings (name, val) values
('PAYMENT_RECIPIENT_TITLE', 'Company Intl. Inc');

insert into Settings (name, val) values
('PAYMENT_TERMS', 'On receipt');

-- mail-check-to, en dash
insert into Settings (name, val) values
('PAYMENT_RECIPIENT_ADDRESS', concat('Company Inc.
Attn: ABCD ', _utf8 x'E28093', ' J. Doe
1234 Long Street
City, ST 12345'));
