ALTER TABLE colltrain.`main` ADD `discountCount` INT NULL DEFAULT '0' AFTER `refundAmount`, ADD `discountAmount` FLOAT NULL DEFAULT '0' AFTER `discountCount`, ADD `vatExemptCount` INT NULL DEFAULT '0' AFTER `discountAmount`, ADD `vatExemptAmount` FLOAT NULL DEFAULT '0' AFTER `vatExemptCount`, ADD `vat12Count` INT NULL DEFAULT '0' AFTER `vatExemptAmount`, ADD `vat12Amount` FLOAT NULL DEFAULT '0' AFTER `vat12Count`, ADD `vatsaleCount` INT NULL DEFAULT '0' AFTER `vat12Amount`, ADD `vatsaleAmount` FLOAT NULL DEFAULT '0' AFTER `vatsaleCount`;
ALTER TABLE zread.`main` CHANGE `datetimeOut` `datetimeOut` DATETIME NULL DEFAULT NULL;

ALTER TABLE colltrain.`main` ADD `grossAmount` DOUBLE NULL DEFAULT '0' AFTER `totalAmount`;
ALTER TABLE colltrain.`main` ADD `grossCount` INT NULL DEFAULT '0' AFTER `grossAmount`;

ALTER TABLE carpark.`exit_trans` ADD `GrossAmount` FLOAT NULL DEFAULT NULL AFTER `Amount`;  
ALTER TABLE carpark.`exit_trans` ADD `discount` DOUBLE NULL AFTER `GrossAmount`, ADD `vat12` DOUBLE NULL AFTER `discount`, ADD `vatsale` DOUBLE NULL AFTER `vat12`, ADD `vatexempt` DOUBLE NULL AFTER `vatsale`;