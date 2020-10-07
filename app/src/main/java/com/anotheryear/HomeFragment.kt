package com.anotheryear

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import java.util.*

class HomeFragment: Fragment() {


    private lateinit var button: Button // TODO: just a place holder

    // Include callback for switching to appropriate birthday detail screen
    interface Callbacks {
        fun viewDetails(id: UUID)
    }

    private var callbacks: Callbacks? = null

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize view objects
        button = view.findViewById(R.id.button)

        button.setOnClickListener {
            // TODO: Hardcoded for now, should use ID from selected birthday
            callbacks?.viewDetails(UUID.fromString("bfd69755-dae1-4b3e-a18f-f0ad603c1803"))
        }

        return view
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