package com.bigbang.advancedfragment

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.profile_fragment_layout.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class ProfileFragment : Fragment() {

    private val REQUEST_CODE = 707
    private lateinit var imageDirectory: String

    private lateinit var progressDialog: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutInflater.inflate(R.layout.profile_fragment_layout, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog = ProgressDialog(context)
        progressDialog.setTitle("Uploading Image")
        progressDialog.setCancelable(false)

        change_pic_button.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            context?.let { activityContext ->
                if (cameraIntent.resolveActivity(activityContext.packageManager) != null) {//Verified that this device has a camera
                    try {
                        val temporaryFile = createTemporaryFile()
                        temporaryFile?.let { file ->

                            val imageUri: Uri = FileProvider.getUriForFile(
                                activityContext,
                                getString(R.string.my_provider_name),
                                temporaryFile
                            )

                            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                            startActivityForResult(cameraIntent, REQUEST_CODE)
                        }

                    } catch (exception: Exception) {
                        Log.d("TAG_X", exception.localizedMessage)
                    }
                }
            }
        }
    }

    @Throws(IOException::class)
    private fun createTemporaryFile(): File? {

        val fileName = getString(
            R.string.image_name_text,
            SimpleDateFormat(getString(R.string.date_format_text), Locale.US).format(Date())
        )

        val fileDirectory = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

        val actualImage: File = File.createTempFile(
            fileName,
            getString(R.string.image_postfix),
            fileDirectory
        )

        imageDirectory = actualImage.absolutePath
        return actualImage
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {

            val storedImage = BitmapFactory.decodeFile(imageDirectory)

            val baOS = ByteArrayOutputStream()
            storedImage.compress(Bitmap.CompressFormat.JPEG, 100, baOS)
            //At this point the image has been compressed into baOS

            val firebaseStorageReference = FirebaseStorage.getInstance()
                .reference
                .child("ProfilePicture/")
            progressDialog.show()
            val uploadTask = firebaseStorageReference.putBytes(baOS.toByteArray())

            uploadTask.addOnCompleteListener{ completeTask ->

                if(completeTask.isSuccessful){
                    firebaseStorageReference.downloadUrl
                        .addOnCompleteListener { downloadTask ->
                            if(downloadTask.isSuccessful){
                                progressDialog.dismiss()
                                Log.d("TAG_X FIREBASE", downloadTask.result.toString())
                                displayFromFirebase(downloadTask.result.toString())
                            } else {
                                progressDialog.dismiss()
                                Log.d("TAG_X", "Error: ${downloadTask.exception?.localizedMessage}")
                            }
                        }

                } else {
                    Log.d("TAG_X", "Error ${completeTask.exception?.localizedMessage}")
                }
            }
        }
    }

    private fun displayFromFirebase(storedImage: String) {
        context?.let { activityContext ->
            Glide.with(activityContext)
                .applyDefaultRequestOptions(RequestOptions().centerCrop())
                .load(storedImage)//This is pic is low quality because we are using a thumbnail
                .into(profile_pic_imageview)
        }
    }


}