package com.anotheryear.gift

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anotheryear.R
import com.anotheryear.etsyApi.*

private const val TAG = "GiftResultListFragment"
private const val ARG_KEYWORDS = "keywords"

class GiftResultListFragment : Fragment() {

    private val giftResultListViewModel: GiftResultListViewModel by lazy {
        ViewModelProviders.of(this).get(GiftResultListViewModel::class.java)
    }

    interface Callbacks {
        fun onGiftSelected(giftID: Int, bitmap: Bitmap)
    }

    private var callbacks: Callbacks? = null
    private lateinit var giftRecyclerView: RecyclerView
    private var adapter: GiftAdapter? = GiftAdapter(emptyList())
    private lateinit var thumbnailDownloader: ThumbnailDownloader<GiftHolder>

    /**
     * Attaching callbacks
     */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        retainInstance = true

        val keywords: Array<String>? = arguments?.getSerializable(ARG_KEYWORDS) as Array<String>?
        if(keywords != null){
            Log.d(TAG, "args bundle received keywords")
            giftResultListViewModel.loadKeywords(keywords)
        }
        val responseHandler = Handler()
        thumbnailDownloader =
            ThumbnailDownloader(responseHandler) { giftHolder, bitmap ->
                val drawable = BitmapDrawable(resources, bitmap)
                giftHolder.bindDrawable(drawable)
            }
        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewLifecycleOwner.lifecycle.addObserver(
            thumbnailDownloader.viewLifecycleObserver
        )
        val view = inflater.inflate(R.layout.fragment_gift_result_list, container, false)
        Log.d(TAG, "onCreateView() called")

        giftRecyclerView = view.findViewById(R.id.gift_recycler_view) as RecyclerView
        giftRecyclerView.layoutManager = LinearLayoutManager(context)
        giftRecyclerView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        giftResultListViewModel.giftListLiveData.observe(
            viewLifecycleOwner,
            Observer { listingItems ->
                Log.d(TAG, "Response received: $listingItems")
                updateUI(listingItems)
            })
    }

    private fun updateUI(gifts: List<Listing>) {
        // Add adapter
        adapter = GiftAdapter(gifts)
        giftRecyclerView.adapter = adapter
    }

    companion object {
        fun newInstance(keywords: Array<String>): GiftResultListFragment {
            val args = Bundle().apply {
                putSerializable(ARG_KEYWORDS, keywords) // TODO these are gonna have to be used within the list view model to get the right listings
            }
            return GiftResultListFragment().apply {
                arguments = args
            }
        }
    }

    private inner class GiftHolder(view: View) : RecyclerView.ViewHolder(view) , View.OnClickListener {

        private lateinit var gift: Listing

        private val giftImageView: ImageView = itemView.findViewById(R.id.gift_image)
        private val titleTextView: TextView = itemView.findViewById(R.id.gift_title_text)
        private val priceTextView: TextView = itemView.findViewById(R.id.gift_price_text)

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(gift: Listing) {
            this.gift = gift
            titleTextView.setText(this.gift.title)
            priceTextView.setText(this.gift.price)
        }
        val bindDrawable: (Drawable) -> Unit  = giftImageView::setImageDrawable

        override fun onClick(v: View) {
            Log.d(TAG, "Listing selected: ${this.gift.listing_id}")
            val bitmap: Bitmap = (giftImageView.getDrawable() as BitmapDrawable).bitmap
            callbacks?.onGiftSelected(this.gift.listing_id, bitmap)
        }
    }

    private inner class GiftAdapter(var gifts: List<Listing>) : RecyclerView.Adapter<GiftHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : GiftHolder {
            val view = layoutInflater.inflate(R.layout.list_item_gift, parent, false)
            Log.d(TAG, "onCreateViewHolder() called")
            return GiftHolder(view)
        }

        override fun getItemCount() = gifts.size

        override fun onBindViewHolder(holder: GiftHolder, position: Int) {
            Log.d(TAG, "onBindViewHolder() called")
            val gift = gifts[position]
            EtsyGetter().fetchImageListing(gift.listing_id, object : OnEtsyImageResponse {

                override fun images(images: EtsyImageResponse?) {
                    var listingItems: List<ListingImage> = images?.results
                        ?: mutableListOf()
                    listingItems = listingItems.filterNot {
                        it.url_75x75.isBlank()
                        it.url_170x135.isBlank()
                    }
                    if (listingItems.count() != 0)
                        thumbnailDownloader.queueThumbnail(holder, listingItems[0]!!.url_170x135)
                    else
                        Toast.makeText(
                            activity,
                            "oopsie poopsie slow down there cowboy", Toast.LENGTH_SHORT
                        ).show()
                }
            })
            holder.apply { holder.bind(gift) }
        }
    }

    /**
     * Lifecycle calls
     */
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

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
        lifecycle.removeObserver(
            thumbnailDownloader.fragmentLifecycleObserver
        )
    }
    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwner.lifecycle.removeObserver(
            thumbnailDownloader.viewLifecycleObserver
        )
    }
}