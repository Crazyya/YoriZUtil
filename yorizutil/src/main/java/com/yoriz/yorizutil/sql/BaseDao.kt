package com.yoriz.yorizutil.sql

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by yoriz
 * on 2019-04-29 09:43.
 */
abstract class BaseDao {
    protected abstract val db: SQLiteDatabase
    protected abstract val helper: SQLiteOpenHelper


    fun deleteTableData(nowSQLVersion: Int): Boolean {
        db.beginTransaction()
        helper.onUpgrade(db, nowSQLVersion, nowSQLVersion)
        db.setTransactionSuccessful()
        db.endTransaction()
        return true
    }

    /**
     * 关闭db
     * 不需要关闭了
     */
    private fun closeDB(): Boolean {
        if (db.isOpen) {
            try {
                db.setTransactionSuccessful()
            } catch (e: Exception) {
            }
            try {
                db.endTransaction()
            } catch (e: Exception) {
            }
            try {
                db.close()
            } catch (e: Exception) {
            }
        }
        return db.isOpen
    }
}