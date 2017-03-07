package com.example.barry215.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.example.barry215.greendao.CommentCache;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMMENT_CACHE".
*/
public class CommentCacheDao extends AbstractDao<CommentCache, Long> {

    public static final String TABLENAME = "COMMENT_CACHE";

    /**
     * Properties of entity CommentCache.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property HelpId = new Property(1, Long.class, "helpId", false, "HELP_ID");
        public final static Property ParentId = new Property(2, Long.class, "parentId", false, "PARENT_ID");
        public final static Property CommentClass = new Property(3, String.class, "commentClass", false, "COMMENT_CLASS");
        public final static Property CommentBody = new Property(4, String.class, "commentBody", false, "COMMENT_BODY");
        public final static Property CommentUser = new Property(5, String.class, "commentUser", false, "COMMENT_USER");
        public final static Property CommentedUser = new Property(6, String.class, "commentedUser", false, "COMMENTED_USER");
        public final static Property CommentTime = new Property(7, java.util.Date.class, "commentTime", false, "COMMENT_TIME");
    };


    public CommentCacheDao(DaoConfig config) {
        super(config);
    }
    
    public CommentCacheDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMMENT_CACHE\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"HELP_ID\" INTEGER," + // 1: helpId
                "\"PARENT_ID\" INTEGER," + // 2: parentId
                "\"COMMENT_CLASS\" TEXT," + // 3: commentClass
                "\"COMMENT_BODY\" TEXT," + // 4: commentBody
                "\"COMMENT_USER\" TEXT," + // 5: commentUser
                "\"COMMENTED_USER\" TEXT," + // 6: commentedUser
                "\"COMMENT_TIME\" INTEGER);"); // 7: commentTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMMENT_CACHE\"";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, CommentCache entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        Long helpId = entity.getHelpId();
        if (helpId != null) {
            stmt.bindLong(2, helpId);
        }
 
        Long parentId = entity.getParentId();
        if (parentId != null) {
            stmt.bindLong(3, parentId);
        }
 
        String commentClass = entity.getCommentClass();
        if (commentClass != null) {
            stmt.bindString(4, commentClass);
        }
 
        String commentBody = entity.getCommentBody();
        if (commentBody != null) {
            stmt.bindString(5, commentBody);
        }
 
        String commentUser = entity.getCommentUser();
        if (commentUser != null) {
            stmt.bindString(6, commentUser);
        }
 
        String commentedUser = entity.getCommentedUser();
        if (commentedUser != null) {
            stmt.bindString(7, commentedUser);
        }
 
        java.util.Date commentTime = entity.getCommentTime();
        if (commentTime != null) {
            stmt.bindLong(8, commentTime.getTime());
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public CommentCache readEntity(Cursor cursor, int offset) {
        CommentCache entity = new CommentCache( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1), // helpId
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2), // parentId
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // commentClass
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // commentBody
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // commentUser
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // commentedUser
            cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)) // commentTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, CommentCache entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHelpId(cursor.isNull(offset + 1) ? null : cursor.getLong(offset + 1));
        entity.setParentId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
        entity.setCommentClass(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCommentBody(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCommentUser(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setCommentedUser(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setCommentTime(cursor.isNull(offset + 7) ? null : new java.util.Date(cursor.getLong(offset + 7)));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(CommentCache entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(CommentCache entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
