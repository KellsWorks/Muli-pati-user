@file:Suppress("DEPRECATION")

package app.mulipati.ui.profile.edit

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.Images.Media.getBitmap
import android.util.Base64.DEFAULT
import android.util.Base64.encodeToString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import app.mulipati.R
import app.mulipati.databinding.FragmentEditImageBinding
import app.mulipati.network.ApiClient
import app.mulipati.network.Routes
import app.mulipati.util.Constants
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.IOException


class EditImageFragment : Fragment() {

    private lateinit var bitmap: Bitmap

    private lateinit var editImageBinding: FragmentEditImageBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        editImageBinding = FragmentEditImageBinding.inflate(inflater, container, false)
        editImageBinding.lifecycleOwner = this

        return editImageBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userPreferences = context?.getSharedPreferences("user", Context.MODE_PRIVATE)

        update()

            Glide
                .with(requireContext())
                .load(Constants.PROFILE_URL+ userPreferences?.getString("photo", ""))
                .centerCrop()
                .into(editImageBinding.editedIcon)

    }
    private fun update(){

        editImageBinding.selectImage.setOnClickListener {
            uploadImage()
        }
    }

    private fun upload() {
        val dialog = ProgressDialog(requireContext())
        dialog.setMessage("Updating...")
        dialog.setCancelable(false)

        Toast.makeText(requireContext(), imageToString(bitmap).toString(), Toast.LENGTH_SHORT)
                .show()

        dialog.show()

        val api = ApiClient.client!!.create(Routes::class.java)
        val userPreferences = context?.getSharedPreferences("user", Context.MODE_PRIVATE)

        val upload: Call<app.mulipati.data.Response?>? = api.photoUpdate(userPreferences?.getInt("profile_id", 0), imageToString(bitmap).toString())
        upload?.enqueue(object : Callback<app.mulipati.data.Response?> {
            override fun onFailure(call: Call<app.mulipati.data.Response?>, t: Throwable) {
                Toast.makeText(
                        requireContext(),
                        t.message,
                        Toast.LENGTH_SHORT
                ).show()

                Timber.d(t)
                Timber.e(imageToString(bitmap).toString())
                dialog.dismiss()
            }

            override fun onResponse(call: Call<app.mulipati.data.Response?>, response: Response<app.mulipati.data.Response?>) {

                Toast.makeText(
                        requireContext(),
                        "Success",
                        Toast.LENGTH_SHORT
                ).show()
                dialog.dismiss()

                val user = context?.getSharedPreferences("user", Context.MODE_PRIVATE)?.edit()

                user?.putString("photo", response.body()?.photo)
                user?.apply()

                findNavController().navigate(R.id.action_editImageFragment2_to_personalEditFragment)

                dialog.dismiss()
            }

        })
    }

    private fun uploadImage(){

        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, Constants.IMG_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.IMG_REQUEST && resultCode == RESULT_OK && data != null){

            val path: Uri? = data.data
            try {
                bitmap = getBitmap(requireContext().contentResolver, path)
                editImageBinding.iconName.text = "${data.data?.path}"

                editImageBinding.editedIcon.setImageBitmap(bitmap)

            }
            catch (e: IOException){
                e.printStackTrace()
            }
        }
    }

    private fun imageToString(bitmap: Bitmap): Any {

        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream)
        val imgBytes: ByteArray = byteArrayOutputStream.toByteArray()
        return encodeToString(imgBytes, DEFAULT)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        editImageBinding.toEditPersonal.setOnClickListener {
            upload()
        }

        editImageBinding.iconEditBack.setOnClickListener {
            findNavController().navigateUp()
        }

        editImageBinding.skipEditIcon.setOnClickListener {
            findNavController().navigate(R.id.action_editImageFragment2_to_personalEditFragment)
        }
    }
}