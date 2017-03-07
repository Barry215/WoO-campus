package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class DaoGen {

    //辅助文件生成的相对路径
    public static final String DAO_PATH = "app/src/main/java-gen";
    //辅助文件的包名
    public static final String PACKAGE_NAME = "com.example.barry215.greendao";
    //数据库的版本号
    public static final int DATA_VERSION_CODE = 1;

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(DATA_VERSION_CODE,PACKAGE_NAME);
        addHelpInfoCache(schema, "HelpInfoCache");
        addPicCache(schema, "PicCache");
        addCommentCache(schema, "CommentCache");
        addUserCache(schema, "UserCache");
        addFollowCache(schema, "FollowCache");
        addHelperCache(schema, "HelperCache");
        addLoginCache(schema, "LoginCache");
        addMessageCache(schema, "MessageCache");
        addThanksCache(schema, "ThanksCache");
        addComplaintCache(schema, "ComplaintCache");
        addChatInfoCache(schema,"ChatInfoCache");

        new DaoGenerator().generateAll(schema, DAO_PATH);
    }

    private static void addChatInfoCache(Schema schema, String tableName) {
        Entity joke = schema.addEntity(tableName);

        joke.addIdProperty().primaryKey().autoincrement();

        joke.addStringProperty("senderId");

        joke.addStringProperty("receiverId");

        joke.addStringProperty("msg");

        joke.addDateProperty("time");
    }

    private static void addComplaintCache(Schema schema, String tableName) {
        Entity joke = schema.addEntity(tableName);

        joke.addIdProperty().primaryKey().autoincrement();

        joke.addStringProperty("userId");

        joke.addStringProperty("complainedUserId");

        joke.addLongProperty("helpId");

        joke.addStringProperty("complaints");

        joke.addDateProperty("complaintTime");

    }

    private static void addThanksCache(Schema schema, String tableName) {
        Entity joke = schema.addEntity(tableName);

        joke.addIdProperty().primaryKey().autoincrement();

        joke.addStringProperty("userId");

        joke.addStringProperty("thankerId");
    }

    private static void addMessageCache(Schema schema, String tableName) {
        Entity joke = schema.addEntity(tableName);

        joke.addIdProperty().primaryKey().autoincrement();

        joke.addStringProperty("senderId");

        joke.addStringProperty("receiverId");

        joke.addStringProperty("messageBody");

        joke.addDateProperty("messageTime");

        joke.addLongProperty("helpId");

        joke.addBooleanProperty("isRead");
    }

    private static void addPicCache(Schema schema, String tableName) {
        Entity joke = schema.addEntity(tableName);

        joke.addIdProperty().primaryKey().autoincrement();

        joke.addLongProperty("helpId");

        joke.addStringProperty("picName");
    }

    private static void addCommentCache(Schema schema, String tableName) {
        Entity joke = schema.addEntity(tableName);

        joke.addIdProperty().primaryKey().autoincrement();

        joke.addLongProperty("helpId");

        joke.addLongProperty("parentId");

        joke.addStringProperty("commentClass");

        joke.addStringProperty("commentBody");

        joke.addStringProperty("commentUser");

        joke.addStringProperty("commentedUser");

        joke.addDateProperty("commentTime");

    }

    private static void addUserCache(Schema schema, String tableName) {
        Entity joke = schema.addEntity(tableName);

        joke.addStringProperty("PlayId");

        joke.addStringProperty("userId").primaryKey();

        joke.addStringProperty("sex");

        joke.addStringProperty("userName");

        joke.addStringProperty("userHead");

        joke.addStringProperty("userState");

        joke.addStringProperty("userInfo");

        joke.addStringProperty("university");

        joke.addStringProperty("college");

        joke.addStringProperty("password");

        joke.addStringProperty("phone");

        joke.addStringProperty("email");

        joke.addStringProperty("alipay");
    }

    private static void addHelpInfoCache(Schema schema, String tableName) {

        Entity joke = schema.addEntity(tableName);

        joke.addIdProperty().primaryKey().autoincrement();

        joke.addStringProperty("userName");

        joke.addStringProperty("userId");

        joke.addStringProperty("userSchool");

        joke.addStringProperty("userSex");

        joke.addDateProperty("publishTime");

        joke.addStringProperty("userHead");

        joke.addStringProperty("helpState");

        joke.addStringProperty("deadTime");

        joke.addStringProperty("reward");

        joke.addStringProperty("helpBody");

        joke.addStringProperty("helpType");

        joke.addStringProperty("helpTab");

        joke.addStringProperty("chooseHelperId");

        joke.addStringProperty("systemHelp");

        joke.addStringProperty("finisherId");

        joke.addFloatProperty("rating");

    }

    private static void addFollowCache(Schema schema, String tableName) {
        Entity joke = schema.addEntity(tableName);

        joke.addIdProperty().primaryKey().autoincrement();

        joke.addStringProperty("userId");

        joke.addStringProperty("followId");

    }

    private static void addHelperCache(Schema schema, String tableName){
        Entity joke = schema.addEntity(tableName);

        joke.addIdProperty().primaryKey().autoincrement();

        joke.addLongProperty("helpId");

        joke.addStringProperty("helperId");
    }

    private static void addLoginCache(Schema schema, String tableName){
        Entity joke = schema.addEntity(tableName);

        joke.addIdProperty().primaryKey().autoincrement();

        joke.addBooleanProperty("isLogin");

        joke.addDateProperty("lastLoginTime");

        joke.addStringProperty("userId");

        joke.addStringProperty("playId");
    }
}
