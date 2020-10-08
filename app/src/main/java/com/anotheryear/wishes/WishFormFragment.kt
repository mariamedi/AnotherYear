package com.anotheryear.wishes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.anotheryear.R

class WishFormFragment : Fragment() {

    companion object {
        fun newInstance(): WishFormFragment {
            return WishFormFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wish_form, container, false)

        return view
    }
}