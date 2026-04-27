class InventoryViewModel : ViewModel() {
    
    // قائمة الفئات التي ستظهر في الـ RecyclerView
    val inventoryItems = MutableLiveData<MutableList<CategoryItem>>(mutableListOf())
    val showStartButton = MutableLiveData<Boolean>(false)
    val totalSizeText = MutableLiveData<String>("0.00 GB")

    private var totalBytes: Long = 0

    fun startScanning(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. جرد الصور
            val photoSize = calculateMediaSize(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            updateCategory("الصور", photoSize, R.drawable.ic_image)

            // 2. جرد الفيديوهات
            val videoSize = calculateMediaSize(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
            updateCategory("الفيديوهات", videoSize, R.drawable.ic_video)

            // 3. جرد الأسماء
            val contactsCount = getContactsCount(context)
            updateCategory("الأسماء", contactsCount.toLong(), R.drawable.ic_contacts, isCountOnly = true)

            // تحديث حالة الزر عند الانتهاء
            showStartButton.postValue(true)
        }
    }

    private fun updateCategory(name: String, value: Long, icon: Int, isCountOnly: Boolean = false) {
        val currentList = inventoryItems.value ?: mutableListOf()
        val detail = if (isCountOnly) "$value اسم" else formatSize(value)
        
        if (!isCountOnly) {
            totalBytes += value
            totalSizeText.postValue(formatSize(totalBytes))
        }

        currentList.add(CategoryItem(name, detail, icon))
        inventoryItems.postValue(currentList)
    }

    // دالة لحساب حجم الوسائط من MediaStore
    private fun calculateMediaSize(context: Context, uri: Uri): Long {
        var totalSize = 0L
        val projection = arrayOf(MediaStore.MediaColumns.SIZE)
        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            val sizeColumn = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)
            while (cursor.moveToNext()) {
                totalSize += cursor.getLong(sizeColumn)
            }
        }
        return totalSize
    }

    private fun formatSize(bytes: Long): String {
        if (bytes <= 0) return "0 B"
        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(bytes.toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(bytes / 1024.0.pow(digitGroups.toDouble())) + " " + units[digitGroups]
    }
}
