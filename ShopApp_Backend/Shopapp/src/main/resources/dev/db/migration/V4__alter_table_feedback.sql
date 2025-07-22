SELECT COUNT(*)
INTO @columnCount
FROM INFORMATION_SCHEMA.COLUMNS
WHERE TABLE_NAME = 'feedback'
    AND TABLE_SCHEMA = 'shopapp'
    AND COLUMN_NAME = 'created_at';

SET @alterStatement = IF(@columnCount = 0, 'ALTER TABLE feedback ADD COLUMN created_at DATETIME;','');

PREPARE stmt FROM @alterStatement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;