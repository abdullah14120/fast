package com.fast.transfer

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.yourdomain.datatransfer.databinding.ActivityTransferLiveBinding
import com.yourdomain.datatransfer.viewmodels.TransferViewModel

class TransferActivity : AppCompatActivity() {

    // 1. الربط بالـ ViewModel باستخدام الطريقة الحديثة
    private val viewModel: TransferViewModel by viewModels()
    
    // 2. استخدام View Binding للوصول للعناصر بسرعة فائقة
    private lateinit var binding: ActivityTransferLiveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // تفعيل الـ Binding
        binding = ActivityTransferLiveBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 3. منع انطفاء الشاشة أثناء النقل (ضروري جداً لضمان عدم الانقطاع)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

        // 4. إعداد القائمة الحية للملفات
        setupRecyclerView()

        // 5. بدء مراقبة البيانات (الربط الفعلي)
        observeTransferData()
    }

    private fun setupRecyclerView() {
        binding.rvLiveFiles.layoutManager = LinearLayoutManager(this)
        // هنا سنربط الـ Adapter لاحقاً لعرض أسماء الملفات التي تمر حالياً
    }

    private fun observeTransferData() {
        // مراقبة سرعة النقل وتحديث العداد الدائري
        viewModel.currentSpeed.observe(this) { speed ->
            binding.tvCurrentSpeed.text = speed
        }

        // مراقبة شريط التقدم الكلي
        viewModel.progress.observe(this) { percent ->
            binding.mainProgressBar.progress = percent
            binding.tvProgressPercent.text = "جاري نقل البيانات.. $percent%"
        }

        // مراقبة اسم الملف الحالي
        viewModel.currentFileName.observe(this) { fileName ->
            // تحديث اسم الملف في الواجهة (يمكن إضافته لنص توضيحي)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // تنظيف الموارد عند إغلاق الشاشة
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}
