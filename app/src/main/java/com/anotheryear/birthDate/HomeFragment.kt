package com.anotheryear.birthDate

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anotheryear.Birthday
import com.anotheryear.R
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "HomeFragment"

/**
 * Fragment class that holds all the functionality for the Home Screen section of the application
 */
class HomeFragment: Fragment() {

    private lateinit var addBtn: Button
    private lateinit var birthdayRecyclerView: RecyclerView
    private var adapter: BirthdayAdapter? = BirthdayAdapter(emptyList())
    private var birthdayListViewModel: BirthdayListViewModel? = null

    /**
     * Callback interface to access and send data to the BirthDateActivity
     */
    interface Callbacks {
        fun viewDetailFragment(id: UUID?, date: Date?, frag: String)
        val getBirthdayListViewModel : BirthdayListViewModel
        fun selectNavIcon(navIcon: String)
    }

    private var callbacks: Callbacks? = null

    /**
     * Override for the onCreateView method that initializes elements that need to be manipulated
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        Log.d(TAG, "onCreateView() called")

        // Get the birthdayListViewModel from the BirthDateActivity via the callback val
        birthdayListViewModel = callbacks!!.getBirthdayListViewModel

        // Initialization for UI elements to be manipulated
        addBtn = view.findViewById(R.id.home_add_btn)
        addBtn.setOnClickListener { callbacks?.viewDetailFragment(null, null, "Home") }

        // Initialization for RecyclerView elements
        birthdayRecyclerView = view.findViewById(R.id.home_recycler_view) as RecyclerView
        birthdayRecyclerView.layoutManager = LinearLayoutManager(context)
        birthdayRecyclerView.adapter = adapter

        return view
    }

    /**
     * Override for the onViewCreated method that observes the list of birthdays
     * from the BirthdayListViewModel and displays each birthday in the UI
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated() called")
        birthdayListViewModel?.birthdayList?.observe(
            viewLifecycleOwner,
            Observer { birthdays ->
                birthdays?.let {
                    Log.d(TAG, "Got Birthdays: ${birthdays.size}")
                    updateUI(birthdays)
                }
            })
    }

    /**
     * Function that adds the BirthdayAdapter to the recycler view
     * to show the birthdays in the UI
     */
    private fun updateUI(birthdays: List<Birthday>) {
        adapter = BirthdayAdapter(birthdays)
        birthdayRecyclerView.adapter = adapter
    }

    /**
     * Companion object to create a new instance of HomeFragment
     */
    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
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
     * Adding logs messages for onStart, onResume, onPause, onStop and onDestroy
     */
    override fun onStart() {
        super.onStart()
        // set the proper nav icon selection when this fragment starts
        callbacks?.selectNavIcon("Home")
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
    }

    /**
     * Inner class for the BirthdayHolder that populates individual birthday list entries
     */
    private inner class BirthdayHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var birthday: Birthday

        // Deceleration of UI elements that will be manipulated
        private val birthDateTextView: TextView = itemView.findViewById(R.id.list_date)
        private val nameTextView: TextView = itemView.findViewById(R.id.list_name)

        init {
            itemView.setOnClickListener(this)
        }

        // Binding function that builds the birthday list entry
        @SuppressLint("SimpleDateFormat")
        fun bind(birthday: Birthday) {
            this.birthday = birthday

            val dateFormatter: Format = SimpleDateFormat("MM/dd")
            birthDateTextView.text = dateFormatter.format(birthday.birthdate).toString()
            nameTextView.text = getString(R.string.name_concat,this.birthday.firstName, this.birthday.lastName)
        }

        // onClick functionality for the BirthdayListItems that switches to the BirthdayDetailFragment and passes the UUID
        override fun onClick(v: View?) {
            Log.d(TAG, "itemView.onClick() called")
            callbacks?.viewDetailFragment(birthday.id, null, "Home")
        }
    }

    /**
     * Inner class for the BirthdayAdapter that populates the recycler view with all the birthday list entries
     */
    private inner class BirthdayAdapter(var birthdays: List<Birthday>) : RecyclerView.Adapter<BirthdayHolder>() {

        /**
         * Overrides the onCreateViewHolder method to make a view holder for the Birthday List
         */
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : BirthdayHolder {
            val view = layoutInflater.inflate(R.layout.list_item_birthday, parent, false)
            Log.d(TAG, "onCreateViewHolder() called")

            return BirthdayHolder(view)
        }

        // gets the size of the list of birthdays
        override fun getItemCount() = birthdays.size

        /**
         * Overrides the onBindViewHolder method that binds a birthday list entry to the view holder
         */
        override fun onBindViewHolder(holder: BirthdayHolder, position: Int) {
            val birthday = birthdays[position]
            holder.apply {
                holder.bind(birthday)
            }
            Log.d(TAG, "onBindViewHolder() called")
        }
    }
}