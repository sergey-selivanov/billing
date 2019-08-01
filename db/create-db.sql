CREATE DATABASE billing;
ALTER DATABASE billing DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
GRANT ALL PRIVILEGES ON billing.* TO 'billing'@'localhost' IDENTIFIED by 'billing';
flush privileges;

GRANT ALL PRIVILEGES ON billing.* TO 'billing'@'192.168.101.0/255.255.255.0' IDENTIFIED by 'billing';
flush privileges;

-- -----------------------

CREATE DATABASE billing_jobcard;
ALTER DATABASE billing_jobcard DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
GRANT ALL PRIVILEGES ON billing_jobcard.* TO 'billing'@'localhost' IDENTIFIED by 'billing';
flush privileges;

GRANT ALL PRIVILEGES ON billing_jobcard.* TO 'billing'@'192.168.101.0/255.255.255.0' IDENTIFIED by 'billing';
flush privileges;

-- -----------------------

CREATE DATABASE billing_jira;
ALTER DATABASE billing_jira DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
GRANT ALL PRIVILEGES ON billing_jira.* TO 'billing'@'localhost' IDENTIFIED by 'billing';
flush privileges;

GRANT ALL PRIVILEGES ON billing_jira.* TO 'billing'@'192.168.101.0/255.255.255.0' IDENTIFIED by 'billing';
flush privileges;
