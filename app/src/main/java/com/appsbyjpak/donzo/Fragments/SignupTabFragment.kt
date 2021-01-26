package com.appsbyjpak.donzo.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.appsbyjpak.donzo.R

class SignupTabFragment : Fragment() {

    lateinit var email: EditText
    lateinit var password: EditText
    lateinit var confirm: EditText
    lateinit var signupButton: Button
    var v = 0f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var root = inflater.inflate(R.layout.fragment_signup_tab, container, false)
        email = root.findViewById(R.id.signup_email_input)
        password = root.findViewById(R.id.signup_password_input)
        confirm = root.findViewById(R.id.signup_confirm_input)
        signupButton = root.findViewById(R.id.signup_button)

        email.translationY = 300f
        password.translationY = 300f
        confirm.translationY = 300f
        signupButton.translationY = 300f

        email.alpha = v
        password.alpha = v
        confirm.alpha = v
        signupButton.alpha = v

        email.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(300).start()
        password.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(500).start()
        confirm.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(500).start()
        signupButton.animate().translationY(0f).alpha(1f).setDuration(1000).setStartDelay(700).start()

        return root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SignupTabFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance() =
            SignupTabFragment().apply {
            }
    }
}