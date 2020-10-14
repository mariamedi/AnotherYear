package com.anotheryear.gift

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.anotheryear.R
import com.anotheryear.etsyApi.Listing

private const val TAG =  "GiftDetailFragment"
private const val ARG_LISTING_ID = "listingId"
private const val ARG_BITMAP = "bitmap"

class GiftDetailFragment : Fragment() {

    private lateinit var gift: Listing

    // Declare view objects
    private lateinit var titleTextView:TextView
    private lateinit var giftImageView: ImageView
    private lateinit var giftPriceTextView: TextView
    private lateinit var giftDescriptionTextView: TextView
    private lateinit var siteButton:Button

    // Declare ViewModel for accessing and saving birthday
    private val giftDetailViewModel: GiftDetailViewModel by lazy {
        ViewModelProviders.of(this).get(GiftDetailViewModel::class.java)
    }

    interface Callbacks {
        fun onSiteSelected(url: String)
        fun finishedLoading()
        fun selectNavIcon(navIcon: String)
    }

    private var callbacks: Callbacks? = null

    companion object {

        // Default fragment creation for testing purposes
        fun newInstance(): GiftDetailFragment {
            return GiftDetailFragment()
        }

        // Fragment creation when listing selected
        fun newInstance(listingId: Int, bitmap: Bitmap): GiftDetailFragment {
            val args = Bundle().apply {
                putSerializable(ARG_LISTING_ID, listingId)
                putParcelable(ARG_BITMAP, bitmap)
            }
            return GiftDetailFragment().apply {
                arguments = args
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gift = Listing(-1,"","","","")

        val listingId: Int? = arguments?.getSerializable(ARG_LISTING_ID) as Int?
        val bitmap: Bitmap? = arguments?.getParcelable(ARG_BITMAP) as Bitmap?

        if(listingId != null && bitmap != null){
            giftDetailViewModel.loadGift(listingId)
            giftDetailViewModel.loadImage(bitmap)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gift_detail, container, false)

        // Initialize view objects
        titleTextView = view.findViewById(R.id.gift_title_text)
        giftImageView = view.findViewById(R.id.gift_image)
        giftPriceTextView = view.findViewById(R.id.gift_price_text)
        giftDescriptionTextView = view.findViewById(R.id.gift_description_text)
        siteButton = view.findViewById(R.id.site_button)

        siteButton.setOnClickListener {
            Log.d(TAG, "Item URL: " + giftDetailViewModel.giftLiveData.value!!.url)
            callbacks?.onSiteSelected(giftDetailViewModel.giftLiveData.value!!.url)
        }

        updateUI()

        callbacks?.finishedLoading()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        giftDetailViewModel.giftLiveData.observe(
            viewLifecycleOwner,
            Observer { gift ->
                gift?.let {
                    this.gift = gift
                    updateUI()
                }
            })
    }

    private fun updateUI(){
        titleTextView.text = gift.title
        giftImageView.setImageResource(R.drawable.ic_gift)
        giftPriceTextView.text = gift.price
        giftDescriptionTextView.text = gift.description
        giftImageView.setImageBitmap(giftDetailViewModel.bitmap)
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "Started GiftDetailFragment")
        callbacks?.selectNavIcon("Gift")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }
}