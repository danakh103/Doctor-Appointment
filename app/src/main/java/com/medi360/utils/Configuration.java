package com.medi360.utils;

/**
 * Created by TPS5 on 19-Apr-16.
 */

//import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.view.inputmethod.InputMethodManager;

public class Configuration {
    // public static String DbFilePath = new File(android.environment)
    public static boolean IsLoginRowsDeleted = false;
    private static SQLiteDatabase dbKiosk = null;

    public static boolean IsValidEmail(String str) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (str.matches(emailPattern)) {
            return true;
        }
        return false;
    }

    public static Integer tryParseInt(String obj) {
        Integer retVal;
        try {
            retVal = Integer.parseInt(obj);
        } catch (Exception e) {
            retVal = 0; // or null if that is your preference
        }
        return retVal;
    }

    public static boolean IsTableExist(SQLiteDatabase db, String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[]{"table", tableName});
        if (!cursor.moveToFirst()) {
            cursor.close();
            return false;
        } else {
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
    }

    public static void InitializeDatabase(Context c) {
        dbKiosk = c.openOrCreateDatabase("kiosk", Context.MODE_PRIVATE, null);
   /*     dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS tblLogin(vName VARCHAR,vEmail VARCHAR,"
                + "vPassword VARCHAR, vPhoneNumber VARCHAR, vKioskName VARCHAR, vKioskId VARCHAR,"
                + "vClientId VARCHAR, vEcgCharge VARCHAR, vBpCharge VARCHAR);"); */

/*

        dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS tblLOGin(ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "KioskID VARCHAR, KioskPassword VARCHAR, KioskPersonEmailId VARCHAR ); ");


        dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS tblPatient1(ID INTEGER PRIMARY KEY AUTOINCREMENT,ComplainID VARCHAR," +
                " FirstName VARCHAR,LastName VARCHAR ,Gender VARCHAR, MartialStatus VARCHAR, Age VARCHAR, Address1 VARCHAR," +
                "Address2 VARCHAR,State VARCHAR, City VARCHAR, Country VARCHAR,Pincode VARCHAR,Email VARCHAR,Phone VARCHAR,Adhar VARCHAR," +
                "PanNum VARCHAR, vHeight VARCHAR, vWeight VARCHAR, vBMI VARCHAR ); ");


        dbKiosk.execSQL("DROP TABLE IF EXISTS ecgDATA;");
        dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS ecgDATA(ID INTEGER PRIMARY KEY AUTOINCREMENT,data VARCHAR); ");

*/


    /*    if (!Configuration.IsTableExist(dbKiosk, "tblKioskName")) {
            dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS tblKioskName(iKioskId VARCHAR,vKioskName VARCHAR);");
            dbKiosk.execSQL("INSERT INTO  tblKioskName VALUES('1','KORAMANGALA 1');");
            dbKiosk.execSQL("INSERT INTO  tblKioskName VALUES('2','MADIWALA 1');");
        }

        dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS tblPatient(vPatientId VARCHAR,vFirstName VARCHAR,"
                + "vLastName VARCHAR, vGender VARCHAR, vAge VARCHAR, vMartialStatus VARCHAR,"
                + "vAddressLine1 VARCHAR, vAddressLine2 VARCHAR, vCity VARCHAR, vState VARCHAR,"
                + "vPinCode VARCHAR, vAadharPanVoter VARCHAR, vHeight VARCHAR, vWeight VARCHAR, vBMI VARCHAR);"); */

      /*  dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS tblPatient(vFirstName VARCHAR,"
                + "vLastName VARCHAR, vGender VARCHAR, vAge VARCHAR, vMaritalStatus VARCHAR,"
                + "vAddressLine1 VARCHAR, vAddressLine2 VARCHAR, vCity VARCHAR, vState VARCHAR,"
                + "vPinCode VARCHAR, vAadharPanVoter VARCHAR, vHeight VARCHAR, vWeight VARCHAR, vBMI VARCHAR);"); */

/*
        if (!Configuration.IsTableExist(dbKiosk, "tblComplaintType")) {
            dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS tblComplaintType(iComplaintType VARCHAR,  vComplaintText VARCHAR);");
            dbKiosk.execSQL("INSERT INTO  tblComplaintType VALUES('1','Complaint Type 1');");
            dbKiosk.execSQL("INSERT INTO  tblComplaintType VALUES('2','Complaint Type 2');");
        }
*/

      /*  if (!Configuration.IsTableExist(dbKiosk, "masterhospital")) {
            dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS masterhospital(hospitalid INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "hospitalname VARCHAR);");
            dbKiosk.execSQL("INSERT INTO 'masterhospital' select 1 as 'hospitalid',Medi360 AS 'hospitalname'" +
                    "UNION ALL SELECT 2,'JAYADEVA'");
        }*/
        if (!Configuration.IsTableExist(dbKiosk, "masterstatus")) {
            dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS masterstatus(statusid INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "statusname VARCHAR);");
            dbKiosk.execSQL("INSERT INTO 'masterstatus' select 1 as 'statusid','Single' AS 'statusname'" +
                    "UNION ALL SELECT 2,'Married'");
        }

            if (!Configuration.IsTableExist(dbKiosk, "mastergender")) {
                dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS mastergender(genderid INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "gendername VARCHAR);");
                dbKiosk.execSQL("INSERT INTO 'mastergender' select 0 as 'genderid','MALE' AS 'gendername'" +
                        "UNION ALL SELECT 1,'FEMALE'");
            }


            if (!Configuration.IsTableExist(dbKiosk, "masterstate")) {
                dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS masterstate(stateid INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "statename VARCHAR," +
                        "countryid  INTEGER);");
                dbKiosk.execSQL("INSERT INTO 'masterstate'  select 1 AS 'stateid','Andaman and Nicobar' AS 'statename',1 AS countryid" +
                                " UNION ALL SELECT 2,'Andhra Pradesh',1" +
                                " UNION ALL SELECT 3,'Arunachal Pradesh',1" +
                                " UNION ALL SELECT 4,'Assam',1" +
                                " UNION ALL SELECT 5,'Bihar',1" +
                                " UNION ALL SELECT 6,'Chandigarh',1" +
                                " UNION ALL SELECT 7,'Chhattisgarh',1" +
                                " UNION ALL SELECT 8,'Dadra and Nagar Haveli',1" +
                                " UNION ALL SELECT 9,'Daman and Diu',1" +
                                " UNION ALL SELECT 10,'Delhi',1" +
                                " UNION ALL SELECT 11,'Goa',1" +
                                " UNION ALL SELECT 12,'Gujarat',1" +
                                " UNION ALL SELECT 13,'Haryana',1" +
                                " UNION ALL SELECT 14,'Himachal Pradesh',1" +
                                " UNION ALL SELECT 15,'Jammu and Kashmir',1" +
                                " UNION ALL SELECT 16,'Jharkhand',1" +
                                " UNION ALL SELECT 17,'Karnataka',1" +
                                " UNION ALL SELECT 18,'Kerala',1" +
                                " UNION ALL SELECT 19,'Lakshadweep',1" +
                                " UNION ALL SELECT 20,'Madhya Pradesh',1" +
                                " UNION ALL SELECT 21,'Maharashtra',1" +
                                " UNION ALL SELECT 22,'Manipur',1" +
                                " UNION ALL SELECT 23,'Meghalaya',1" +
                                " UNION ALL SELECT 24,'Mizoram',1" +
                                " UNION ALL SELECT 25,'Nagaland',1" +
                                " UNION ALL SELECT 26,'Orissa',1" +
                                " UNION ALL SELECT 27,'Puducherry',1" +
                                " UNION ALL SELECT 28,'Punjab',1" +
                                " UNION ALL SELECT 29,'Rajasthan',1" +
                                " UNION ALL SELECT 30,'Sikkim',1" +
                                " UNION ALL SELECT 31,'Tamil Nadu',1" +
                                " UNION ALL SELECT 32,'Telangana',1" +
                                " UNION ALL SELECT 33,'Tripura',1" +
                                " UNION ALL SELECT 34,'Uttarakhand',1" +
                                " UNION ALL SELECT 35,'Uttar Pradesh',1" +
                                " UNION ALL SELECT 36,'West Bengal',1"
                );
            }
            if (!Configuration.IsTableExist(dbKiosk, "mastercity")) {
                dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS mastercity(cityid INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + "cityname  VARCHAR,"
                        + "stateid INTEGER);");

                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(1,'North and Middle Andaman',1);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(2,'South Andaman',1);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(3,'Nicobar',1);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(4,'Anantapur',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(5,'Chittoor',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(6,'East Godavari',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(7,'Guntur',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(8,'Kadapa',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(9,'Krishna',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(10,'Kurnool',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(11,'Nellore',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(12,'Prakasam',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(13,'Srikakulam',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(14,'Vishakhapatnam',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(15,'Vizianagaram',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(16,'West Godavari',2);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(17,'Anjaw',3);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(18,'Changlang',3);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(19,'East Kameng',3);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(20,'Lohit',3);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(21,'Lower Subansiri',3);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(22,'Papum Pare',3);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(23,'Tirap',3);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(24,'Dibang Valley',3);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(25,'Upper Subansiri',3);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(26,'West Kameng',3);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(27,'Barpeta',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(28,'Bongaigaon',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(29,'Cachar',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(30,'Darrang',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(31,'Dhemaji',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(32,'Dhubri',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(33,'Dibrugarh',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(34,'Goalpara',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(35,'Golaghat',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(36,'Hailakandi',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(37,'Jorhat',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(38,'Karbi Anglong',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(39,'Karimganj',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(40,'Kokrajhar',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(41,'Lakhimpur',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(42,'Marigaon',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(43,'Nagaon',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(44,'Nalbari',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(45,'North Cachar Hills',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(46,'Sibsagar',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(47,'Sonitpur',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(48,'Tinsukia',4);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(49,'Araria',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(50,'Aurangabad',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(51,'Banka',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(52,'Begusarai',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(53,'Bhagalpur',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(54,'Bhojpur',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(55,'Buxar',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(56,'Darbhanga',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(57,'Purba Champaran',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(58,'Gaya',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(59,'Gopalganj',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(60,'Jamui',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(61,'Jehanabad',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(62,'Khagaria',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(63,'Kishanganj',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(64,'Kaimur',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(65,'Katihar',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(66,'Lakhisarai',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(67,'Madhubani',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(68,'Munger',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(69,'Madhepura',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(70,'Muzaffarpur',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(71,'Nalanda',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(72,'Nawada',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(73,'Patna',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(74,'Purnia',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(75,'Rohtas',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(76,'Saharsa',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(77,'Samastipur',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(78,'Sheohar',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(79,'Sheikhpura',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(80,'Saran',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(81,'Sitamarhi',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(82,'Supaul',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(83,'Siwan',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(84,'Vaishali',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(85,'Pashchim Champaran',5);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(86,'Chandigarh',6);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(87,'Bastar',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(88,'Bilaspur',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(89,'Dantewada',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(90,'Dhamtari',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(91,'Durg',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(92,'Jashpur',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(93,'Janjgir-Champa',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(94,'Korba',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(95,'Koriya',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(96,'Kanker',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(97,'Kawardha',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(98,'Mahasamund',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(99,'Raigarh',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(100,'Rajnandgaon',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(101,'Raipur',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(102,'Surguja',7);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(103,'Dadra and Nagar Haveli',8);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(104,'Diu',9);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(105,'Daman',9);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(106,'Central Delhi',10);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(107,'East Delhi',10);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(108,'New Delhi',10);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(109,'North Delhi',10);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(110,'North East Delhi',10);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(111,'North West Delhi',10);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(112,'South Delhi',10);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(113,'South West Delhi',10);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(114,'West Delhi',10);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(115,'North Goa',11);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(116,'South Goa',11);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(117,'Ahmedabad',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(118,'Amreli District',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(119,'Anand',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(120,'Banaskantha',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(121,'Bharuch',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(122,'Bhavnagar',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(123,'Dahod',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(124,'The Dangs',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(125,'Gandhinagar',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(126,'Jamnagar',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(127,'Junagadh',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(128,'Kutch',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(129,'Kheda',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(130,'Mehsana',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(131,'Narmada',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(132,'Navsari',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(133,'Patan',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(134,'Panchmahal',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(135,'Porbandar',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(136,'Rajkot',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(137,'Sabarkantha',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(138,'Surendranagar',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(139,'Surat',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(140,'Vadodara',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(141,'Valsad',12);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(142,'Ambala',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(143,'Bhiwani',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(144,'Faridabad',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(145,'Fatehabad',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(146,'Gurgaon',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(147,'Hissar',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(148,'Jhajjar',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(149,'Jind',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(150,'Karnal',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(151,'Kaithal',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(152,'Kurukshetra',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(153,'Mahendragarh',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(154,'Mewat',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(155,'Panchkula',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(156,'Panipat',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(157,'Rewari',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(158,'Rohtak',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(159,'Sirsa',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(160,'Sonepat',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(161,'Yamuna Nagar',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(162,'Palwal',13);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(163,'Bilaspur',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(164,'Chamba',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(165,'Hamirpur',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(166,'Kangra',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(167,'Kinnaur',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(168,'Kulu',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(169,'Lahaul and Spiti',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(170,'Mandi',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(171,'Shimla',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(172,'Sirmaur',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(173,'Solan',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(174,'Una',14);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(175,'Anantnag',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(176,'Badgam',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(177,'Bandipore',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(178,'Baramula',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(179,'Doda',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(180,'Jammu',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(181,'Kargil',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(182,'Kathua',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(183,'Kupwara',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(184,'Leh',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(185,'Poonch',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(186,'Pulwama',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(187,'Rajauri',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(188,'Srinagar',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(189,'Samba',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(190,'Udhampur',15);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(191,'Bokaro',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(192,'Chatra',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(193,'Deoghar',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(194,'Dhanbad',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(195,'Dumka',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(196,'Purba Singhbhum',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(197,'Garhwa',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(198,'Giridih',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(199,'Godda',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(200,'Gumla',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(201,'Hazaribagh',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(202,'Koderma',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(203,'Lohardaga',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(204,'Pakur',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(205,'Palamu',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(206,'Ranchi',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(207,'Sahibganj',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(208,'Seraikela and Kharsawan',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(209,'Pashchim Singhbhum',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(210,'Ramgarh',16);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(211,'Bidar',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(212,'Belgaum',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(213,'Bijapur',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(214,'Bagalkot',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(215,'Bellary',17);");
//                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(216,'Bangalore Rural District',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(217,'Bangalore',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(218,'Chamarajnagar',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(219,'Chikmagalur',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(220,'Chitradurga',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(221,'Davanagere',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(222,'Dharwad',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(223,'Dakshina Kannada',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(224,'Gadag',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(225,'Gulbarga',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(226,'Hassan',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(227,'Haveri District',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(228,'Kodagu',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(229,'Kolar',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(230,'Koppal',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(231,'Mandya',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(232,'Mysore',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(233,'Raichur',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(234,'Shimoga',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(235,'Tumkur',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(236,'Udupi',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(237,'Uttara Kannada',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(238,'Ramanagara',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(239,'Chikballapur',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(240,'Yadagiri',17);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(241,'Alappuzha',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(242,'Ernakulam',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(243,'Idukki',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(244,'Kollam',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(245,'Kannur',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(246,'Kasaragod',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(247,'Kottayam',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(248,'Kozhikode',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(249,'Malappuram',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(250,'Palakkad',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(251,'Pathanamthitta',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(252,'Thrissur',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(253,'Thiruvananthapuram',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(254,'Wayanad',18);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(255,'Lakshadweep',19);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(256,'Alirajpur',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(257,'Anuppur',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(258,'Ashok Nagar',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(259,'Balaghat',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(260,'Barwani',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(261,'Betul',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(262,'Bhind',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(263,'Bhopal',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(264,'Burhanpur',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(265,'Chhatarpur',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(266,'Chhindwara',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(267,'Damoh',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(268,'Datia',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(269,'Dewas',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(270,'Dhar',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(271,'Dindori',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(272,'Guna',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(273,'Gwalior',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(274,'Harda',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(275,'Hoshangabad',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(276,'Indore',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(277,'Jabalpur',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(278,'Jhabua',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(279,'Katni',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(280,'Khandwa',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(281,'Khargone',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(282,'Mandla',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(283,'Mandsaur',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(284,'Morena',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(285,'Narsinghpur',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(286,'Neemuch',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(287,'Panna',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(288,'Rewa',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(289,'Rajgarh',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(290,'Ratlam',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(291,'Raisen',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(292,'Sagar',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(293,'Satna',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(294,'Sehore',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(295,'Seoni',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(296,'Shahdol',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(297,'Shajapur',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(298,'Sheopur',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(299,'Shivpuri',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(300,'Sidhi',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(301,'Singrauli',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(302,'Tikamgarh',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(303,'Ujjain',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(304,'Umaria',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(305,'Vidisha',20);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(306,'Ahmednagar',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(307,'Akola',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(308,'Amrawati',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(309,'Aurangabad',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(310,'Bhandara',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(311,'Beed',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(312,'Buldhana',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(313,'Chandrapur',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(314,'Dhule',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(315,'Gadchiroli',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(316,'Gondiya',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(317,'Hingoli',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(318,'Jalgaon',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(319,'Jalna',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(320,'Kolhapur',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(321,'Latur',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(322,'Mumbai City',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(323,'Mumbai suburban',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(324,'Nandurbar',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(325,'Nanded',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(326,'Nagpur',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(327,'Nashik',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(328,'Osmanabad',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(329,'Parbhani',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(330,'Pune',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(331,'Raigad',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(332,'Ratnagiri',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(333,'Sindhudurg',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(334,'Sangli',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(335,'Solapur',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(336,'Satara',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(337,'Thane',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(338,'Wardha',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(339,'Washim',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(340,'Yavatmal',21);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(341,'Bishnupur',22);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(342,'Churachandpur',22);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(343,'Chandel',22);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(344,'Imphal East',22);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(345,'Senapati',22);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(346,'Tamenglong',22);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(347,'Thoubal',22);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(348,'Ukhrul',22);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(349,'Imphal West',22);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(350,'East Garo Hills',23);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(351,'East Khasi Hills',23);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(352,'Jaintia Hills',23);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(353,'Ri-Bhoi',23);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(354,'South Garo Hills',23);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(355,'West Garo Hills',23);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(356,'West Khasi Hills',23);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(357,'Aizawl',24);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(358,'Champhai',24);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(359,'Kolasib',24);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(360,'Lawngtlai',24);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(361,'Lunglei',24);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(362,'Mamit',24);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(363,'Saiha',24);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(364,'Serchhip',24);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(365,'Dimapur',25);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(366,'Kohima',25);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(367,'Mokokchung',25);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(368,'Mon',25);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(369,'Phek',25);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(370,'Tuensang',25);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(371,'Wokha',25);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(372,'Zunheboto',25);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(373,'Angul',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(374,'Boudh',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(375,'Bhadrak',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(376,'Bolangir',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(377,'Bargarh',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(378,'Baleswar',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(379,'Cuttack',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(380,'Debagarh',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(381,'Dhenkanal',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(382,'Ganjam',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(383,'Gajapati',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(384,'Jharsuguda',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(385,'Jajapur',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(386,'Jagatsinghpur',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(387,'Khordha',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(388,'Kendujhar',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(389,'Kalahandi',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(390,'Kandhamal',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(391,'Koraput',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(392,'Kendrapara',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(393,'Malkangiri',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(394,'Mayurbhanj',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(395,'Nabarangpur',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(396,'Nuapada',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(397,'Nayagarh',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(398,'Puri',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(399,'Rayagada',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(400,'Sambalpur',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(401,'Subarnapur',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(402,'Sundargarh',26);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(403,'Karaikal',27);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(404,'Mahe',27);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(405,'Puducherry',27);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(406,'Yanam',27);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(407,'Amritsar',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(408,'Bathinda',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(409,'Firozpur',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(410,'Faridkot',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(411,'Fatehgarh Sahib',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(412,'Gurdaspur',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(413,'Hoshiarpur',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(414,'Jalandhar',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(415,'Kapurthala',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(416,'Ludhiana',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(417,'Mansa',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(418,'Moga',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(419,'Mukatsar',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(420,'Nawan Shehar',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(421,'Patiala',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(422,'Rupnagar',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(423,'Sangrur',28);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(424,'Ajmer',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(425,'Alwar',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(426,'Bikaner',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(427,'Barmer',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(428,'Banswara',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(429,'Bharatpur',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(430,'Baran',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(431,'Bundi',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(432,'Bhilwara',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(433,'Churu',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(434,'Chittorgarh',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(435,'Dausa',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(436,'Dholpur',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(437,'Dungapur',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(438,'Ganganagar',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(439,'Hanumangarh',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(440,'Juhnjhunun',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(441,'Jalore',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(442,'Jodhpur',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(443,'Jaipur',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(444,'Jaisalmer',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(445,'Jhalawar',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(446,'Karauli',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(447,'Kota',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(448,'Nagaur',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(449,'Pali',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(450,'Pratapgarh',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(451,'Rajsamand',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(452,'Sikar',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(453,'Sawai Madhopur',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(454,'Sirohi',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(455,'Tonk',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(456,'Udaipur',29);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(457,'East Sikkim',30);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(458,'North Sikkim',30);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(459,'South Sikkim',30);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(460,'West Sikkim',30);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(461,'Ariyalur',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(462,'Chennai',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(463,'Coimbatore',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(464,'Cuddalore',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(465,'Dharmapuri',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(466,'Dindigul',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(467,'Erode',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(468,'Kanchipuram',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(469,'Kanyakumari',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(470,'Karur',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(471,'Madurai',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(472,'Nagapattinam',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(473,'The Nilgiris',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(474,'Namakkal',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(475,'Perambalur',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(476,'Pudukkottai',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(477,'Ramanathapuram',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(478,'Salem',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(479,'Sivagangai',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(480,'Tiruppur',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(481,'Tiruchirappalli',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(482,'Theni',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(483,'Tirunelveli',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(484,'Thanjavur',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(485,'Thoothukudi',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(486,'Thiruvallur',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(487,'Thiruvarur',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(488,'Tiruvannamalai',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(489,'Vellore',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(490,'Villupuram',31);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(491,'Adilabad',32);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(492,'Hyderabad',32);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(493,'Karimnagar',32);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(494,'Khammam',32);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(495,'Mahbubnagar',32);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(496,'Medak',32);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(497,'Nalgonda',32);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(498,'Nizamabad',32);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(499,'Rangareddi',32);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(500,'Warangal',32);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(501,'Dhalai',33);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(502,'North Tripura',33);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(503,'South Tripura',33);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(504,'West Tripura',33);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(505,'Almora',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(506,'Bageshwar',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(507,'Chamoli',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(508,'Champawat',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(509,'Dehradun',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(510,'Haridwar',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(511,'Nainital',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(512,'Pauri Garhwal',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(513,'Pithoragharh',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(514,'Rudraprayag',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(515,'Tehri Garhwal',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(516,'Udham Singh Nagar',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(517,'Uttarkashi',34);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(518,'Agra',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(519,'Allahabad',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(520,'Aligarh',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(521,'Ambedkar Nagar',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(522,'Auraiya',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(523,'Azamgarh',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(524,'Barabanki',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(525,'Badaun',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(526,'Bagpat',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(527,'Bahraich',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(528,'Bijnor',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(529,'Ballia',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(530,'Banda',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(531,'Balrampur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(532,'Bareilly',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(533,'Basti',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(534,'Bulandshahr',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(535,'Chandauli',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(536,'Chitrakoot',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(537,'Deoria',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(538,'Etah',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(539,'Kanshiram Nagar',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(540,'Etawah',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(541,'Firozabad',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(542,'Farrukhabad',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(543,'Fatehpur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(544,'Faizabad',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(545,'Gautam Buddha Nagar',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(546,'Gonda',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(547,'Ghazipur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(548,'Gorkakhpur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(549,'Ghaziabad',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(550,'Hamirpur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(551,'Hardoi',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(552,'Mahamaya Nagar',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(553,'Jhansi',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(554,'Jalaun',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(555,'Jyotiba Phule Nagar',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(556,'Jaunpur District',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(557,'Kanpur Dehat',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(558,'Kannauj',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(559,'Kanpur Nagar',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(560,'Kaushambi',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(561,'Kushinagar',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(562,'Lalitpur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(563,'Lakhimpur Kheri',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(564,'Lucknow',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(565,'Mau',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(566,'Meerut',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(567,'Maharajganj',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(568,'Mahoba',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(569,'Mirzapur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(570,'Moradabad',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(571,'Mainpuri',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(572,'Mathura',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(573,'Muzaffarnagar',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(574,'Pilibhit',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(575,'Pratapgarh',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(576,'Rampur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(577,'Rae Bareli',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(578,'Saharanpur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(579,'Sitapur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(580,'Shahjahanpur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(581,'Sant Kabir Nagar',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(582,'Siddharthnagar',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(583,'Sonbhadra',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(584,'Sant Ravidas Nagar',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(585,'Sultanpur',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(586,'Shravasti',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(587,'Unnao',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(588,'Varanasi',35);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(589,'Birbhum',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(590,'Bankura',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(591,'Bardhaman',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(592,'Darjeeling',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(593,'Dakshin Dinajpur',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(594,'Hooghly',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(595,'Howrah',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(596,'Jalpaiguri',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(597,'Cooch Behar',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(598,'Kolkata',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(599,'Malda',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(600,'Midnapore',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(601,'Murshidabad',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(602,'Nadia',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(603,'North 24 Parganas',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(604,'South 24 Parganas',36);");
                dbKiosk.execSQL("INSERT INTO  mastercity VALUES(605,'Purulia',36);");

            }

            if (!Configuration.IsTableExist(dbKiosk, "mastercountry")) {
                dbKiosk.execSQL("CREATE TABLE IF NOT EXISTS mastercountry(Countryid INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "countryname VARCHAR);");
                dbKiosk.execSQL("INSERT INTO `mastercountry`" +
                        "  select  1 AS 'Countryid','India' AS 'countryname'" +
                        "UNION ALL SELECT 2,'Africa'" +
                        "UNION ALL SELECT 3,'America'" +
                        "UNION ALL SELECT 4,'Japan'" +
                        "UNION ALL SELECT 5,'Kenya'" +
                        "UNION ALL SELECT 6,'Spain'" +
                        "UNION ALL SELECT 7,'Germany'" +
                        "UNION ALL SELECT 8,'Sweden'" +
                        "UNION ALL SELECT 9,'Italy'" +
                        "UNION ALL SELECT 10,'Indonesia'" +
                        "UNION ALL SELECT 11,'France'" +
                        "UNION ALL SELECT 12,'China'" +
                        "UNION ALL SELECT 13,'U A E'" +
                        "UNION ALL SELECT 14,'Turkmenistan'" +
                        "UNION ALL SELECT 15,'South Africa'" +
                        "UNION ALL SELECT 16,'VIETNAM'" +
                        "UNION ALL SELECT 17,'KOREA'" +
                        "UNION ALL SELECT 18,'QATAR'" +
                        "UNION ALL SELECT 19,'DUBAI'" +
                        "UNION ALL SELECT 20,'eryt'" +
                        "UNION ALL SELECT 21,'EGYPT'" +
                        "UNION ALL SELECT 22,'Test country'" +
                        "UNION ALL SELECT 23,'new country'" +
                        "UNION ALL SELECT 24,'another test country'");
            }

        }
/*
    public static String GetEmrDataDesc(int iEMRTypeID) {
        String objResult = "";
        switch (iEMRTypeID) {
            case 1:
                objResult = "ECG DATA";
                break;
            case 2:
                objResult = "SPO2 DATA";
                break;
            case 3:
                objResult = "BP DATA";
                break;
            case 4:
                objResult = "FETAL DATA";
                break;
            case 5:
                objResult = "SPIRO DATA";
                break;
            case 6:
                objResult = "STETH DATA";
                break;
            case 7:
                objResult = "BODY DATA";
                break;
            case 8:
                objResult = "ANALYZER DATA";
                break;
            case 9:
                objResult = "GLUCO DATA";
                break;
            case 10:
                objResult = "ENT DATA";
                break;

        }
        return objResult + String.valueOf(System.currentTimeMillis());
    }
*/

//    public static int GetEmrDataId(String sEmrDataDesc) {
//        int iEmrDataId = 0;
//
//        if(sEmrDataDesc == "ECG DATA") {
//            iEmrDataId = 1;
//        } else if(sEmrDataDesc == "SPO2 DATA") {
//            iEmrDataId = 2;
//        } else if(sEmrDataDesc == "BP DATA") {
//            iEmrDataId = 3;
//        } else if(sEmrDataDesc == "FETAL DATA") {
//            iEmrDataId = 4;
//        } else if(sEmrDataDesc == "SPIRO DATA") {
//            iEmrDataId = 5;
//        } else if(sEmrDataDesc == "STETH DATA") {
//            iEmrDataId = 6;
//        } else if(sEmrDataDesc == "BODY DATA") {
//            iEmrDataId = 7;
//        } else if(sEmrDataDesc == "ANALYZER DATA") {
//            iEmrDataId = 8;
//        } else if(sEmrDataDesc == "GLUCO DATA") {
//            iEmrDataId = 9;
//        } else if(sEmrDataDesc == "ENT DATA") {
//            iEmrDataId = 10;
//        }
//
//
//        /*
//        switch (sEmrDataDesc) {
//            case "ECG DATA":
//                iEmrDataId = 1;
//                break;
//            case "SPO2 DATA":
//                iEmrDataId = 2;
//                break;
//            case "BP DATA":
//                iEmrDataId = 3;
//                break;
//            case "FETAL DATA":
//                iEmrDataId = 4;
//                break;
//            case "SPIRO DATA":
//                iEmrDataId = 5;
//                break;
//            case "STETH DATA":
//                iEmrDataId = 6;
//                break;
//            case "BODY DATA":
//                iEmrDataId = 7;
//                break;
//            case "ANALYZER DATA":
//                iEmrDataId = 8;
//                break;
//            case "GLUCO DATA":
//                iEmrDataId = 9;
//                break;
//            case "ENT DATA":
//                iEmrDataId = 10;
//                break;
//
//        } */
//        return iEmrDataId;
//    }
//
//    public static boolean isInternetOn(Activity ob) {
//
//        // get Connectivity Manager object to check connection
//        ConnectivityManager connec =
//                (ConnectivityManager) ob.getSystemService(ob.getBaseContext().CONNECTIVITY_SERVICE);
//
//        if (connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
//                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
//                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
//                connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {
//            Toast.makeText(ob, " Connected ", Toast.LENGTH_LONG).show();
//            return true;
//        } else if (
//                connec.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
//                        connec.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {
//            Toast.makeText(ob, " Not Connected ", Toast.LENGTH_LONG).show();
//            return false;
//        }
//        return false;
//    }
//
//    public static void PerformSync(Activity ob) {
//        final Activity objActivity = ob;
//        AlertDialog.Builder builder = new AlertDialog.Builder(objActivity);
//        builder.setMessage("sync");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                if (isInternetOn(objActivity)) {
//                    Toast.makeText(objActivity.getBaseContext(), " Performing sync ", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
//
    public static SQLiteDatabase getDatabase() {
        return dbKiosk;
    }


//    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
//    }

    public static String CurrentPatientId = "";
    public static String COPM_ID="";
    public static String path =""; // this is for folder creation



    public static String Kiosk_person_emailId="";
    public static String Kiosk_ID="";
    public static String Kiosk_Password="";

    public static String PatientId="";
    public static String ComplaintID="";
    public static String FirstName="";
    public static String LastName="";
    public static Integer gender=null;
    public static String Address1="";
    public static String Address2="";
    public static String city="";
    public static String state="";
    public static String country="";
    public static String pincode="";
    public static String phoneNum="";
    public static String email_id="";
    public static String height="";
    public static String weight="";
    public static String bmi="";
    public static String adhar="";
    public static String pan="";
    public static String age="";
    public static Bitmap photo;
    public static String martialstatus="";
    public static String sys_data="";
    public static String dia_data="";
    public static String pulse_data="";
    public static String cityid="";
    public static String countryid="";
    public static String stateid="";
    public static String glucose_data="";
    public static String compliant_description="";

    public static String oxy_value="0";
    public static String pulse_value="0";
    public static String data="";  //this is for spo2, to check whether device is On.
    public static Boolean inserting =false; //this is for ECG, to check whether its generating byte data
    public static String ecgfilename="";
    public static String server="LIVE";

}

