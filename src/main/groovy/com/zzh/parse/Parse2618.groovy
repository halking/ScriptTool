package com.zzh.parse

def map = [487:"RENG",
491:"WENG",
495:"WSIZ",
499:"PENG",
511:"GCAR",
523:"WRAP",
527:"VATI",
535:"NDSK",
539:"NSTK"]

def sql = """INSERT INTO `payment_rules` (`COUNTRY`, `BRAND`, `DELIVERY_TYPE_ID`, `DELIVERY_DATE_OPTION`, `SERVICE_TYPE_ID`, `ORDER_TYPE_ID`, `PAYMENT_TYPE_ID`, `AUTHORIZED`, `CC_TYPE_ID`) VALUES
('CN', 'MB', 171, 0, NULL, 183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 1, NULL, 183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 0, 523,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 0, 495,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 0, 527,  183, @PAYMENT_TYPE_ID, 0, NULL),
('CN', 'MB', 171, 0, 511,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 0, 535,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 0, 539,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 1, 523,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 1, 495,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 1, 527,  183, @PAYMENT_TYPE_ID, 0, NULL),
('CN', 'MB', 171, 1, 511,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 1, 535,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 1, 539,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 0, 487,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 1, 487,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 0, 499,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 1, 499,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 0, 491,  183, @PAYMENT_TYPE_ID, 1, NULL),
('CN', 'MB', 171, 1, 491,  183, @PAYMENT_TYPE_ID, 1, NULL);"""

map.each {key, value ->
    println "select @${value}_SERVICE_TYPE_ID := id from SERVICE_TYPE where COUNTRY = 'MB' and COUNTRY = 'CN' and CODE = '${value}';"
    sql = sql.replaceAll(key+",", "@${value}_SERVICE_TYPE_ID,")
}

println sql