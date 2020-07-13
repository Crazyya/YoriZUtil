package com.yoriz.yorizutil.exception

import android.app.Activity
import android.content.Context
import java.io.ByteArrayOutputStream

/**
 * Created by yoriz
 * on 2018/12/18 3:03 PM.
 */
object ErrorDetail {

    /**
     * 获取异常崩溃后保存在txt文件内的数据
     *
     * @return 字符串异常信息
     */
    fun getErrorTXTMessage(context: Context): String {
        var result = ""
        try {
            val inputStream = context.applicationContext.openFileInput(GlobalException.crashFileName)
            val bytes = ByteArray(1024)
            val arrayOutputStream = ByteArrayOutputStream()
            while (inputStream.read(bytes) != -1) {
                arrayOutputStream.write(bytes, 0, bytes.size)
            }
            inputStream.close()
            arrayOutputStream.close()
            result = String(arrayOutputStream.toByteArray())
        } catch (ignored: Exception) {
        }

        return result
    }

    /**
     * 清空txt文件内容
     */
    fun clearErrorTxtMessage(context: Context) {
        try {
            val outputStream = context.applicationContext.openFileOutput(GlobalException.crashFileName, Activity.MODE_PRIVATE)
            outputStream.write("".toByteArray())
            outputStream.flush()
            outputStream.close()
        } catch (ignored: Exception) {
        }

    }
}
