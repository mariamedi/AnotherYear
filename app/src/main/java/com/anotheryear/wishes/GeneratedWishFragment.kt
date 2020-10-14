package com.anotheryear.wishes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.anotheryear.R
import java.io.File
import java.io.FileOutputStream
import java.util.*

private const val TAG = "GeneratedWishFragment"
private const val REQUEST_PHOTO = 0
private const val ARG_IMAGE_FILE = "ImageFile"

/**
 * Generated Wish Fragment
 */
class GeneratedWishFragment : Fragment() {
    private lateinit var birthdayWish: TextView
    private lateinit var newWishButton: Button
    private lateinit var changeSettButton: Button
    private lateinit var birthEmail: EditText
    private lateinit var sendWish: Button
    private lateinit var addImageButton: ImageButton
    private lateinit var imageView: ImageView
    private var wishViewModel: WishesViewModel? = null

    // vars needed for camera intent
    private var photoUri: Uri? = null
    private var photoFile: File? = null

    /**
     * Callback interface to access and send data to the WishesActivity
     */
    interface Callbacks {
        fun changeSettings(photoFile: File)
        val getWishViewModel : WishesViewModel
        fun selectNavIcon(navIcon: String)
    }
    private var callbacks: Callbacks? = null

    /**
     * Override for the onCreateView method that initializes elements that need to be manipulated
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_generated_wish, container, false)

        var updateImage = true // used to track if the imageView needs to be updated on load

        // Initialization of UI Elements
        birthdayWish = view.findViewById(R.id.PW_birthday_wish) as TextView
        newWishButton = view.findViewById(R.id.PW_generate_new_wish_button) as Button
        changeSettButton = view.findViewById(R.id.PW_change_settings_button) as Button
        birthEmail = view.findViewById(R.id.PW_their_email) as EditText
        sendWish = view.findViewById(R.id.PW_send_wish_button) as Button
        addImageButton = view.findViewById(R.id.PW_add_image) as ImageButton
        imageView = view.findViewById(R.id.PW_show_image) as ImageView

        // get a passed in photoFile if there is one
        photoFile = arguments?.getSerializable(ARG_IMAGE_FILE) as File?


        // set the photoFile if it is null and set updateImage to false since this is a new photofile being made
        if(photoFile == null) {
            updateImage = false
            photoFile = File(context!!.applicationContext.filesDir, "wishPic" + UUID.randomUUID())
        }
        // create the URI if there isn't one
        if(photoUri == null) {
            photoUri = FileProvider.getUriForFile(
                requireActivity(),
                "com.anotheryear.fileprovider",
                photoFile!!
            )
        }

        // if we are trying to update an image from an existing file, do it
        if(updateImage) {
            try {
                val bitmap = getScaledBitmap(photoFile!!.path, requireActivity())
                imageView.setImageBitmap(rotateImage(bitmap, photoFile))
                imageView.visibility = View.VISIBLE
            } catch (e: Exception){

            }
        }

        // listener for birthday person's email
        birthEmail.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
            }
        })

        // Get the wishViewModel from the WishesActivity via the callback val
        wishViewModel = callbacks!!.getWishViewModel

        // Add current wish to TextView
        birthdayWish.text = wishViewModel?.currentWish

        // listener for Generate New Wish Button
        newWishButton.setOnClickListener{
            // generate new wish
            wishViewModel?.generateWish()

            //update textView
            // Add wish to current TextView
            birthdayWish.text = wishViewModel?.currentWish
        }

        // go back to settings page
         changeSettButton.setOnClickListener{
             // go back to settings page
             callbacks?.changeSettings(photoFile!!)
         }

        // listener for send wish button
        sendWish.setOnClickListener{
          requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
           val bitmap = getScaledBitmap(photoFile!!.path, requireActivity())
        }

        return view
    }

    private fun sendEmail(wish: String?, uri: Uri) {


        if (!birthEmail.text.toString().isEmpty()) {
            val msg = wish
            Log.i("Email", "Sending email")

            val TO = arrayOf(birthEmail.getText().toString())
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.type = "text/plain"

            emailIntent.putExtra(Intent.EXTRA_EMAIL, TO)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Happy Birthday!")
            emailIntent.putExtra(Intent.EXTRA_TEXT, msg)
            emailIntent.putExtra(Intent.EXTRA_STREAM, uri)
            emailIntent.setType("image/png");

            try {
                startActivity(Intent.createChooser(emailIntent, "Send mail..."))
                activity?.finish()
                Log.i("Email.", "Sending email")
            } catch (ex: ActivityNotFoundException) {
                Toast.makeText(
                    activity,
                    "There is no email client installed.", Toast.LENGTH_SHORT
                ).show()
            }
        }
        else{
            Toast.makeText(
                activity,
                "Enter email.", Toast.LENGTH_SHORT
            ).show()
        }
    }

    /**
     * Companion object to creates new instances of GeneratedWishFragment
     */
    companion object {
        fun newInstance(): GeneratedWishFragment {
            return GeneratedWishFragment()
        }

        // get an existing photo file on creation
        fun newInstance(photoFile: File): GeneratedWishFragment {
            val args = Bundle().apply {
                putSerializable(ARG_IMAGE_FILE, photoFile)
            }
            return GeneratedWishFragment().apply {
                arguments = args
            }
        }
    }

    /**
     * Overrides the onAttach method to initialize the callbacks
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d(TAG, "onAttach() called")
        callbacks = context as Callbacks?
    }

    /**
     * Overrides the onDetach method to set the callbacks to null
     */
    override fun onDetach() {
        super.onDetach()
        Log.d(TAG, "onDetach() called")
        callbacks = null
    }

    /**
     * Override for the onStart() method that sets the nav bar selection and image button functionality
     */
    @SuppressLint("QueryPermissionsNeeded")
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
        // set the proper nav icon selection when this fragment starts
        callbacks?.selectNavIcon("Wish")

        addImageButton.apply {
            val packageManager: PackageManager = requireActivity().packageManager
            val captureImage = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            val resolvedActivity: ResolveInfo? = packageManager.resolveActivity(
                captureImage,
                PackageManager.MATCH_DEFAULT_ONLY
            )

            if (resolvedActivity == null) {
                isEnabled = false
            }
            setOnClickListener {
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                val cameraActivities: List<ResolveInfo> = packageManager.queryIntentActivities(
                    captureImage,
                    PackageManager.MATCH_DEFAULT_ONLY
                )

                for (cameraActivity in cameraActivities) {
                    requireActivity().grantUriPermission(
                        cameraActivity.activityInfo.packageName,
                        photoUri,
                        Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    )
                }
                startActivityForResult(captureImage, REQUEST_PHOTO)
            }
        }
    }

    /**
     * Overrides the onActivityResult method in order to updates the ImageView
     * once the user returns from the camera app
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Log.d(TAG, "onActivityResult() called")
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_OK) {
            return
        }
        if (requestCode == REQUEST_PHOTO) {
            requireActivity().revokeUriPermission(photoUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            val bitmap = getScaledBitmap(photoFile!!.path, requireActivity())
            imageView.setImageBitmap(rotateImage(bitmap, photoFile))
            imageView.visibility = View.VISIBLE
        }
    }

    /**
     * Adding logs messages for onResume, onPause, onStop and onDestroy
     */
    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }
    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }
    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }
}