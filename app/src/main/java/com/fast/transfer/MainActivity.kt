class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // إعداد المستمعين للأزرار بحركات أنيميشن ناعمة
        setupClickListeners()
        
        // طلب الأذونات الأساسية فور الدخول
        checkAndRequestPermissions()
    }

    private fun setupClickListeners() {
        binding.cardSend.setOnClickListener {
            startConnectActivity("SENDER")
        }

        binding.cardReceive.setOnClickListener {
            startConnectActivity("RECEIVER")
        }

        binding.btnConnectUsb.setOnClickListener {
            // كود تفعيل وضع USB (سنبرمجه لاحقاً)
            Toast.makeText(this, "جاري البحث عن كابل USB...", Toast.LENGTH_SHORT).show()
        }
    }

    private fun startConnectActivity(mode: String) {
        val intent = Intent(this, ConnectActivity::class.java)
        intent.putExtra("MODE", mode)
        startActivity(intent)
        // إضافة أنيميشن احترافي عند الانتقال
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }

    private fun checkAndRequestPermissions() {
        val permissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_CALL_LOG
        )
        
        // أذونات إضافية لأندرويد 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.NEARBY_WIFI_DEVICES)
        }

        ActivityCompat.requestPermissions(this, permissions.toTypedArray(), 100)
        
        // طلب إذن الوصول الشامل للملفات (خاص بأندرويد 11+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
        }
    }
}
