class ConnectActivity : AppCompatActivity() {

    private lateinit var mode: String // "SENDER" أو "RECEIVER"
    private lateinit var manager: WifiP2pManager
    private lateinit var channel: WifiP2pManager.Channel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        mode = intent.getStringExtra("MODE") ?: "SENDER"
        
        // تهيئة الواي فاي دايركت
        manager = getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager
        channel = manager.initialize(this, mainLooper, null)

        if (mode == "RECEIVER") {
            setupAsReceiver()
        } else {
            setupAsSender()
        }
    }

    private fun setupAsReceiver() {
        // 1. المستقبل ينشئ المجموعة ويكون هو الـ Group Owner
        manager.createGroup(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                manager.requestGroupInfo(channel) { group ->
                    val ssid = group.networkName
                    val password = group.passphrase
                    // توليد الباركود (الكود الذي كتبناه سابقاً)
                    displayQRCode(ssid, password)
                }
            }
            override fun onFailure(p0: Int) {
                Toast.makeText(this@ConnectActivity, "فشل إنشاء الشبكة", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupAsSender() {
        // 2. المرسل يفتح الكاميرا لمسح الباركود
        val cameraPreview = findViewById<PreviewView>(R.id.cameraPreview)
        cameraPreview.visibility = View.VISIBLE
        findViewById<ImageView>(R.id.imgQrCode).visibility = View.GONE
        
        startCameraAndScanQR()
    }
    
    // سيتم إضافة كود CameraX لمسح الباركود هنا...
}
