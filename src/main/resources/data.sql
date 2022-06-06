REPLACE INTO `roles` VALUES (1,'USER');
REPLACE INTO `roles` VALUES (2,'ADMIN');
REPLACE INTO `roles` VALUES (3,'ACCOUNTANT');
REPLACE INTO `roles` VALUES (4,'OWNER');

REPLACE INTO `delegation_status` VALUES (1,'WAITING');
REPLACE INTO `delegation_status` VALUES (2,'APPROVED');
REPLACE INTO `delegation_status` VALUES (3,'REJECTED');
REPLACE INTO `delegation_status` VALUES (4,'FINISHED');
REPLACE INTO `delegation_status` VALUES (5,'SETTLED');
REPLACE INTO `delegation_status` VALUES (6,'CANCELLED');

REPLACE INTO `categories` VALUES (1,'Dieta');
REPLACE INTO `categories` VALUES (2,'Ryczałt');
REPLACE INTO `categories` VALUES (3,'Nocleg');

REPLACE INTO `currencies` VALUES (1,'PLN');
REPLACE INTO `currencies` VALUES (2,'EUR');
REPLACE INTO `currencies` VALUES (3,'USD');
REPLACE INTO `currencies` VALUES (4,'JPY');
REPLACE INTO `currencies` VALUES (5,'GBP');
REPLACE INTO `currencies` VALUES (6,'CHF');

SET @@global.sql_mode= 'NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION';