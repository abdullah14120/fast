package com.yourdomain.datatransfer.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TransferViewModel : ViewModel() {
    
    val currentSpeed = MutableLiveData<String>("0.0")
    val progress = MutableLiveData<Int>(0)
    val currentFileName = MutableLiveData<String>("جاري البدء...")

    private var lastBytes: Long = 0
    private var lastTime: Long = System.currentTimeMillis()

    fun updateTransferStatus(transferredBytes: Long, totalBytes: Long, fileName: String) {
        val currentTime = System.currentTimeMillis()
        val timeDiff = (currentTime - lastTime) / 1000.0 // بالثواني

        // تحديث السرعة كل ثانية لثبات المؤشر ومنع الرعشة
        if (timeDiff >= 1.0) {
            val bytesSentInPeriod = transferredBytes - lastBytes
            val speedMBps = (bytesSentInPeriod / timeDiff) / (1024 * 1024)
            
            currentSpeed.postValue(String.format("%.1f", speedMBps))
            
            lastBytes = transferredBytes
            lastTime = currentTime
        }

        // حساب النسبة المئوية بدقة
        if (totalBytes > 0) {
            val percent = ((transferredBytes.toDouble() / totalBytes) * 100).toInt()
            progress.postValue(percent)
        }
        
        currentFileName.postValue(fileName)
    }
}
