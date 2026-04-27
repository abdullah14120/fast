package com.fast.transfer.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.yourdomain.datatransfer.viewmodels.TransferViewModel
import kotlinx.coroutines.*

class TransferService : Service() {

    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)

    // سنستخدم هذا المتغير للوصول إلى ViewModel (طريقة الـ Singleton للتبسيط هنا)
    companion object {
        var viewModel: TransferViewModel? = null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // تشغيل إشعار Foreground لضمان عدم توقف النظام
        startForeground(1, createNotification("جاري نقل البيانات..."))

        // بدء عملية النقل في خيط منفصل (Background Thread)
        startDataTransfer()

        return START_STICKY
    }

    private fun startDataTransfer() {
        serviceScope.launch {
            try {
                // محاكاة أو استدعاء محرك السوكيت (Socket Engine)
                var bytesTransferred = 0L
                val totalSize = 1000000000L // مثال: 1 جيجابايت
                
                while (bytesTransferred < totalSize) {
                    delay(500) // تحديث كل نصف ثانية
                    bytesTransferred += 50000000 // محاكاة نقل 50 ميجا
                    
                    // تحديث الـ ViewModel مباشرة
                    viewModel?.updateTransferStatus(
                        bytesTransferred, 
                        totalSize, 
                        "Video_2024.mp4"
                    )
                }
            } catch (e: Exception) {
                // معالجة الخطأ وإعادة المحاولة التلقائية
            }
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel() // إيقاف كافة العمليات عند تدمير الخدمة
    }
}
