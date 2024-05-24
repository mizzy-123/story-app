package com.bangkit.storyapp.ui.activities

import android.annotation.SuppressLint
import android.app.Instrumentation.ActivityResult
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import cn.pedant.SweetAlert.SweetAlertDialog
import com.bangkit.storyapp.R
import com.bangkit.storyapp.compressImageFile
import com.bangkit.storyapp.databinding.ActivityAddStoryBinding
import com.bangkit.storyapp.getImageUri
import com.bangkit.storyapp.ui.viewmodels.StoryViewModel
import java.io.File

class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImage: Uri? = null
    private lateinit var storyViewModel: StoryViewModel
    private lateinit var pDialog: SweetAlertDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initComponents()
        setupAction()
    }

    private fun initComponents(){
        storyViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(StoryViewModel::class.java)
        pDialog = SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.titleText = "Loading..."
        pDialog.progressHelper.barColor = Color.parseColor("#2D3D4F")
        pDialog.setCancelable(false)
    }

    private fun setupAction(){
        binding.btnUpload.setOnClickListener {
            postStories()
        }

        binding.btnCamera.setOnClickListener {
            currentImage = getImageUri(this)
            launcherIntentCamera.launch(currentImage)
        }

        binding.btnGallery.setOnClickListener {
            launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        storyViewModel.loadingPostStories.observe(this){

            if (!it){
                pDialog.dismiss()
            }

        }

        storyViewModel.isStoriesCreated.observe(this){
            if (it){
                val intent = Intent(this@AddStoryActivity, StoryActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }

    @SuppressLint("Recycle")
    private fun postStories() {
        val image = currentImage ?: return
        pDialog.show()

        val description = binding.edDescription.text.toString()
        var filePhoto: File? = null
        val inputStream = this.contentResolver.openInputStream(image)
        val cursor = this.contentResolver.query(image, null, null, null, null)
        cursor?.use { c ->
            val nameIndex = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (c.moveToFirst()){
                val name = c.getString(nameIndex)
                inputStream?.let { inputStream ->
                    val file = File(this.cacheDir, name)
                    val os = file.outputStream()
                    os.use {
                        inputStream.copyTo(it)
                    }

                    filePhoto = file
                }
            }
        }

        val sharedPreferences = getSharedPreferences("userpref", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        if (token != null && filePhoto != null){
            storyViewModel.postStories(
                this@AddStoryActivity,
                token,
                description,
                compressImageFile(contentResolver, currentImage!!, filePhoto!!)
            )
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null){
            currentImage = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess){
            showImage()
        }
    }

    private fun showImage(){
        currentImage?.let {
            binding.previewImageView.setImageURI(it)
        }
    }
}