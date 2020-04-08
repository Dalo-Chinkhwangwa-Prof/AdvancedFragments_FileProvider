package com.bigbang.advancedfragment

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.profile_fragment_layout.*

class ProfileFragment : Fragment() {

    private val REQUEST_CODE = 707

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = layoutInflater.inflate(R.layout.profile_fragment_layout, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        change_pic_button.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

            context?.packageManager?.let { pkgMgr ->
                if (cameraIntent.resolveActivity(pkgMgr) != null) {//Verified that this device has a camera
                    startActivityForResult(cameraIntent, REQUEST_CODE)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQUEST_CODE){
            data?.extras?.get("data")?.let { imageItem ->

                context?.let { activityContext ->
                    Glide.with(activityContext)
                        .applyDefaultRequestOptions(RequestOptions().centerCrop())
                        .load(imageItem as Bitmap)
                        .into(profile_pic_imageview)
                }


            }

        }
    }

}