package com.example.caritaappnew.view.main.story

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.example.caritaappnew.R
import com.example.caritaappnew.databinding.ActivityAddStoryBinding
import com.example.caritaappnew.model.ApiCallback
import com.example.caritaappnew.model.UserModel
import com.example.caritaappnew.model.reduceFileImage
import com.example.caritaappnew.model.uriToFile
import com.example.caritaappnew.model.createTempFile
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

@Suppress("DEPRECATION", "SameParameterValue")
class AddStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddStoryBinding
    private lateinit var user : UserModel
    private lateinit var viewModel: AddStoryViewModel
    private lateinit var photopath : String

    private var getFile : File? = null
    private var result : Bitmap? = null

    companion object{
        const val CAMERA_X_RESULT = 200
        const val EXTRA_USER = "user"
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        user = intent.getParcelableExtra(EXTRA_USER)!!
        viewModel = ViewModelProvider(this).get(AddStoryViewModel::class.java)

        getPermission()
        onClick()
        showLoad(false)
    }

    private fun showLoad(b: Boolean) {
        if (b){
            binding.ProgressBarAddStory.visibility = View.VISIBLE
        } else {
            binding.ProgressBarAddStory.visibility = View.GONE
        }
    }

    private fun getPermission(){
        if(!allPermissionsGranted()){
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
    }


    private fun onClick(){
        binding.CameraButton.setOnClickListener { startCamera() }
        binding.galleryButton.setOnClickListener { startGallery() }
        binding.UploadButton.setOnClickListener { startUpload() }
    }


    @SuppressLint("QueryPermissionsNeeded")
    private fun startCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        createTempFile(application).also {
            val photoUri : Uri = FileProvider.getUriForFile(
                this@AddStoryActivity,
                "camera",
                it
            )
            photopath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            launcherIntentCamera.launch(intent)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK){
            val myFile = File(photopath)

            getFile = myFile
            result = BitmapFactory.decodeFile(myFile.path)
            binding.ivPreview.setImageBitmap(result)
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == RESULT_OK){
            val selectedImage : Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImage, this@AddStoryActivity)
            getFile = myFile
            binding.ivPreview.setImageURI(selectedImage)
        }
    }

    private fun startUpload() {
        val desc = binding.edAddDescription.text.toString()
        if(getFile != null && desc.isNotEmpty()){
            val file = reduceFileImage(getFile as File)
            val requestImage = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart = MultipartBody.Part.createFormData("photo", file.name, requestImage)
            showLoad(true)
            viewModel.upload(user, desc, imageMultipart, object : ApiCallback {
                override fun onResponse(success: Boolean, message: String) {
                    if(success){
                        AlertDialog.Builder(this@AddStoryActivity).apply {
                            setTitle(getString(R.string.information))
                            setMessage(getString(R.string.upload_success))
                            setPositiveButton(getString(R.string.next)) {_, _ ->
                                finish()
                            }
                            create()
                            show()
                        }
                    } else {
                        AlertDialog.Builder(this@AddStoryActivity).apply {
                            setTitle(getString(R.string.information))
                            setMessage(getString(R.string.upload_failed) + ", $message")
                            setPositiveButton(this@AddStoryActivity.getString(R.string.next)) { _, _ ->
                                binding.ProgressBarAddStory.visibility = View.GONE
                            }
                            create()
                            show()
                        }
                    }
                }
            })
        }else{
            Toast.makeText(this@AddStoryActivity, R.string.errorupload,
                Toast.LENGTH_SHORT).show()
        }
    }

}